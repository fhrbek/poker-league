package cz.fhsoft.poker.league.client.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.Column;

public abstract class AlignedColumn<T, C> extends Column<T, C> {

	public AlignedColumn(Cell<C> cell, HorizontalAlignmentConstant horizontalAlignment) {
		this(cell, horizontalAlignment, null);
	}

	public AlignedColumn(Cell<C> cell, VerticalAlignmentConstant verticalAlignment) {
		this(cell, null, verticalAlignment);
	}

	public AlignedColumn(Cell<C> cell, HorizontalAlignmentConstant horizontalAlignment, VerticalAlignmentConstant verticalAlignment) {
		super(cell);
		if(horizontalAlignment != null)
			setHorizontalAlignment(horizontalAlignment);
		if(verticalAlignment != null)
			setVerticalAlignment(verticalAlignment);
	}

}
