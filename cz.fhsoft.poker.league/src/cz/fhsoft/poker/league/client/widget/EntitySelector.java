package cz.fhsoft.poker.league.client.widget;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.persistence.EntityDigestProvider;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public class EntitySelector<E extends IdentifiableEntity> extends ListBox implements HasValue<E> {
	
	private Class<E> entityClass;
	
	private Comparator<? super E> entityComparator;
	
	private EntityDigestProvider<? super E> entityDigestProvider;
	
	private Map<Integer, E> valueMap = new HashMap<Integer, E>();
	
	public EntitySelector(Class<E> entityClass, Comparator<? super E> entityComparator, EntityDigestProvider<? super E> entityDigestProvider) {
		this.entityClass = entityClass;
		this.entityComparator = entityComparator;
		this.entityDigestProvider = entityDigestProvider;
	}

	public void setValue(E entity) {
		setValue(entity, false);
	}

	public void setValue(E entity, final boolean fireEvents) {
		if(entity != null)
			ClientEntityManager.getInstance().resolveEntity(entity, new AsyncCallback<E>() {
	
				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}
	
				@Override
				public void onSuccess(E resolvedEntity) {
					setResolvedSelectedEntity(resolvedEntity, fireEvents);
				}
				
			});
		else
			setResolvedSelectedEntity(entity, fireEvents);
	}

	private void setResolvedSelectedEntity(final E selectedEntity, final boolean fireEvents) {
		clear();
		valueMap.clear();
		
		ClientEntityManager.getInstance().list(entityClass, new AsyncCallback<List<E>>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(List<E> allEntities) {
				Collections.sort(allEntities, entityComparator);

				addItem("-- Vyberte položku ---", "0");
				int i = 1;
				int selectedIndex = 0;

				for(E entity : allEntities) {
					valueMap.put(i, entity);
					addItem(entityDigestProvider.getDigest(entity), "" + entity.getId());
					if(selectedEntity != null && entity.getId() == selectedEntity.getId())
						selectedIndex = i;
					i++;
				}
				
				setSelectedIndex(selectedIndex);
				if(fireEvents)
					ValueChangeEvent.fire(EntitySelector.this, selectedEntity);
			}
			
		});
	}

	public E getValue() {
		return valueMap.get(getSelectedIndex());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<E> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
