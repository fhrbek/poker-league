package cz.fhsoft.poker.league.client.view;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;

public class InvitationConfirmationViewImpl extends Composite implements InvitationConfirmationView {
	
	private static CurrentTournamentsUiBinder uiBinder = GWT
			.create(CurrentTournamentsUiBinder.class);

	@UiTemplate("InvitationConfirmationView.ui.xml")
	interface CurrentTournamentsUiBinder extends UiBinder<Widget, InvitationConfirmationViewImpl> {
	}

	private Presenter presenter;
	
	@UiField
	Label tournamentLabel;
	
	@UiField
	Label confirmed;
	
	@UiField
	Label rejected;
	
	@UiField
	Label overLimit;
	
	@UiField
	Label tooLate;
	
	@UiField
	FlowPanel invitationsClosedPanel;
	
	@UiField
	Label invitationContact;
	
	@UiField
	Label changeConfirmation;
	
	@UiField
	Widget confirmationForm;
	
	@UiField
	Button buttonAccept;

	@UiField
	Button buttonReject;

	@UiField
	CellTable<Invitation> invitationsTable;
	
	private Invitation recentInvitation;

	private int recentTournamentCapacity;
	
	private boolean recentTournamentOutOfCapacity;
	
	private boolean recentInvitationsClosed;
	
	private String recentInvitationContact;
	
	private boolean acceptAllowed;
	
	private boolean rejectAllowed;
	
	private boolean changeAllowed;
	
	public InvitationConfirmationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		setInvitation(null, 0, 0, false, null);
		
		invitationsTable.addColumn(new Column<Invitation, String>(new TextCell()) {

			@Override
			public String getValue(Invitation invitation) {
				if(invitation.getOrdinal() > 0) {
					if(invitation.getOrdinal() <= recentTournamentCapacity)
						return invitation.getOrdinal() + ".";

					return "(" + invitation.getOrdinal() + ".)";
				}
				
				
				return "<bez pořadí>";
			}
		}, "Pořadí");

		invitationsTable.addColumn(new Column<Invitation, String>(new TextCell()) {

			@Override
			public String getValue(Invitation invitation) {
				return invitation.getPlayer().getNick();
			}
		}, "Přezdívka");
		
		invitationsTable.addColumn(new Column<Invitation, String>(new TextCell()) {

			@Override
			public String getValue(Invitation invitation) {
				switch(invitation.getReply()) {
					case ACCEPTED:
						return "ANO";
					case REJECTED:
						return "NE";
					default:
						return "<bez reakce>";
				}
			}
		}, "Účast");
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setTournamentLabel(String tournamentLabel) {
		this.tournamentLabel.setText(tournamentLabel);
	}

	@Override
	public void setInvitations(List<Invitation> invitations) {
		if(invitations == null)
			invitationsTable.setRowCount(0, false);
		else
			invitationsTable.setRowData(invitations);
	}

	@Override
	public void setInvitation(Invitation invitation, int tournamentCapacity, int acceptedCount, boolean invitationsClosed, String contact) {
		recentInvitation = invitation;
		recentTournamentCapacity = tournamentCapacity;
		recentTournamentOutOfCapacity = acceptedCount >= tournamentCapacity;
		recentInvitationsClosed = invitationsClosed;
		recentInvitationContact = contact;
		resetLayout();
	}
	
	@Override
	public void resetLayout() {
		confirmed.getElement().getStyle().setDisplay(Display.NONE);
		rejected.getElement().getStyle().setDisplay(Display.NONE);
		overLimit.getElement().getStyle().setDisplay(Display.NONE);
		tooLate.getElement().getStyle().setDisplay(Display.NONE);
		invitationsClosedPanel.getElement().getStyle().setDisplay(Display.NONE);
		changeConfirmation.getElement().getStyle().setDisplay(Display.NONE);
		confirmationForm.getElement().getStyle().setDisplay(Display.NONE);
		
		acceptAllowed = false;
		rejectAllowed = false;
		changeAllowed = false;

		if(recentInvitation == null)
			return;
		
		ClientEntityManager.getInstance().resolveEntity(recentInvitation, new AsyncCallback<Invitation>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Invitation invitation) {
				recentInvitation = invitation;

				switch(recentInvitation.getReply()) {
					case ACCEPTED:
						confirmed.getElement().getStyle().clearDisplay();
						if (!recentInvitationsClosed) {
							changeConfirmation.getElement().getStyle().clearDisplay();
							changeAllowed = true;
							acceptAllowed = true;
							rejectAllowed = true;
						}
						break;
					case REJECTED:
						rejected.getElement().getStyle().clearDisplay();
						if (!recentInvitationsClosed) {
							if (!recentTournamentOutOfCapacity) {
								changeConfirmation.getElement().getStyle().clearDisplay();
								acceptAllowed = true;
								rejectAllowed = true;
								changeAllowed = true;
							}
							else
								overLimit.getElement().getStyle().clearDisplay();
						}
						else
							tooLate.getElement().getStyle().clearDisplay();

						break;
					case NO_REPLY:
						if (!recentInvitationsClosed) {
							confirmationForm.getElement().getStyle().clearDisplay();
							rejectAllowed = true;
							
							if (!recentTournamentOutOfCapacity)
								acceptAllowed = true;
							else
								overLimit.getElement().getStyle().clearDisplay();
						}
						else
							tooLate.getElement().getStyle().clearDisplay();
						break;
				}

				if (recentInvitationsClosed || recentTournamentOutOfCapacity) {
					invitationsClosedPanel.getElement().getStyle().clearDisplay();
					invitationContact.setText(recentInvitationContact);
				}

				buttonAccept.getElement().setPropertyBoolean("disabled", !acceptAllowed);
				buttonReject.getElement().setPropertyBoolean("disabled", !rejectAllowed);
			}
			
		});
	}
	
	@UiHandler("buttonAccept")
	void onAcceptClicked(ClickEvent event) {
		if (acceptAllowed)
			presenter.onAcceptClicked();
	}
	
	@UiHandler("buttonReject")
	void onRejectClicked(ClickEvent event) {
		if (rejectAllowed)
			presenter.onRejectClicked();
	}
	
	@UiHandler("changeConfirmation")
	void onChangeConfirmationClicked(ClickEvent event) {
		if (changeAllowed) {
			confirmed.getElement().getStyle().setDisplay(Display.NONE);
			rejected.getElement().getStyle().setDisplay(Display.NONE);
			overLimit.getElement().getStyle().setDisplay(Display.NONE);
			tooLate.getElement().getStyle().setDisplay(Display.NONE);
			invitationsClosedPanel.getElement().getStyle().setDisplay(Display.NONE);
			changeConfirmation.getElement().getStyle().setDisplay(Display.NONE);
			confirmationForm.getElement().getStyle().clearDisplay();
		}
	}

}
