package cz.fhsoft.poker.league.client.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Formatter {
	
	public static String format(double number, int precision) {
		BigDecimal bd = new BigDecimal(number);
		BigDecimal formatted = bd.setScale(precision, RoundingMode.HALF_UP);
		return formatted.toPlainString();
	}

}
