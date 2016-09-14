package genesis.block.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class TileEntityInventoryBase extends TileEntityBase implements IInventory {
    protected final ItemStack[] inventory;
    private String customName;
    private boolean hasCustomName;

    public TileEntityInventoryBase(int slots, boolean hasCustomName) {
        this.inventory = new ItemStack[slots];
        this.hasCustomName = hasCustomName;
    }

    public TileEntityInventoryBase(int slots)
    {
        this(slots, false);
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    @Override
    @Nullable
    public ItemStack getStackInSlot(int index)
    {
        return this.inventory[index];
    }

    @Override
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        this.inventory[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < this.inventory.length; ++i)
        {
            this.inventory[i] = null;
        }
    }

    @Override
    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean hasCustomName()
    {
        return this.hasCustomName;
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString(I18n.translateToLocalFormatted(getName()));
    }

    @Override
    protected void readVisualData(NBTTagCompound compound, boolean save)
    {
        NBTTagList tagList = compound.getTagList("Items", 10);

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            int j = tagCompound.getByte("Slot") & 255;
            if (j >= 0 && j < this.inventory.length)
            {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

    @Override
    protected void writeVisualData(NBTTagCompound compound, boolean save)
    {
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }

        if (this.hasCustomName() && this.customName != null)
        {
            compound.setString("CustomName", this.customName);
        }

        compound.setTag("Items", tagList);
    }

    private IItemHandler itemHandler;

    private IItemHandler createUnSidedHandler()
    {
        return new InvWrapper(this);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return (T) (itemHandler == null ? (itemHandler = createUnSidedHandler()) : itemHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
}