package genesis.block.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import genesis.common.Genesis;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class TileEntityInventoryLootBase extends TileEntityInventoryBase {
    protected ResourceLocation lootTable;
    protected long lootTableSeed;
    
    public TileEntityInventoryLootBase(int slots, boolean hasCustomName) {
        super(slots, hasCustomName);
    }
    
    public TileEntityInventoryLootBase(int slots) {
        super(slots);
    }
    
    @Override
    @Nullable
    public ItemStack getStackInSlot(int index)
    {
        this.fillWithLoot();
        
        return super.getStackInSlot(index);
    }
    
    @Override
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        this.fillWithLoot();
        
        return super.decrStackSize(index, count);
    }
    
    @Override
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        this.fillWithLoot();
        
        return super.removeStackFromSlot(index);
    }
    
    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        this.fillWithLoot();
        
        super.setInventorySlotContents(index, stack);
    }
    
    @Override
    protected void readVisualData(NBTTagCompound compound, boolean save)
    {
        if (!this.checkLootAndRead(compound))
        {
            super.readVisualData(compound, save);
        }
    }
    
    @Override
    protected void writeVisualData(NBTTagCompound compound, boolean save)
    {
        if (!this.checkLootAndWrite(compound))
        {
            super.writeVisualData(compound, save);
        }
    }
    
    @Override
    public void clear()
    {
        this.fillWithLoot();
        
        super.clear();
    }
    
    protected boolean checkLootAndRead(NBTTagCompound compound)
    {
        if (compound.hasKey("LootTable", 8))
        {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
            return true;
        }
        else
        {
            return false;
        }
    }
    
    protected boolean checkLootAndWrite(NBTTagCompound compound)
    {
        if (this.lootTable != null)
        {
            compound.setString("LootTable", this.lootTable.toString());
            
            if (this.lootTableSeed != 0L)
            {
                compound.setLong("LootTableSeed", this.lootTableSeed);
            }
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    protected void fillWithLoot()
    {
        if (this.lootTable != null)
        {
        	if (this.worldObj.getLootTableManager() == null)
        	{
        		Genesis.logger.info("Could not get loot manager.");
        		return;
        	}
        	
            LootTable table = this.worldObj.getLootTableManager().getLootTableFromLocation(this.lootTable);
            this.lootTable = null;
            Random random;
            
            if (this.lootTableSeed == 0L)
            {
                random = new Random();
            }
            else
            {
                random = new Random(this.lootTableSeed);
            }
            
            LootContext.Builder lootBuilder = new LootContext.Builder((WorldServer)this.worldObj);
            
            table.fillInventory(this, random, lootBuilder.build());
        }
    }
    
    public void setLootTable(ResourceLocation lootTable, long lootTableSeed)
    {
        this.lootTable = lootTable;
        this.lootTableSeed = lootTableSeed;
    }
}