package cz.fhsoft.poker.league.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.util.TransferrableException;

@RemoteServiceRelativePath("invitationService")
public interface InvitationService extends RemoteService {
	Invitation findInvitation(String invitationUUID) throws TransferrableException;
	
	long acceptInvitation(String invitationUUID) throws TransferrableException;
	
	long rejectInvitation(String invitationUUID) throws TransferrableException;
}
