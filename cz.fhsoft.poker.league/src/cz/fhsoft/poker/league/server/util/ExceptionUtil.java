package cz.fhsoft.poker.league.server.util;

import cz.fhsoft.poker.league.shared.util.TransferrableException;

public class ExceptionUtil {

	private ExceptionUtil() {
		// don't instantiate
	}

	public static TransferrableException transferrableException(Throwable t) {
		
		if (t instanceof TransferrableException)
			return (TransferrableException) t;

		String message = t.getMessage();
		
		if (message == null || message.length() == 0)
			message = t.getClass().getName();

		return new TransferrableException(message);
	}
}
