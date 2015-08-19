package genesis.block.tileentity.crafting;

import genesis.block.tileentity.TileEntityKnapper.*;
import genesis.util.*;
import genesis.util.render.ISpriteUVs;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class KnappingRecipeRegistry
{
	public static interface ISlotsKnapping
	{
		public int getKnappingWidth();
		public int getHeight();
		public ItemStack getKnappingRecipeMaterial();
		public ItemStack getKnappingRecipeTool();
		public KnappingState getKnappingSlotState(int x, int y);
	}
	
	public static interface IKnappingRecipe
	{
		public boolean shouldShowKnapping(ISlotsKnapping slots, TileEntity te);
		public boolean hasRecipe(ISlotsKnapping slots, TileEntity te);
		public ItemStack getOutput(ISlotsKnapping slots, TileEntity te);
		public void onOutputTaken(ISlotsKnapping slots, TileEntity te, EntityPlayer player);
	}
	
	public static abstract class KnappingRecipeBase implements IKnappingRecipe
	{
		@Override
		public boolean hasRecipe(ISlotsKnapping slots, TileEntity te)
		{
			return getOutput(slots, te) != null;
		}
		
		protected abstract float getExperienceDropped(ISlotsKnapping slots, TileEntity te, EntityPlayer player);
		
		@Override
		public void onOutputTaken(ISlotsKnapping slots, TileEntity te, EntityPlayer player)
		{
			World world = te.getWorld();
			WorldUtils.spawnXPOrbs(world, player.posX, player.posY + 0.5, player.posZ, getExperienceDropped(slots, te, player));
		}
	}
	
	public static class DumbKnappingRecipe extends KnappingRecipeBase
	{
		protected final ItemStack output;
		protected final int w;
		protected final int h;
		protected final boolean flip;
		protected final ItemStackKey material;
		protected final float experience;
		protected final boolean[] states;
		
		public DumbKnappingRecipe(ItemStack output, int w, int h, boolean flip, ItemStack material, float experience, boolean... states)
		{
			this.output = output;
			this.w = w;
			this.h = h;
			this.flip = flip;
			this.material = new ItemStackKey(material);
			this.experience = experience;
			this.states = states;
		}
		
		@Override
		public boolean shouldShowKnapping(ISlotsKnapping slots, TileEntity te)
		{
			if (material.equalsStack(slots.getKnappingRecipeMaterial()))
			{
				return true;
			}
			
			return false;
		}
		
		@Override
		public ItemStack getOutput(ISlotsKnapping slots, TileEntity te)
		{
			if (shouldShowKnapping(slots, te) && material.equalsStack(slots.getKnappingRecipeMaterial()))
			{
				for (int offX = 0; offX < slots.getKnappingWidth() - w + 1; offX++)
				{
					for (int offY = 0; offY < slots.getHeight() - h + 1; offY++)
					{
						for (int flippedI = 0; flippedI < 2; flippedI++)
						{
							boolean flipped = flippedI > 0;
							boolean matches = true;
							
							gridCheck:
							for (int y = 0; y < slots.getHeight(); y++)
							{
								for (int x = 0; x < slots.getKnappingWidth(); x++)
								{
									boolean target = false;
									
									if (x >= offX && x < offX + w &&
										y >= offY && y < offY + h)
									{
										int indexX = x - offX;
										int indexY = y - offY;
										
										if (flipped)
										{
											indexX = (w - 1) - indexX;
										}
										
										target = states[indexY * w + indexX];
									}
										
									if (!slots.getKnappingSlotState(x, y).isKnapped() != target)
									{
										matches = false;
										break gridCheck;
									}
								}
							}
							
							if (matches)
							{
								return output;
							}
						}
					}
				}
			}
			
			return null;
		}

		@Override
		protected float getExperienceDropped(ISlotsKnapping slots, TileEntity te, EntityPlayer player)
		{
			return experience;
		}
	}
	
	public static interface IMaterialData
	{
		public static class Impl implements IMaterialData
		{
			protected final int destroyTime;
			protected final RandomIntRange countUsed;
			protected final RandomIntRange toolDamage;
			protected final ItemStack waste;
			protected final ISpriteUVs texture;
			
			public Impl(int destroyTime, RandomIntRange countUsed, RandomIntRange toolDamage, ItemStack waste, ISpriteUVs texture)
			{
				if (countUsed == null)
					throw new IllegalArgumentException("RandomIntRange countUsed cannot be null.");
				if (toolDamage == null)
					throw new IllegalArgumentException("RandomIntRange toolDamage cannot be null.");
				if (texture == null)
					throw new IllegalArgumentException("ISpriteUVs texture cannot be null.");
				
				this.destroyTime = destroyTime;
				this.countUsed = countUsed;
				this.toolDamage = toolDamage;
				this.waste = waste;
				this.texture = texture;
			}
			
			public Impl(int destroyTime, int countUsed, int toolDamage, ItemStack waste, ISpriteUVs texture)
			{
				this(destroyTime, new RandomIntRange(1), new RandomIntRange(1), waste, texture);
			}
			
			@Override
			public int getDestroyTime()
			{
				return destroyTime;
			}

			@Override
			public int getCountUsed(Random rand)
			{
				return countUsed.getRandom(rand);
			}

			@Override
			public int getToolDamage(Random rand)
			{
				return toolDamage.getRandom(rand);
			}

			@Override
			public ItemStack getWaste(Random rand)
			{
				return waste;
			}
			
			@Override
			public ISpriteUVs getTexture()
			{
				return texture;
			}
		}

		public int getDestroyTime();
		public int getCountUsed(Random rand);
		public int getToolDamage(Random rand);
		public ItemStack getWaste(Random rand);
		public ISpriteUVs getTexture();
	}
	
	protected static List<IKnappingRecipe> recipes = Lists.newArrayList();
	protected static Set<ItemStackKey> tools = Sets.newHashSet();
	protected static Map<ItemStackKey, IMaterialData> materialData = Maps.newHashMap();
	
	public static IKnappingRecipe registerRecipe(IKnappingRecipe recipe)
	{
		recipes.add(recipe);
		return recipe;
	}
	
	public static IMaterialData getMaterialData(ItemStack material)
	{
		if (material == null)
		{
			return null;
		}
		
		return materialData.get(new ItemStackKey(material));
	}
	
	public static boolean isValidMaterial(ItemStack material)
	{
		return getMaterialData(material) != null;
	}
	
	public static void registerMaterialData(ItemStackKey material, IMaterialData data)
	{
		materialData.put(material, data);
	}
	
	public static void registerMaterialData(ItemStack material, IMaterialData data)
	{
		materialData.put(new ItemStackKey(material), data);
	}
	
	public static void registerMaterialData(ItemStack material, int destroyTime, RandomIntRange countUsed, RandomIntRange toolDamage, ItemStack waste, ISpriteUVs texture)
	{
		registerMaterialData(material, new IMaterialData.Impl(destroyTime, countUsed, toolDamage, waste, texture));
	}
	
	public static void registerMaterialData(ItemStack material, int destroyTime, int countUsed, int toolDamage, ItemStack waste, ISpriteUVs texture)
	{
		registerMaterialData(material, new IMaterialData.Impl(destroyTime, countUsed, toolDamage, waste, texture));
	}
	
	public static void registerKnappingTool(ItemStack tool)
	{
		tools.add(new ItemStackKey(tool));
	}
	
	public static boolean isKnappingTool(ItemStack knappingTool)
	{
		if (knappingTool == null)
			return false;
		
		return tools.contains(new ItemStackKey(knappingTool));
	}
	
	public static DumbKnappingRecipe registerRecipe(ItemStack output, int w, int h, boolean flip, ItemStack material, float experience, boolean... states)
	{
		if (w * h != states.length)
		{
			throw new IllegalArgumentException("Error creating dumb knapping recipe: The size of the knapping matrix states array for this recipe does not match the width and height provided.");
		}
		
		DumbKnappingRecipe recipe = new DumbKnappingRecipe(output, w, h, flip, material, experience, states);
		registerRecipe(recipe);
		return recipe;
	}
	
	public static DumbKnappingRecipe registerRecipe(ItemStack output, int w, int h, ItemStack material, float experience, boolean... states)
	{
		return registerRecipe(output, w, h, true, material, experience, states);
	}
	
	public static IKnappingRecipe getRecipe(ISlotsKnapping slots, TileEntity te)
	{
		for (IKnappingRecipe recipe : recipes)
		{
			if (recipe.hasRecipe(slots, te))
			{
				return recipe;
			}
		}
		
		return null;
	}
	
	public static boolean shouldShowKnapping(ISlotsKnapping slots, TileEntity te)
	{
		if (isValidMaterial(slots.getKnappingRecipeMaterial()))
		{
			for (IKnappingRecipe recipe : recipes)
			{
				if (recipe.shouldShowKnapping(slots, te))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static ItemStack getRecipeOutput(ISlotsKnapping slots, TileEntity te)
	{
		IKnappingRecipe recipe = getRecipe(slots, te);
		return recipe != null ? recipe.getOutput(slots, te) : null;
	}
	
	public static void onOutputTaken(ISlotsKnapping slots, TileEntity te, EntityPlayer player)
	{
		IKnappingRecipe recipe = getRecipe(slots, te);
		recipe.onOutputTaken(slots, te, player);
	}
}
