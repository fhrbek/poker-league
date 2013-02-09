package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;

public class InvitationsViewImpl extends Composite implements InvitationsView {
	
	private static InvitationsUiBinder uiBinder = GWT.create(InvitationsUiBinder.class);

	@UiTemplate("InvitationsView.ui.xml")
	interface InvitationsUiBinder extends UiBinder<Widget, InvitationsViewImpl> {
	}

	@SuppressWarnings("unused")
	private Presenter presenter;

	@UiField
	FlowPanel invitationTableContainer;
	
	private AbstractEntityListEditor<Invitation> editor;
	
	public InvitationsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		updateForMode();
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setInvitationsEditor(AbstractEntityListEditor<Invitation> editor) {
		invitationTableContainer.clear();
		invitationTableContainer.add(this.editor = editor);
	}

	@Override
	public void refresh() {
		editor.refresh();
	}

	@Override
	public void updateForMode() {
		if(editor != null)
			editor.updateForMode();
	}
}
