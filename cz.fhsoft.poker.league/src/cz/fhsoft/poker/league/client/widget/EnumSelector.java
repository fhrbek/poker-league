package cz.fhsoft.poker.league.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class EnumSelector<E extends Enum<E>> extends ListBox implements HasValue<E> {
	
	private List<E> values;
	
	private Comparator<E> enumComparator;
	
	private Map<Integer, E> valueMap = new HashMap<Integer, E>();
	
	public EnumSelector(List<E> values) {
		this(values, null);
	}

	public EnumSelector(List<E> values, Comparator<E> enumComparator) {
		this.values = new ArrayList<E>(values);
		this.enumComparator = enumComparator;
	}

	public void setValue(E value) {
		setValue(value, false);
	}

	public void setValue(E selectedValue, final boolean fireEvents) {
		clear();
		valueMap.clear();
		Collections.sort(values, enumComparator);

		addItem("-- Vyberte položku ---", "0");
		int i = 1;
		int selectedIndex = 0;

		for(E value : values) {
			valueMap.put(i, value);
			addItem(value.name(), "" + value.ordinal());
			if(selectedValue != null && value.ordinal() == selectedValue.ordinal())
				selectedIndex = i;
			i++;
		}
		
		setSelectedIndex(selectedIndex);
		if(fireEvents)
			ValueChangeEvent.fire(EnumSelector.this, selectedValue);
	}

	public E getValue() {
		return valueMap.get(getSelectedIndex());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<E> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
