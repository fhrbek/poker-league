package cz.fhsoft.poker.league.client.widget;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public abstract class AbstractPersistentEntityListEditor<E extends IdentifiableEntity> extends AbstractEntityListEditor<E> {
	
	public AbstractPersistentEntityListEditor(String newEntityLinkText, final AbstractPersistentEntityEditor<E> entityEditor, DataProvider<E> dataProvider, List<LabeledColumn<E>> columns) {
		super(newEntityLinkText, entityEditor, dataProvider, columns);
	}
	
	@Override
	protected void getEntity(int id, AsyncCallback<E> callback) {
		ClientEntityManager.getInstance().find(entityClass, id, callback);
	}
	
	@Override
	protected void removeItem(int id) {
		ClientEntityManager.getInstance().find(entityClass, id, new AsyncCallback<E>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(E entity) {
				ClientEntityManager.getInstance().remove(entity, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(Void result) {
						// Nothing - an event should rerender player list automatically
					}
					
				});
			}
			
		});
	}
}
