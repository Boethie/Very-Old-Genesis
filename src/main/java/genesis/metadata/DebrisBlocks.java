package genesis.metadata;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import genesis.block.BlockGenesisDebris;
import genesis.item.ItemBlockMulti;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class DebrisBlocks extends VariantsCombo<IMetadata, BlockGenesisDebris, ItemBlockMulti<IMetadata>>
{
	public static final ObjectType<BlockGenesisDebris, ItemBlockMulti<IMetadata>> TYPE 
			= ObjectType.createBlock("debris", BlockGenesisDebris.class);

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
	
	public DebrisBlocks()
	{
		super(TYPE, VARIANTS);
	}
}
