package cz.fhsoft.poker.league.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

public interface Presenter {
	
	Presenter getParentPresenter();
	
	boolean isVisible();
	
	void setVisible(boolean visible);
	
	void go(HasWidgets container);

}
