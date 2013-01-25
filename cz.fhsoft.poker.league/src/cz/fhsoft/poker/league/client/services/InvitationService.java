package cz.fhsoft.poker.league.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.fhsoft.poker.league.shared.model.v1.Invitation;

@RemoteServiceRelativePath("invitationService")
public interface InvitationService extends RemoteService {
	Invitation findInvitation(String invitationUUID);
	
	long acceptInvitation(String invitationUUID);
	
	long rejectInvitation(String invitationUUID);
}
