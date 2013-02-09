package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor;
import cz.fhsoft.poker.league.shared.model.v1.Player;

public class PlayersViewImpl extends Composite implements PlayersView {
	
	private static PlayersUiBinder uiBinder = GWT
			.create(PlayersUiBinder.class);

	@UiTemplate("PlayersView.ui.xml")
	interface PlayersUiBinder extends UiBinder<Widget, PlayersViewImpl> {
	}

	@SuppressWarnings("unused")
	private Presenter presenter;

	@UiField
	FlowPanel playerTableContainer;
	
	private AbstractEntityListEditor<Player> editor;
	
	public PlayersViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		updateForMode();
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setPlayersEditor(AbstractEntityListEditor<Player> editor) {
		playerTableContainer.clear();
		playerTableContainer.add(this.editor = editor);
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
