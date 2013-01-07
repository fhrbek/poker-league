package cz.fhsoft.poker.league.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.widget.AbstractTemporaryEntityEditor.SaveCallback;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public abstract class AbstractTemporaryEntityListEditor<E extends IdentifiableEntity> extends AbstractEntityListEditor<E> {
	
	private List<E> temporaryList = new ArrayList<E>();
	
	private int nextTemporaryId = -1;
	
	public AbstractTemporaryEntityListEditor(String newEntityLinkText, final AbstractTemporaryEntityEditor<E> entityEditor, DataProvider<E> dataProvider, List<LabeledColumn<E>> columns) {
		this(newEntityLinkText, entityEditor, dataProvider, columns, null);
	}

	public AbstractTemporaryEntityListEditor(String newEntityLinkText, final AbstractTemporaryEntityEditor<E> entityEditor, DataProvider<E> dataProvider, List<LabeledColumn<E>> columns,
			final Comparator<E> comparator) {
		super(newEntityLinkText, entityEditor, dataProvider, columns);
		
		entityEditor.setSaveCallback(new SaveCallback<E>() {

			@Override
			public void onSave(E entity, AsyncCallback<Void> callback) {
				if(entity.getId() == 0) {
					entity.setId(nextTemporaryId--);
					temporaryList.add(entity);
				}
				else {
					int index = 0;
					int indexToReplace = -1;
					for(E candidate : temporaryList) {
						if(candidate.getId() == entity.getId()) {
							indexToReplace = index;
							break;
						}
						index++;
					}
					
					if(indexToReplace != -1)
						temporaryList.set(indexToReplace, entity);
					else
						callback.onFailure(new Exception("Item to update was not found"));
				}

				callback.onSuccess(null);
				
				if(comparator != null)
					Collections.sort(temporaryList, comparator);

				// redraw the table
				setRowData(temporaryList);
			}
			
		});
	}
	
	public List<E> getEntities() {
		return temporaryList;
	}
	
	@Override
	protected void setEntities(List<E> entities) {
		if(temporaryList != null)
			temporaryList.clear();
		
		if(entities != null)
			temporaryList.addAll(entities);

		super.setEntities(temporaryList);
	}

	@Override
	protected void getEntity(int id, AsyncCallback<E> callback) {
		for(E candidate : temporaryList) {
			if(candidate.getId() == id) {
				if(candidate.isProxy())
					ClientEntityManager.getInstance().resolveEntity(candidate, callback);
				else
					callback.onSuccess(candidate);
				return;
			}
		}
		
		callback.onFailure(new Exception("Entity with id=" + id + " could not be found"));
	}
	
	@Override
	protected void removeItem(int id) {
		Iterator<E> iterator = temporaryList.iterator();
		
		while(iterator.hasNext()) {
			E candidate = iterator.next();
			if(candidate.getId() == id) {
				iterator.remove();
				break;
			}
		}

		// redraw the table
		setRowData(temporaryList);
	}
}
