package genesis.potion;

import net.minecraft.potion.Potion;

public class PotionGenesis extends Potion
{
	public PotionGenesis(boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);
	}

	@Override
	public PotionGenesis setIconIndex(int column, int row)
	{
		super.setIconIndex(column, row);
		return this;
	}

	@Override
	public PotionGenesis setEffectiveness(double effectivenessIn)
	{
		super.setEffectiveness(effectivenessIn);
		return this;
	}
}
