package cz.fhsoft.poker.league.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.util.StyleUtil;

public class AppControllerSuper implements Presenter {

	public static AppControllerSuper INSTANCE = null;
	
	private FlowPanel superContainer;
	
	private FlowPanel mainContainer;
	
	private FlowPanel gameContainer;
	
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
		superContainer = new FlowPanel();
		mainContainer = new FlowPanel();
		gameContainer = new FlowPanel();
		StyleUtil.makeAbsoluteFull(superContainer);
		StyleUtil.makeAbsoluteFull(mainContainer);
		StyleUtil.makeAbsoluteFull(gameContainer);
		superContainer.add(mainContainer);
		superContainer.add(gameContainer);
		superContainer.getElement().getStyle().setZIndex(0);
		container.add(superContainer);

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
					appControllerMain.go(mainContainer);
				}
				else
					appControllerMain.setVisible(true);

				mainContainer.getElement().getStyle().clearDisplay();
				mainContainer.getElement().getStyle().setZIndex(1);
				gameContainer.getElement().getStyle().setDisplay(Display.NONE);
				gameContainer.getElement().getStyle().setZIndex(0);
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
					appControllerGame.go(gameContainer);
				}
				else
					appControllerGame.setVisible(true);

				gameContainer.getElement().getStyle().clearDisplay();
				gameContainer.getElement().getStyle().setZIndex(1);
				mainContainer.getElement().getStyle().setDisplay(Display.NONE);
				mainContainer.getElement().getStyle().setZIndex(0);
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
