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

public class TournamentViewImpl extends Composite implements TournamentView {
	
	private static TournamentUiBinder uiBinder = GWT.create(TournamentUiBinder.class);

	@UiTemplate("TournamentView.ui.xml")
	interface TournamentUiBinder extends UiBinder<Widget, TournamentViewImpl> {
	}

	private Presenter presenter;
	
	@UiField
	Label name;

	@UiField
	FlowPanel rankingContainer;
	
	@UiField
	FlowPanel invitationsContainer;
	
	@UiField
	FlowPanel gamesContainer;
	
	public TournamentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("edit")
	void onEditTournamentClicked(ClickEvent event) {
		presenter.onEdit();
	}

	@UiHandler("remove")
	void onRemoveTournamentClicked(ClickEvent event) {
		presenter.onRemove();
	}

	@UiHandler("toggleShowRanking")
	void onToggleShowRankingClicked(ClickEvent event) {
		presenter.onToggleShowRanking();
	}

	@UiHandler("toggleShowInvitations")
	void onToggleShowInvitationsClicked(ClickEvent event) {
		presenter.onToggleShowInvitations();
	}

	@UiHandler("toggleShowGames")
	void onToggleShowGamesClicked(ClickEvent event) {
		presenter.onToggleShowGames();
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
	public HasWidgets getInvitationsContainer() {
		return invitationsContainer;
	}

	@Override
	public HasWidgets getGamesContainer() {
		return gamesContainer;
	}

	@Override
	public void setName(String name) {
		this.name.setText(name);
	}
}
