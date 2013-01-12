package cz.fhsoft.poker.league.client.presenter;

import cz.fhsoft.poker.league.client.AppController;
import cz.fhsoft.poker.league.client.persistence.DataChangeEvent;
import cz.fhsoft.poker.league.client.persistence.DataChangeEventHandler;

public abstract class PresenterWithVersionedData implements Presenter {

	private Presenter parentPresenter;
	
	private boolean visible = true;
	
	protected PresenterWithVersionedData(Presenter parentPresenter) {
		this.parentPresenter = parentPresenter;

		AppController.INSTANCE.getEventBus().addHandler(DataChangeEvent.TYPE, new DataChangeEventHandler() {
			
			@Override
			public void onDataChange(DataChangeEvent event) {
				if(isVisible())
					refresh();
			}
		});

		AppController.INSTANCE.getEventBus().addHandler(VisibilityChangeEvent.TYPE, new VisibilityChangeEventHandler() {
			
			@Override
			public void onVisibilityChange(VisibilityChangeEvent event) {
				Presenter source = event.getPresenter();
				if(!source.isVisible())
					return;

				if(isInVisibilityPath(source))
					refresh();
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
		boolean change = this.visible != visible;
		this.visible = visible;
		
		if(change)
			AppController.INSTANCE.getEventBus().fireEvent(new VisibilityChangeEvent(this));
	}


	private boolean isInVisibilityPath(Presenter source) {
		Presenter node = this;
		
		while(node != null) {
			if(node == source)
				return true;
			node = node.getParentPresenter();
		}

		return false;
	}
}
