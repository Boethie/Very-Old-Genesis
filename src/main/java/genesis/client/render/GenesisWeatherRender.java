package genesis.client.render;

import genesis.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.IRenderHandler;

public class GenesisWeatherRender extends IRenderHandler {

	public static final ResourceLocation locationSandstormPng = new ResourceLocation(Constants.MOD_ID,"textures/environment/dust_storm.png");
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) 
	{
		float f = mc.theWorld.getRainStrength(partialTicks);
		
		EntityRenderer er = mc.entityRenderer;

        if (f > 0.0F)
        {
            er.enableLightmap();
            Entity entity = mc.getRenderViewEntity();
            int i = MathHelper.floor_double(entity.posX);
            int j = MathHelper.floor_double(entity.posY);
            int k = MathHelper.floor_double(entity.posZ);
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            GlStateManager.disableCull();
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.alphaFunc(516, 0.1F);
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
            int l = MathHelper.floor_double(d1);
            int i1 = 5;

            if (mc.gameSettings.fancyGraphics)
            {
                i1 = 10;
            }

            int j1 = -1;
            float f1 = er.rendererUpdateCount + partialTicks;
            vertexbuffer.setTranslation(-d0, -d1, -d2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k1 = k - i1; k1 <= k + i1; ++k1)
            {
                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                    double d3 = er.rainXCoords[i2] * 0.5D;
                    double d4 = er.rainYCoords[i2] * 0.5D;
                    blockpos$mutableblockpos.set(l1, 0, k1);
                    BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos$mutableblockpos);

                    if (biomegenbase.canRain() || biomegenbase.getEnableSnow())
                    {
                        int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;

                        if (k2 < j2)
                        {
                            k2 = j2;
                        }

                        if (l2 < j2)
                        {
                            l2 = j2;
                        }

                        int i3 = j2;

                        if (j2 < l)
                        {
                            i3 = l;
                        }

                        if (k2 != l2)
                        {
                            er.random.setSeed(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761);
                            blockpos$mutableblockpos.set(l1, k2, k1);
                            float f2 = biomegenbase.getFloatTemperature(blockpos$mutableblockpos);
                            

                            if (world.getBiomeProvider().getTemperatureAtHeight(f2, j2) >= 0.15F)
                            {
                                if (j1 != 0)
                                {
                                    if (j1 >= 0)
                                    {
                                        tessellator.draw();
                                    }

                                    j1 = 0;
                                    mc.getTextureManager().bindTexture(er.locationRainPng);
                                    vertexbuffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d5 = -((double)(er.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + partialTicks) / 32.0D * (3.0D + er.random.nextDouble());
                                double d6 = l1 + 0.5F - entity.posX;
                                double d7 = k1 + 0.5F - entity.posZ;
                                float f3 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / i1;
                                float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                                blockpos$mutableblockpos.set(l1, i3, k1);
                                int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                                int k3 = j3 >> 16 & 65535;
                                int l3 = j3 & 65535;
                                vertexbuffer.pos(l1 - d3 + 0.5D, l2, k1 - d4 + 0.5D).tex(0.0D, k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                                vertexbuffer.pos(l1 + d3 + 0.5D, l2, k1 + d4 + 0.5D).tex(1.0D, k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                                vertexbuffer.pos(l1 + d3 + 0.5D, k2, k1 + d4 + 0.5D).tex(1.0D, l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                                vertexbuffer.pos(l1 - d3 + 0.5D, k2, k1 - d4 + 0.5D).tex(0.0D, l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                            }
                            else
                            {
                                if (j1 != 1)
                                {
                                    if (j1 >= 0)
                                    {
                                        tessellator.draw();
                                    }

                                    j1 = 1;
                                    mc.getTextureManager().bindTexture(er.locationSnowPng);
                                    vertexbuffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d8 = -((er.rendererUpdateCount & 511) + partialTicks) / 512.0F;
                                double d9 = er.random.nextDouble() + f1 * 0.01D * ((float)er.random.nextGaussian());
                                double d10 = er.random.nextDouble() + f1 * (float)er.random.nextGaussian() * 0.001D;
                                double d11 = l1 + 0.5F - entity.posX;
                                double d12 = k1 + 0.5F - entity.posZ;
                                float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / i1;
                                float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
                                blockpos$mutableblockpos.set(l1, i3, k1);
                                int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                                int j4 = i4 >> 16 & 65535;
                                int k4 = i4 & 65535;
                                vertexbuffer.pos(l1 - d3 + 0.5D, l2, k1 - d4 + 0.5D).tex(0.0D + d9, k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                                vertexbuffer.pos(l1 + d3 + 0.5D, l2, k1 + d4 + 0.5D).tex(1.0D + d9, k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                                vertexbuffer.pos(l1 + d3 + 0.5D, k2, k1 + d4 + 0.5D).tex(1.0D + d9, l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                                vertexbuffer.pos(l1 - d3 + 0.5D, k2, k1 - d4 + 0.5D).tex(0.0D + d9, l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                            }
                        }
                    }
                    else
                    {
                    	int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;

                        if (k2 < j2)
                        {
                            k2 = j2;
                        }

                        if (l2 < j2)
                        {
                            l2 = j2;
                        }

                        int i3 = j2;

                        if (j2 < l)
                        {
                            i3 = l;
                        }

                        if (k2 != l2)
                        {
                        	er.random.setSeed(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761);
                            blockpos$mutableblockpos.set(l1, k2, k1);
                            
                            if (j1 != 1)
                            {
                                if (j1 >= 0)
                                {
                                    tessellator.draw();
                                }

                                j1 = 1;
                                mc.getTextureManager().bindTexture(locationSandstormPng);
                                vertexbuffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                            }

                            double d8 = -((er.rendererUpdateCount & 511) + partialTicks) / (4+er.random.nextFloat());//512
                            double d9 = er.random.nextDouble() + f1 * 0.01D * ((float)er.random.nextGaussian());
                            double d10 = er.random.nextDouble() + f1 * (float)er.random.nextGaussian() * 0.001D;
                            double d11 = l1 + 0.5F - entity.posX;
                            double d12 = k1 + 0.5F - entity.posZ;
                            float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / i1;
                            float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
                            blockpos$mutableblockpos.set(l1, i3, k1);
                            int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                            int j4 = i4 >> 16 & 65535;
                            int k4 = i4 & 65535;
                            vertexbuffer.pos(l1 - d3 + 0.5D, l2, k1 - d4 + 0.5D).tex(0.0D + d9 / 4, k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                            vertexbuffer.pos(l1 + d3 + 0.5D, l2, k1 + d4 + 0.5D).tex(1.0D + d9 / 4, k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                            vertexbuffer.pos(l1 + d3 + 0.5D, k2, k1 + d4 + 0.5D).tex(1.0D + d9 / 4, l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                            vertexbuffer.pos(l1 - d3 + 0.5D, k2, k1 - d4 + 0.5D).tex(0.0D + d9 / 4, l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                        }
                    }
                }
            }

            if (j1 >= 0)
            {
                tessellator.draw();
            }

            vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            er.disableLightmap();
        }
	}
}