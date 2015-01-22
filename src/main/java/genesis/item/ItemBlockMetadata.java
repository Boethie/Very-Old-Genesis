package genesis.item;

import genesis.util.Metadata;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

import com.google.common.base.Function;

public class ItemBlockMetadata extends ItemMultiTexture
{
	public ItemBlockMetadata(Block block, final Class clazz)
	{
		super(block, block, new Function()
		{
			@Override
			public Object apply(Object obj)
			{
				return Metadata.get(clazz, ((ItemStack) obj).getMetadata()).getUnlocalizedName();
			}
		});
	}
}
