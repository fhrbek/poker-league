package cz.fhsoft.poker.league.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.presenter.InvitationConfirmationPresenter;
import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.presenter.PresenterWithVersionedData;
import cz.fhsoft.poker.league.client.services.InvitationService;
import cz.fhsoft.poker.league.client.services.InvitationServiceAsync;
import cz.fhsoft.poker.league.client.util.StyleUtil;
import cz.fhsoft.poker.league.client.view.InvitationConfirmationView;
import cz.fhsoft.poker.league.client.view.InvitationConfirmationViewImpl;

public class AppControllerInvitation extends PresenterWithVersionedData {

	public static AppControllerInvitation INSTANCE = null;
	
	private InvitationConfirmationPresenter invitationConfirmationPresenter;
	
	private InvitationConfirmationView invitationConfirmationView;
	
	private InvitationServiceAsync invitationService = GWT.create(InvitationService.class);
	
	public AppControllerInvitation(Presenter parentPresenter) {
		super(parentPresenter);

		if(INSTANCE != null)
			throw new IllegalArgumentException("AppControllerInvitation already exists");

		INSTANCE = this;
	}
	
	@Override
	public void go(HasWidgets container) {
		if(invitationConfirmationView == null) {
			invitationConfirmationView = new InvitationConfirmationViewImpl();
			StyleUtil.makeAbsoluteFull(invitationConfirmationView.asWidget());
		}

		if(invitationConfirmationPresenter == null)
			invitationConfirmationPresenter =  new InvitationConfirmationPresenter(this, invitationConfirmationView);
		
		invitationConfirmationPresenter.go(container);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if(invitationConfirmationView != null) {
			Style style = invitationConfirmationView.asWidget().getElement().getStyle();
			
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

	public InvitationServiceAsync getInvitationService() {
		return invitationService;
	}

	public void setInvitationUUID(String invitationUUID) {
		if(invitationConfirmationPresenter != null)
			invitationConfirmationPresenter.setInvitationUUID(invitationUUID);
	}
}
