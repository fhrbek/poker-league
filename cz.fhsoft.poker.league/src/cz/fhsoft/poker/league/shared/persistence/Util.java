package cz.fhsoft.poker.league.shared.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public class Util {

	@SuppressWarnings("unchecked")
	public static <E extends IdentifiableEntity, R extends IdentifiableEntity, C extends Collection<R>> LazyCollection<E, R, C> asLazyCollection(Collection<R> collection) {
		return (LazyCollection<E, R, C>) collection;
	}

	@SuppressWarnings("unchecked")
	public static <E extends IdentifiableEntity, R extends IdentifiableEntity> LazyList<E, R> asLazyList(List<R> list) {
		return (LazyList<E, R>) list;
	}

	@SuppressWarnings("unchecked")
	public static <E extends IdentifiableEntity, R extends IdentifiableEntity> LazySet<E, R> asLazySet(Set<R> set) {
		return (LazySet<E, R>) set;
	}

	public static <E extends IdentifiableEntity, R extends IdentifiableEntity> Set<E> cloneSet(Set<E> set) {
		if(set instanceof LazySet) {
			return asLazySet(set).createClone(); 
		}

		return new HashSet<E>(set);
	}

	public static <E extends IdentifiableEntity, R extends IdentifiableEntity> List<E> cloneList(List<E> list) {
		if(list instanceof LazyList) {
			return asLazyList(list).createClone(); 
		}

		return new ArrayList<E>(list);
	}

	public static <E extends IdentifiableEntity> Set<E> asSet(Collection<E> selectedEntities) {
		if(selectedEntities instanceof Set)
			return (Set<E>) selectedEntities;

		return new HashSet<E>(selectedEntities);
	}
	
	public static <E extends IdentifiableEntity> void resolve(Set<E> set, AsyncCallback<Set<E>> callback) {
		if(set instanceof LazySet)
			asLazySet(set).resolve(callback);
		else
			callback.onSuccess(set);
	}
	
	public static <E extends IdentifiableEntity> void resolve(List<E> list, AsyncCallback<List<E>> callback) {
		if(list instanceof LazyList)
			asLazyList(list).resolve(callback);
		else
			callback.onSuccess(list);
	}

	public static <E extends IdentifiableEntity> E proxify(E entity, E newInstance) {
		if(entity != null && entity.getId() > 0) {
			newInstance.setProxy(true);
			newInstance.setId(entity.getId());
		}
		else
			newInstance = null;

		return newInstance;
	}

	@SuppressWarnings("unchecked")
	public static <E extends IdentifiableEntity> boolean proxify(Collection<E> entities) {
		if(entities instanceof LazyCollection) {
			((LazyCollection<E, ?, ?>) entities).unresolve();
			return true;
		}
		
		return false;
	}


	@SuppressWarnings("unchecked")
	public static <E extends IdentifiableEntity> boolean isResolved(Collection<E> entities) {
		if(entities instanceof LazyCollection)
			return ((LazyCollection<E, ?, ?>) entities).isResolved();
		
		return true;
	}
}
