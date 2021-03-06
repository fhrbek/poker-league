package cz.fhsoft.poker.league.client.presenter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.persistence.DigestProviders;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.GamesView;
import cz.fhsoft.poker.league.client.view.GamesViewImpl;
import cz.fhsoft.poker.league.client.view.InvitationsView;
import cz.fhsoft.poker.league.client.view.InvitationsViewImpl;
import cz.fhsoft.poker.league.client.view.RankingView;
import cz.fhsoft.poker.league.client.view.RankingViewImpl;
import cz.fhsoft.poker.league.client.view.TournamentView;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityEditor;
import cz.fhsoft.poker.league.client.widget.EntitySelector;
import cz.fhsoft.poker.league.shared.model.v1.Competition;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRuleSet;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;
import cz.fhsoft.poker.league.shared.util.StringUtil;

public class TournamentPresenter extends RankablePresenter<Tournament, TournamentView.Presenter, TournamentView> implements TournamentView.Presenter {
	
	private RankingView.Presenter rankingPresenterCumulative;
	
	private RankingView rankingViewCumulative;
	
	private InvitationsPresenter invitationsPresenter;
	
	private InvitationsView invitationsView;

	private GamesPresenter gamesPresenter;
	
	private GamesView gamesView;
	
	private static final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd.MM.yyyy H:mm");
	
	private static final RegExp descriptionParser = RegExp.compile("^((?:[^\\\\\\[]|\\\\.)*)\\[((?:[^\\|\\]])+)(?:\\|((?:[^\\]])+))?\\](.*)$");

	interface SHtmlTemplates extends SafeHtmlTemplates {
		@Template("<a href='{0}' target='_blank'>{1}</a>")
		SafeHtml link(SafeUri url, String label);
	}

	private static final SHtmlTemplates sHtmlTemplates = GWT.create(SHtmlTemplates.class);

	public static final AbstractPersistentEntityEditor<Tournament> tournamentEditor = new AbstractPersistentEntityEditor<Tournament>() {
		
		private Entry<TextBox> nameEntry = new Entry<TextBox>("Název", new TextBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setText(entity.getName());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setName(getWidget().getValue());
			}
			
		};

		private Entry<TextBox> descriptionEntry = new Entry<TextBox>("Popis", new TextBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setText(entity.getDescription());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setDescription(getWidget().getValue());
			}
			
		};
		
		private Entry<IntegerBox> announcementLeadEntry = new Entry<IntegerBox>("Předstih odeslání pozvánek", new IntegerBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getTournamentAnnouncementLead());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setTournamentAnnouncementLead(getWidget().getValue());
			}
			
		};
		
		private Entry<IntegerBox> invitationClosureEntry = new Entry<IntegerBox>("Uzavírka pozvánek", new IntegerBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getTournamentInvitationClosure());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setTournamentInvitationClosure(getWidget().getValue());
			}
			
		};
		
		private Entry<TextBox> invitationContactEntry = new Entry<TextBox>("Kontakt pro řešení pozvánek", new TextBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getTournamentInvitationContact());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setTournamentInvitationContact(getWidget().getValue());
			}
			
		};
		
		private Entry<TextBox> startTimeEntry = new Entry<TextBox>("Zahájení", new TextBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(dateFormatter.format(entity.getTournamentStart()));
			}

			@Override
			public void updateEntity(Tournament entity) {
				try {
					entity.setTournamentStart(dateFormatter.parseStrict(getWidget().getValue()));
				}
				catch(IllegalArgumentException e) {
					entity.setTournamentStart(null);
				}
			}
			
		};
		
		private Entry<TextBox> endTimeEntry = new Entry<TextBox>("Ukončení", new TextBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(dateFormatter.format(entity.getTournamentEnd()));
			}

			@Override
			public void updateEntity(Tournament entity) {
				try {
					entity.setTournamentEnd(dateFormatter.parseStrict(getWidget().getValue()));
				}
				catch(IllegalArgumentException e) {
					entity.setTournamentEnd(null);
				}
			}
			
		};
		
		private Entry<IntegerBox> minPlayersEntry = new Entry<IntegerBox>("Minimální počet hráčů", new IntegerBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getMinPlayers());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setMinPlayers(getWidget().getValue());
			}
			
		};
		
		private Entry<IntegerBox> maxPlayersEntry = new Entry<IntegerBox>("Maximální počet hráčů", new IntegerBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getMaxPlayers());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setMaxPlayers(getWidget().getValue());
			}
			
		};
		
		private Entry<IntegerBox> defaultBuyInEntry = new Entry<IntegerBox>("Výchozí buy-in", new IntegerBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getDefaultBuyIn());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setDefaultBuyIn(getWidget().getValue());
			}
			
		};
		
		private Entry<EntitySelector<PrizeMoneyRuleSet>> defaultPrizeMoneyRuleSetEntry = new Entry<EntitySelector<PrizeMoneyRuleSet>>("Výchozí pravidlo hodnocení", new EntitySelector<PrizeMoneyRuleSet>(PrizeMoneyRuleSet.class, Comparators.DESCRIBED_ENTITY_COMPARATOR, DigestProviders.DESCRIBED_ENTITY_PROVIDER)) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getDefaultPrizeMoneyRuleSet());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setDefaultPrizeMoneyRuleSet(getWidget().getValue());
			}
			
		};
		
		{
			addEntry(nameEntry);
			addEntry(descriptionEntry);
			addEntry(announcementLeadEntry);
			addEntry(invitationClosureEntry);
			addEntry(invitationContactEntry);
			addEntry(startTimeEntry);
			addEntry(endTimeEntry);
			addEntry(minPlayersEntry);
			addEntry(maxPlayersEntry);
			addEntry(defaultBuyInEntry);
			addEntry(defaultPrizeMoneyRuleSetEntry);
		}

		@Override
		protected SafeHtml getLabel(Tournament entity) {
			return SafeHtmlUtils.fromString(entity.getId() > 0
					? "Turnaj " + entity.getName()
					: "Nový turnaj");
		}

		@Override
		protected Tournament createTemporaryEntity() {
			return new Tournament();
		}
		
		@Override
		protected Tournament stripEntity(Tournament tournament) {
			tournament.setCompetition(Util.proxify(tournament.getCompetition(), new Competition()));
			tournament.setDefaultPrizeMoneyRuleSet(Util.proxify(tournament.getDefaultPrizeMoneyRuleSet(), new PrizeMoneyRuleSet()));
			Util.proxify(tournament.getGames()); // this should always make games a proxy
			if(!Util.proxify(tournament.getInvitations())) // this may not make invitations a proxy in case it's newly created
				for(Invitation invitation : tournament.getInvitations())
					invitation.setPlayer(Util.proxify(invitation.getPlayer(), new Player()));
			
			return tournament;
		}

		@Override
		protected void validate(Tournament entity, AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
			Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
			if(StringUtil.isEmpty(entity.getName(), true))
				errorMap.put(nameEntry, "Název musí být vyplněn");
			if(entity.getTournamentStart() == null)
				errorMap.put(startTimeEntry, "Datum a čas zahájení musí být platná hodnota ve formátu DD.MM.YYYY HH:MM");
			if(entity.getTournamentEnd() == null)
				errorMap.put(endTimeEntry, "Datum a čas ukončení musí být platná hodnota ve formátu DD.MM.YYYY HH:MM");
			//TODO Validate tournament
			
			callback.onSuccess(errorMap);
		}
		
	};
	
	public TournamentPresenter(Presenter parentPresenter, TournamentView view) {
		super(parentPresenter, view);
	}

	@Override
	public void onToggleShowRankingCumulative() {
		if(entity != null) {
			ClientEntityManager.getInstance().resolveEntity(entity, new AsyncCallback<Tournament>() {

				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}

				@Override
				public void onSuccess(Tournament resolvedEntity) {
					entity = resolvedEntity;

					if(rankingViewCumulative == null)
						rankingViewCumulative = new RankingViewImpl(true);

					if(rankingPresenterCumulative == null) {
						rankingPresenterCumulative = new RankingPresenter(TournamentPresenter.this, rankingViewCumulative, entity.getCompetition()) {
							@Override
							protected Date getDateLimit() {
								Date dateLimit = entity != null
										? entity.getTournamentStart()
										: null;
								return dateLimit != null
										? dateLimit
										: new Date(0);
							}
							
						};

						rankingPresenterCumulative.go(view.getRankingContainerCumulative());
					}

					rankingPresenterCumulative.setVisible(!rankingPresenterCumulative.isVisible());
				}
				
			});
		}
	}

	@Override
	protected void refresh() {
		if(entity != null) {
			ClientEntityManager.getInstance().resolveEntity(entity, new AsyncCallback<Tournament>() {

				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}

				@Override
				public void onSuccess(Tournament resolvedEntity) {
					entity = resolvedEntity;
					view.setName(entity.getName());
					view.setDescription(parse(entity.getDescription()));
					//TODO Refresh all displayed values
				}
				
			});
		}
	}

	@Override
	public void onToggleShowInvitations() {
		if(invitationsView == null)
			invitationsView = new InvitationsViewImpl();

		if(invitationsPresenter == null) {
			invitationsPresenter = new InvitationsPresenter(this, invitationsView);
			invitationsPresenter.go(view.getInvitationsContainer());
		}

		invitationsPresenter.setVisible(!invitationsPresenter.isVisible());
	}

	@Override
	public void onToggleShowGames() {
		if(gamesView == null)
			gamesView = new GamesViewImpl();

		if(gamesPresenter == null) {
			gamesPresenter = new GamesPresenter(this, gamesView);
			gamesPresenter.go(view.getGamesContainer());
		}

		gamesPresenter.setVisible(!gamesPresenter.isVisible());
	}

	@Override
	protected AbstractPersistentEntityEditor<Tournament> getEditor() {
		return tournamentEditor;
	}

	protected SafeHtml parse(String markUp) {
		MatchResult result = null;
		String remaining = markUp;
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		
		while ((result = descriptionParser.exec(remaining)) != null) {
			builder.appendEscaped(result.getGroup(1));
			String href = result.getGroup(2);
			String text = result.getGroup(3);
			if (text == null || text.length() == 0)
				text = href;
			builder.append(sHtmlTemplates.link(UriUtils.fromString(href), result.getGroup(3)));
			remaining = result.getGroup(4);
		}

		builder.appendEscaped(remaining);

		return builder.toSafeHtml();
	}

}
