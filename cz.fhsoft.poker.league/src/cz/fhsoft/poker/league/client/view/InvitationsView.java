package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;

public interface InvitationsView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.Presenter {

	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

	void setInvitationsEditor(AbstractEntityListEditor<Invitation> invitationsEditor);
	
	void refresh();
}
