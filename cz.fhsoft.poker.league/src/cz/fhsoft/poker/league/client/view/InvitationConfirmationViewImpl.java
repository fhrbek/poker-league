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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

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
	Label confirmedButOverLimit;
	
	@UiField
	Label changeConfirmation;
	
	@UiField
	Widget confirmationForm;

	@UiField
	CellTable<Invitation> invitationsTable;
	
	private Invitation recentInvitation;

	private int recentTournamentCapacity;
	
	public InvitationConfirmationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		setInvitation(null, 0);
		
		invitationsTable.addColumn(new Column<Invitation, String>(new TextCell()) {

			@Override
			public String getValue(Invitation invitation) {
				if(invitation.getOrdinal() > 0)
					return invitation.getOrdinal() + ".";
				
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
	public void setInvitation(Invitation invitation, int tournamentCapacity) {
		recentInvitation = invitation;
		recentTournamentCapacity = tournamentCapacity;
		resetLayout();
	}
	
	@Override
	public void resetLayout() {
		confirmed.getElement().getStyle().setDisplay(Display.NONE);
		rejected.getElement().getStyle().setDisplay(Display.NONE);
		confirmedButOverLimit.getElement().getStyle().setDisplay(Display.NONE);
		changeConfirmation.getElement().getStyle().setDisplay(Display.NONE);
		confirmationForm.getElement().getStyle().setDisplay(Display.NONE);

		if(recentInvitation == null)
			return;
		
		switch(recentInvitation.getReply()) {
			case ACCEPTED:
				if(recentInvitation.getOrdinal() <= recentTournamentCapacity)
					confirmed.getElement().getStyle().clearDisplay();
				else
					confirmedButOverLimit.getElement().getStyle().clearDisplay();
				changeConfirmation.getElement().getStyle().clearDisplay();
				break;
			case REJECTED:
				rejected.getElement().getStyle().clearDisplay();
				changeConfirmation.getElement().getStyle().clearDisplay();
				break;
			case NO_REPLY:
				confirmationForm.getElement().getStyle().clearDisplay();
				break;
		}
	}
	
	@UiHandler("buttonAccept")
	void onAcceptClicked(ClickEvent event) {
		presenter.onAcceptClicked();
	}
	
	@UiHandler("buttonReject")
	void onRejectClicked(ClickEvent event) {
		presenter.onRejectClicked();
	}
	
	@UiHandler("changeConfirmation")
	void onChangeConfirmationClicked(ClickEvent event) {
		confirmed.getElement().getStyle().setDisplay(Display.NONE);
		rejected.getElement().getStyle().setDisplay(Display.NONE);
		confirmedButOverLimit.getElement().getStyle().setDisplay(Display.NONE);
		changeConfirmation.getElement().getStyle().setDisplay(Display.NONE);
		confirmationForm.getElement().getStyle().clearDisplay();
	}

}
