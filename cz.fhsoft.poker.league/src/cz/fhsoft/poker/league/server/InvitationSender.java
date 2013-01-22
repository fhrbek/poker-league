package cz.fhsoft.poker.league.server;

import java.util.Date;
import java.util.Properties;
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
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.InvitationEvent;
import cz.fhsoft.poker.league.shared.model.v1.InvitationEventType;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;

public class InvitationSender extends HttpServlet {

	private static final long serialVersionUID = 7912253024066497238L;

	private static final Query unsentInvitations = ServletInitializer.getEntityManager().createQuery(
			"SELECT i FROM cz.fhsoft.poker.league.shared.model.v1.InvitationEvent i WHERE i.sent = 0 AND i.eventType = :eventType");
	
	static {
		unsentInvitations.setParameter("eventType", InvitationEventType.GENERATED);
	}
	
	private static final String PLACEHOLDER_TOURNAMENT = "@@TOURNAMENT@@";
	
	private static final String PLACEHOLDER_TOURNAMENT_START = "@@TOURNAMENT_START@@";
	
	private static final String PLACEHOLDER_INVITATION_UUID = "@@INVITATION_UUID@@";
	
	private static final String MAIL_TEMPLATE =
			"Ahoj,\n\nWittman Poker Club Tě zve na turnaj " + PLACEHOLDER_TOURNAMENT + ".\n\nZačátek: " + PLACEHOLDER_TOURNAMENT_START + ".\n\n" +
			"Svoji účast či neúčast prosím potvrď na této adrese: http://wittmannpokerleague.appspot.com#invitation?" + PLACEHOLDER_INVITATION_UUID + ".";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		synchronized(EntityServiceImpl.LOCK) {
            ServletInitializer.getEntityManager().getTransaction().begin();
            
            try {
            	boolean change = false;
	
	            for(Object obj : unsentInvitations.getResultList()) {
					InvitationEvent invitationEvent = (InvitationEvent) obj;
					ServletInitializer.getEntityManager().refresh(invitationEvent);
					Invitation invitation = invitationEvent.getInvitation();
	
					Tournament tournament = invitation.getTournament();
					if(tournament.getTournamentStart().getTime() - new Date().getTime() <= tournament.getTournamentAnnouncementLead() * 3600000) {
				        Properties props = new Properties();
				        Session session = Session.getDefaultInstance(props, null);
				        
				        String formattedDate = String.format("%1$td.%1$tm.%1$tY %1$tH:%1$tM", tournament.getTournamentStart());
	
				        String msgBody = MAIL_TEMPLATE
				        		.replace(PLACEHOLDER_TOURNAMENT, tournament.getName())
				        		.replace(PLACEHOLDER_TOURNAMENT_START, formattedDate)
				        		.replace(PLACEHOLDER_INVITATION_UUID, invitation.getUuid());
	
				        try {
				            Message msg = new MimeMessage(session);
				            msg.setFrom(new InternetAddress("wittmannpoker@gmail.com", "Wittmann Poker"));
				            msg.addRecipient(Message.RecipientType.TO,
				                             new InternetAddress(invitation.getPlayer().getEmailAddress(), invitation.getPlayer().getNick()));
				            msg.setSubject("WPL Turnaj " + formattedDate);
				            msg.setText(msgBody);
				            Transport.send(msg);
				            
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
	            ServletInitializer.getEntityManager().getTransaction().commit();
            }
            finally {
            	if(ServletInitializer.getEntityManager().getTransaction().isActive())
            		ServletInitializer.getEntityManager().getTransaction().rollback();
            }
		}
	}

}
