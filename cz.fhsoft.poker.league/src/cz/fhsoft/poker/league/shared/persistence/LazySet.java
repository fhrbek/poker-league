package cz.fhsoft.poker.league.shared.persistence;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public class LazySet<E extends IdentifiableEntity, R extends IdentifiableEntity> extends HashSet<R> implements LazyCollection<E, R, Set<R>> {
	
	private static final long serialVersionUID = 2315646646240805846L;

	private E referrer;
	
	private String referenceName;

	private boolean resolved;
	
	protected LazySet() {
	}
	
	public LazySet<E, R> createClone() {
		LazySet<E, R> clone = new LazySet<E, R>();
		clone.referrer = referrer;
		clone.referenceName = referenceName;
		clone.resolved = resolved;
		
		if(resolved)
			clone.addAll(this);

		return clone;
	}
	
	public LazySet(E referrer, String referenceName) {
		this.referrer = referrer;
		this.referenceName = referenceName;
	}

	public LazySet(E referrer, String referenceName, Collection<R> items) {
		this(referrer, referenceName);
		this.resolved = true;
		addAll(items);
	}

	public void reset(Collection<R> items) {
		this.resolved = true;
		clear();
		addAll(items);
	}

	public void resolve(final AsyncCallback<Set<R>> callback) {
		if(!GWT.isClient())
			throw new IllegalArgumentException("This method can only be called from the client");

		if(resolved) {
			callback.onSuccess(this);
			return;
		}

		ClientEntityManager.getInstance().resolveReference(referrer.getClass(), referrer.getId(), referenceName, new AsyncCallback<List<R>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<R> resolvedReference) {
				resolved = true;
				clear();
				addAll(resolvedReference);
				callback.onSuccess(LazySet.this);
			}
			
		});
	}

	@Override
	public boolean isResolved() {
		return resolved;
	}

	@Override
	public boolean add(R e) {
		assertResolved();
		
		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends R> c) {
		assertResolved();
		
		return super.addAll(c);
	}

	@Override
	public void clear() {
		assertResolved();
		
		super.clear();
	}

	@Override
	public boolean contains(Object o) {
		assertResolved();
		
		return super.contains(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		assertResolved();
		
		return super.containsAll(c);
	}
	
	@Override
	public boolean isEmpty() {
		assertResolved();
		
		return super.isEmpty();
	}
	
	@Override
	public boolean remove(Object o) {
		assertResolved();
		
		return super.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		assertResolved();
		
		return super.removeAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		assertResolved();
		
		return super.retainAll(c);
	}
	
	@Override
	public Object clone() {
		return super.clone();
	}
	
	@Override
	public Object[] toArray() {
		assertResolved();
		
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		assertResolved();
		
		return super.toArray(a);
	}

	@Override
	public String toString() {
		if(resolved)
			return super.toString();
		
		return "Unresolved set " + referrer.getClass() + "@" + referrer.getId() + ":" + referenceName;
	}

	private void assertResolved() {
		if(GWT.isClient() && !resolved)
			throw new IllegalArgumentException("This set is not resolved");
	}
}
