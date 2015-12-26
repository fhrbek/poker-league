package cz.fhsoft.poker.league.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.fhsoft.poker.league.server.persistence.EntityServiceImpl;
import cz.fhsoft.poker.league.server.persistence.EntityServiceImpl.DataAction;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.InvitationEvent;
import cz.fhsoft.poker.league.shared.model.v1.InvitationEventType;
import cz.fhsoft.poker.league.shared.model.v1.Settings;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.util.TransferrableException;

public class InvitationSender extends HttpServlet {

	private static final long serialVersionUID = 7912253024066497238L;

	private static final Query unsentInvitations = ServletInitializer.getEntityManager().createQuery(
			"SELECT i FROM cz.fhsoft.poker.league.shared.model.v1.InvitationEvent i WHERE i.sent = 0 AND i.eventType = :eventType");
	
	static {
		unsentInvitations.setParameter("eventType", InvitationEventType.GENERATED);
	}
	
	private static final Query settingsQuery = ServletInitializer.getEntityManager().createQuery(
			"SELECT s FROM cz.fhsoft.poker.league.shared.model.v1.Settings s WHERE s.id = 1");
	
	private static final String PLACEHOLDER_TOURNAMENT = "@@TOURNAMENT@@";
	
	private static final String PLACEHOLDER_TOURNAMENT_START = "@@TOURNAMENT_START@@";
	
	private static final String PLACEHOLDER_INVITATION_UUID = "@@INVITATION_UUID@@";
	
	private static final String PLACEHOLDER_URL_BASE = "@@URL_BASE@@";
	
	private static final String MAIL_TEMPLATE =
			"Ahoj,\n\nWittmann Poker Club Tě zve na turnaj " + PLACEHOLDER_TOURNAMENT + ".\n\nZačátek: " + PLACEHOLDER_TOURNAMENT_START + ".\n\n" +
			"Svoji účast či neúčast prosím potvrď na této adrese: " + PLACEHOLDER_URL_BASE + "#invitation?" + PLACEHOLDER_INVITATION_UUID;
	
	private Settings settings;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)  {
		try {
			EntityServiceImpl.doWithLock(new DataAction<Void>() {

				@Override
				public Void run() throws TransferrableException {
			    	boolean change = false;

			        for(Object obj : unsentInvitations.getResultList()) {
						InvitationEvent invitationEvent = (InvitationEvent) obj;

						Invitation invitation = invitationEvent.getInvitation();

						Tournament tournament = invitation.getTournament();
						if(tournament.getTournamentStart().getTime() - new Date().getTime() <= (long) tournament.getTournamentAnnouncementLead() * 3600000L) {
					        Properties props = new Properties();
					        props.put("mail.smtp.host", getSettings().getSmtpHost());
					        props.put("mail.smtp.port", getSettings().getSmtpPort());
					        props.put("mail.smtp.starttls.enable", "true");
					        Session session = Session.getDefaultInstance(props, null);
					        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(getSettings().getDefaultTimeZone()));
					        cal.setTime(tournament.getTournamentStart());
					        
					        String formattedDate = String.format("%1$td.%1$tm.%1$tY %1$tH:%1$tM", cal);

					        String msgBody = MAIL_TEMPLATE
					        		.replace(PLACEHOLDER_TOURNAMENT, tournament.getName())
					        		.replace(PLACEHOLDER_TOURNAMENT_START, formattedDate)
					        		.replace(PLACEHOLDER_INVITATION_UUID, invitation.getUuid())
					        		.replace(PLACEHOLDER_URL_BASE, getSettings().getUrlBase());

					        try {
					            Message msg = new MimeMessage(session);
					            msg.setFrom(new InternetAddress(getSettings().getSmtpUser(), getSettings().getSmtpFrom()));
					            msg.addRecipient(Message.RecipientType.TO,
					                             new InternetAddress(invitation.getPlayer().getEmailAddress(), invitation.getPlayer().getNick()));
					            msg.setSubject("WPL Turnaj " + formattedDate);
					            msg.setText(msgBody);
					            Transport.send(msg, getSettings().getSmtpUser(), getSettings().getSmtpPassword());
					            
					            invitationEvent.setSent(true);
					            change = true;
					            ServletInitializer.getEntityManager().merge(invitationEvent);
					        }
					        catch (Exception e) {
					            // Let's log it and try again later
					        	Logger.getLogger(InvitationSender.class.getName()).severe(e.getMessage());
					        }
					    }
					}
			        
			        if(change)
			        	EntityServiceImpl.updateDataVersion();
			        
			        return null;
				}
				
			});
		} catch (TransferrableException te) {
			try {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, te.getMessage());
			} catch (IOException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}

	private Settings getSettings() {
		if(settings != null)
			return settings;

		return settings = (Settings) settingsQuery.getSingleResult();
	}

}
