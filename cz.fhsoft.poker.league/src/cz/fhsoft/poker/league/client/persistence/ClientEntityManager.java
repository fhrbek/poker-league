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
	
	private void checkDataVersion(long newDataVersion) {
		if(newDataVersion > recentDataVersion) {
			if(recentDataVersion > 0) {
				invalidateCache();
				if(eventBus != null)
					eventBus.fireEvent(new DataChangeEvent());
			}
			
			recentDataVersion = newDataVersion;
		}
	}

	public void invalidateCache() {
		for(Map.Entry<Class<? extends IdentifiableEntity>, Map<Object, IdentifiableEntity>> entry : entities.entrySet())
			for(IdentifiableEntity entity : entry.getValue().values())
				entity.setProxy(true);
		
		loadedLists.clear();
	}

	public <E extends IdentifiableEntity> void list(final Class<E> clazz, final AsyncCallback<List<E>> callback) {
		if(loadedLists.contains(clazz)) {
			List<E> cachedEntities = new ArrayList<E>();
			@SuppressWarnings("unchecked")
			Collection<? extends E> castCollection = (Collection<? extends E>) entities.get(clazz).values(); 
			cachedEntities.addAll(castCollection);
			callback.onSuccess(cachedEntities);
			return;
		}

		entityService.list(clazz.getName(), new AsyncCallback<List<E>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<E> retrievedEntities) {

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
				callback.onSuccess(retrievedEntities);
			}
			
		});
	}

	public <E extends IdentifiableEntity> void find(Class<E> entityClass, final Number id, final AsyncCallback<E> callback) {
		Map<Object, IdentifiableEntity> auxLoaded = entities.get(entityClass);
		if(auxLoaded == null)
			entities.put(entityClass, auxLoaded = new HashMap<Object, IdentifiableEntity>());
		
		final Map<Object, IdentifiableEntity> loaded = auxLoaded;

		@SuppressWarnings("unchecked")
		E entity = (E) loaded.get(id);
		if(entity != null && !entity.isProxy())
			callback.onSuccess(entity);
		else
			entityService.find(entityClass.getName(), id, new AsyncCallback<E>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(E entity) {
					if(entity != null)
						loaded.put(id, entity);
					else
						loaded.remove(id);

					callback.onSuccess(entity);
				}
				
			});
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
}
