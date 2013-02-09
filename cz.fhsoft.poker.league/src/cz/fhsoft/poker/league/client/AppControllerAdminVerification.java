package cz.fhsoft.poker.league.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.presenter.PresenterWithVersionedData;
import cz.fhsoft.poker.league.client.presenter.VerificationPresenter;
import cz.fhsoft.poker.league.client.services.VerificationService;
import cz.fhsoft.poker.league.client.services.VerificationServiceAsync;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.util.StyleUtil;
import cz.fhsoft.poker.league.client.view.VerificationView;
import cz.fhsoft.poker.league.client.view.VerificationViewImpl;

public class AppControllerAdminVerification extends PresenterWithVersionedData {

	public static AppControllerAdminVerification INSTANCE = null;
	
	private VerificationView verificationView;

	private VerificationPresenter verificationPresenter;
	
	private AsyncCallback<Void> callback;
	
	private VerificationServiceAsync verificationService = GWT.create(VerificationService.class);
	
	public AppControllerAdminVerification(Presenter parentPresenter, AsyncCallback<Void> callback) {
		super(parentPresenter);
		
		this.callback = callback;

		if(INSTANCE != null)
			throw new IllegalArgumentException("AppControllerAdminVerification already exists");

		INSTANCE = this;
	}
	
	@Override
	public void go(final HasWidgets container) {
		final boolean savedVisible = isVisible();
		setVisible(false);

		verificationService.checkAccess(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Boolean accessGranted) {
				if(accessGranted)
					callback.onSuccess(null);
				else {
					setVisible(savedVisible);
					init(container);
				}
			}
			
		});
	}

	private void init(HasWidgets container) {
		if(verificationView == null) {
			verificationView = new VerificationViewImpl();
			StyleUtil.makeAbsoluteFull(verificationView.asWidget());
		}
		
		verificationPresenter = new VerificationPresenter(this, verificationView, verificationService, callback);
		
		verificationPresenter.go(container);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if(verificationView != null) {
			Style style = verificationView.asWidget().getElement().getStyle();
			
			if(visible)
				style.clearDisplay();
			else
				style.setDisplay(Display.NONE);
		}
	}

	@Override
	protected void refresh() {
		// nothing to do here, inner presenters will do their job
	}

	@Override
	public void updateForMode() {
		// nothing to do here
	}

	public void clear() {
		if(verificationView != null)
			verificationView.clearForm();
	}

	public void setCallback(AsyncCallback<Void> callback) {
		this.callback = callback;
	}
}
