package cz.fhsoft.poker.league.shared.persistence;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public interface LazyCollection<E extends IdentifiableEntity, R extends IdentifiableEntity, C extends Collection<R>> {

	LazyCollection<E, R, C> createClone();

	void resolve(final AsyncCallback<C> callback);
	
	public void unresolve();
	
	boolean isResolved();
}
