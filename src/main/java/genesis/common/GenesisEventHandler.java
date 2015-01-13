package genesis.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class GenesisEventHandler {
    public void onHoeUse(UseHoeEvent event) {
        World world = event.world;
        BlockPos pos = event.pos;

        if (/*side != EnumFacing.DOWN &&*/ world.isAirBlock(pos.up()) && world.getBlockState(pos).getBlock() == GenesisBlocks.moss) {
            IBlockState newState = Blocks.farmland.getDefaultState();
            double x = (double) ((float) pos.getX() + 0.5F);
            double y = (double) ((float) pos.getY() + 0.5F);
            double z = (double) ((float) pos.getZ() + 0.5F);
            String soundName = newState.getBlock().stepSound.getStepSound();
            float volume = (newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F;
            float pitch = newState.getBlock().stepSound.getFrequency() * 0.8F;

            world.playSoundEffect(x, y, z, soundName, volume, pitch);

            if (!world.isRemote) {
                world.setBlockState(pos, newState);
            }

            event.setResult(Event.Result.ALLOW);
        }
    }
}
