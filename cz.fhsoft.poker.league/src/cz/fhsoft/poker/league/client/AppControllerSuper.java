package cz.fhsoft.poker.league.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.util.ErrorReporter;

public class AppControllerSuper implements Presenter {

	public static AppControllerSuper INSTANCE = null;
	
	private HasWidgets container;
	
	private Presenter currentAppController = null;
	
	private Presenter appControllerMain = null;
	
	private Presenter appControllerGame = null;
	
	public AppControllerSuper() {
		if(INSTANCE != null)
			throw new IllegalArgumentException("AppControllerMain already exists");

		INSTANCE = this;
	}
	
	@Override
	public void go(HasWidgets container) {
		this.container = container;
		selectApplicationEntry(Location.getHash());
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				selectApplicationEntry(event.getValue());
			}
			
		});
	}
	
	private void selectApplicationEntry(String entryName) {
		if(entryName == null)
			entryName = "";
		
		if(entryName.startsWith("#"))
			entryName = entryName.substring(1);

		if("".equals(entryName)) {
			showMain();
		}
		else if("game".equals(entryName)) {
			showGame();
		}
		else
			Location.assign("#");
		
	}
	
	private void showMain() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onFailure(Throwable reason) {
				ErrorReporter.error(reason);
			}

			@Override
			public void onSuccess() {
				if(currentAppController != null)
					currentAppController.setVisible(false);
				if(appControllerMain == null) {
					appControllerMain = new AppControllerMain(AppControllerSuper.this);
					appControllerMain.go(container);
				}
				else
					appControllerMain.setVisible(true);

				currentAppController = appControllerMain;
			}
			
		});
	}

	private void showGame() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onFailure(Throwable reason) {
				ErrorReporter.error(reason);
			}

			@Override
			public void onSuccess() {
				if(currentAppController != null)
					currentAppController.setVisible(false);
				if(appControllerGame == null) {
					appControllerGame = new AppControllerGame(AppControllerSuper.this);
					appControllerGame.go(container);
				}
				else
					appControllerGame.setVisible(true);

				currentAppController = appControllerGame;
			}
			
		});
	}

	@Override
	public Presenter getParentPresenter() {
		return null;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void setVisible(boolean visible) {
		throw new IllegalArgumentException("AppControllerSuper visibility cannot be changed");
	}
}
