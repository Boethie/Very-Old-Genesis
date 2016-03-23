package genesis.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.input.Mouse;

import genesis.combo.OreBlocks;
import genesis.combo.variant.EnumOre;
import genesis.common.GenesisBlocks;
import genesis.util.Constants;
import genesis.util.blocks.BlockTexture;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.AchievementPage;

public class GuiGenesisAchievements extends GuiScreen implements IProgressMeter
{
	private static final int minDisplayColum = AchievementList.minDisplayColumn * 24 - 112;
	private static final int minDisplayRow = AchievementList.minDisplayRow * 24 - 112;
	private static final int maxDisplayColumn = AchievementList.maxDisplayColumn * 24 - 77;
	private static final int maxDisplayRow = AchievementList.maxDisplayRow * 24 - 77;

	private static final ResourceLocation GENESIS_ACHIEVEMENT_BACKGROUND = new ResourceLocation(
			Constants.MOD_ID + ":textures/gui/achievement.png");
	private static final ResourceLocation DEFAULT_ACHIEVEMENT_BACKGROUND = new ResourceLocation(
			"textures/gui/achievement/achievement_background.png");

	protected GuiScreen parentScreen;
	protected int guiWidth = 256;
	protected int guiHeight = 202;

	protected int mouseX;
	protected int mouseY;

	protected float guiZoom = 1.0F;

	protected double achivementOffsetX = 0.0D;
	protected double achivementOffsetY = 0.0D;

	private boolean clicked;

	private StatFileWriter statFileWriter;
	private boolean loadingAchievements = true;

	private int currentPage = -1;
	private GuiButton button;

	private java.util.LinkedList<Achievement> minecraftAchievements = new java.util.LinkedList<Achievement>();

	public GuiGenesisAchievements(GuiScreen parentScreenIn, StatFileWriter statFileWriterIn)
	{
		this.parentScreen = parentScreenIn;
		this.statFileWriter = statFileWriterIn;

		this.achivementOffsetX = (AchievementList.openInventory.displayColumn * 24 - 141 / 2 - 12);
		this.achivementOffsetY = (AchievementList.openInventory.displayRow * 24 - 141 / 2);
		minecraftAchievements.clear();
		for (Achievement achievement : AchievementList.achievementList)
		{
			if (!AchievementPage.isAchievementInPages(achievement))
			{
				minecraftAchievements.add(achievement);
			}
		}
	}

	@Override
	public void initGui()
	{
		this.mc.getNetHandler()
				.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
		this.buttonList.clear();
		this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20,
				I18n.format("gui.done", new Object[0])));
		this.buttonList.add(button = new GuiButton(2, (width - guiWidth) / 2 + 24, height / 2 + 74, 125, 20,
				AchievementPage.getTitle(currentPage)));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (!this.loadingAchievements)
		{
			if (button.id == 1)
			{
				this.mc.displayGuiScreen(this.parentScreen);
			}

			if (button.id == 2)
			{
				currentPage++;
				if (currentPage >= AchievementPage.getAchievementPages().size())
				{
					currentPage = -1;
				}
				// Converts fist letter to Uppercase.
				String modName = AchievementPage.getTitle(currentPage).substring(0, 1).toUpperCase();
				int modNameLength = AchievementPage.getTitle(currentPage).length();
				modName += AchievementPage.getTitle(currentPage).substring(1, modNameLength);
				this.button.displayString = modName;
				this.guiZoom = 1.0F;
				this.achivementOffsetX = (AchievementList.openInventory.displayColumn * 24 - 141 / 2 - 12);
				this.achivementOffsetY = (AchievementList.openInventory.displayRow * 24 - 141 / 2);
			}
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
		}
		else
		{
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (this.loadingAchievements)
		{
			this.drawDefaultBackground();
			this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]),
					this.width / 2, this.height / 2, 16777215);
			this.drawCenteredString(this.fontRendererObj,
					lanSearchStates[(int) (Minecraft.getSystemTime() / 150L % lanSearchStates.length)], this.width / 2,
					this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
		}
		else
		{
			if (Mouse.isButtonDown(0))
			{
				int guiCenterX = (this.width - this.guiWidth) / 2;
				int guiCenterY = (this.height - this.guiHeight) / 2;
				int minCickAreaX = guiCenterX + 8;
				int minCickAreaY = guiCenterY + 17;

				if ((this.clicked || !this.clicked) && mouseX >= minCickAreaX && mouseX < minCickAreaX + 224
						&& mouseY >= minCickAreaY && mouseY < minCickAreaY + 155)
				{
					if (!this.clicked)
					{
						this.clicked = true;
					}
					else
					{
						this.achivementOffsetX -= ((mouseX - this.mouseX) * this.guiZoom);
						this.achivementOffsetY -= ((mouseY - this.mouseY) * this.guiZoom);
					}

					this.mouseX = mouseX;
					this.mouseY = mouseY;
				}
			}
			else
			{
				this.clicked = false;
			}

			int i1 = Mouse.getDWheel();
			float f3 = this.guiZoom;

			if (i1 < 0)
			{
				this.guiZoom += 0.25F;
			}
			else if (i1 > 0)
			{
				this.guiZoom -= 0.25F;
			}

			this.guiZoom = MathHelper.clamp_float(this.guiZoom, 1.0F, 2.0F);

			if (this.guiZoom != f3)
			{
				float f4 = f3 * this.guiWidth;
				float f = f3 * this.guiHeight;
				float f1 = this.guiZoom * this.guiWidth;
				float f2 = this.guiZoom * this.guiHeight;
				this.achivementOffsetX -= (f1 - f4) * 0.5F;
				this.achivementOffsetY -= (f2 - f) * 0.5F;
			}

			if (this.achivementOffsetX < minDisplayColum)
			{
				this.achivementOffsetX = minDisplayColum;
			}

			if (this.achivementOffsetY < minDisplayRow)
			{
				this.achivementOffsetY = minDisplayRow;
			}

			if (this.achivementOffsetX >= maxDisplayColumn)
			{
				this.achivementOffsetX = maxDisplayColumn - 1;
			}

			if (this.achivementOffsetY >= maxDisplayRow)
			{
				this.achivementOffsetY = maxDisplayRow - 1;
			}

			this.drawDefaultBackground();
			this.drawAchievementScreen(mouseX, mouseY, partialTicks);
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			this.drawTitle();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}
	}

	@Override
	public void doneLoading()
	{
		if (this.loadingAchievements)
		{
			this.loadingAchievements = false;
		}
	}

	@Override
	public void updateScreen()
	{
		if (!this.loadingAchievements)
		{
			double d0 = this.achivementOffsetX - this.achivementOffsetX;
			double d1 = this.achivementOffsetY - this.achivementOffsetY;

			if (d0 * d0 + d1 * d1 < 4.0D)
			{
				this.achivementOffsetX += d0;
				this.achivementOffsetY += d1;
			}
			else
			{
				this.achivementOffsetX += d0 * 0.85D;
				this.achivementOffsetY += d1 * 0.85D;
			}
		}
	}

	protected void drawTitle()
	{
		int colour = 4210752;
		if (AchievementPage.getTitle(currentPage).equals(Constants.MOD_ID))
			colour = Constants.TITLE_COLOUR;
		int i = (this.width - this.guiWidth) / 2;
		int j = (this.height - this.guiHeight) / 2;
		this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), i + 15, j + 5, colour);
	}

	protected void drawAchievementScreen(int p_146552_1_, int p_146552_2_, float p_146552_3_)
	{
		int i = MathHelper
				.floor_double(this.achivementOffsetX + (this.achivementOffsetX - this.achivementOffsetX) * p_146552_3_);
		int j = MathHelper
				.floor_double(this.achivementOffsetY + (this.achivementOffsetY - this.achivementOffsetY) * p_146552_3_);

		if (i < minDisplayColum)
		{
			i = minDisplayColum;
		}

		if (j < minDisplayRow)
		{
			j = minDisplayRow;
		}

		if (i >= maxDisplayColumn)
		{
			i = maxDisplayColumn - 1;
		}

		if (j >= maxDisplayRow)
		{
			j = maxDisplayRow - 1;
		}

		int k = (this.width - this.guiWidth) / 2;
		int l = (this.height - this.guiHeight) / 2;
		int i1 = k + 16;
		int j1 = l + 17;
		this.zLevel = 0.0F;
		GlStateManager.depthFunc(518);
		GlStateManager.pushMatrix();
		GlStateManager.translate(i1, j1, -200.0F);

		GlStateManager.scale(1.0F / this.guiZoom, 1.0F / this.guiZoom, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();

		if (AchievementPage.getTitle(currentPage).equals(Constants.MOD_ID))
		{
			generateGenesisBackgroundBlocks(i, j);
		}
		else
		{
			generateDefaultBackgroundBlocks(i, j);
		}

		GlStateManager.enableDepth();
		GlStateManager.depthFunc(515);
		java.util.List<Achievement> achievementList = (currentPage == -1 ? minecraftAchievements
				: AchievementPage.getAchievementPage(currentPage).getAchievements());
		this.mc.getTextureManager().bindTexture(GENESIS_ACHIEVEMENT_BACKGROUND);
		
		int showMaxSubAchievements = 4;

		if (AchievementPage.getTitle(currentPage).equals(Constants.MOD_ID))
			showMaxSubAchievements = achievementList.size();
		
		System.out.println(showMaxSubAchievements);
		
		for (int j5 = 0; j5 < achievementList.size(); ++j5)
		{
			Achievement achievement1 = achievementList.get(j5);

			if (achievement1.parentAchievement != null && achievementList.contains(achievement1.parentAchievement))
			{
				int achXPos = achievement1.displayColumn * 24 - i + 11;
				int achYPos = achievement1.displayRow * 24 - j + 11;
				int achXEndPos = achievement1.parentAchievement.displayColumn * 24 - i + 11;
				int achYEndPos = achievement1.parentAchievement.displayRow * 24 - j + 11;
				boolean achUnlocked = this.statFileWriter.hasAchievementUnlocked(achievement1);
				boolean achCanUnlock = this.statFileWriter.canUnlockAchievement(achievement1);
				int k4 = this.statFileWriter.func_150874_c(achievement1);

				if (k4 <= showMaxSubAchievements)
				{
					int lineColour = -16777216;

					if (AchievementPage.getTitle(currentPage).equals(Constants.MOD_ID))
						lineColour = Color.RED.getRGB();

					if (achUnlocked)
					{
						lineColour = -16711936;
					}
					else if (achCanUnlock)
					{
						lineColour = -6250336;
					}

					this.drawHorizontalLine(achXPos, achXEndPos, achYPos, lineColour);
					this.drawVerticalLine(achXEndPos, achYPos, achYEndPos, lineColour);

					if (achXPos > achXEndPos)
					{
						this.drawTexturedModalRect(achXPos - 11 - 7, achYPos - 5, 114, 234, 7, 11);
					}
					else if (achXPos < achXEndPos)
					{
						this.drawTexturedModalRect(achXPos + 11, achYPos - 5, 107, 234, 7, 11);
					}
					else if (achYPos > achYEndPos)
					{
						this.drawTexturedModalRect(achXPos - 5, achYPos - 11 - 7, 96, 234, 11, 7);
					}
					else if (achYPos < achYEndPos)
					{
						this.drawTexturedModalRect(achXPos - 5, achYPos + 11, 96, 241, 11, 7);
					}
				}
			}
		}

		Achievement achievement = null;
		float f3 = (p_146552_1_ - i1) * this.guiZoom;
		float f4 = (p_146552_2_ - j1) * this.guiZoom;
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();

		for (int i6 = 0; i6 < achievementList.size(); ++i6)
		{
			Achievement achievement2 = achievementList.get(i6);
			int l6 = achievement2.displayColumn * 24 - i;
			int j7 = achievement2.displayRow * 24 - j;

			if (l6 >= -24 && j7 >= -24 && l6 <= 224.0F * this.guiZoom && j7 <= 155.0F * this.guiZoom)
			{
				int l7 = this.statFileWriter.func_150874_c(achievement2);

				if (this.statFileWriter.hasAchievementUnlocked(achievement2))
				{
					float f5 = 0.75F;
					GlStateManager.color(f5, f5, f5, 1.0F);
				}
				else if (this.statFileWriter.canUnlockAchievement(achievement2))
				{
					float f6 = 1.0F;
					GlStateManager.color(f6, f6, f6, 1.0F);
				}
				else if (l7 < 3)
				{
					float f7 = 0.3F;
					GlStateManager.color(f7, f7, f7, 1.0F);
				}
				else if (l7 == 3)
				{
					float f8 = 0.2F;
					GlStateManager.color(f8, f8, f8, 1.0F);
				}
				else
				{
					if (l7 != showMaxSubAchievements && !AchievementPage.getTitle(currentPage).equals(Constants.MOD_ID))
						continue;

					float f9 = 0.1F;
					GlStateManager.color(f9, f9, f9, 1.0F);
				}

				if (AchievementPage.getTitle(currentPage).equals(Constants.MOD_ID))
				{
					this.mc.getTextureManager().bindTexture(GENESIS_ACHIEVEMENT_BACKGROUND);
				}
				else
				{
					this.mc.getTextureManager().bindTexture(DEFAULT_ACHIEVEMENT_BACKGROUND);
				}

				GlStateManager.enableBlend();

				if (achievement2.getSpecial())
				{
					this.drawTexturedModalRect(l6 - 2, j7 - 2, 26, 202, 26, 26);
				}
				else
				{
					this.drawTexturedModalRect(l6 - 2, j7 - 2, 0, 202, 26, 26);
				}
				GlStateManager.disableBlend();

				if (!this.statFileWriter.canUnlockAchievement(achievement2))
				{
					float f10 = 0.1F;
					GlStateManager.color(f10, f10, f10, 1.0F);
					this.itemRender.func_175039_a(false);
				}

				GlStateManager.disableLighting();
				GlStateManager.enableCull();
				this.itemRender.renderItemAndEffectIntoGUI(achievement2.theItemStack, l6 + 3, j7 + 3);
				GlStateManager.blendFunc(770, 771);
				GlStateManager.disableLighting();

				if (!this.statFileWriter.canUnlockAchievement(achievement2))
				{
					this.itemRender.func_175039_a(true);
				}

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

				if (f3 >= l6 && f3 <= (l6 + 22) && f4 >= j7 && f4 <= (j7 + 22))
				{
					achievement = achievement2;
				}
			}
		}

		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (AchievementPage.getTitle(currentPage).equals(Constants.MOD_ID))
		{
			this.mc.getTextureManager().bindTexture(GENESIS_ACHIEVEMENT_BACKGROUND);
		}
		else
		{
			this.mc.getTextureManager().bindTexture(DEFAULT_ACHIEVEMENT_BACKGROUND);
		}
		this.drawTexturedModalRect(k, l, 0, 0, this.guiWidth, this.guiHeight);
		this.zLevel = 0.0F;
		GlStateManager.depthFunc(515);
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
		super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);

		if (achievement != null)
		{
			String s = achievement.getStatName().getUnformattedText();
			String s1 = achievement.getDescription();
			int i7 = p_146552_1_ + 12;
			int k7 = p_146552_2_ - 4;
			int i8 = this.statFileWriter.func_150874_c(achievement);

			if (this.statFileWriter.canUnlockAchievement(achievement))
			{
				int j8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
				int i9 = this.fontRendererObj.splitStringWidth(s1, j8);

				if (this.statFileWriter.hasAchievementUnlocked(achievement))
				{
					i9 += 12;
				}

				this.drawGradientRect(i7 - 3, k7 - 3, i7 + j8 + 3, k7 + i9 + 3 + 12, -1073741824, -1073741824);
				this.fontRendererObj.drawSplitString(s1, i7, k7 + 12, j8, -6250336);

				if (this.statFileWriter.hasAchievementUnlocked(achievement))
				{
					this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken", new Object[0]), i7,
							(k7 + i9 + 4), -7302913);
				}
			}
			else if (i8 == 3)
			{
				s = I18n.format("achievement.unknown", new Object[0]);
				int k8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
				String s2 = (new ChatComponentTranslation("achievement.requires", new Object[]
				{ achievement.parentAchievement.getStatName() })).getUnformattedText();
				int i5 = this.fontRendererObj.splitStringWidth(s2, k8);
				this.drawGradientRect(i7 - 3, k7 - 3, i7 + k8 + 3, k7 + i5 + 12 + 3, -1073741824, -1073741824);
				this.fontRendererObj.drawSplitString(s2, i7, k7 + 12, k8, -9416624);
			}
			else if (i8 < 3)
			{
				int l8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
				String s3 = (new ChatComponentTranslation("achievement.requires", new Object[]
				{ achievement.parentAchievement.getStatName() })).getUnformattedText();
				int j9 = this.fontRendererObj.splitStringWidth(s3, l8);
				this.drawGradientRect(i7 - 3, k7 - 3, i7 + l8 + 3, k7 + j9 + 12 + 3, -1073741824, -1073741824);
				this.fontRendererObj.drawSplitString(s3, i7, k7 + 12, l8, -9416624);
			}
			else
			{
				s = null;
			}

			if (s != null)
			{
				this.fontRendererObj.drawStringWithShadow(s, i7, k7,
						this.statFileWriter.canUnlockAchievement(achievement) ? (achievement.getSpecial() ? -128 : -1)
								: (achievement.getSpecial() ? -8355776 : -8355712));
			}
		}

		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		RenderHelper.disableStandardItemLighting();
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return !this.loadingAchievements;
	}

	private void generateDefaultBackgroundBlocks(int i, int j)
	{
		int k1 = i + 288 >> 4;
		int l1 = j + 288 >> 4;
		int i2 = (i + 288) % 16;
		int j2 = (j + 288) % 16;
		Random random = new Random();
		float f = 16.0F / this.guiZoom;
		float f1 = 16.0F / this.guiZoom;
		for (int l3 = 0; l3 * f - j2 < 155.0F; ++l3)
		{
			float f2 = 0.6F - (l1 + l3) / 25.0F * 0.3F;
			GlStateManager.color(f2, f2, f2, 1.0F);

			for (int i4 = 0; i4 * f1 - i2 < 224.0F; ++i4)
			{
				random.setSeed((this.mc.getSession().getPlayerID().hashCode() + k1 + i4 + (l1 + l3) * 16));
				int j4 = random.nextInt(1 + l1 + l3) + (l1 + l3) / 2;
				TextureAtlasSprite textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.sand);

				if (j4 <= 37 && l1 + l3 != 35)
				{
					if (j4 == 22)
					{
						if (random.nextInt(2) == 0)
						{
							textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.diamond_ore);
						}
						else
						{
							textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.redstone_ore);
						}
					}
					else if (j4 == 10)
					{
						textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.iron_ore);
					}
					else if (j4 == 8)
					{
						textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.coal_ore);
					}
					else if (j4 > 4)
					{
						textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.stone);
					}
					else if (j4 > 0)
					{
						textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.dirt);
					}
				}
				else
				{
					Block block = Blocks.bedrock;
					textureatlassprite = BlockTexture.getTextureFromBlock(block);
				}

				this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				this.drawTexturedModalRect(i4 * 16 - i2, l3 * 16 - j2, textureatlassprite, 16, 16);
			}
		}
	}

	private void generateGenesisBackgroundBlocks(int i, int j)
	{
		int k1 = i + 288 >> 4;
		int l1 = j + 288 >> 4;
		int i2 = (i + 288) % 16;
		int j2 = (j + 288) % 16;
		Random random = new Random();
		float f = 16.0F / this.guiZoom;
		float f1 = 16.0F / this.guiZoom;
		for (int l3 = 0; l3 * f - j2 < 155.0F; ++l3)
		{
			float f2 = 0.6F - (l1 + l3) / 25.0F * 0.3F;
			GlStateManager.color(f2, f2, f2, 1.0F);

			for (int i4 = 0; i4 * f1 - i2 < 224.0F; ++i4)
			{
				random.setSeed((this.mc.getSession().getPlayerID().hashCode() + k1 + i4 + (l1 + l3) * 16));
				int j4 = random.nextInt(1 + l1 + l3) + (l1 + l3) / 2;
				TextureAtlasSprite textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.sand);

				if (j4 <= 37 && l1 + l3 != 35)
				{
					if (j4 == 22)
					{
						if (random.nextInt(2) == 0)
						{
							textureatlassprite = BlockTexture.getTextureFromBlock(
									GenesisBlocks.ores.getBlockState(OreBlocks.ORE, EnumOre.OLIVINE));
						}
						else
						{
							textureatlassprite = BlockTexture.getTextureFromBlock(
									GenesisBlocks.ores.getBlockState(OreBlocks.ORE, EnumOre.GARNET));
						}
					}
					else if (j4 == 10)
					{
						textureatlassprite = BlockTexture
								.getTextureFromBlock(GenesisBlocks.ores.getBlockState(OreBlocks.ORE, EnumOre.ZIRCON));
					}
					else if (j4 == 8)
					{
						textureatlassprite = BlockTexture
								.getTextureFromBlock(GenesisBlocks.ores.getBlockState(OreBlocks.ORE, EnumOre.QUARTZ));
					}
					else if (j4 > 4)
					{
						textureatlassprite = BlockTexture.getTextureFromBlock(GenesisBlocks.granite.getDefaultState());
					}
					else if (j4 > 0)
					{
						textureatlassprite = BlockTexture.getTextureFromBlock(Blocks.dirt);
					}
				}
				else
				{
					Block block = Blocks.bedrock;
					textureatlassprite = BlockTexture.getTextureFromBlock(block.getDefaultState());
				}

				this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				this.drawTexturedModalRect(i4 * 16 - i2, l3 * 16 - j2, textureatlassprite, 16, 16);
			}
		}
	}
}