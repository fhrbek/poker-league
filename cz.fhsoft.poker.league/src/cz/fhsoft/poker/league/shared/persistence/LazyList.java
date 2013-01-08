package cz.fhsoft.poker.league.shared.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public class LazyList<E extends IdentifiableEntity, R extends IdentifiableEntity> extends ArrayList<R> implements LazyCollection<E, R, List<R>> {
	
	private static final long serialVersionUID = 2315646646240805846L;

	private E referrer;
	
	private String referenceName;

	private boolean resolved;
	
	protected LazyList() {
	}
	
	public LazyList<E, R> createClone() {
		LazyList<E, R> clone = new LazyList<E, R>();
		clone.referrer = referrer;
		clone.referenceName = referenceName;
		clone.resolved = resolved;
		
		if(resolved)
			clone.addAll(this);

		return clone;
	}
	
	public LazyList(E referrer, String referenceName) {
		this.referrer = referrer;
		this.referenceName = referenceName;
	}

	public LazyList(E referrer, String referenceName, Collection<R> items) {
		this(referrer, referenceName);
		this.resolved = true;
		addAll(items);
	}
	
	public void reset(Collection<R> items) {
		this.resolved = true;
		clear();
		addAll(items);
	}

	public void resolve(final AsyncCallback<List<R>> callback) {
		if(!GWT.isClient())
			throw new IllegalArgumentException("This method can only be called from the client");

		if(resolved)
			callback.onSuccess(this);

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
				callback.onSuccess(LazyList.this);
			}
			
		});
	}

	@Override
	public boolean isResolved() {
		return resolved;
	}
	
	@Override
	public int indexOf(Object o) {
		assertResolved();
		
		return super.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		assertResolved();
		
		return super.lastIndexOf(o);
	}

	@Override
	public boolean add(R e) {
		assertResolved();
		
		return super.add(e);
	}

	@Override
	public void add(int index, R e) {
		assertResolved();
		
		super.add(index, e);
	}

	@Override
	public boolean addAll(Collection<? extends R> c) {
		assertResolved();
		
		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends R> c) {
		assertResolved();
		
		return super.addAll(index, c);
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
	public R get(int index) {
		assertResolved();
		
		return super.get(index);
	}

	@Override
	public ListIterator<R> listIterator() {
		assertResolved();
		
		return super.listIterator();
	}

	@Override
	public ListIterator<R> listIterator(int index) {
		assertResolved();
		
		return super.listIterator(index);
	}

	@Override
	public R set(int index, R element) {
		assertResolved();
		
		return super.set(index, element);
	}

	@Override
	public List<R> subList(int fromIndex, int toIndex) {
		assertResolved();
		
		return super.subList(fromIndex, toIndex);
	}

	@Override
	public void removeRange(int fromIndex, int toIndex) {
		assertResolved();
		
		super.removeRange(fromIndex, toIndex);
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
		
		return "Unresolved list " + referrer.getClass() + "@" + referrer.getId() + ":" + referenceName;
	}

	@Override
	public void trimToSize() {
		assertResolved();
		
		super.trimToSize();
	}

	private void assertResolved() {
		if(GWT.isClient() && !resolved)
			throw new IllegalArgumentException("This list is not resolved");
	}
}
