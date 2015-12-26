package cz.fhsoft.poker.league.client.widget;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.persistence.EntityDigestProvider;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;
import cz.fhsoft.poker.league.shared.persistence.LazyCollection;
import cz.fhsoft.poker.league.shared.persistence.Util;

public class EntityMultiSelector<E extends IdentifiableEntity> extends FlowPanel {
	
	private Class<E> entityClass;
	
	private Comparator<? super E> entityComparator;
	
	private EntityDigestProvider<? super E> entityDigestProvider;
	
	private Map<CheckBox, E> checkBoxMap;

	public EntityMultiSelector(Class<E> entityClass, Comparator<? super E> entityComparator, EntityDigestProvider<? super E> entityDigestProvider) {
		this.entityClass = entityClass;
		this.entityComparator = entityComparator;
		this.entityDigestProvider = entityDigestProvider;
		
		addStyleName("multiSelector");
		
		checkBoxMap = new HashMap<CheckBox, E>();
	}

	public void setSelectedEntities(Collection<E> entities) {
		if(entities instanceof LazyCollection)
			Util.asLazyCollection(entities).resolve(new AsyncCallback<Collection<E>>() {

				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}

				@Override
				public void onSuccess(Collection<E> resolvedEntities) {
					setResolvedSelectedEntities(resolvedEntities);
				}
				
			});
		else
			setResolvedSelectedEntities(entities);
	}

	private void setResolvedSelectedEntities(final Collection<E> entities) {
		clear();
		checkBoxMap.clear();
		
		final Set<Integer> ids = new HashSet<Integer>(entities.size());
		for(E entity : entities)
			ids.add(entity.getId());
		
		ClientEntityManager.getInstance().list(entityClass, new AsyncCallback<List<E>>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(List<E> allEntities) {
				Collections.sort(allEntities, entityComparator);
				
				for(E entity : allEntities) {
					CheckBox checkBox = new CheckBox(entityDigestProvider.getDigest(entity));
					checkBox.addStyleName("multiSelectorCheckBox");
					checkBox.setValue(ids.contains(entity.getId()));
					checkBoxMap.put(checkBox, entity);
					add(checkBox);
				}
			}
			
		});
	}

	public Collection<E> getSelectedEntities() {
		Set<E> selectedEntities = new HashSet<E>(checkBoxMap.size());
		
		for(Map.Entry<CheckBox, E> entry : checkBoxMap.entrySet())
			if(entry.getKey().getValue())
				selectedEntities.add(entry.getValue());
		
		return selectedEntities;
	}
}
