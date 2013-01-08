package cz.fhsoft.poker.league.client.persistence;

import cz.fhsoft.poker.league.shared.model.v1.DescribedEntity;
import cz.fhsoft.poker.league.shared.model.v1.Player;

public class DigestProviders {

	public static final EntityDigestProvider<DescribedEntity> DESCRIBED_ENTITY_PROVIDER = new EntityDigestProvider<DescribedEntity>() {

		@Override
		public String getDigest(DescribedEntity entity) {
			return entity.getName();
		}
		
	};
	
	public static final EntityDigestProvider<Player> PLAYER_DIGEST_PROVIDER = new EntityDigestProvider<Player>() {

		@Override
		public String getDigest(Player entity) {
			return entity.getNick();
		}
		
	};

}
