package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class GameViewImpl extends Composite implements GameView {
	
	private static GameUiBinder uiBinder = GWT.create(GameUiBinder.class);

	@UiTemplate("GameView.ui.xml")
	interface GameUiBinder extends UiBinder<Widget, GameViewImpl> {
	}

	private Presenter presenter;
	
	@UiField
	Label ordinal;

	@UiField
	FlowPanel rankingContainer;

	public GameViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("edit")
	void onEditGameClicked(ClickEvent event) {
		presenter.onEdit();
	}

	@UiHandler("remove")
	void onRemoveGameClicked(ClickEvent event) {
		presenter.onRemove();
	}

	@UiHandler("toggleShowRanking")
	void onToggleShowRankinwClicked(ClickEvent event) {
		presenter.onToggleShowRanking();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


	@Override
	public HasWidgets getRankingContainer() {
		return rankingContainer;
	}

	@Override
	public void setOrdinal(int ordinal) {
		this.ordinal.setText("Hra č. " + ordinal);
	}
}
