package cz.fhsoft.poker.league.client.presenter;

import cz.fhsoft.poker.league.client.Bootstrap;
import cz.fhsoft.poker.league.client.event.ModeChangeEvent;
import cz.fhsoft.poker.league.client.event.ModeChangeEventHandler;
import cz.fhsoft.poker.league.client.event.VisibilityChangeEvent;
import cz.fhsoft.poker.league.client.event.VisibilityChangeEventHandler;
import cz.fhsoft.poker.league.client.persistence.DataChangeEvent;
import cz.fhsoft.poker.league.client.persistence.DataChangeEventHandler;

public abstract class PresenterWithVersionedData implements PresenterWithMode {

	private Presenter parentPresenter;
	
	private boolean visible = true;
	
	private boolean dataChanged = true;
	
	protected PresenterWithVersionedData(Presenter parentPresenter) {
		this.parentPresenter = parentPresenter;

		Bootstrap.INSTANCE.getEventBus().addHandler(DataChangeEvent.TYPE, new DataChangeEventHandler() {
			
			@Override
			public void onDataChange(DataChangeEvent event) {
				dataChanged = true;

				if(isVisible())
					refresh();
			}
		});

		Bootstrap.INSTANCE.getEventBus().addHandler(VisibilityChangeEvent.TYPE, new VisibilityChangeEventHandler() {
			
			@Override
			public void onVisibilityChange(VisibilityChangeEvent event) {
				Presenter source = event.getPresenter();
				if(!source.isVisible())
					return;

				if(isInVisibilityPath(source))
					refresh();
			}
		});

		Bootstrap.INSTANCE.getEventBus().addHandler(ModeChangeEvent.TYPE, new ModeChangeEventHandler() {
			
			@Override
			public void onModeChange(ModeChangeEvent event) {
				updateForMode();
			}
		});
	}
	
	protected abstract void refresh();
	
	protected boolean isDataChanged() {
		return isDataChanged(true);
	}

	protected boolean isDataChanged(boolean reset) {
		try {
			return dataChanged;
		}
		finally {
			if(reset)
				dataChanged = false;
		}
	}
	
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
			Bootstrap.INSTANCE.getEventBus().fireEvent(new VisibilityChangeEvent(this));
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
