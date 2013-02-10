package cz.fhsoft.poker.league.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ModeChangeEvent extends GwtEvent<ModeChangeEventHandler> {

	public static Type<ModeChangeEventHandler> TYPE = new Type<ModeChangeEventHandler>();
	
	@Override
	public Type<ModeChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ModeChangeEventHandler handler) {
		handler.onModeChange(this);
	}

}
