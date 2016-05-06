package genesis.util;

import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

import com.google.common.collect.*;

public final class StreamUtils
{
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static final class ImmutableCollectionCollector<T, R extends ImmutableCollection<T>>
			implements Collector<T, ImmutableCollection.Builder<T>, R>
	{
		static final BiConsumer<ImmutableCollection.Builder<Object>, Object> ACCUMULATOR =
				ImmutableCollection.Builder::add;
		static final BinaryOperator<ImmutableCollection.Builder<Object>> COMBINER =
				(l, r) -> l.addAll(r.build());
		static final Function<ImmutableCollection.Builder<Object>, Object> FINISHER =
				ImmutableCollection.Builder::build;
		
		final Supplier<ImmutableCollection.Builder<T>> supplier;
		final Set<Characteristics> characteristics;
		
		ImmutableCollectionCollector(Supplier<? extends ImmutableCollection.Builder<T>> supplier,
				Characteristics... characteristics)
		{
			this.supplier = () -> supplier.get();
			this.characteristics = MiscUtils.unmodifiableSet(characteristics);
		}
		
		@Override
		public Supplier<ImmutableCollection.Builder<T>> supplier()
		{
			return supplier;
		}
		
		@Override
		public BiConsumer<ImmutableCollection.Builder<T>, T> accumulator()
		{
			return (BiConsumer) ACCUMULATOR;
		}
		
		@Override
		public BinaryOperator<ImmutableCollection.Builder<T>> combiner()
		{
			return (BinaryOperator) COMBINER;
		}
		
		@Override
		public Function<ImmutableCollection.Builder<T>, R> finisher()
		{
			return (Function) FINISHER;
		}
		
		@Override
		public Set<Characteristics> characteristics()
		{
			return characteristics;
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static final class ImmutableMapCollector<T, K, V, R extends ImmutableMap<K, V>>
			implements Collector<T, ImmutableMap.Builder<K, V>, R>
	{
		static final BinaryOperator<ImmutableMap.Builder<Object, Object>> COMBINER = (l, r) -> l.putAll(r.build());
		static final Function<ImmutableMap.Builder<Object, Object>, Object> FINISHER = ImmutableMap.Builder::build;
		
		final Supplier<ImmutableMap.Builder<K, V>> supplier;
		final BiConsumer<ImmutableMap.Builder<K, V>, T> accumulator;
		final Set<Characteristics> characteristics;
		
		ImmutableMapCollector(
				Supplier<? extends ImmutableMap.Builder<K, V>> supplier,
				Function<? super T, ? extends K> key, Function<? super T, ? extends V> value,
				Characteristics... characteristics)
		{
			this.supplier = () -> supplier.get();
			this.accumulator = (m, t) -> m.put(key.apply(t), value.apply(t));
			this.characteristics = MiscUtils.unmodifiableSet(characteristics);
		}
		
		@Override
		public Supplier<ImmutableMap.Builder<K, V>> supplier()
		{
			return supplier;
		}
		
		@Override
		public BiConsumer<ImmutableMap.Builder<K, V>, T> accumulator()
		{
			return accumulator;
		}
		
		@Override
		public BinaryOperator<ImmutableMap.Builder<K, V>> combiner()
		{
			return (BinaryOperator) COMBINER;
		}
		
		@Override
		public Function<ImmutableMap.Builder<K, V>, R> finisher()
		{
			return (Function) FINISHER;
		}
		
		@Override
		public Set<Characteristics> characteristics()
		{
			return characteristics;
		}
	}
	
	/**
	 * @return A collector for an {@link ImmutableList}.
	 */
	public static <T> Collector<T, ImmutableCollection.Builder<T>, ImmutableList<T>> toImmList()
	{
		return new ImmutableCollectionCollector<>(ImmutableList::builder);
	}

	/**
	 * @return A collector for an {@link ImmutableSet}.
	 */
	public static <T> Collector<T, ImmutableCollection.Builder<T>, ImmutableSet<T>> toImmSet()
	{
		return new ImmutableCollectionCollector<>(ImmutableSet::builder);
	}
	
	public static <T, K, V> Collector<T, ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> toImmMap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper)
	{
		return new ImmutableMapCollector<>(ImmutableMap::builder, keyMapper, valueMapper);
	}
	
	/**
	 * @param clazz The class to filter to.
	 * @return A function to be used in {@link Stream#flatMap}.
	 */
	public static <T, R> Function<T, Stream<R>> instances(Class<R> clazz)
	{
		return (t) -> clazz.isInstance(t) ? Stream.of(clazz.cast(t)) : Stream.empty();
	}
}
