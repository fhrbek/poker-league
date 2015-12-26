package cz.fhsoft.poker.league.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.shared.model.v1.Invitation;


public interface InvitationConfirmationView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.Presenter {
		
		void setInvitationUUID(String invitationUUID);

		void onAcceptClicked();
		
		void onRejectClicked();
	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

	void setTournamentLabel(String tournamentLabel);
	
	void setInvitation(Invitation invitation, int tournamentCapacity, int acceptedCount, boolean invitationsClosed, String contact);

	void setInvitations(List<Invitation> invitations);
	
	void resetLayout();
}
