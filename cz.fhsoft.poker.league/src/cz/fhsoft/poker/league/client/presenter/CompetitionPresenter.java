package cz.fhsoft.poker.league.client.presenter;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import cz.fhsoft.poker.league.client.persistence.DigestProviders;
import cz.fhsoft.poker.league.client.view.CompetitionView;
import cz.fhsoft.poker.league.client.view.TournamentsView;
import cz.fhsoft.poker.league.client.view.TournamentsViewImpl;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityEditor;
import cz.fhsoft.poker.league.client.widget.EntityMultiSelector;
import cz.fhsoft.poker.league.client.widget.EntitySelector;
import cz.fhsoft.poker.league.shared.model.v1.Competition;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRuleSet;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;
import cz.fhsoft.poker.league.shared.util.StringUtil;

public class CompetitionPresenter extends RankablePresenter<Competition, CompetitionView.Presenter, CompetitionView> implements CompetitionView.Presenter {
	
	private TournamentsView.Presenter tournamentsPresenter;
	
	private TournamentsView tournamentsView;
	
	public static final AbstractPersistentEntityEditor<Competition> competitionEditor = new AbstractPersistentEntityEditor<Competition>() {
		
		private Entry<TextBox> nameEntry = new Entry<TextBox>("Název", new TextBox()) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setText(entity.getName());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setName(getWidget().getValue());
			}
			
		};

		private Entry<TextBox> descriptionEntry = new Entry<TextBox>("Popis", new TextBox()) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setText(entity.getDescription());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setDescription(getWidget().getValue());
			}
			
		};
		
		private Entry<DatePicker> startDateEntry = new Entry<DatePicker>("Zahájení", new DatePicker()) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setValue(entity.getStartDate());
				getWidget().setCurrentMonth(entity.getStartDate());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setStartDate(getWidget().getValue());
			}
			
		};
		
		private Entry<DatePicker> endDateEntry = new Entry<DatePicker>("Ukončení", new DatePicker()) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setValue(entity.getEndDate());
				getWidget().setCurrentMonth(entity.getEndDate());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setEndDate(getWidget().getValue());
			}
			
		};
		
		private Entry<IntegerBox> defaultMinPlayersEntry = new Entry<IntegerBox>("Výchozí minimální počet hráčů", new IntegerBox()) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setValue(entity.getDefaultMinPlayers());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setDefaultMinPlayers(getWidget().getValue());
			}
			
		};
		
		private Entry<IntegerBox> defaultMaxPlayersEntry = new Entry<IntegerBox>("Výchozí maximální počet hráčů", new IntegerBox()) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setValue(entity.getDefaultMaxPlayers());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setDefaultMaxPlayers(getWidget().getValue());
			}
			
		};
		
		private Entry<IntegerBox> defaultBuyInEntry = new Entry<IntegerBox>("Výchozí buy-in", new IntegerBox()) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setValue(entity.getDefaultBuyIn());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setDefaultBuyIn(getWidget().getValue());
			}
			
		};
		
		private Entry<IntegerBox> defaultTournamentDeadlineEntry = new Entry<IntegerBox>("Výchozí uzávěrka přihlášek (hodiny před začátkem turnaje)", new IntegerBox()) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setValue(entity.getDefaultTournamentDeadline());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setDefaultTournamentDeadline(getWidget().getValue());
			}
			
		};
		
		private Entry<EntityMultiSelector<Player>> playersEntry = new Entry<EntityMultiSelector<Player>>("Registrovaní hráči", new EntityMultiSelector<Player>(Player.class, Comparators.PLAYERS_COMPARATOR, DigestProviders.PLAYER_DIGEST_PROVIDER)) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setSelectedEntities(entity.getPlayers());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setPlayers(Util.asSet(getWidget().getSelectedEntities()));
			}
			
		};
		
		private Entry<EntitySelector<PrizeMoneyRuleSet>> defaultPrizeMoneyRuleSetEntry = new Entry<EntitySelector<PrizeMoneyRuleSet>>("Výchozí pravidlo hodnocení", new EntitySelector<PrizeMoneyRuleSet>(PrizeMoneyRuleSet.class, Comparators.DESCRIBED_ENTITY_COMPARATOR, DigestProviders.DESCRIBED_ENTITY_PROVIDER)) {

			@Override
			public void setUpWidget(Competition entity) {
				getWidget().setValue(entity.getDefaultPrizeMoneyRuleSet());
			}

			@Override
			public void updateEntity(Competition entity) {
				entity.setDefaultPrizeMoneyRuleSet(getWidget().getValue());
			}
			
		};
		
		{
			addEntry(nameEntry);
			addEntry(descriptionEntry);
			addEntry(startDateEntry);
			addEntry(endDateEntry);
			addEntry(defaultMinPlayersEntry);
			addEntry(defaultMaxPlayersEntry);
			addEntry(defaultBuyInEntry);
			addEntry(defaultTournamentDeadlineEntry);
			addEntry(playersEntry);
			addEntry(defaultPrizeMoneyRuleSetEntry);
		}

		@Override
		protected SafeHtml getLabel(Competition entity) {
			return SafeHtmlUtils.fromString(entity.getId() > 0
					? "Soutěž " + entity.getName()
					: "Nová soutěž");
		}

		@Override
		protected Competition createTemporaryEntity() {
			return new Competition();
		}

		@Override
		protected void validate(Competition entity, AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
			Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
			if(StringUtil.isEmpty(entity.getName(), true))
				errorMap.put(nameEntry, "Název musí být vyplněn");
			//TODO Validate competition
			
			callback.onSuccess(errorMap);
		}
		
	};
	
	public CompetitionPresenter(Presenter parentPresenter, CompetitionView view) {
		super(parentPresenter, view);
	}

	@Override
	protected AbstractPersistentEntityEditor<Competition> getEditor() {
		return competitionEditor;
	}

	@Override
	protected void refresh() {
		if(entity != null) {
			view.setName(entity.getName());
			//TODO Refresh all displayed values
		}
	}

	@Override
	public void onToggleShowTournaments() {
		if(tournamentsView == null)
			tournamentsView = new TournamentsViewImpl();

		if(tournamentsPresenter == null) {
			tournamentsPresenter = new TournamentsPresenter(this, tournamentsView);
			tournamentsPresenter.go(view.getTournamentsContainer());
		}

		tournamentsPresenter.setVisible(!tournamentsPresenter.isVisible());
	}

}
