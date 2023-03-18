package g41.si2022.util;

/**
 * This class represents a pair of items.
 * It is used in {@link SwingMain} to bundle the * {@link TabbedFrame} and {@link JButton}
 * on a single {@link Map} value.
 * 
 * @param <K> First item
 * @param <V> Second item
 * 
 * @see SwingMain
 * @see TabbedFrame
 * @see JButton
 * @see Map
 * 
 * @author Alex // UO281827
 */
public class Pair<K, V> {

	private K k;
	private V v;
	
	/**
	 * Creates a new Pair of items.
	 * 
	 * @param k First item of type K
	 * @param v Second item of type V
	 */
	public Pair (K k, V v) {
		this.k = k;
		this.v = v;
	}
	
	/**
	 * Returns the first item.
	 * 
	 * @return First item.
	 */
	public K getFirst () {
		return this.k;
	}
	
	/**
	 * Returns the second item.
	 * 
	 * @return Second.item.
	 */
	public V getSecond () {
		return this.v;
	}
}
