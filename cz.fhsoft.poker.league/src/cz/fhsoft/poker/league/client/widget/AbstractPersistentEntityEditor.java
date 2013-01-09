package cz.fhsoft.poker.league.client.widget;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public abstract class AbstractPersistentEntityEditor<E extends IdentifiableEntity> extends AbstractEntityEditor<E> {
	
	protected void save(E entity, final AsyncCallback<Void> callback) {
		ClientEntityManager.getInstance().merge(entity, new AsyncCallback<E>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(E mergedEntity) {
				callback.onSuccess(null);
			}
			
		});
	}
	
}
