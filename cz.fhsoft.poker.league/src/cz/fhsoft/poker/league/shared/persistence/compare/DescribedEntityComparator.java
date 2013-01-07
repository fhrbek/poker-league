package cz.fhsoft.poker.league.shared.persistence.compare;

import cz.fhsoft.poker.league.shared.model.v1.DescribedEntity;
import cz.fhsoft.poker.league.shared.util.StringUtil;

public class DescribedEntityComparator<E extends DescribedEntity> extends IdentifiableEntityComparator<E> {

	public static final DescribedEntityComparator<DescribedEntity> BASIC_INSTANCE = new DescribedEntityComparator<DescribedEntity>(); 

	@Override
	public int compare(E e1, E e2) {
		int result = StringUtil.nonNullString(e1.getName()).compareTo(StringUtil.nonNullString(e2.getName()));
		if(result == 0)
			result = StringUtil.nonNullString(e1.getDescription()).compareTo(StringUtil.nonNullString(e2.getDescription()));

		if(result == 0)
			return super.compare(e1, e2);

		return result;
	}
}
