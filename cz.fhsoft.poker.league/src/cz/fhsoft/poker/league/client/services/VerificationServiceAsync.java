package cz.fhsoft.poker.league.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VerificationServiceAsync {

	void checkAccess(AsyncCallback<Boolean> callback);

	void registerWithPassword(String password, AsyncCallback<Boolean> callback);

}
