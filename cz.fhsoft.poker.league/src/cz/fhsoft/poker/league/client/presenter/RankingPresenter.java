package cz.fhsoft.poker.league.client.presenter;

import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.AppControllerMain;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.RankingView;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;
import cz.fhsoft.poker.league.shared.services.RankingRecord;

public class RankingPresenter extends PresenterWithVersionedData implements RankingView.Presenter {
	
	private RankingView view;
	
	private HasWidgets container;
	
	private IdentifiableEntity rankingEvent;

	public RankingPresenter(Presenter parentPresenter, RankingView view, IdentifiableEntity rankingEvent) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
		
		this.rankingEvent = rankingEvent;
		
		setVisible(false);
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		refresh();
	}

	@Override
	protected void refresh() {
		if(isDataChanged()) {
			container.clear();
			container.add(view.asWidget());
			view.setRecords(null);
			
			AppControllerMain.INSTANCE.getStatisticsService().getRanking(rankingEvent.getClass().getName(), rankingEvent.getId(), new AsyncCallback<List<RankingRecord>>() {
	
				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}
	
				@Override
				public void onSuccess(List<RankingRecord> rankingRecords) {
	
					Collections.sort(rankingRecords, Comparators.RANKING_RECORD_COMPARATOR_STRICT);
					int i=1;
					int split=0;
					RankingRecord previousRecord = null; 
					for(RankingRecord rankingRecord : rankingRecords) {
						int rank = i++;
						if(previousRecord != null && Comparators.RANKING_RECORD_COMPARATOR_WITH_SPLIT.compare(rankingRecord, previousRecord) == 0) {
							previousRecord.setSplit(true);
							rankingRecord.setSplit(true);
							split++;
						}
						else
							split = 0;
	
						rankingRecord.setRank(rank - split);
						previousRecord = rankingRecord;
					}
	
					view.setRecords(rankingRecords);
				}
				
			});
		}
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if(visible)
			view.asWidget().getElement().getStyle().clearDisplay();
		else
			view.asWidget().getElement().getStyle().setDisplay(Display.NONE);
	}

}
