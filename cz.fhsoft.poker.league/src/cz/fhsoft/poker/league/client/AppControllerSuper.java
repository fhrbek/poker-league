package cz.fhsoft.poker.league.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.presenter.Presenter;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.client.util.StyleUtil;

public class AppControllerSuper implements Presenter {

	public static AppControllerSuper INSTANCE = null;
	
	private FlowPanel superContainer;
	
	private FlowPanel mainContainer;
	
	private FlowPanel gameContainer;
	
	private FlowPanel invitationContainer;
	
	private List<Widget> containers;
	
	private Presenter currentAppController = null;
	
	private Presenter appControllerMain = null;
	
	private Presenter appControllerGame = null;
	
	private Presenter appControllerInvitation = null;
	
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
		invitationContainer = new FlowPanel();
		containers = new ArrayList<Widget>();
		containers.add(mainContainer);
		containers.add(gameContainer);
		containers.add(invitationContainer);
		StyleUtil.makeAbsoluteFull(superContainer);
		StyleUtil.makeAbsoluteFull(mainContainer);
		StyleUtil.makeAbsoluteFull(gameContainer);
		StyleUtil.makeAbsoluteFull(invitationContainer);
		superContainer.add(mainContainer);
		superContainer.add(gameContainer);
		superContainer.add(invitationContainer);
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
		else if(entryName.startsWith("invitation?")) {
			int pos = entryName.indexOf('?');
			showInvitation(entryName.substring(pos+1));
		}
		else
			Location.assign("#");
		
	}
	
	private void setVisibleContainer(Widget visibleContainer) {
		for(Widget container : containers)
			if(container == visibleContainer) {
				container.getElement().getStyle().clearDisplay();
				container.getElement().getStyle().setZIndex(1);
			}
			else {
				container.getElement().getStyle().setDisplay(Display.NONE);
				container.getElement().getStyle().setZIndex(0);
			}
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

				setVisibleContainer(mainContainer);
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

				setVisibleContainer(gameContainer);
				currentAppController = appControllerGame;
			}
			
		});
	}

	private void showInvitation(final String invitationUUID) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onFailure(Throwable reason) {
				ErrorReporter.error(reason);
			}

			@Override
			public void onSuccess() {
				if(currentAppController != null)
					currentAppController.setVisible(false);
				if(appControllerInvitation == null) {
					appControllerInvitation = new AppControllerInvitation(AppControllerSuper.this);
					appControllerInvitation.go(invitationContainer);
				}
				else
					appControllerInvitation.setVisible(true);
				
				((AppControllerInvitation) appControllerInvitation).setInvitationUUID(invitationUUID);

				setVisibleContainer(invitationContainer);
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
