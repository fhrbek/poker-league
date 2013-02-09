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
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.AppControllerSuper;

public class TournamentsViewImpl extends Composite implements TournamentsView {
	
	private static TournamentsUiBinder uiBinder = GWT.create(TournamentsUiBinder.class);

	@UiTemplate("TournamentsView.ui.xml")
	interface TournamentsUiBinder extends UiBinder<Widget, TournamentsViewImpl> {
	}

	private Presenter presenter;

	@UiField
	Widget newTournament;

	@UiField
	FlowPanel tournamentsContainer;
	
	public TournamentsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		updateForMode();
	}
	
	@UiHandler("newTournament")
	void onNewTournamentClicked(ClickEvent event) {
		presenter.onAddTournament();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public HasWidgets getTournamentsContainer() {
		return tournamentsContainer;
	}

	@Override
	public void updateForMode() {
		if(AppControllerSuper.INSTANCE.isAdminMode()) {
			newTournament.getElement().getStyle().clearVisibility();
		}
		else {
			newTournament.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		}
	}
}
