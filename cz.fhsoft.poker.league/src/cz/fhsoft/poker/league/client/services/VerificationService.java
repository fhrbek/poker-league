package cz.fhsoft.poker.league.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("verificationService")
public interface VerificationService extends RemoteService {

	Boolean checkAccess();
	
	Boolean registerWithPassword(String password);
}
