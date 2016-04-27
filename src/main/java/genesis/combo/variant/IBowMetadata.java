package genesis.combo.variant;

public interface IBowMetadata<T extends IBowMetadata<T>> extends IMetadata<T>
{
	public int getDurability();
	public int getDraw();
	public float getVelocity();
	public float getDamage();
	public float getSpread();
}
