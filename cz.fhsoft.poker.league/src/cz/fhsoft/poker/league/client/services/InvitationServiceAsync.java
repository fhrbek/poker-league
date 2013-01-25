package cz.fhsoft.poker.league.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.shared.model.v1.Invitation;

public interface InvitationServiceAsync {

	void findInvitation(String invitationUUID,
			AsyncCallback<Invitation> callback);

	void acceptInvitation(String invitationUUID,
			AsyncCallback<Long> callback);

	void rejectInvitation(String invitationUUID,
			AsyncCallback<Long> callback);

}
