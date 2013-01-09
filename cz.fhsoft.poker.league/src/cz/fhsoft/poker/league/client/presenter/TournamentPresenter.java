package cz.fhsoft.poker.league.client.presenter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.persistence.DigestProviders;
import cz.fhsoft.poker.league.client.util.Dialog;
import cz.fhsoft.poker.league.client.util.Dialog.Option;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.GamesView;
import cz.fhsoft.poker.league.client.view.GamesViewImpl;
import cz.fhsoft.poker.league.client.view.InvitationsView;
import cz.fhsoft.poker.league.client.view.InvitationsViewImpl;
import cz.fhsoft.poker.league.client.view.TournamentView;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityEditor;
import cz.fhsoft.poker.league.client.widget.EntitySelector;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRuleSet;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;
import cz.fhsoft.poker.league.shared.util.StringUtil;

public class TournamentPresenter extends PresenterWithVersionedData implements TournamentView.Presenter {
	
	private Tournament tournament;

	private TournamentView view;

	private InvitationsPresenter invitationsPresenter;
	
	private InvitationsView invitationsView;

	private GamesPresenter gamesPresenter;
	
	private GamesView gamesView;

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
		
		private Entry<DatePicker> startTimeEntry = new Entry<DatePicker>("Zahájení", new DatePicker()) {
			//TODO Enable setting hours

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getTournamentStart());
				getWidget().setCurrentMonth(entity.getTournamentStart());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setTournamentStart(new Date(getWidget().getValue().getTime() + 86400 * 20)); //TODO Hard coded start time 20:00!!!
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
		
		private Entry<IntegerBox> deadlineEntry = new Entry<IntegerBox>("Uzávěrka přihlášek (hodiny před začátkem turnaje)", new IntegerBox()) {

			@Override
			public void setUpWidget(Tournament entity) {
				getWidget().setValue(entity.getDeadline());
			}

			@Override
			public void updateEntity(Tournament entity) {
				entity.setDeadline(getWidget().getValue());
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
			addEntry(startTimeEntry);
			addEntry(minPlayersEntry);
			addEntry(maxPlayersEntry);
			addEntry(defaultBuyInEntry);
			addEntry(deadlineEntry);
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
		protected void validate(Tournament entity, AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
			Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
			if(StringUtil.isEmpty(entity.getName(), true))
				errorMap.put(nameEntry, "Název musí být vyplněn");
			//TODO Validate tournament
			
			callback.onSuccess(errorMap);
		}
		
	};
	
	public TournamentPresenter(Presenter parentPresenter, TournamentView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget()); // no clear since we really want to append this widget
		refresh();
	}

	@Override
	protected void refresh() {
		if(tournament != null) {
			view.setName(tournament.getName());
			//TODO Refresh all displayed values
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
		TournamentPresenter.tournamentEditor.setEntity(tournament, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Void result) {
				TournamentPresenter.tournamentEditor.showAsPopupPanel();
			}
			
		});
	}

	@Override
	public void onRemove() {
		Dialog.show("Potvrzení odstranění", "Opravdu chcete smazat tuto položku?",
				new Option("Ano", null) {

					@Override
					public void run() {
						ClientEntityManager.getInstance().resolveEntity(tournament, new AsyncCallback<Tournament>() {

							@Override
							public void onFailure(Throwable caught) {
								ErrorReporter.error(caught);
							}

							@Override
							public void onSuccess(Tournament tournamentToRemove) {
								ClientEntityManager.getInstance().remove(tournamentToRemove, new AsyncCallback<Void>() {

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
	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
		refresh();
	}

	@Override
	public Tournament getTournament() {
		return tournament;
	}

}
