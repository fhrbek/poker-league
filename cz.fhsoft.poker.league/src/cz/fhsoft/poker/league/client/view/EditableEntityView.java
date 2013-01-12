package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;


public interface EditableEntityView<E extends IdentifiableEntity, P extends EditableEntityView.Presenter<E>> {
	
	public interface Presenter<E extends IdentifiableEntity> extends cz.fhsoft.poker.league.client.presenter.OrderedPresenter {

		void onEdit();
		
		void onRemove();
		
		void setEntity(E entity);
		
		E getEntity();
	}

	void setPresenter(P presenter);

	Widget asWidget();
	
}
