package cz.fhsoft.poker.league.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.shared.services.RankingRecord;



public interface RankingView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.Presenter {

	}

	void setPresenter(Presenter presenter);

	Widget asWidget();

	void setRecords(List<RankingRecord> records);
}
