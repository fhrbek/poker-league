package cz.fhsoft.poker.league.shared.util;

public class StringUtil {

	public static boolean isEmpty(String str) {
		return isEmpty(str, false);
	}

	public static boolean isEmpty(String str, boolean trim) {
		return str == null || (trim ? str.trim() : str).length() == 0;
	}

	public static String nonNullString(String str) {
		if(str != null)
			return str;
		
		return "";
	}
}
