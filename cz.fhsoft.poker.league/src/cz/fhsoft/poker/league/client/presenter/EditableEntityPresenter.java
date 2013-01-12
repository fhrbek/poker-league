package cz.fhsoft.poker.league.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.Dialog;
import cz.fhsoft.poker.league.client.util.Dialog.Option;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.EditableEntityView;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityEditor;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public abstract class EditableEntityPresenter<E extends IdentifiableEntity, P extends EditableEntityView.Presenter<E>, V extends EditableEntityView<E, P>> extends PresenterWithVersionedData implements EditableEntityView.Presenter<E> {
	
	abstract protected AbstractPersistentEntityEditor<E> getEditor();
	
	protected V view;
	
	protected E entity;
	
	@SuppressWarnings("unchecked")
	public EditableEntityPresenter(Presenter parentPresenter, V view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter((P) this);
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget()); // no clear since we really want to append this widget
		refresh();
	}

	@Override
	abstract protected void refresh();

	@Override
	public void moveViewToTop() {
		if(view.asWidget().getParent() instanceof FlowPanel) {
			FlowPanel parent = (FlowPanel) view.asWidget().getParent();
			parent.remove(view.asWidget());
			parent.insert(view.asWidget(), 0);
		}
	}

	@Override
	public void removeView() {
		view.asWidget().removeFromParent();
	}
	
	@Override
	public void onEdit() {
		getEditor().setEntity(entity, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Void result) {
				getEditor().showAsPopupPanel();
			}
			
		});
	}

	@Override
	public void onRemove() {
		Dialog.show("Potvrzení odstranění", "Opravdu chcete smazat tuto položku?",
				new Option("Ano", null) {

					@Override
					public void run() {
						ClientEntityManager.getInstance().resolveEntity(entity, new AsyncCallback<E>() {

							@Override
							public void onFailure(Throwable caught) {
								ErrorReporter.error(caught);
							}

							@Override
							public void onSuccess(E entityToRemove) {
								ClientEntityManager.getInstance().remove(entityToRemove, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										ErrorReporter.error(caught);
									}

									@Override
									public void onSuccess(Void result) {
										// let the event handlers do their job
									}
									
								});
							}
							
						});
					}
			
				},
				new Option("Ne", null));
	}

	@Override
	public void setEntity(E entity) {
		this.entity = entity;
		refresh();
	}

	@Override
	public E getEntity() {
		return entity;
	}

}
