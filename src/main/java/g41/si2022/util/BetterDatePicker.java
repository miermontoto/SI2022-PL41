package g41.si2022.util;

import com.github.lgooddatepicker.components.DatePicker;

/**
 * Updated version of {@link DatePicker} implementing the {@link Comparable}
 * interface.
 *
 * @see DatePicker
 * @see Comparable
 * 
 * @author Alex // UO281827
 */
public class BetterDatePicker extends DatePicker implements Comparable<DatePicker> {

	private static final long serialVersionUID = 1L;

	@Override
	public int compareTo(DatePicker o) {
		boolean thisIsNull = this.getDate() == null;
		boolean oIsNull = o.getDate() == null;
		if (thisIsNull && oIsNull) {
			return 0;
		}
		if (thisIsNull) {
			return -1;
		}
		if (oIsNull) {
			return 1;
		}
		return (int) (this.getDate().toEpochDay() - o.getDate().toEpochDay());
	}

}
