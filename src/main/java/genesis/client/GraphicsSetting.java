package genesis.client;

import net.minecraft.client.Minecraft;

public enum GraphicsSetting
{
	FAST("Fast"), FANCY("Fancy");
	
	private final String name;
	
	GraphicsSetting(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public boolean isFast()
	{
		return this == FAST;
	}
	
	public boolean isFancy()
	{
		return this == FANCY;
	}
	
	public static GraphicsSetting getCurrentSetting()
	{
		return Minecraft.isFancyGraphicsEnabled() ? GraphicsSetting.FANCY : GraphicsSetting.FAST;
	}
}
