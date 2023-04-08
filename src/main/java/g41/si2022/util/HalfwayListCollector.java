package g41.si2022.util;

import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * HalfwayListCollector.<br>
 * <p>
 * A HalfwayListCollector is a <strong>concurrent</strong> {@link java.util.stream.Collector} 
 * that has most methods implemented to handle {@link java.util.List}.<br>
 * This way, we can avoid implementing them every time we need a {@link java.util.stream.Collector}
 * to transform data into a {@link java.util.List}.
 * </p> <p>
 * A {@link java.util.stream.Collector} can be used to transform a 
 * data type {@code T} into a {@link java.util.List} of {@code A}.
 * </p> <p>
 * To do this, this class needs the method {@link #accumulator()} to be implemented.
 * </p> <p>
 * Practical Example:<br>
 * If we have a {@code java.util.HashMap<String, String>} and we need a
 * {@code java.util.List<String>} that contains all the values of the map,
 * we can use a {@code HalfwayListCollector<Hashmap<String, String>, String>}.<br>
 * The method {@link #accumulator()} could be something like this:<br>
 * <code>
 * public BiConsumer&lt;List&lt;String&gt;, Map&lt;String, String>> accumulator() {<br>
 * &nbsp;&nbsp;return (list, mapEntry) -> list.add(mapEntry.getValue());<br>
 * }
 * </code>
 * </p>
 *
 * @param <T> Original type
 * @param <A> Type that is to be contained by the result {@link List}
 * 
 * @see java.util.List
 * @see java.util.stream.Stream
 * @see java.util.stream.Collector
 * 
 * @author Alex // UO281827
 */
public abstract class HalfwayListCollector<T, A> implements java.util.stream.Collector<T, List<A>, List<A>> {

	@Override
	public Supplier<List<A>> supplier() {
		return java.util.ArrayList::new;
	}

	@Override
	public BinaryOperator<List<A>> combiner() {
		return (listA, listB) -> { listA.addAll(listB); return listA; };
	}

	@Override
	public Function<List<A>, List<A>> finisher() {
		return java.util.Collections::unmodifiableList;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Set.of(Characteristics.CONCURRENT);
	}

}
