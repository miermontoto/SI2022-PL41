package g41.si2022.util;

public class ReturnValue<T> {

	private boolean state;
	private T output;

	public ReturnValue () {
		this.state = false;
	}
	
	public ReturnValue (boolean b) {
		this.state = b;
	}

	public ReturnValue (T value) {
		this.state = true;
		this.output = value;
	}
	
	public boolean isOkay () {
		return this.state;
	}
	
	public T get() {
		if (this.isOkay()) return this.output;
		return null;
	}

}
