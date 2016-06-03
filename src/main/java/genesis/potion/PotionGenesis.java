package genesis.potion;

import genesis.util.render.RenderHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionGenesis extends Potion
{
	private ResourceLocation icon;
	
	public PotionGenesis(boolean isBadEffect, int liquidColor)
	{
		super(isBadEffect, liquidColor);
	}

	@Override
	public PotionGenesis setIconIndex(int column, int row)
	{
		super.setIconIndex(column, row);
		return this;
	}

	@Override
	public PotionGenesis setEffectiveness(double effectiveness)
	{
		super.setEffectiveness(effectiveness);
		return this;
	}

	public ResourceLocation getIcon()
	{
		return icon;
	}

	public PotionGenesis setIcon(ResourceLocation icon)
	{
		this.icon = icon;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		if (icon != null)
		{
			x += 8;
			y += 8;
			
			RenderHelpers.drawTextureWithTessellator(x, y, 1, 16, 16, icon, -1);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
	{
		if (icon != null)
		{
			x += 4;
			y += 4;
			
			RenderHelpers.drawTextureWithTessellator(x, y, 1, 16, 16, icon, alpha);
		}
	}
}
