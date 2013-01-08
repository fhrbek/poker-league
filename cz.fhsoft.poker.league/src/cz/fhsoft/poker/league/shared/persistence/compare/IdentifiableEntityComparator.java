package cz.fhsoft.poker.league.shared.persistence.compare;

import java.util.Comparator;

import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public class IdentifiableEntityComparator<E extends IdentifiableEntity> implements Comparator<E> {
	
	@Override
	public int compare(E e1, E e2) {
		return e1.getId() > e2.getId()
				? 1
				: (e1.getId() < e2.getId()
						? -1
						: 0);
	}
}
