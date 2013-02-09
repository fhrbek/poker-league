package cz.fhsoft.poker.league.server.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Query;
import javax.servlet.http.Cookie;

import cz.fhsoft.poker.league.client.services.VerificationService;
import cz.fhsoft.poker.league.server.AbstractServiceImpl;
import cz.fhsoft.poker.league.server.ServletInitializer;

@SuppressWarnings("serial")
public class VerificationServiceImpl extends AbstractServiceImpl implements VerificationService {
	
	private static final String CLIENT_TICKET_COOKIE_NAME = "admin.ticket";
	
	private static final Query adminPassword = ServletInitializer.getEntityManager().createQuery(
			"SELECT s.adminPassword FROM cz.fhsoft.poker.league.shared.model.v1.Settings s WHERE s.id = 1");
	
	private static final MessageDigest MD5;
	
	static {
		try {
			MD5 = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			throw new Error(e);
		}
	}

	@Override
	public Boolean checkAccess() {
		for(Cookie cookie : getThreadLocalRequest().getCookies()) {
			if(CLIENT_TICKET_COOKIE_NAME.equals(cookie.getName())) {
				String clientTicket = cookie.getValue();
				if(clientTicket != null && clientTicket.equals(getPasswordHash()))
					return true;

				return false;
			}
		}
		
		return false;
	}

	@Override
	public Boolean registerWithPassword(String password) {
		if(password != null && password.equals(getPassword())) {
			getThreadLocalResponse().addCookie(new Cookie(CLIENT_TICKET_COOKIE_NAME, md5(password)));
			return true;
		}
		
		return false;
	}

	private String md5(String str) {
		StringBuilder bld = new StringBuilder();
		for(byte b : MD5.digest(str.getBytes()))
			bld.append(String.format("%02x", b));
		
		return bld.toString();
	}

	private String getPassword() {
		return (String) adminPassword.getSingleResult();
	}

	private String getPasswordHash() {
		return md5(getPassword());
	}

}
