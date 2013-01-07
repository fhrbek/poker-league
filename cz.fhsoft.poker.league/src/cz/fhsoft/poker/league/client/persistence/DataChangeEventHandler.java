package cz.fhsoft.poker.league.client.persistence;

import com.google.gwt.event.shared.EventHandler;

public interface DataChangeEventHandler extends EventHandler {

	void onDataChange(DataChangeEvent event);

}
