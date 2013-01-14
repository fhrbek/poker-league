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
import com.google.gwt.user.client.ui.Widget;

public class GamesViewImpl extends Composite implements GamesView {
	
	private static GamesUiBinder uiBinder = GWT.create(GamesUiBinder.class);

	@UiTemplate("GamesView.ui.xml")
	interface GamesUiBinder extends UiBinder<Widget, GamesViewImpl> {
	}

	private Presenter presenter;

	@UiField
	FlowPanel gamesContainer;
	
	public GamesViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("newGame")
	void onNewTournamentClicked(ClickEvent event) {
		presenter.onAddGame();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasWidgets getGamesContainer() {
		return gamesContainer;
	}
}