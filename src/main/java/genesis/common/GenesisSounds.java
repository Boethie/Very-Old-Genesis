package genesis.common;

import genesis.util.Constants;
import net.minecraft.block.Block;

public final class GenesisSounds {
    public static final GenesisSoundType MOSS = new GenesisSoundType("moss", 10.0F, 1.0F);
    public static final GenesisSoundType FERN = new GenesisSoundType("fern", 10.0F, 1.0F) {
        @Override
        public String getBreakSound() {
            return Block.soundTypeGrass.getBreakSound();
        }
    };

    public static class GenesisSoundType extends Block.SoundType {
        public GenesisSoundType(String name, float volume, float frequency) {
            super(name, volume, frequency);
        }

        @Override
        public String getBreakSound() {
            return Constants.ASSETS + super.getBreakSound();
        }

        @Override
        public String getStepSound() {
            return Constants.ASSETS + super.getStepSound();
        }
    }
}
