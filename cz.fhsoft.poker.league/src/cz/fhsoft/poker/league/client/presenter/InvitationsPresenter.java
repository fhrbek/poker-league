package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.persistence.DigestProviders;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.util.UUID;
import cz.fhsoft.poker.league.client.view.InvitationsView;
import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor.LabeledColumn;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityEditor;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityListEditor;
import cz.fhsoft.poker.league.client.widget.EntitySelector;
import cz.fhsoft.poker.league.client.widget.EnumSelector;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.InvitationEvent;
import cz.fhsoft.poker.league.shared.model.v1.InvitationEventType;
import cz.fhsoft.poker.league.shared.model.v1.InvitationReply;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;

public class InvitationsPresenter extends PresenterWithVersionedData implements InvitationsView.Presenter {
	
	private InvitationsView view;
	
	private AbstractPersistentEntityEditor<Invitation> invitationEditor = new AbstractPersistentEntityEditor<Invitation>() {
		
		private Entry<EntitySelector<Player>> playerEntry = new Entry<EntitySelector<Player>>("Hráč", new EntitySelector<Player>(Player.class, Comparators.PLAYERS_COMPARATOR, DigestProviders.PLAYER_DIGEST_PROVIDER)) {

			@Override
			public void setUpWidget(Invitation entity) {
				getWidget().setValue(entity.getPlayer());
			}

			@Override
			public void updateEntity(Invitation entity) {
				entity.setPlayer(getWidget().getValue());
			}
			
		};

		private Entry<EnumSelector<InvitationReply>> replyEntry = new Entry<EnumSelector<InvitationReply>>("Stav pozvánky", new EnumSelector<InvitationReply>(InvitationReply.VALUES)) {

			@Override
			public void setUpWidget(Invitation entity) {
				getWidget().setValue(entity.getReply());
			}

			@Override
			public void updateEntity(Invitation entity) {
				entity.setReply(getWidget().getValue());
			}
			
		};

		private Entry<IntegerBox> ordinalEntry = new Entry<IntegerBox>("Pořadí", new IntegerBox()) {

			@Override
			public void setUpWidget(Invitation entity) {
				getWidget().setValue(entity.getOrdinal());
			}

			@Override
			public void updateEntity(Invitation entity) {
				entity.setOrdinal(getWidget().getValue());
			}
			
		};

		{
			addEntry(playerEntry);
			addEntry(replyEntry);
			addEntry(ordinalEntry);
		}

		@Override
		protected SafeHtml getLabel(Invitation entity) {
			return SafeHtmlUtils.fromString(entity.getId() > 0
					? "Pozvánka pro hráče " + entity.getPlayer().getNick()
					: "Nová pozvánka");
		}

		@Override
		protected Invitation createTemporaryEntity() {
			return new Invitation();
		}
		
		@Override
		protected Invitation stripEntity(Invitation invitation) {
			invitation.setPlayer(Util.proxify(invitation.getPlayer(), new Player()));
			invitation.setTournament(Util.proxify(invitation.getTournament(), new Tournament()));
			return invitation;
		}

		@Override
		protected void validate(Invitation entity, AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
			Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
			if(entity.getPlayer() == null)
				errorMap.put(playerEntry, "Nebyl vybrán hráč");
			if(entity.getReply() == null)
				errorMap.put(replyEntry, "Stav pozvánky musí být vyplněn");
			
			callback.onSuccess(errorMap);
		}
		
	};
	
	public InvitationsPresenter(Presenter parentPresenter, InvitationsView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
		
		List<LabeledColumn<Invitation>> columns = new ArrayList<LabeledColumn<Invitation>>();

		columns.add(new LabeledColumn<Invitation>(new Column<Invitation, String>(new TextCell()) {

			@Override
			public String getValue(Invitation invitation) {
				if(invitation.getOrdinal() > 0)
					return invitation.getOrdinal() + ".";
				
				return "<bez reakce>";
			}
		}, "Pořadí"));

		columns.add(new LabeledColumn<Invitation>(new Column<Invitation, String>(new TextCell()) {

			@Override
			public String getValue(Invitation invitation) {
				return invitation.getPlayer().getNick();
			}
		}, "Přezdívka"));
		
		columns.add(new LabeledColumn<Invitation>(new Column<Invitation, String>(new TextCell()) {

			@Override
			public String getValue(Invitation invitation) {
				return invitation.getReply().getLiteral();
			}
		}, "Stav"));

		view.setInvitationsEditor(new AbstractPersistentEntityListEditor<Invitation>("Nová pozvánka", invitationEditor,
				new AbstractPersistentEntityListEditor.DataProvider<Invitation>() {
			
			@Override
			protected void getData(final AsyncCallback<List<Invitation>> callback) {
				ClientEntityManager.getInstance().resolveEntity(getTournament(), new AsyncCallback<Tournament>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(Tournament tournament) {
						Util.resolve(tournament.getInvitations(), new AsyncCallback<Set<Invitation>>() {

							@Override
							public void onFailure(Throwable caught) {
								callback.onFailure(caught);
							}

							@Override
							public void onSuccess(Set<Invitation> invitationsSet) {
								List<Invitation> invitations = new ArrayList<Invitation>(invitationsSet);
								Collections.sort(invitations, Comparators.INVITATIONS_COMPARATOR);
								callback.onSuccess(invitations);
							}
							
						});
					}
					
				});
			}
			
		},
		columns) {

			@Override
			protected Invitation getNewEntity() {
				Invitation invitation = new Invitation();
				
				invitation.setTournament(getTournament());
				invitation.setOrdinal(0);
				invitation.setReply(InvitationReply.NO_REPLY);
				invitation.setUuid(UUID.generateUUID());
				
				InvitationEvent event = new InvitationEvent();
				event.setInvitation(invitation);
				event.setEventTime(new Date());
				event.setEventType(InvitationEventType.GENERATED);
				
				invitation.addToEvents(event);

				return invitation;
			}
			
		});

		setVisible(false);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		refresh();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if(visible)
			view.asWidget().getElement().getStyle().clearDisplay();
		else
			view.asWidget().getElement().getStyle().setDisplay(Display.NONE);
	}

	@Override
	protected void refresh() {
		view.refresh();
	}


	private Tournament getTournament() {
		return ((TournamentPresenter) getParentPresenter()).getEntity();
	}
}
