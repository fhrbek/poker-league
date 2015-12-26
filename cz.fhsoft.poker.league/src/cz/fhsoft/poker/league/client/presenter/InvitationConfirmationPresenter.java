package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.AppControllerInvitation;
import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.InvitationConfirmationView;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.InvitationReply;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;

public class InvitationConfirmationPresenter extends PresenterWithVersionedData implements InvitationConfirmationView.Presenter {
	
	private static DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd.MM.yyyy H:mm");

	private String invitationUUID;
	
	private InvitationConfirmationView view;
	
	private HasWidgets container;
	
	private boolean viewInitialized;

	public InvitationConfirmationPresenter(Presenter parentPresenter, InvitationConfirmationView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		refresh();
	}

	@Override
	protected void refresh() {
		refresh(false);
	}

	private void refresh(boolean force) {
		if(invitationUUID == null || !(force || isDataChanged()))
			return;

		if(container != null) {
			if(!viewInitialized) {
				container.add(view.asWidget());
				viewInitialized = true;
			}
			
			if(invitationUUID != null) {
				view.setTournamentLabel("<načítá se>");
				view.setInvitation(null, 0, 0, false, null);
				view.setInvitations(null);
				
				AppControllerInvitation.INSTANCE.getInvitationService().findInvitation(invitationUUID, new AsyncCallback<Invitation>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(final Invitation invitation) {
						if(invitation == null) {
							ErrorReporter.error("Tato pozvánka je neplatná");
							return;
						}
						
						ClientEntityManager.getInstance().registerEntity(invitation);

						ClientEntityManager.getInstance().resolveEntity(invitation.getTournament(), new AsyncCallback<Tournament>() {

							@Override
							public void onFailure(Throwable caught) {
								ErrorReporter.error(caught);
							}

							@Override
							public void onSuccess(final Tournament tournament) {
								view.setTournamentLabel(tournament.getName() + ", zahájení " + dateFormatter.format(tournament.getTournamentStart()) +
										" (min. " + tournament.getMinPlayers() + ", max. " + tournament.getMaxPlayers() + " hráčů)");

								Util.asLazyCollection(tournament.getInvitations()).resolve(new AsyncCallback<Collection<Invitation>>() {

									@Override
									public void onFailure(Throwable caught) {
										ErrorReporter.error(caught);
									}

									@Override
									public void onSuccess(Collection<Invitation> invitations) {
										List<Invitation> sortedInvitations = new ArrayList<Invitation>(invitations);
										Collections.sort(sortedInvitations, Comparators.INVITATIONS_COMPARATOR);

										int acceptedCount = 0;
										for (Invitation invitation : sortedInvitations) {
											if (invitation.getReply() == InvitationReply.ACCEPTED)
												acceptedCount++;
										}

										boolean invitationsClosed =
												(tournament.getTournamentStart().getTime() - new Date().getTime()) <=
												(tournament.getTournamentInvitationClosure() * 3600000);
										view.setInvitation(invitation, tournament.getMaxPlayers(), acceptedCount, invitationsClosed, tournament.getTournamentInvitationContact());
										view.setInvitations(sortedInvitations);
									}
									
								});
							}
							
						});
					}
					
				});
			}
		}
	}

	@Override
	public void setInvitationUUID(String invitationUUID) {
		boolean changed = !invitationUUID.equals(this.invitationUUID);
		this.invitationUUID = invitationUUID;
		refresh(changed);
	}

	@Override
	public void onAcceptClicked() {
		AppControllerInvitation.INSTANCE.getInvitationService().acceptInvitation(invitationUUID, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Long newDataVersion) {
				if(!ClientEntityManager.getInstance().checkDataVersion(newDataVersion))
					view.resetLayout();
			}
			
		});
	}

	@Override
	public void onRejectClicked() {
		AppControllerInvitation.INSTANCE.getInvitationService().rejectInvitation(invitationUUID, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Long newDataVersion) {
				if(!ClientEntityManager.getInstance().checkDataVersion(newDataVersion))
					view.resetLayout();
			}
			
		});
	}

	@Override
	public void updateForMode() {
		// nothing to do here
	}
	
}
