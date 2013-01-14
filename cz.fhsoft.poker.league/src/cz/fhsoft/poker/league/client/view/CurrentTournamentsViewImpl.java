package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CurrentTournamentsViewImpl extends Composite implements CurrentTournamentsView {
	
	private static CurrentTournamentsUiBinder uiBinder = GWT
			.create(CurrentTournamentsUiBinder.class);

	@UiTemplate("CurrentTournamentsView.ui.xml")
	interface CurrentTournamentsUiBinder extends UiBinder<Widget, CurrentTournamentsViewImpl> {
	}

	@SuppressWarnings("unused")
	private Presenter presenter;
	
	@UiField
	Widget noTournament;

	@UiField
	HasWidgets currentTournamentsContainer;
	
	public CurrentTournamentsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasWidgets getCurrentTournamentsContainer() {
		return currentTournamentsContainer;
	}

	@Override
	public void setNoTournament(boolean flag) {
		if(flag)
			noTournament.getElement().getStyle().clearDisplay();
		else
			noTournament.getElement().getStyle().setDisplay(Display.NONE);
	}
}
