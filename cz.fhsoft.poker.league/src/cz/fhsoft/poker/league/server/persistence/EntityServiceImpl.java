package cz.fhsoft.poker.league.server.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.LockModeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Transient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cz.fhsoft.poker.league.client.persistence.EntityService;
import cz.fhsoft.poker.league.client.persistence.NativeQuery;
import cz.fhsoft.poker.league.server.ServletInitializer;
import cz.fhsoft.poker.league.shared.model.v1.DataVersion;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;
import cz.fhsoft.poker.league.shared.persistence.LazyCollection;
import cz.fhsoft.poker.league.shared.persistence.LazyList;
import cz.fhsoft.poker.league.shared.persistence.LazySet;

@SuppressWarnings("serial")
public class EntityServiceImpl extends RemoteServiceServlet implements EntityService {
	
	public static final Object LOCK = new Object();
	
	private static final int DATA_VERSION_ID = 1;

	@Override
	public long getDataVersion() {
		return getDataVersionStatic();
	}

	public static long getDataVersionStatic() {
		synchronized(LOCK) {
			DataVersion dataVersion = ServletInitializer.getEntityManager().find(DataVersion.class, DATA_VERSION_ID);
			if(dataVersion == null) {
				dataVersion = new DataVersion();
				dataVersion.setId(DATA_VERSION_ID);
				dataVersion.setCurrentVersion(new Date());
				ServletInitializer.getEntityManager().getTransaction().begin();
				ServletInitializer.getEntityManager().persist(dataVersion);
				ServletInitializer.getEntityManager().getTransaction().commit();
			}
			
			return dataVersion.getCurrentVersion().getTime();
		}
	}

	public static long updateDataVersion() {
		synchronized(LOCK) {
			DataVersion dataVersion = ServletInitializer.getEntityManager().find(DataVersion.class, DATA_VERSION_ID);
			if(dataVersion == null) {
				dataVersion = new DataVersion();
				dataVersion.setId(DATA_VERSION_ID);
				dataVersion.setCurrentVersion(new Date());
				ServletInitializer.getEntityManager().persist(dataVersion);
			}
			else {
				dataVersion.setCurrentVersion(new Date());
				ServletInitializer.getEntityManager().merge(dataVersion);
			}
			
			return dataVersion.getCurrentVersion().getTime();
		}
	}

	@Override
	public <E extends IdentifiableEntity> E find(String entityClass, Number id) {
		synchronized(LOCK) {
			return find(entityClass, id, true);
		}
	}
	
	@Override
	public <E extends IdentifiableEntity> List<E> list(String entityClass) {
		synchronized(LOCK) {
			try {
				Class<?> realEntityClass = Class.forName(entityClass);
				
				Entity entityAnnotation = realEntityClass.getAnnotation(Entity.class);
				if(entityAnnotation == null)
					throw new IllegalArgumentException(entityClass + " is not an entity");
	
				Query query = ServletInitializer.getEntityManager().createQuery("SELECT e FROM " + entityAnnotation.name() + " e");
				
				List<E> results = new ArrayList<E>();
	
				for(Object obj : query.getResultList()) {
					@SuppressWarnings("unchecked")
					E entity = (E) obj;
					results.add(makeTransferable(entity));
				}
				
				return results;
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	@Override
	public <E extends IdentifiableEntity> List<E> resolveReference(String entityClass, Number id, String referenceName) {
		synchronized(LOCK) {
			IdentifiableEntity entity = find(entityClass, id, false);
			if(entity == null)
				return Collections.emptyList();
	
			ServletInitializer.getEntityManager().refresh(entity);
	
			try {
				Field referenceField = ReflectUtil.getDeclaredField(entity.getClass(), referenceName);
				referenceField.setAccessible(true);
				@SuppressWarnings("unchecked")
				Collection<E> referenceList = (Collection<E>) referenceField.get(entity);
	
				List<E> transferrableList = new ArrayList<E>(referenceList.size());
				for(E referencedEntity : referenceList)
					transferrableList.add(makeTransferable(referencedEntity));
	
				return transferrableList;
	
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	private <E extends IdentifiableEntity> E find(String entityClass, Number id, boolean makeTransferrable) {
		synchronized(LOCK) {
			try {
				Class<?> realEntityClass = Class.forName(entityClass);
	
				@SuppressWarnings("unchecked")
				E entity = (E) ServletInitializer.getEntityManager().find(realEntityClass, id);
				
				if(entity != null && makeTransferrable)
					makeTransferable(entity);
				
				return entity;
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public static <E extends IdentifiableEntity> E makeTransferable(E entity) {
		synchronized(LOCK) {
			Set<IdentifiableEntity> visited = new HashSet<IdentifiableEntity>();
			return makeTransferable(entity, visited, false);
		}
	}

	private static <E extends IdentifiableEntity> E makeTransferable(E entity, Set<IdentifiableEntity> visited, boolean asProxy) {
		synchronized(LOCK) {
			if(entity == null || !visited.add(entity))
				return entity;
	
			try {
				entity.setProxy(asProxy);
				//Make multiple references lazy and make 1-st level n-to-one references transferable
				for(Field field : ReflectUtil.getDeclaredFields(entity.getClass())) {
					if((field.getModifiers() & Modifier.STATIC) != 0 || field.getAnnotation(Transient.class) != null)
						continue;
					field.setAccessible(true);
					if(field.getAnnotation(OneToOne.class) != null || field.getAnnotation(ManyToOne.class) != null) {
						try {
							makeTransferable((IdentifiableEntity) field.get(entity), visited, true);
						} catch (IllegalAccessException e) {
							throw new IllegalArgumentException(e);
						}
					}
					else if(field.getAnnotation(OneToMany.class) != null || field.getAnnotation(ManyToMany.class) != null) {
						try {
							if(Set.class.isAssignableFrom(field.getType())) {
								if(asProxy)
									field.set(entity, new HashSet<E>());
								else
									field.set(entity, new LazySet<E, IdentifiableEntity>(entity, field.getName()));
							}
							else if(List.class.isAssignableFrom(field.getType())) {
								if(asProxy)
									field.set(entity, new ArrayList<E>());
								else
									field.set(entity, new LazyList<E, IdentifiableEntity>(entity, field.getName()));
							}
						} catch (IllegalAccessException e) {
							throw new IllegalArgumentException(e);
						} 
					}
				}
			} finally {
				ServletInitializer.getEntityManager().detach(entity);
			}
	
			return entity;
		}
	}

	@Override
	public <E extends IdentifiableEntity> EntityWithDataVersion<E> persist(E entity) {
		synchronized(LOCK) {
			ServletInitializer.getEntityManager().getTransaction().begin();
			
			long dataVersion = 0;
			
			try {
				localResolveExistingReferences(entity, false);
		
				ServletInitializer.getEntityManager().persist(entity);
				dataVersion = updateDataVersion();
				ServletInitializer.getEntityManager().getTransaction().commit();
			}
			catch(Throwable t){
				if(ServletInitializer.getEntityManager().getTransaction().isActive())
					ServletInitializer.getEntityManager().getTransaction().rollback();
				throw new RuntimeException(t);
			}
			
			return new EntityWithDataVersion<E>(makeTransferable(entity), dataVersion);
		}
	}
	
	private <E extends IdentifiableEntity> E localResolveExistingReferences(E entity, boolean forMerge) throws IllegalArgumentException, IllegalAccessException {
		synchronized(LOCK) {
			Set<IdentifiableEntity> visited = new HashSet<IdentifiableEntity>();
			return localResolveExistingReferences(entity, forMerge, visited);
		}
	}

	private <E extends IdentifiableEntity> E localResolveExistingReferences(E entity, boolean forMerge, Set<IdentifiableEntity> visited) throws IllegalArgumentException, IllegalAccessException {
		synchronized(LOCK) {
			if(entity == null || !visited.add(entity))
				return entity;
	
			if(entity.getId() < 0)
				entity.setId(0); // fix temporary IDs from the client
	
			// replace existing entities with delivered referenced entities
			for(Field field : ReflectUtil.getDeclaredFields(entity.getClass())) {
				if((field.getModifiers() & Modifier.STATIC) != 0 || field.getAnnotation(Transient.class) != null)
					continue;
	
				field.setAccessible(true);
	
				if(field.getAnnotation(OneToOne.class) != null || field.getAnnotation(ManyToOne.class) != null) {
					IdentifiableEntity ref = (IdentifiableEntity) field.get(entity);
					if(ref != null) {
						IdentifiableEntity existingRef = forMerge && !ref.isProxy() ? null : ServletInitializer.getEntityManager().find(ref.getClass(), ref.getId());
						if(existingRef != null)
							field.set(entity, existingRef);
						else
							localResolveExistingReferences(ref, forMerge, visited);
					}
				}
				else if(field.getAnnotation(OneToMany.class) != null || field.getAnnotation(ManyToMany.class) != null) {
					@SuppressWarnings("unchecked")
					Collection<IdentifiableEntity> refCollection = (Collection<IdentifiableEntity>) field.get(entity);
					if(refCollection != null) {
						if(refCollection instanceof LazyCollection) {
							LazyCollection<?, ?, ?> lazyRef = (LazyCollection<?, ?, ?>) refCollection;
							if(!lazyRef.isResolved()) {
								if(forMerge) {
									IdentifiableEntity managedEntity = ServletInitializer.getEntityManager().find(entity.getClass(), entity.getId());
	
									@SuppressWarnings("unchecked")
									Collection<IdentifiableEntity> existingCollection = managedEntity != null ? (Collection<IdentifiableEntity>) field.get(managedEntity) : null;
									if(existingCollection != null)
										field.set(entity, existingCollection);
									else
										field.set(entity, null);
								}
								else
									field.set(entity, null);
								continue;
							}
						}
	
						Collection<IdentifiableEntity> resolvedCollection = null;
						if(Set.class.isAssignableFrom(field.getType()))
							resolvedCollection = new HashSet<IdentifiableEntity>();
						else if(List.class.isAssignableFrom(field.getType()))
							resolvedCollection = new ArrayList<IdentifiableEntity>();
						else continue;
	
						for(IdentifiableEntity ref : refCollection) {
							if(ref != null) {
								IdentifiableEntity existingRef = forMerge && !ref.isProxy() ? null : ServletInitializer.getEntityManager().find(ref.getClass(), ref.getId());
								if(existingRef != null)
									resolvedCollection.add(existingRef);
								else
									resolvedCollection.add(localResolveExistingReferences(ref, forMerge, visited));
							}
							else
								resolvedCollection.add(null);
						}
	
						if(forMerge) {
							IdentifiableEntity managedEntity = ServletInitializer.getEntityManager().find(entity.getClass(), entity.getId());
	
							@SuppressWarnings("unchecked")
							Collection<IdentifiableEntity> existingCollection = managedEntity != null ? (Collection<IdentifiableEntity>) field.get(managedEntity) : null;
							if(existingCollection != null) {
								existingCollection.clear();
								existingCollection.addAll(resolvedCollection);
								field.set(entity, existingCollection);
							}
							else
								field.set(entity, resolvedCollection);
						}
						else
							field.set(entity, resolvedCollection);
					}
				}
			}
			
			return entity;
		}
	}

	@Override
	public <E extends IdentifiableEntity> EntityWithDataVersion<E> merge(E entity) {
		synchronized(LOCK) {
			ServletInitializer.getEntityManager().getTransaction().begin();
			
			long dataVersion = 0;
			
			try {
				localResolveExistingReferences(entity, true);
		
				entity = ServletInitializer.getEntityManager().merge(entity);
				dataVersion = updateDataVersion();
				ServletInitializer.getEntityManager().getTransaction().commit();
			}
			catch(Throwable t){
				if(ServletInitializer.getEntityManager().getTransaction().isActive())
					ServletInitializer.getEntityManager().getTransaction().rollback();
				throw new RuntimeException(t);
			}
			
			return new EntityWithDataVersion<E>(makeTransferable(entity), dataVersion);
		}
	}

	@Override
	public <E extends IdentifiableEntity> long remove(E entity) {
		synchronized(LOCK) {
			@SuppressWarnings("unchecked")
			E managedEntity = (E) ServletInitializer.getEntityManager().find(entity.getClass(), entity.getId(), LockModeType.PESSIMISTIC_WRITE);
			if(managedEntity != null) {
				long dataVersion = 0;
				try {
					ServletInitializer.getEntityManager().getTransaction().begin();
					ServletInitializer.getEntityManager().remove(managedEntity);
					dataVersion = updateDataVersion();
					ServletInitializer.getEntityManager().getTransaction().commit();
				}
				catch(Throwable t){
					if(ServletInitializer.getEntityManager().getTransaction().isActive())
						ServletInitializer.getEntityManager().getTransaction().rollback();
					throw new RuntimeException(t);
				}
				
				return dataVersion;
			}
			
			return getDataVersion();
		}
	}

	@Override
	synchronized public List<List<Object>> executeNativeQuery(NativeQuery query) {
		synchronized(LOCK) {
			List<List<Object>> results = new ArrayList<List<Object>>();
			javax.persistence.Query nativeQuery = ServletInitializer.getEntityManager().createNativeQuery(query.getQueryString());
			
			for(Map.Entry<Integer, Object> entry : query.getParameters().entrySet())
				nativeQuery.setParameter(entry.getKey(), entry.getValue());
	
			for(Object row : nativeQuery.getResultList()) {
				//We cannot use Arrays.asList() since the result wouldn't be serializable by GWT
				List<Object> rowList = new ArrayList<Object>();
				for(Object obj : (Object[]) row)
					rowList.add(obj);
				results.add(rowList);
			}
	
			return results;
		}
	}

	@Override
	public WhiteList whiteListDummy(WhiteList whiteList) {
		throw new IllegalArgumentException("This is just a white list registration dummy method");
	}

}
