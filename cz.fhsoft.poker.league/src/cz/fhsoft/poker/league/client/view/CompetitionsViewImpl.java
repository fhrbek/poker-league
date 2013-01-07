package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CompetitionsViewImpl extends Composite implements CompetitionsView {
	
	private static CompetitionsUiBinder uiBinder = GWT.create(CompetitionsUiBinder.class);

	@UiTemplate("CompetitionsView.ui.xml")
	interface CompetitionsUiBinder extends UiBinder<Widget, CompetitionsViewImpl> {
	}

	private Presenter presenter;

	@UiField
	FlowPanel competitionsContainer;
	
	public CompetitionsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("newCompetition")
	void onNewCompetitionClicked(ClickEvent event) {
		presenter.onAddCompetition();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasWidgets getCompetitionsContainer() {
		return competitionsContainer;
	}
}
