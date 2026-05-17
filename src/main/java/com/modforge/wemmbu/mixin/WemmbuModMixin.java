package com.modforge.wemmbu.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.modforge.wemmbu.WemmbuMod;

@Mixin(Block.class)
public class WemmbuModMixin {
    private static boolean modforge_isOre(BlockState state) {
        try {
            return state.isIn(net.minecraft.registry.tag.BlockTags.COAL_ORES)
                || state.isIn(net.minecraft.registry.tag.BlockTags.IRON_ORES)
                || state.isIn(net.minecraft.registry.tag.BlockTags.GOLD_ORES)
                || state.isIn(net.minecraft.registry.tag.BlockTags.DIAMOND_ORES)
                || state.isIn(net.minecraft.registry.tag.BlockTags.EMERALD_ORES)
                || state.isIn(net.minecraft.registry.tag.BlockTags.LAPIS_ORES)
                || state.isIn(net.minecraft.registry.tag.BlockTags.REDSTONE_ORES)
                || state.isIn(net.minecraft.registry.tag.BlockTags.COPPER_ORES);
        } catch (Throwable t) {
            return false;
        }
    }

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true, require = 0)
    private static void modforge_xray(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (!WemmbuMod.XRAY_ENABLED) return;
            if (!modforge_isOre(state)) {
                cir.setReturnValue(false);
            }
        } catch (Throwable ignored) {}
    }
}
