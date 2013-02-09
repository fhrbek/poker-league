package cz.fhsoft.poker.league.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.Dialog;
import cz.fhsoft.poker.league.client.util.Dialog.Option;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.RankableView;
import cz.fhsoft.poker.league.client.view.RankingView;
import cz.fhsoft.poker.league.client.view.RankingViewImpl;
import cz.fhsoft.poker.league.shared.model.v1.Game;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public abstract class RankablePresenter<E extends IdentifiableEntity, P extends RankableView.Presenter<E>, V extends RankableView<E, P>> extends EditableEntityPresenter<E, P, V> implements RankableView.Presenter<E> {
	
	private RankingView.Presenter rankingPresenter;
	
	private RankingView rankingView;
	
	public RankablePresenter(Presenter parentPresenter, V view) {
		super(parentPresenter, view);
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget()); // no clear since we really want to append this widget
		refresh();
	}

	@Override
	public void onToggleShowRanking() {
		if(rankingView == null)
			rankingView = new RankingViewImpl(!(entity instanceof Game));

		if(rankingPresenter == null) {
			rankingPresenter = new RankingPresenter(this, rankingView, entity);
			rankingPresenter.go(view.getRankingContainer());
		}

		rankingPresenter.setVisible(!rankingPresenter.isVisible());
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

	@Override
	public void updateForMode() {
		view.updateForMode();
	}
}
