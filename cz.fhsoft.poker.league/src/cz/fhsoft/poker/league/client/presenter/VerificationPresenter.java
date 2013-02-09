package cz.fhsoft.poker.league.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.services.VerificationServiceAsync;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.VerificationView;

public class VerificationPresenter extends PresenterWithVersionedData implements VerificationView.Presenter {
	
	private VerificationView view;
	
	private VerificationServiceAsync verificationService;
	
	private AsyncCallback<Void> callback;

	public VerificationPresenter(Presenter parentPresenter, VerificationView view, VerificationServiceAsync verificationService, AsyncCallback<Void> callback) {
		super(parentPresenter);
		this.view = view;
		this.verificationService = verificationService;
		this.callback = callback;
		view.setPresenter(this);
	}


	@Override
	protected void refresh() {
		// nothing to do here
	}

	@Override
	public void updateForMode() {
		// nothing to do here
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		view.clearForm();
	}


	@Override
	public void onPassword(String password) {
		verificationService.registerWithPassword(password, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Boolean accessGranted) {
				if(accessGranted)
					callback.onSuccess(null);
				else
					view.reportWrongPassword();
			}
			
		});
	}
}
