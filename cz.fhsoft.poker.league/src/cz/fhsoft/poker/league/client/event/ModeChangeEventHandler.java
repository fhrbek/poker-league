package cz.fhsoft.poker.league.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ModeChangeEventHandler extends EventHandler {

	void onModeChange(ModeChangeEvent event);

}
