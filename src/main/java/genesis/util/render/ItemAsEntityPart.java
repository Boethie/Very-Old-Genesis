package genesis.util.render;

import genesis.util.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;

public class ItemAsEntityPart extends CustomEntityPart
{
	public ItemStack itemStack;
	
	public ItemAsEntityPart(ModelBase model)
	{
		super(model);
		
		offsetX += 0.5F;
		offsetZ += 0.5F;
		setDefaultState();
	}
	
	public void setStack(ItemStack stack)
	{
		itemStack = stack;
	}
	
	@Override
	public void doRender(float pxSize)
	{
		if (itemStack != null)
		{
			float scale = pxSize * 16;
			GlStateManager.scale(scale, scale, scale);
			Minecraft.getMinecraft().getRenderItem().renderItemModel(itemStack);
		}
	}
}
