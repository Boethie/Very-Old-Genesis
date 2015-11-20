package genesis.metadata;

import com.google.common.base.Predicate;
import com.google.common.collect.*;
import genesis.block.BlockGenesisDebris;
import genesis.item.ItemBlockMulti;
import genesis.util.Constants.Unlocalized;

import java.util.*;

public class DebrisBlocks extends VariantsCombo<IMetadata, BlockGenesisDebris, ItemBlockMulti<IMetadata>>
{
	public static final List<IMetadata> VARIANTS = new ImmutableList.Builder<IMetadata>()
			.addAll(Iterables.filter(Arrays.asList(EnumTree.values()), new Predicate<EnumTree>()
			{
				@Override
				public boolean apply(EnumTree input)
				{
					return !EnumTree.NO_DEBRIS.contains(input);
				}
			}))
			.addAll(Arrays.asList(EnumDebrisOther.values()))
			.build();
	
	public DebrisBlocks()
	{
		super(ObjectType.createBlock("debris", BlockGenesisDebris.class), VARIANTS);
		
		setUnlocalizedPrefix(Unlocalized.PREFIX);
		
		soleType.setTypeNamePosition(TypeNamePosition.POSTFIX);
	}
}
