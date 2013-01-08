package cz.fhsoft.poker.league.client.persistence;

import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public interface EntityDigestProvider<E extends IdentifiableEntity> {
	
	String getDigest(E entity);
}
