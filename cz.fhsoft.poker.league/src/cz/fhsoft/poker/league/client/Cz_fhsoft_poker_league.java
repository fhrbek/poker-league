package cz.fhsoft.poker.league.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Cz_fhsoft_poker_league implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Document.get().getElementById("splashScreen").removeFromParent();
		new AppController().go(RootLayoutPanel.get());
	}
}
