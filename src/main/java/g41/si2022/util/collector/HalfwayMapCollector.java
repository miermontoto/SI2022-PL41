package g41.si2022.util.collector;

import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class HalfwayMapCollector<KA, VA, KB, VB> 
	implements java.util.stream.Collector<Map.Entry<KA, VA>, Map<KB, VB>, Map<KB, VB>>  {

	@Override
	public Supplier<Map<KB, VB>> supplier() {
		return java.util.HashMap<KB, VB>::new;
	}

	@Override
	public BinaryOperator<Map<KB, VB>> combiner() {
		return (mapA, mapB) -> { mapA.putAll(mapB); return mapA; };
	}

	@Override
	public Function<Map<KB, VB>, Map<KB, VB>> finisher() {
		return java.util.Collections::unmodifiableMap;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return new java.util.HashSet<>(java.util.Arrays.asList(Characteristics.CONCURRENT));
	}

}
