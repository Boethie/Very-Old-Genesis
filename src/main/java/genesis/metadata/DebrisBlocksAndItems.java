package genesis.metadata;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import genesis.block.BlockGenesisDebris;
import genesis.item.ItemBlockMulti;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class DebrisBlocksAndItems extends VariantsOfTypesCombo<IMetadata>
{
	public static final ObjectType<BlockGenesisDebris, ItemBlockMulti<EnumTree>> LEAVES = ObjectType.createBlock("debrisLeaves", BlockGenesisDebris.class);
	public static final ObjectType<BlockGenesisDebris, ItemBlockMulti<EnumDebrisOther>> OTHERS = ObjectType.createBlock("debrisOther", BlockGenesisDebris.class);
	
	public static final List<ObjectType<BlockGenesisDebris, ?>> TYPES = new ImmutableList.Builder<ObjectType<BlockGenesisDebris, ?>>()
			.add(LEAVES)
			.add(OTHERS)
			.build();
	public static final List<IMetadata> VARIANTS = new ImmutableList.Builder<IMetadata>()
			.addAll(Iterables.filter(Arrays.asList(EnumTree.values()), new Predicate<EnumTree>()
			{
				@Override
				public boolean apply(@Nullable EnumTree input)
				{
					return !EnumTree.NO_DEBRIS.contains(input);
				}
			}))
			.addAll(Arrays.asList(EnumDebrisOther.values()))
			.build();
	
	public DebrisBlocksAndItems()
	{
		super(TYPES, VARIANTS);
	}
}
