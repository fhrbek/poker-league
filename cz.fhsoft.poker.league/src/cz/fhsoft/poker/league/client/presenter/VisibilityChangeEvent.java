package cz.fhsoft.poker.league.client.presenter;

import com.google.gwt.event.shared.GwtEvent;

public class VisibilityChangeEvent extends GwtEvent<VisibilityChangeEventHandler> {

	public static Type<VisibilityChangeEventHandler> TYPE = new Type<VisibilityChangeEventHandler>();
	
	private Presenter presenter;
	
	public VisibilityChangeEvent(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public Type<VisibilityChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(VisibilityChangeEventHandler handler) {
		handler.onVisibilityChange(this);
	}

}
