package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class WorkbenchViewImpl extends Composite implements WorkbenchView {
	
	private static WorkbenchUiBinder uiBinder = GWT
			.create(WorkbenchUiBinder.class);

	@UiTemplate("WorkbenchView.ui.xml")
	interface WorkbenchUiBinder extends UiBinder<Widget, WorkbenchViewImpl> {
	}

	private Presenter presenter;
	
	@UiField
	Anchor modeSelector;

	@UiField
	TabLayoutPanel mainTabs;

	@UiField
	HasWidgets competitionsPanel;
	
	@UiField
	HasWidgets playersPanel;
	
	@UiField
	HasWidgets prizeMoneyRuleSetPanel;
	
	public WorkbenchViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasWidgets getCompetitionsContainer() {
		return competitionsPanel;
	}
	
	@Override
	public HasWidgets getPlayersContainer() {
		return playersPanel;
	}
	
	@Override
	public HasWidgets getPrizeMoneyRuleSetContainer() {
		return prizeMoneyRuleSetPanel;
	}
	
	@UiHandler("mainTabs")
	void onTabSelected(SelectionEvent<Integer> event) {
		switch(event.getSelectedItem()) {
			case 0:
				presenter.onTabChanged(WorkbenchView.Presenter.Tab.COMPETITIONS);
				break;
			case 1:
				presenter.onTabChanged(WorkbenchView.Presenter.Tab.PLAYERS);
				break;
			case 2:
				presenter.onTabChanged(WorkbenchView.Presenter.Tab.PRIZE_MONEY_RULE_SET);
				break;
		}
	}

	@Override
	public void setMode(String label, String href) {
		modeSelector.setText(label);
		modeSelector.setHref(href);
	}
}
