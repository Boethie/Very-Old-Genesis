package genesis.block.tileentity;

public class TileEntityRottenStorageBox extends TileEntityInventoryLootBase
{
	public static final int SLOTS_W = 3;
	public static final int SLOTS_H = 2;
	public static final int SLOTS_COUNT = SLOTS_W * SLOTS_H;

	public TileEntityRottenStorageBox() {
		super(SLOTS_W * SLOTS_H);
	}
}