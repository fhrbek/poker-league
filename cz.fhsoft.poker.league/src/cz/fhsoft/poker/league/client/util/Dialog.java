package cz.fhsoft.poker.league.client.util;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class Dialog {
	
	public static class Option {
		private String label;
		
		private String title;
		
		public Option(String label, String title) {
			this.label = label;
			this.title = title;
		}

		public void run() {
			// nothing by default;
		}
	}

	public static void show(String label, SafeHtml description, Option... options) {
		final PopupPanel panel = new PopupPanel(false, true);
		FlowPanel container = new FlowPanel();
		panel.add(container);
		panel.addStyleName("dialog");
		container.add(new Label(label));
		
		if(description != null)
			container.add(new HTML(description));
		
		FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.getElement().getStyle().setFloat(Float.RIGHT);
		container.add(buttonPanel);
		
		for(final Option option : options) {
			Button button = new Button(option.label);
			if(option.title != null)
				button.setTitle(option.title);
			button.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					panel.hide();
					option.run();
				}
				
			});
			buttonPanel.add(button);
		}
		
		panel.center();
	}

	public static void show(String label, String description, Option... options) {
		show(label, SafeHtmlUtils.fromString(description), options);
	}
}
