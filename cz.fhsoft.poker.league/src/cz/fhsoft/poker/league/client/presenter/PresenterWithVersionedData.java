package cz.fhsoft.poker.league.client.presenter;

import cz.fhsoft.poker.league.client.AppController;
import cz.fhsoft.poker.league.client.persistence.DataChangeEvent;
import cz.fhsoft.poker.league.client.persistence.DataChangeEventHandler;

public abstract class PresenterWithVersionedData implements Presenter {

	private Presenter parentPresenter;
	
	private boolean visible = true;
	
	private boolean refreshFlag = false;
	
	protected PresenterWithVersionedData(Presenter parentPresenter) {
		this.parentPresenter = parentPresenter;

		AppController.INSTANCE.getEventBus().addHandler(DataChangeEvent.TYPE, new DataChangeEventHandler() {
			
			@Override
			public void onDataChange(DataChangeEvent event) {
				if(isVisible()) {
					refreshFlag = false;
					refresh();
				}
				else
					refreshFlag = true;
			}
		});
	}
	
	protected abstract void refresh();
	
	@Override
	public Presenter getParentPresenter() {
		return parentPresenter;
	}
	
	@Override
	public boolean isVisible() {
		if(!visible)
			return false;
		
		if(parentPresenter != null)
			return parentPresenter.isVisible();
		
		return true;
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(refreshFlag) {
			refreshFlag = false;
			refresh();
		}

		this.visible = visible;
	}
}
