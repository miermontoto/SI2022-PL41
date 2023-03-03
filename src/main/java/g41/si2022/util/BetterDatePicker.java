package g41.si2022.util;

import com.github.lgooddatepicker.components.DatePicker;

public class BetterDatePicker extends DatePicker implements Comparable<DatePicker> {

	private static final long serialVersionUID = 1L;

	@Override
	public int compareTo(DatePicker o) {
		return (int) (this.getDate().toEpochDay() - o.getDate().toEpochDay());
	}
	
}
