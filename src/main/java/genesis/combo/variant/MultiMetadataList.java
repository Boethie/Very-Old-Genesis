package genesis.combo.variant;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import genesis.combo.variant.MultiMetadataList.*;
import genesis.util.MiscUtils;

import com.google.common.collect.*;

public class MultiMetadataList implements List<MultiMetadata>
{
	public static <V extends IMetadata<V>> Predicate<MultiMetadata> filter(Class<V> clazz)
	{
		return (v) -> clazz.isInstance(v.getOriginal());
	}
	
	private final List<MultiMetadata> variants;
	private final Map<IMetadata<?>, MultiMetadata> map;
	
	public class MultiMetadata implements IMetadata<MultiMetadata>
	{
		final IMetadata<?> variant;
		
		private MultiMetadata(IMetadata<?> variant)
		{
			this.variant = variant;
		}
		
		@Override public String getName()
		{
			return variant.getName();
		}
		
		@Override public String getUnlocalizedName()
		{
			return variant.getUnlocalizedName();
		}
		
		public IMetadata<?> getOriginal()
		{
			return variant;
		}
		
		@Override public int compareTo(MultiMetadata o)
		{
			return Integer.compare(variants.indexOf(this), variants.indexOf(o));
		}
		
		@Override public String toString()
		{
			return variant.toString();
		}
	}
	
	public MultiMetadataList(List<? extends IMetadata<?>> list)
	{
		ImmutableList.Builder<MultiMetadata> listBuilder = ImmutableList.builder();
		ImmutableMap.Builder<IMetadata<?>, MultiMetadata> mapBuilder = ImmutableMap.builder();
		
		for (IMetadata<?> variant : list)
		{
			MultiMetadata multiVariant = new MultiMetadata(variant);
			listBuilder.add(multiVariant);
			mapBuilder.put(variant, multiVariant);
		}
		
		variants = listBuilder.build();
		map = mapBuilder.build();
	}
	
	public MultiMetadataList(IMetadata<?>... array)
	{
		this(Arrays.asList(array));
	}
	
	@SafeVarargs
	private static List<IMetadata<?>> getSorted(final Iterable<? extends IMetadata<?>>... iters)
	{
		List<IMetadata<?>> list = new ArrayList<>();
		
		final Map<IMetadata<?>, Integer> indexes = new HashMap<>();
		int i = 0;
		
		for (Iterable<? extends IMetadata<?>> iter : iters)
		{
			Iterables.addAll(list, iter);
			
			for (IMetadata<?> variant : iter)
			{
				indexes.put(variant, i);
			}
			
			i++;
		}
		
		Collections.sort(list, new Comparator<IMetadata<?>>()
		{
			@SuppressWarnings("unchecked")
			private <V extends Comparable<V>> int compareVariants(IMetadata<?> v1, IMetadata<?> v2)
			{
				return ((V) v1).compareTo((V) v2);
			}
			
			@Override
			public int compare(IMetadata<?> v1, IMetadata<?> v2)
			{
				int compArrays = indexes.get(v1).compareTo(indexes.get(v2));
				return compArrays != 0 ? compArrays : compareVariants(v1, v2);
			}
		});
		
		return list;
	}
	
	@SafeVarargs
	public MultiMetadataList(final Iterable<? extends IMetadata<?>>... iters)
	{
		this(getSorted(iters));
	}
	
	private static Iterable<IMetadata<?>>[] getIterables(IMetadata<?>[]... variantArrays)
	{
		@SuppressWarnings("unchecked")
		Iterable<IMetadata<?>>[] iterables = new Iterable[variantArrays.length];
		int i = 0;
		
		for (final IMetadata<?>[] variants : variantArrays)
		{
			iterables[i] = MiscUtils.iterable(variants);
			i++;
		}
		
		return iterables;
	}
	
	public MultiMetadataList(final IMetadata<?>[]... variantArrays)
	{
		this(getIterables(variantArrays));
	}
	
	public MultiMetadata getVariant(IMetadata<?> variant)
	{
		return map.get(variant);
	}
	
	public List<MultiMetadata> getVariants(Stream<? extends IMetadata<?>> variants)
	{
		return variants.map((v) -> getVariant(v)).collect(Collectors.toList());
	}
	
	public List<MultiMetadata> getVariants(Collection<? extends IMetadata<?>> variants)
	{
		return getVariants(variants.stream());
	}
	
	public List<MultiMetadata> getVariants(IMetadata<?>... variants)
	{
		return getVariants(Arrays.stream(variants));
	}
	
	@Override public int size() { return variants.size(); }
	
	@Override public boolean isEmpty() { return variants.isEmpty(); }
	
	@Override public boolean contains(Object o) { return variants.contains(o); }
	
	@Override public Iterator<MultiMetadata> iterator() { return variants.iterator(); }
	
	@Override public Object[] toArray() { return variants.toArray(); }
	
	@Override public <T> T[] toArray(T[] a) { return variants.toArray(a); }
	
	@Override public boolean add(MultiMetadata e) { return variants.add(e); }
	
	@Override public boolean remove(Object o) { return variants.remove(o); }
	
	@Override public boolean containsAll(Collection<?> c) { return variants.containsAll(c); }
	
	@Override public boolean addAll(Collection<? extends MultiMetadata> c) { return variants.addAll(c); }
	
	@Override public boolean addAll(int index, Collection<? extends MultiMetadata> c) { return variants.addAll(index, c); }
	
	@Override public boolean removeAll(Collection<?> c) { return variants.removeAll(c); }
	
	@Override public boolean retainAll(Collection<?> c) { return variants.retainAll(c); }
	
	@Override public void clear() { variants.clear(); }
	
	@Override public MultiMetadata get(int index) { return variants.get(index); }
	
	@Override public MultiMetadata set(int index, MultiMetadata element) { return variants.set(index, element); }
	
	@Override public void add(int index, MultiMetadata element) { variants.add(index, element); }
	
	@Override public MultiMetadata remove(int index) { return variants.remove(index); }
	
	@Override public int indexOf(Object o) { return variants.indexOf(o); }
	
	@Override public int lastIndexOf(Object o) { return variants.lastIndexOf(o); }
	
	@Override public ListIterator<MultiMetadata> listIterator() { return variants.listIterator(); }
	
	@Override public ListIterator<MultiMetadata> listIterator(int index) { return variants.listIterator(index); }
	
	@Override public List<MultiMetadata> subList(int fromIndex, int toIndex) { return variants.subList(fromIndex, toIndex); }
}
