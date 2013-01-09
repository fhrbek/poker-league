package cz.fhsoft.poker.league.client.presenter;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.view.CompetitionViewImpl;
import cz.fhsoft.poker.league.client.view.CompetitionsView;
import cz.fhsoft.poker.league.shared.model.v1.Competition;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;

public class CompetitionsPresenter extends PresenterWithVersionedData implements CompetitionsView.Presenter {
	
	private CompetitionsView view;
	
	private Map<Integer, CompetitionPresenter> competitionPresenterMap = new HashMap<Integer, CompetitionPresenter>();
	
	public CompetitionsPresenter(Presenter parentPresenter, CompetitionsView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		refresh();
	}

	@Override
	protected void refresh() {
		ClientEntityManager.getInstance().list(Competition.class, new AsyncCallback<List<Competition>>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(List<Competition> competitions) {
				Collections.sort(competitions, Collections.reverseOrder(Comparators.COMPETITIONS_COMPARATOR));

				Set<Integer> usedIds = new HashSet<Integer>();

				for(Competition competition : competitions) {
					CompetitionPresenter competitionPresenter = competitionPresenterMap.get(competition.getId());
					if(competitionPresenter == null) {
						competitionPresenter = new CompetitionPresenter(CompetitionsPresenter.this, new CompetitionViewImpl());
						competitionPresenter.setCompetition(competition);
						competitionPresenter.go(view.getCompetitionsContainer());
						competitionPresenterMap.put(competition.getId(), competitionPresenter);
					}

					competitionPresenter.moveViewToTop();
					usedIds.add(competition.getId());
				}

				Iterator<Map.Entry<Integer, CompetitionPresenter>> iterator = competitionPresenterMap.entrySet().iterator();

				while(iterator.hasNext()) {
					Map.Entry<Integer, CompetitionPresenter> entry = iterator.next();
					if(!usedIds.contains(entry.getKey())) {
						entry.getValue().removeView();
						iterator.remove();
					}
				}

			}
			
		});
	}

	@Override
	public void onAddCompetition() {
		Competition newCompetition = new Competition();
		Date currentDate = new Date();
		newCompetition.setStartDate(currentDate);
		newCompetition.setEndDate(currentDate);

		// texo ignores default values from the model, let's hard code them here
		newCompetition.setDefaultMinPlayers(3);
		newCompetition.setDefaultMaxPlayers(10);

		//TODO Assign all active players to it by default
		CompetitionPresenter.competitionEditor.setEntity(newCompetition, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ErrorReporter.error(caught);
			}

			@Override
			public void onSuccess(Void result) {
				CompetitionPresenter.competitionEditor.showAsPopupPanel();
			}
			
		});
	}

}
