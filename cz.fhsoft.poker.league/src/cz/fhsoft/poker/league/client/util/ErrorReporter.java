package cz.fhsoft.poker.league.client.util;

import com.google.gwt.user.client.Window;

public class ErrorReporter {

	public static void error(String message) {
		Window.alert(message);
		Window.Location.reload();
	}

	public static void error(Throwable t) {
		if(t.getMessage() != null)
			error(t.getMessage());
		else
			error(t.getClass().getName());
	}
}
