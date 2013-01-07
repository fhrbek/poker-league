package cz.fhsoft.poker.league.client.persistence;

import com.google.gwt.event.shared.GwtEvent;

public class DataChangeEvent extends GwtEvent<DataChangeEventHandler> {

	public static Type<DataChangeEventHandler> TYPE = new Type<DataChangeEventHandler>();

	@Override
	public Type<DataChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DataChangeEventHandler handler) {
		handler.onDataChange(this);
	}

}
