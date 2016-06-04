package genesis.combo.variant;

public interface IBowMetadata<T extends IBowMetadata<T>> extends IMetadata<T>
{
	int getDurability();
	int getDraw();
	float getVelocity();
	float getDamage();
	float getSpread();
}
