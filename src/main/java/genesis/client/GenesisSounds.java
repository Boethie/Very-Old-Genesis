package genesis.client;

import genesis.util.Constants;
import net.minecraft.block.Block;

public final class GenesisSounds
{
	public static final GenesisSoundType MOSS = new GenesisSoundType("moss", 10.0F, 1.0F);
	public static final GenesisSoundType FERN = new GenesisSoundType("fern", 10.0F, 1.0F)
	{
		@Override
		public String getBreakSound()
		{
			return Block.soundTypeGrass.getBreakSound();
		}
	};
	public static final GenesisSoundType CORAL = new GenesisSoundType("coral", 1.0F, 1.0F, true);

	public static class GenesisSoundType extends Block.SoundType
	{
		private final boolean hasCustomPlaceSound;

		public GenesisSoundType(String name, float volume, float frequency)
		{
			this(name, volume, frequency, false);
		}

		public GenesisSoundType(String name, float volume, float frequency, boolean hasCustomPlaceSound)
		{
			super(name, volume, frequency);
			this.hasCustomPlaceSound = hasCustomPlaceSound;
		}

		@Override
		public String getBreakSound()
		{
			return Constants.ASSETS + super.getBreakSound();
		}

		@Override
		public String getStepSound()
		{
			return Constants.ASSETS + super.getStepSound();
		}

		@Override
		public String getPlaceSound()
		{
			return hasCustomPlaceSound ? Constants.ASSETS + "place." + soundName : super.getPlaceSound();
		}
	}
}
