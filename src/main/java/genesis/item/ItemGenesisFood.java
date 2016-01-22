package genesis.item;

import java.util.*;

import genesis.combo.VariantsOfTypesCombo;
import genesis.combo.VariantsOfTypesCombo.*;
import genesis.combo.variant.*;
import net.minecraft.block.Block;

public class ItemGenesisFood<V extends IMetadata<V> & IFood> extends ItemGenesisEdible<V>
{
	public ItemGenesisFood(VariantsOfTypesCombo<V> owner,
			ObjectType<Block, ? extends ItemGenesisEdible<V>> type,
			List<V> variants, Class<V> variantClass)
	{
		super(owner, type, variants, variantClass);
	}
}
