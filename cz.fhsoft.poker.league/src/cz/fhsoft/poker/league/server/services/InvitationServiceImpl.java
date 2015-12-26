package cz.fhsoft.poker.league.server.services;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import cz.fhsoft.poker.league.client.services.InvitationService;
import cz.fhsoft.poker.league.server.AbstractServiceImpl;
import cz.fhsoft.poker.league.server.ServletInitializer;
import cz.fhsoft.poker.league.server.persistence.EntityServiceImpl;
import cz.fhsoft.poker.league.server.persistence.EntityServiceImpl.DataAction;
import cz.fhsoft.poker.league.shared.model.v1.Invitation;
import cz.fhsoft.poker.league.shared.model.v1.InvitationReply;
import cz.fhsoft.poker.league.shared.model.v1.Tournament;
import cz.fhsoft.poker.league.shared.util.TransferrableException;

@SuppressWarnings("serial")
public class InvitationServiceImpl extends AbstractServiceImpl implements InvitationService {
	
	private static final Query invitationByUUID = ServletInitializer.getEntityManager().createQuery(
			"SELECT i FROM cz.fhsoft.poker.league.shared.model.v1.Invitation i WHERE i.uuid = :uuid");

	@Override
	public Invitation findInvitation(final String invitationUUID) throws TransferrableException {
		return EntityServiceImpl.doWithLock(new DataAction<Invitation>() {

			@Override
			public Invitation run() throws TransferrableException {
				invitationByUUID.setParameter("uuid", invitationUUID);
				Invitation invitation = null;
				
				try {
					invitation = (Invitation) invitationByUUID.getSingleResult();
				} catch (NoResultException e) {
					// let invitation set to null
				}

				return EntityServiceImpl.makeTransferable(invitation);
			}
			
		});
	}

	@Override
	public long acceptInvitation(final String invitationUUID) throws TransferrableException {
		return EntityServiceImpl.doWithLock(new DataAction<Long>() {

			@Override
			public Long run() throws TransferrableException {
				invitationByUUID.setParameter("uuid", invitationUUID);
				Invitation invitation = (Invitation) invitationByUUID.getSingleResult();
				if(invitation == null)
					throw new IllegalArgumentException("Nebyla nalezena pozvánka s UUID=" + invitationUUID);

				Tournament tournament = invitation.getTournament();

				int maxInvitationOrdinal = 0;
				
				for(Invitation inv : tournament.getInvitations()) {
					if(inv.getOrdinal() > maxInvitationOrdinal)
						maxInvitationOrdinal = inv.getOrdinal();
					
					if(inv.getId() == invitation.getId()) {
						if(inv.getReply() == InvitationReply.ACCEPTED)
							return EntityServiceImpl.getDataVersionStatic(); // nothing to do
						
						invitation = inv; // get reference from the tournament to be sure that it's really the managed instance
					}
				}

				invitation.setReply(InvitationReply.ACCEPTED);
				invitation.setOrdinal(maxInvitationOrdinal+1);
				
				ServletInitializer.getEntityManager().merge(tournament);
				long dataVersion = EntityServiceImpl.updateDataVersion();

				return dataVersion;
			}
			
		});
	}

	@Override
	public long rejectInvitation(final String invitationUUID) throws TransferrableException {
		return EntityServiceImpl.doWithLock(new DataAction<Long>() {

			@Override
			public Long run() throws TransferrableException {
				invitationByUUID.setParameter("uuid", invitationUUID);
				Invitation invitation = (Invitation) invitationByUUID.getSingleResult();
				if(invitation == null)
					throw new IllegalArgumentException("Nebyla nalezena pozvánka s UUID=" + invitationUUID);

				Tournament tournament = invitation.getTournament();
				
				int rejectedInvitationOrdinal = invitation.getOrdinal();
				
				for(Invitation inv : tournament.getInvitations()) {
					if(inv.getId() == invitation.getId()) {
						if(inv.getReply() == InvitationReply.REJECTED)
							return EntityServiceImpl.getDataVersionStatic(); // nothing to do
						
						invitation = inv; // get reference from the tournament to be sure that it's really the managed instance
					}

					if(rejectedInvitationOrdinal > 0 && inv.getOrdinal() > rejectedInvitationOrdinal)
						inv.setOrdinal(inv.getOrdinal() - 1);
				}

				invitation.setReply(InvitationReply.REJECTED);
				invitation.setOrdinal(0);
				
				ServletInitializer.getEntityManager().merge(tournament);
				long dataVersion = EntityServiceImpl.updateDataVersion();

				return dataVersion;
			}
			
		});
	}
}
