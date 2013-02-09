package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.AppControllerSuper;

public class CompetitionViewImpl extends Composite implements CompetitionView {
	
	private static CompetitionUiBinder uiBinder = GWT.create(CompetitionUiBinder.class);

	@UiTemplate("CompetitionView.ui.xml")
	interface CompetitionUiBinder extends UiBinder<Widget, CompetitionViewImpl> {
	}

	private Presenter presenter;
	
	@UiField
	Label name;

	@UiField
	FlowPanel rankingContainer;

	@UiField
	FlowPanel tournamentsContainer;
	
	@UiField
	Widget edit;
	
	@UiField
	Widget remove;
	
	public CompetitionViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		updateForMode();
	}
	
	@UiHandler("edit")
	void onEditCompetitionClicked(ClickEvent event) {
		presenter.onEdit();
	}

	@UiHandler("remove")
	void onRemoveCompetitionClicked(ClickEvent event) {
		presenter.onRemove();
	}

	@UiHandler("toggleShowRanking")
	void onToggleShowRankinwClicked(ClickEvent event) {
		presenter.onToggleShowRanking();
	}

	@UiHandler("toggleShowTournaments")
	void onToggleShowTournamentsClicked(ClickEvent event) {
		presenter.onToggleShowTournaments();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasWidgets getRankingContainer() {
		return rankingContainer;
	}

	@Override
	public HasWidgets getTournamentsContainer() {
		return tournamentsContainer;
	}

	@Override
	public void setName(String name) {
		this.name.setText(name);
	}

	@Override
	public void updateForMode() {
		if(AppControllerSuper.INSTANCE.isAdminMode()) {
			edit.getElement().getStyle().clearVisibility();
			remove.getElement().getStyle().clearVisibility();
		}
		else {
			edit.getElement().getStyle().setVisibility(Visibility.HIDDEN);
			remove.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		}
	}
}
