package cz.fhsoft.poker.league.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PlayerInGame;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;

public class CurrentTournamentViewImpl extends Composite implements CurrentTournamentView {
	
	private static CurrentTournamentUiBinder uiBinder = GWT
			.create(CurrentTournamentUiBinder.class);

	@UiTemplate("CurrentTournamentView.ui.xml")
	interface CurrentTournamentUiBinder extends UiBinder<Widget, CurrentTournamentViewImpl> {
	}

	private Presenter presenter;
	
	@UiField
	Widget newGame;
	
	@UiField
	HasWidgets newPlayersContainer;
	
	@UiField
	Label tournamentName;

	@UiField
	Widget currentGame;
	
	@UiField
	Label gameName;

	@UiField
	HasWidgets playersInGameContainer;
	
	private Map<CheckBox, Integer> newGamePlayerIdMap;
	
	private Map<CheckBox, Integer> playerInGameIdMap;
	
	public CurrentTournamentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setNewGameVisible(false);
		setCurrentGameVisible(false);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("startNewGame")
	void onNewGame(ClickEvent event) {
		List<Integer> newPlayersIds = new ArrayList<Integer>();
		for(Map.Entry<CheckBox, Integer> entry : newGamePlayerIdMap.entrySet()) {
			if(entry.getKey().getValue())
				newPlayersIds.add(entry.getValue());
		}

		presenter.onNewGame(newPlayersIds);
	}

	@UiHandler("seatOpen")
	void onSeatOpen(ClickEvent event) {
		List<Integer> seatOpenIds = new ArrayList<Integer>();
		for(Map.Entry<CheckBox, Integer> entry : playerInGameIdMap.entrySet()) {
			if(entry.getKey().getValue())
				seatOpenIds.add(entry.getValue());
		}

		presenter.onSeatOpen(seatOpenIds);
	}

	@UiHandler("undoSeatOpen")
	void onUndoSeatOpen(ClickEvent event) {
		List<Integer> undoSeatOpenIds = new ArrayList<Integer>();
		for(Map.Entry<CheckBox, Integer> entry : playerInGameIdMap.entrySet()) {
			if(entry.getKey().getValue())
				undoSeatOpenIds.add(entry.getValue());
		}

		presenter.onUndoSeatOpen(undoSeatOpenIds);
	}

	@Override
	public void setTournamentName(String name) {
		tournamentName.setText(name);
	}


	@Override
	public void setGameName(String name) {
		gameName.setText(name);
	}

	@Override
	public void setPlayerCandidatessForGame(List<Player> players) {
		newGamePlayerIdMap = new HashMap<CheckBox, Integer>();
		newPlayersContainer.clear();
		
		Collections.sort(players, Comparators.PLAYERS_COMPARATOR);

		for(Player player : players) {
			CheckBox checkBox = new CheckBox(player.getNick());
			newGamePlayerIdMap.put(checkBox, player.getId());
			newPlayersContainer.add(checkBox);
		}
	}

	@Override
	public void setPlayersInGame(List<PlayerInGame> currentPlayersInGame) {
		playerInGameIdMap = new HashMap<CheckBox, Integer>();
		playersInGameContainer.clear();
		
		for(PlayerInGame playerInGame : currentPlayersInGame) {
			String html = playerInGame.getRank() == 0
					? playerInGame.getPlayer().getNick()
					: ("<i>" + playerInGame.getRank() + ". " + SafeHtmlUtils.fromString(playerInGame.getPlayer().getNick()).asString() + "</i>");
			CheckBox checkBox = new CheckBox(html, true);
			playerInGameIdMap.put(checkBox, playerInGame.getId());
			playersInGameContainer.add(checkBox);
		}
	}

	@Override
	public void setNewGameVisible(boolean visible) {
		if(visible)
			newGame.getElement().getStyle().clearDisplay();
		else
			newGame.getElement().getStyle().setDisplay(Display.NONE);
	}

	@Override
	public void setCurrentGameVisible(boolean visible) {
		if(visible)
			currentGame.getElement().getStyle().clearDisplay();
		else
			currentGame.getElement().getStyle().setDisplay(Display.NONE);
	}
}
