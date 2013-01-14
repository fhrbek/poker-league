package cz.fhsoft.poker.league.client.util;

import java.util.Date;

import com.google.gwt.user.client.Random;

public class UUID {
	
	public static String generateUUID() {
		long randomLong = (long) Random.nextInt() + 2*Integer.MAX_VALUE;
		long timeLong = new Date().getTime();
		
		return Long.toHexString(timeLong) + "-" + Long.toHexString(randomLong);
	}

}
