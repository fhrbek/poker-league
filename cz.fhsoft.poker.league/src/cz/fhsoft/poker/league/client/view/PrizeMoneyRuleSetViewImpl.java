package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRuleSet;

public class PrizeMoneyRuleSetViewImpl extends Composite implements PrizeMoneyRuleSetView {
	
	private static PrizeMoneyRuleSetUiBinder uiBinder = GWT
			.create(PrizeMoneyRuleSetUiBinder.class);

	@UiTemplate("PrizeMoneyRuleSetView.ui.xml")
	interface PrizeMoneyRuleSetUiBinder extends UiBinder<Widget, PrizeMoneyRuleSetViewImpl> {
	}

	@SuppressWarnings("unused")
	private Presenter presenter;

	@UiField
	FlowPanel prizeMoneyRuleSetTableContainer;
	
	private AbstractEntityListEditor<PrizeMoneyRuleSet> editor;
	
	public PrizeMoneyRuleSetViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		updateForMode();
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setPrizeMoneyRuleSetEditor(AbstractEntityListEditor<PrizeMoneyRuleSet> editor) {
		prizeMoneyRuleSetTableContainer.clear();
		prizeMoneyRuleSetTableContainer.add(this.editor = editor);
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
