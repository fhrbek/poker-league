package cz.fhsoft.poker.league.client.persistence;

import cz.fhsoft.poker.league.shared.model.v1.DescribedEntity;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public interface EntityDigestProvider<E extends IdentifiableEntity> {
	
	public static final EntityDigestProvider<DescribedEntity> DESCRIBED_ENTITY_PROVIDER = new EntityDigestProvider<DescribedEntity>() {

		@Override
		public String getDigest(DescribedEntity entity) {
			return entity.getName();
		}
		
	};
	
	String getDigest(E entity);
}
