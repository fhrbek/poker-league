package cz.fhsoft.poker.league.client.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;
import cz.fhsoft.poker.league.shared.persistence.LazyList;
import cz.fhsoft.poker.league.shared.persistence.LazySet;

@RemoteServiceRelativePath("entityService")
public interface EntityService extends RemoteService {
	public static class WhiteList implements Serializable {
		private static final long serialVersionUID = -1356352194652396619L;

		LazyList<IdentifiableEntity, IdentifiableEntity> lazyList;
		LazySet<IdentifiableEntity, IdentifiableEntity> lazySet;
		Integer integer;
		Date date;
		Timestamp timestamp;
	}

	WhiteList whiteListDummy(WhiteList whiteList);
	
	public static class EntityWithDataVersion<E extends IdentifiableEntity> implements Serializable {
		private static final long serialVersionUID = 9100516256334065760L;

		E entity;

		long dataVersion;
		
		protected EntityWithDataVersion() {
			
		}
		
		public EntityWithDataVersion(E entity, long dataVersion) {
			this.entity = entity;
			this.dataVersion = dataVersion;
		}
	}

	long getDataVersion();

	<E extends IdentifiableEntity> List<E> list(String entityClass);

	<E extends IdentifiableEntity> E find(String entityClass, Number id);

	<E extends IdentifiableEntity> EntityWithDataVersion<E> persist(E entity);

	<E extends IdentifiableEntity> EntityWithDataVersion<E> merge(E entity);

	<E extends IdentifiableEntity> long remove(E entity);

	List<List<Object>> executeNativeQuery(NativeQuery query);

	<E extends IdentifiableEntity> List<E> resolveReference(String entityClass, Number id, String referenceName);
}
