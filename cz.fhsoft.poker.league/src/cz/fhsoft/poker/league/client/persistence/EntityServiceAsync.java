package cz.fhsoft.poker.league.client.persistence;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.persistence.EntityService.EntityWithDataVersion;
import cz.fhsoft.poker.league.client.persistence.EntityService.WhiteList;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public interface EntityServiceAsync {

	void getDataVersion(AsyncCallback<Long> callback);

	<E extends IdentifiableEntity> void list(String entityClass, AsyncCallback<List<E>> callback);

	<E extends IdentifiableEntity> void find(String entityClass, Number id, AsyncCallback<E> callback);

	<E extends IdentifiableEntity> void persist(E entity, AsyncCallback<EntityWithDataVersion<E>> callback);

	<E extends IdentifiableEntity> void merge(E entity, AsyncCallback<EntityWithDataVersion<E>> callback);

	<E extends IdentifiableEntity> void remove(E entity, AsyncCallback<Long> callback);

	void executeNativeQuery(NativeQuery query, AsyncCallback<List<List<Object>>> callback);

	<E extends IdentifiableEntity> void resolveReference(String entityClass, Number id, String referenceName, AsyncCallback<List<E>> callback);

	void whiteListDummy(WhiteList whiteList, AsyncCallback<WhiteList> callback);
}
