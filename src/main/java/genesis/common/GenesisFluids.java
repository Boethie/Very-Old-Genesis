package genesis.common;

import genesis.util.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class GenesisFluids
{

	public static final Fluid KOMATIITIC_LAVA = new Fluid(Constants.PREFIX+"komatiiticlava", new ResourceLocation(Constants.ASSETS+"blocks/komatiitic_lava_still"),
			new ResourceLocation(Constants.ASSETS+"blocks/komatiitic_lava_flow")).setLuminosity(16).setDensity(2000).setViscosity(2000).setTemperature(1800)
			.setUnlocalizedName(Constants.PREFIX+"komatiiticLava");

	public static void registerFluids(){
		FluidRegistry.registerFluid(KOMATIITIC_LAVA);
	}
	
}
