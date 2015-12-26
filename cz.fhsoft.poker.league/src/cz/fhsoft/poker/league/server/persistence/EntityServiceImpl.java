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

import cz.fhsoft.poker.league.client.persistence.EntityService;
import cz.fhsoft.poker.league.client.persistence.NativeQuery;
import cz.fhsoft.poker.league.server.AbstractServiceImpl;
import cz.fhsoft.poker.league.server.ServletInitializer;
import cz.fhsoft.poker.league.server.util.ExceptionUtil;
import cz.fhsoft.poker.league.shared.model.v1.DataVersion;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;
import cz.fhsoft.poker.league.shared.persistence.LazyCollection;
import cz.fhsoft.poker.league.shared.persistence.LazyList;
import cz.fhsoft.poker.league.shared.persistence.LazySet;
import cz.fhsoft.poker.league.shared.util.TransferrableException;

@SuppressWarnings("serial")
public class EntityServiceImpl extends AbstractServiceImpl implements EntityService {
	
	public static interface DataAction<T> {
		T run() throws TransferrableException;
	}
	
	public static final Object LOCK = new Object();
	
	private static final int DATA_VERSION_ID = 1;
	
	public static final <T>  T doWithLock(final DataAction<T> action) throws TransferrableException {
		//Use both java and database lock for case there are multiple entity managers.
		//Besides that, clear entity manager for each action to ensure that fresh data is loaded.
		//Although not so efficient, it should make the system safe when deployed on free GAE which starts as many machines as it wants
		synchronized(LOCK) {
			boolean inSuperTransaction = ServletInitializer.getEntityManager().getTransaction().isActive();

			try {
				if(!inSuperTransaction) {
					ServletInitializer.getEntityManager().clear();
					ServletInitializer.getEntityManager().getEntityManagerFactory().getCache().evictAll();
					ServletInitializer.getEntityManager().getTransaction().begin();
				}

				DataVersion dataVersion = ServletInitializer.getEntityManager().find(DataVersion.class, DATA_VERSION_ID);
				ServletInitializer.getEntityManager().lock(dataVersion, LockModeType.PESSIMISTIC_WRITE);
				return action.run();
			}
			catch(Throwable t) {
				if(ServletInitializer.getEntityManager().getTransaction().isActive())
					ServletInitializer.getEntityManager().getTransaction().rollback();
				throw ExceptionUtil.transferrableException(t);
			}
			finally {
				if(!inSuperTransaction) {
					if(ServletInitializer.getEntityManager().getTransaction().isActive())
						ServletInitializer.getEntityManager().getTransaction().commit();
					ServletInitializer.getEntityManager().clear();
					ServletInitializer.getEntityManager().getEntityManagerFactory().getCache().evictAll();
				}
			}
		}
	}

	@Override
	public long getDataVersion() throws TransferrableException {
		return getDataVersionStatic();
	}

	public static long getDataVersionStatic() throws TransferrableException {
		return doWithLock(new DataAction<Long>() {

			@Override
			public Long run() {
				DataVersion dataVersion = ServletInitializer.getEntityManager().find(DataVersion.class, DATA_VERSION_ID);
				if(dataVersion == null) {
					dataVersion = new DataVersion();
					dataVersion.setId(DATA_VERSION_ID);
					dataVersion.setCurrentVersion(new Date());
					ServletInitializer.getEntityManager().persist(dataVersion);
				}
				
				return dataVersion.getCurrentVersion().getTime();
			}
			
		});
	}

	public static long updateDataVersion() throws TransferrableException {
		return doWithLock(new DataAction<Long>() {

			@Override
			public Long run() {
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
			
		});
	}

	@Override
	public <E extends IdentifiableEntity> E find(final String entityClass, final Number id) throws TransferrableException {
		return doWithLock(new DataAction<E>() {

			@Override
			public E run() throws TransferrableException {
				return find(entityClass, id, true);
			}
				
		});
	}
	
	@Override
	public <E extends IdentifiableEntity> List<E> list(final String entityClass) throws TransferrableException {
		return doWithLock(new DataAction<List<E>>() {

			@Override
			public List<E> run() throws TransferrableException {
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
				} catch (Throwable t) {
					throw ExceptionUtil.transferrableException(t);
				}
			}
				
		});
	}
	
	@Override
	public <E extends IdentifiableEntity> List<E> resolveReference(final String entityClass, final Number id, final String referenceName) throws TransferrableException {
		return doWithLock(new DataAction<List<E>>() {

			@Override
			public List<E> run() throws TransferrableException {
				IdentifiableEntity entity = find(entityClass, id, false);
				if(entity == null)
					return Collections.emptyList();
		
				//TODO Remove permanently - it should not be needed as we don't use caching at all
				//ServletInitializer.getEntityManager().refresh(entity);
		
				try {
					Field referenceField = ReflectUtil.getDeclaredField(entity.getClass(), referenceName);
					referenceField.setAccessible(true);
					@SuppressWarnings("unchecked")
					Collection<E> referenceList = (Collection<E>) referenceField.get(entity);
		
					List<E> transferrableList = new ArrayList<E>(referenceList.size());
					for(E referencedEntity : referenceList)
						transferrableList.add(makeTransferable(referencedEntity));
		
					return transferrableList;
		
				} catch (Throwable t) {
					throw ExceptionUtil.transferrableException(t);
				}
			}
				
		});
	}

	private <E extends IdentifiableEntity> E find(final String entityClass, final Number id, final boolean makeTransferrable) throws TransferrableException {
		return doWithLock(new DataAction<E>() {

			@Override
			public E run() throws TransferrableException {
				try {
					Class<?> realEntityClass = Class.forName(entityClass);
		
					@SuppressWarnings("unchecked")
					E entity = (E) ServletInitializer.getEntityManager().find(realEntityClass, id);
					
					if(entity != null && makeTransferrable)
						makeTransferable(entity);
					
					return entity;
				} catch (Throwable t) {
					throw ExceptionUtil.transferrableException(t);
				}
			}
			
		});
	}

	public static <E extends IdentifiableEntity> E makeTransferable(E entity) throws TransferrableException {
		Set<IdentifiableEntity> visited = new HashSet<IdentifiableEntity>();
		return makeTransferable(entity, visited, false);
	}

	private static <E extends IdentifiableEntity> E makeTransferable(final E entity, final Set<IdentifiableEntity> visited, final boolean asProxy) throws TransferrableException {
		return doWithLock(new DataAction<E>() {

			@Override
			public E run() throws TransferrableException {
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
								throw ExceptionUtil.transferrableException(e);
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
								throw ExceptionUtil.transferrableException(e);
							} 
						}
					}
				}
				finally {
					ServletInitializer.getEntityManager().detach(entity);
				}
		
				return entity;
			}
			
		});
	}

	@Override
	public <E extends IdentifiableEntity> EntityWithDataVersion<E> persist(final E entity) throws TransferrableException {
		return doWithLock(new DataAction<EntityWithDataVersion<E>>() {

			@Override
			public EntityWithDataVersion<E> run() throws TransferrableException {
				
				long dataVersion = 0;
				
				localResolveExistingReferences(entity, false);
		
				ServletInitializer.getEntityManager().persist(entity);
				dataVersion = updateDataVersion();
				
				ServletInitializer.getEntityManager().getTransaction().commit();

				return new EntityWithDataVersion<E>(makeTransferable(entity), dataVersion);
			}
			
		});
	}
	
	private <E extends IdentifiableEntity> E localResolveExistingReferences(E entity, boolean forMerge) throws TransferrableException {
		synchronized(LOCK) {
			Set<IdentifiableEntity> visited = new HashSet<IdentifiableEntity>();
			return localResolveExistingReferences(entity, forMerge, visited);
		}
	}

	private <E extends IdentifiableEntity> E localResolveExistingReferences(final E entity, final boolean forMerge, final Set<IdentifiableEntity> visited) throws TransferrableException {
		return doWithLock(new DataAction<E>() {

			@Override
			public E run() throws TransferrableException {
				if(entity == null || !visited.add(entity))
					return entity;
		
				if(entity.getId() < 0)
					entity.setId(0); // fix temporary IDs from the client

				try {
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
				} catch (Throwable t) {
					throw ExceptionUtil.transferrableException(t);
				}
				
				return entity;
			}
			
		});
	}

	@Override
	public <E extends IdentifiableEntity> EntityWithDataVersion<E> merge(final E entity) throws TransferrableException {
		return doWithLock(new DataAction<EntityWithDataVersion<E>>() {

			@Override
			public EntityWithDataVersion<E> run() throws TransferrableException {
				E localEntity = entity;
				
				long dataVersion = 0;
				
				localResolveExistingReferences(localEntity, true);
		
				localEntity = ServletInitializer.getEntityManager().merge(localEntity);
				dataVersion = updateDataVersion();
				
				ServletInitializer.getEntityManager().getTransaction().commit();

				return new EntityWithDataVersion<E>(makeTransferable(localEntity), dataVersion);
			}
			
		});
	}

	@Override
	public <E extends IdentifiableEntity> long remove(final E entity) throws TransferrableException {
		return doWithLock(new DataAction<Long>() {

			@Override
			public Long run() throws TransferrableException {
				@SuppressWarnings("unchecked")
				E managedEntity = (E) ServletInitializer.getEntityManager().find(entity.getClass(), entity.getId(), LockModeType.PESSIMISTIC_WRITE);
				if(managedEntity != null) {
					long dataVersion = 0;

					ServletInitializer.getEntityManager().remove(managedEntity);
					dataVersion = updateDataVersion();
					
					return dataVersion;
				}
				
				return getDataVersion();
			}
			
		});
	}

	@Override
	synchronized public List<List<Object>> executeNativeQuery(NativeQuery query) throws TransferrableException {
		try {
			//this method does not lock the database since it's supposed to be used for read/view-only queries (such as result tables)
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
		} catch (Throwable t) {
			throw ExceptionUtil.transferrableException(t);
		}
	}

	@Override
	public WhiteList whiteListDummy(WhiteList whiteList) {
		throw new IllegalArgumentException("This is just a white list registration dummy method");
	}

}
