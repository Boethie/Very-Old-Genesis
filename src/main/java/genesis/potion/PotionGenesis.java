package genesis.potion;

import org.lwjgl.opengl.GL11;

import genesis.util.render.RenderHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionGenesis extends Potion
{
	public ResourceLocation texture;
	
	public PotionGenesis(boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);
	}

	@Override
	public PotionGenesis setIconIndex(int column, int row)
	{
		super.setIconIndex(column, row);
		return this;
	}

	@Override
	public PotionGenesis setEffectiveness(double effectivenessIn)
	{
		super.setEffectiveness(effectivenessIn);
		return this;
	}
	
	public PotionGenesis setIcon(ResourceLocation loc)
	{
		texture = loc;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		if(texture != null)
		{
			x+=8;
			y+=8;
			
			RenderHelpers.drawTextureWithTessellator(x, y, 1, 16, 16, texture, -1);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
	{
		if(texture != null)
		{
			x+=4;
			y+=4;
			
			RenderHelpers.drawTextureWithTessellator(x, y, 1, 16, 16, texture, alpha);
		}
	}
}
