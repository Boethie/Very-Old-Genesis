package genesis.combo.variant;

public enum EnumBowType implements IBowMetadata<EnumBowType>
{
	SELF("self", 334, 20, 1.25F, 1.2F, 1), COMPOSITE("composite", 400, 12, 0.75F, 1, 2);
	
	private final String name;
	private final String unloc;
	private final int durability;
	private final int draw;
	private final float velocity;
	private final float damage;
	private final float spread;
	
	EnumBowType(String name, String unloc, int durability, int draw, float velocity, float damage, float spread)
	{
		this.name = name;
		this.unloc = unloc;
		this.durability = durability;
		this.draw = draw;
		this.velocity = velocity;
		this.damage = damage;
		this.spread = spread;
	}
	
	EnumBowType(String name, int durability, int draw, float velocity, float damage, float spread)
	{
		this(name, name, durability, draw, velocity, damage, spread);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unloc;
	}
	
	@Override
	public int getDurability()
	{
		return durability;
	}
	
	@Override
	public int getDraw()
	{
		return draw;
	}
	
	@Override
	public float getVelocity()
	{
		return velocity;
	}
	
	@Override
	public float getDamage()
	{
		return damage;
	}
	
	@Override
	public float getSpread()
	{
		return spread;
	}
}
