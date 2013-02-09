package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;


public interface RankableView<E extends IdentifiableEntity, P extends RankableView.Presenter<E>> extends EditableEntityView<E, P>, ViewWithMode {
	
	public interface Presenter<E extends IdentifiableEntity> extends EditableEntityView.Presenter<E> {

		void onToggleShowRanking();
	}

	HasWidgets getRankingContainer();
}
