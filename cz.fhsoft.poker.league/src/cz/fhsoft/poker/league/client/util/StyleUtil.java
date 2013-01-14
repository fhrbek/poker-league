package cz.fhsoft.poker.league.client.util;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

public class StyleUtil {
	
	public static void makeAbsoluteFull(Widget w) {
		Style style = w.getElement().getStyle();

		style.setPosition(Position.ABSOLUTE);
		style.setTop(0.0, Unit.PX);
		style.setRight(0.0, Unit.PX);
		style.setBottom(0.0, Unit.PX);
		style.setLeft(0.0, Unit.PX);
	}

}
