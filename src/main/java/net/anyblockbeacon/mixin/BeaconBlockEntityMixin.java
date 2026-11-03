package net.anyblockbeacon.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

    // Replaces the vanilla pyramid-level calculation so any full block counts as a base block.
    @Inject(method = "updateLevel", at = @At("HEAD"), cancellable = true)
    private static void anyblockbeacon$updateLevel(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        int level = 0;
        for (int i = 1; i <= 4; level = i++) {
            int j = y - i;
            if (j < world.getBottomY()) {
                break;
            }
            boolean ok = true;
            for (int k = x - i; k <= x + i && ok; k++) {
                for (int l = z - i; l <= z + i; l++) {
                    BlockPos pos = new BlockPos(k, j, l);
                    BlockState state = world.getBlockState(pos);
                    if (!(state.isIn(BlockTags.BEACON_BASE_BLOCKS) || state.isFullCube(world, pos))) {
                        ok = false;
                        break;
                    }
                }
            }
            if (!ok) {
                break;
            }
        }
        cir.setReturnValue(level);
    }
}
