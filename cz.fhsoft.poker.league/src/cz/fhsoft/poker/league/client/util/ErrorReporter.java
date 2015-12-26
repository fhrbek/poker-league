package cz.fhsoft.poker.league.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ErrorReporter {

	interface SHtmlTemplates extends SafeHtmlTemplates {
		@Template("<table style='height: 100%; width: 100%; position: absolute; text-align: center;'><tr><td>"
				+ "<div>Je nám líto, ale něco se podělalo:</div>"
				+ "<div style='color: #dd0000; padding: 15px; font-size: 25px; font-weight: bold;'>{0}</div>"
				+ "<a style='padding-top: 10px;' href='javascript:window.location.reload()'>Načíst znovu</a>"
				+ "</tr></td></table>")
		SafeHtml errorReport(String message);
	}

	private static final SHtmlTemplates sHtmlTemplates = GWT.create(SHtmlTemplates.class);

	public static void error(String message) {
		RootPanel.get().clear();
		RootPanel.get().add(new HTMLPanel(sHtmlTemplates.errorReport(message)));
	}

	public static void error(Throwable t) {
		String message;

		if (t instanceof StatusCodeException) {
			StatusCodeException se = (StatusCodeException) t;
			
			if (se.getStatusCode() == 0)
				message = "Server je nedostupný, zkontrolujte připojení k internetu.";
			else
				message = se.getStatusText() + " (návratový kód " + se.getStatusCode() + ")";
		}
		else
			message = t.getMessage();
		
		if(message == null)
			message = t.getClass().getName();

		error(message);
	}
}
