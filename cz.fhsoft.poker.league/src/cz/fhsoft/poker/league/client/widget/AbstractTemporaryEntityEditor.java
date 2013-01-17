package cz.fhsoft.poker.league.client.widget;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public abstract class AbstractTemporaryEntityEditor<E extends IdentifiableEntity> extends AbstractEntityEditor<E> {
	
	public interface SaveCallback<E extends IdentifiableEntity> {
		void onSave(E entity, AsyncCallback<Void> callback);
	}
	
	private SaveCallback<E> saveCallback;
	
	public void setEntity(E entity, final AsyncCallback<Void> callback) {
		ClientEntityManager.getInstance().resolveEntity(entity, new AsyncCallback<E>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(E resolvedEntity) {
				E tempEntity = createTemporaryEntity();
				copyAllAttributes(resolvedEntity, tempEntity);
				AbstractTemporaryEntityEditor.super.setEntity(tempEntity, callback);
			}
			
		});
	}
	
	public void setSaveCallback(SaveCallback<E> callback) {
		saveCallback = callback;
	}
	
	private void copyAllAttributes(E source, E target) {
		target.setId(source.getId());
		target.setObsolete(source.isObsolete());
		target.setProxy(source.isProxy());
		copyAttributes(source, target);
	}
	
	abstract protected void copyAttributes(E source, E target);
	
	protected void save(E entity, AsyncCallback<Void> callback) {
		if(saveCallback != null)
			saveCallback.onSave(stripEntity(entity), callback);
		else
			callback.onSuccess(null);
	}
	
}
