package cz.fhsoft.poker.league.client.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.persistence.EntityService.EntityWithDataVersion;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public class ClientEntityManager {
	
	private static ClientEntityManager instance = new ClientEntityManager();
	
	private EntityServiceAsync entityService = GWT.create(EntityService.class);

	private Map<Class<? extends IdentifiableEntity>, Map<Object, IdentifiableEntity>> entities = new HashMap<Class<? extends IdentifiableEntity>, Map<Object, IdentifiableEntity>>();
	
	private List<Class<? extends IdentifiableEntity>> loadedLists = new ArrayList<Class<? extends IdentifiableEntity>>();
	
	private long recentDataVersion;
	
	private EventBus eventBus;

	private ClientEntityManager() {
		
	}
	
	public static ClientEntityManager getInstance() {
		return instance;
	}
	
	
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void checkDataVersion(final AsyncCallback<Void> callback) {
		entityService.getDataVersion(new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Long newDataVersion) {
				checkDataVersion(newDataVersion);
				callback.onSuccess(null);
			}
			
		});
	}
	
	public boolean checkDataVersion(long newDataVersion) {
		boolean result = false;

		if(newDataVersion > recentDataVersion) {
			if(recentDataVersion > 0) {
				invalidateCache();
				if(eventBus != null)
					eventBus.fireEvent(new DataChangeEvent());
				
				result = true;
			}
			
			recentDataVersion = newDataVersion;
		}
		
		return result;
	}

	public void invalidateCache() {
		for(Map.Entry<Class<? extends IdentifiableEntity>, Map<Object, IdentifiableEntity>> entry : entities.entrySet())
			for(IdentifiableEntity entity : entry.getValue().values())
				entity.setProxy(true);
		
		loadedLists.clear();
	}

	private Map<Class<? extends IdentifiableEntity>, List<AsyncCallback<List<? extends IdentifiableEntity>>>> listCallbackMap =
			new HashMap<Class<? extends IdentifiableEntity>, List<AsyncCallback<List<? extends IdentifiableEntity>>>>();

	@SuppressWarnings("unchecked")
	private <E extends IdentifiableEntity> List<AsyncCallback<List<E>>> listUncheckedCastToE(List<AsyncCallback<List<? extends IdentifiableEntity>>> list) {
		return (List<AsyncCallback<List<E>>>) (List<?>) list;
	}

	@SuppressWarnings("unchecked")
	private <E extends IdentifiableEntity> List<AsyncCallback<List<? extends IdentifiableEntity>>> listUncheckedCastFromE(List<AsyncCallback<List<E>>> list) {
		return (List<AsyncCallback<List<? extends IdentifiableEntity>>>) (List<?>) list;
	}

	public <E extends IdentifiableEntity> void list(final Class<E> clazz, AsyncCallback<List<E>> callback) {
		List<AsyncCallback<List<E>>> auxPendingCallbacks = listUncheckedCastToE(listCallbackMap.get(clazz));
		
		if(auxPendingCallbacks == null)
			listCallbackMap.put(clazz, listUncheckedCastFromE(auxPendingCallbacks = new ArrayList<AsyncCallback<List<E>>>()));

		final List<AsyncCallback<List<E>>> pendingCallbacks = auxPendingCallbacks;
		pendingCallbacks.add(callback);
		
		if(pendingCallbacks.size() > 1)
			return; // the operation is already there, let's just get the response

		if(loadedLists.contains(clazz)) {
			List<E> cachedEntities = new ArrayList<E>();
			@SuppressWarnings("unchecked")
			Collection<? extends E> castCollection = (Collection<? extends E>) entities.get(clazz).values(); 
			cachedEntities.addAll(castCollection);
			try {
				for(AsyncCallback<List<E>> pendingCallback : pendingCallbacks)
					try {
						pendingCallback.onSuccess(cachedEntities);
					}
					catch(Throwable t) {
						ErrorReporter.error(t);
					}
			}
			finally {
				pendingCallbacks.clear();
			}
			return;
		}

		entityService.list(clazz.getName(), new AsyncCallback<List<E>>() {

			@Override
			public void onFailure(Throwable caught) {
				try {
					for(AsyncCallback<List<E>> callback : pendingCallbacks)
						try {
							callback.onFailure(caught);
						}
						catch(Throwable t) {
							ErrorReporter.error(t);
						}
				}
				finally {
					pendingCallbacks.clear();
				}
			}

			@Override
			public void onSuccess(List<E> retrievedEntities) {
				
				try {
					Map<Integer, E> replacements = null;
					int i=0;

					for(E entity : retrievedEntities) {     
						if(entity != null) {
							Map<Object, IdentifiableEntity> loaded = entities.get(entity.getClass());
							if(loaded == null)
								entities.put(entity.getClass(), loaded = new HashMap<Object, IdentifiableEntity>());

							@SuppressWarnings("unchecked")
							E existingEntity = (E) loaded.get(entity.getId());

							if(existingEntity != null && !existingEntity.isProxy()) {
								if(replacements == null)
									replacements = new HashMap<Integer, E>();
								replacements.put(i, existingEntity);
							}
							else
								loaded.put(entity.getId(), entity);
						}
						
						i++;
					}
					
					if(replacements != null)
						for(Map.Entry<Integer, E> entry : replacements.entrySet())
							retrievedEntities.set(entry.getKey(), entry.getValue());

					loadedLists.add(clazz);
					for(AsyncCallback<List<E>> callback : pendingCallbacks)
						try {
							callback.onSuccess(retrievedEntities);
						}
						catch(Throwable t) {
							ErrorReporter.error(t);
						}
				}
				catch(Throwable t) {
					for(AsyncCallback<List<E>> callback : pendingCallbacks)
						try {
							callback.onFailure(t);
						}
						catch(Throwable t1) {
							ErrorReporter.error(t1);
						}
				}
				finally {
					pendingCallbacks.clear();
				}
			}
			
		});
			
	}

	private Map<Class<? extends IdentifiableEntity>, Map<Number, List<AsyncCallback<? extends IdentifiableEntity>>>> findCallbackMap =
			new HashMap<Class<? extends IdentifiableEntity>, Map<Number, List<AsyncCallback<? extends IdentifiableEntity>>>>();
	
	@SuppressWarnings("unchecked")
	private <E extends IdentifiableEntity> Map<Number, List<AsyncCallback<E>>> findUncheckedCastToE(Map<Number, List<AsyncCallback<? extends IdentifiableEntity>>> map) {
		return (Map<Number, List<AsyncCallback<E>>>) (Map<?, ?>) map;
	}

	@SuppressWarnings("unchecked")
	private <E extends IdentifiableEntity> Map<Number, List<AsyncCallback<? extends IdentifiableEntity>>> findUncheckedCastFromE(Map<Number, List<AsyncCallback<E>>> map) {
		return (Map<Number, List<AsyncCallback<? extends IdentifiableEntity>>>) (Map<?, ?>) map;
	}

	public <E extends IdentifiableEntity> void find(Class<E> entityClass, final Number id, AsyncCallback<E> callback) {
		Map<Object, IdentifiableEntity> auxLoaded = entities.get(entityClass);
		if(auxLoaded == null)
			entities.put(entityClass, auxLoaded = new HashMap<Object, IdentifiableEntity>());
		
		final Map<Object, IdentifiableEntity> loaded = auxLoaded;

		@SuppressWarnings("unchecked")
		E entity = (E) loaded.get(id);
		if(entity != null && !entity.isProxy())
			callback.onSuccess(entity);
		else {
			Map<Number, List<AsyncCallback<E>>> auxPendingCallbackMap = findUncheckedCastToE(findCallbackMap.get(entityClass));
			
			if(auxPendingCallbackMap == null)
				findCallbackMap.put(entityClass, findUncheckedCastFromE(auxPendingCallbackMap = new HashMap<Number, List<AsyncCallback<E>>>()));
			
			final Map<Number, List<AsyncCallback<E>>> pendingCallbackMap = auxPendingCallbackMap;

			List<AsyncCallback<E>> auxPendingCallbacks = auxPendingCallbackMap.get(id);
			
			if(auxPendingCallbacks == null)
				auxPendingCallbackMap.put(id, auxPendingCallbacks = new ArrayList<AsyncCallback<E>>());

			final List<AsyncCallback<E>> pendingCallbacks = auxPendingCallbacks;
			pendingCallbacks.add(callback);
			
			if(pendingCallbacks.size() > 1)
				return; // the operation is already there, let's just get the response

			entityService.find(entityClass.getName(), id, new AsyncCallback<E>() {

				@Override
				public void onFailure(Throwable caught) {
					try {
						for(AsyncCallback<E> pendingCallback : pendingCallbacks)
							try {
								pendingCallback.onFailure(caught);
							}
							catch(Throwable t) {
								ErrorReporter.error(t);
							}
					}
					finally {
						pendingCallbacks.clear();
						pendingCallbackMap.remove(id);
					}
				}

				@Override
				public void onSuccess(E entity) {
					try {
						if(entity != null)
							loaded.put(id, entity);
						else
							loaded.remove(id);

						for(AsyncCallback<E> pendingCallback : pendingCallbacks)
							try {
								pendingCallback.onSuccess(entity);
							}
							catch(Throwable t) {
								ErrorReporter.error(t);
							}
					}
					catch(Throwable t) {
						for(AsyncCallback<E> callback : pendingCallbacks)
							try {
								callback.onFailure(t);
							}
							catch(Throwable t1) {
								ErrorReporter.error(t1);
							}
					}
					finally {
						pendingCallbacks.clear();
						pendingCallbackMap.remove(id);
					}
				}
				
			});
		}
	}
	
	public <E extends IdentifiableEntity> void persist(E entity, final AsyncCallback<E> callback) {
		entityService.persist(entity, new AsyncCallback<EntityWithDataVersion<E>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(EntityWithDataVersion<E> persistedEntityWithDataVersion) {
				E persistedEntity = persistedEntityWithDataVersion.entity;
				Class<? extends IdentifiableEntity> entityClass = persistedEntity.getClass();
				Map<Object, IdentifiableEntity> loaded = entities.get(entityClass);
				if(loaded == null)
					entities.put(entityClass, loaded = new HashMap<Object, IdentifiableEntity>());
				
				loaded.put(persistedEntity.getId(), persistedEntity);

				callback.onSuccess(persistedEntity);

				checkDataVersion(persistedEntityWithDataVersion.dataVersion);
			}
			
		});
	}
	
	public <E extends IdentifiableEntity> void merge(E entity, final AsyncCallback<E> callback) {
		entityService.merge(entity, new AsyncCallback<EntityWithDataVersion<E>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(EntityWithDataVersion<E> persistedEntityWithDataVersion) {
				E persistedEntity = persistedEntityWithDataVersion.entity;
				Class<? extends IdentifiableEntity> entityClass = persistedEntity.getClass();
				Map<Object, IdentifiableEntity> loaded = entities.get(entityClass);
				if(loaded == null)
					entities.put(entityClass, loaded = new HashMap<Object, IdentifiableEntity>());
				
				loaded.put(persistedEntity.getId(), persistedEntity);
				
				callback.onSuccess(persistedEntity);

				checkDataVersion(persistedEntityWithDataVersion.dataVersion);
			}
			
		});
	}
	
	public <E extends IdentifiableEntity> void remove(final E entity, final AsyncCallback<Void> callback) {
		entityService.remove(entity, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Long dataVersion) {
				Class<? extends IdentifiableEntity> entityClass = entity.getClass();
				Map<Object, IdentifiableEntity> loaded = entities.get(entityClass);
				if(loaded != null)
					loaded.remove(entity.getId());

				callback.onSuccess(null);

				checkDataVersion(dataVersion);
			}
			
		});
	}
	
	public void executeNativeQuery(NativeQuery query, AsyncCallback<List<List<Object>>> callback) {
		entityService.executeNativeQuery(query, callback);
	}
	
	public <E extends IdentifiableEntity, R extends IdentifiableEntity> void resolveReference(Class<E> entityClass, final Number id, final String referenceName, final AsyncCallback<List<R>> callback) {
		entityService.resolveReference(entityClass.getName(), id, referenceName, new AsyncCallback<List<R>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<R> resolvedEntities) {

				Map<Integer, R> replacements = null;
				int i=0;

				for(R entity : resolvedEntities) {     
					if(entity != null) {
						Map<Object, IdentifiableEntity> loaded = entities.get(entity.getClass());
						if(loaded == null)
							entities.put(entity.getClass(), loaded = new HashMap<Object, IdentifiableEntity>());

						@SuppressWarnings("unchecked")
						R existingEntity = (R) loaded.get(entity.getId());

						if(existingEntity != null && !existingEntity.isProxy()) {
							if(replacements == null)
								replacements = new HashMap<Integer, R>();
							replacements.put(i, existingEntity);
						}
						else
							loaded.put(entity.getId(), entity);
					}
					
					i++;
				}
				
				if(replacements != null)
					for(Map.Entry<Integer, R> entry : replacements.entrySet())
						resolvedEntities.set(entry.getKey(), entry.getValue());

				callback.onSuccess(resolvedEntities);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <E extends IdentifiableEntity> void resolveEntity(E entity, AsyncCallback<E> callback) {
		if(!entity.isProxy())
			callback.onSuccess(entity);
		else
			find((Class<E>)entity.getClass(), entity.getId(), callback);
	}
	
	public <E extends IdentifiableEntity> void registerEntity(E entity) {
		@SuppressWarnings("unchecked")
		Class<E> entityClass = (Class<E>) entity.getClass();
		Map<Object, IdentifiableEntity> auxLoaded = entities.get(entityClass);
		if(auxLoaded == null)
			entities.put(entityClass, auxLoaded = new HashMap<Object, IdentifiableEntity>());
		
		Map<Object, IdentifiableEntity> loaded = auxLoaded;
		@SuppressWarnings("unchecked")
		E existingEntity = (E) loaded.get(entity.getId());
		if(existingEntity != null && existingEntity != entity)
			existingEntity.setProxy(true);
		loaded.put(entity.getId(), entity);
	}
}
