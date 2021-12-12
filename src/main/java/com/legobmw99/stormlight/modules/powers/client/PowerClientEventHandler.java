package com.legobmw99.stormlight.modules.powers.client;

import com.legobmw99.stormlight.modules.powers.PowersSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PowerClientEventHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final Map<BlockPos, BlockState> savedStates = new HashMap<>();

    @SubscribeEvent
    public static void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (event.getKey() == PowersClientSetup.firstSurge.getKey().getValue() || event.getKey() == PowersClientSetup.secondSurge.getKey().getValue() ||
            event.getKey() == PowersClientSetup.blade.getKey().getValue()) {
            ClientPowerUtils.acceptInput(event.getAction());
        }
    }

    @SubscribeEvent
    public static void onMouseInput(final InputEvent.MouseInputEvent event) {
        // todo investigate
        // ClientPowerUtils.acceptInput(event.getAction());

    }

    // Heavily inspired by Origins, but not a mixin
    @SubscribeEvent
    public static void renderLast(final RenderLevelLastEvent event) {

        if (mc.player != null && mc.player.hasEffect(PowersSetup.COHESION.get())) {
            Set<BlockPos> eyePositions = ClientPowerUtils.getEyePos(mc.player, 0.25F, 0.05F, 0.25F);
            Set<BlockPos> noLongerEyePositions = savedStates.keySet().stream().filter((p -> !eyePositions.contains(p))).collect(Collectors.toSet());

            // restore states
            noLongerEyePositions.forEach(eyePosition -> {
                BlockState state = savedStates.get(eyePosition);
                mc.level.setBlock(eyePosition, state, 0);
                savedStates.remove(eyePosition);
            });


            // set to air (on client only)
            eyePositions.forEach(p -> {
                BlockState stateAtP = mc.level.getBlockState(p);
                if (!savedStates.containsKey(p) && !stateAtP.isAir() && !(stateAtP.getBlock() instanceof LiquidBlock)) {
                    savedStates.put(p, stateAtP);
                    mc.level.setBlock(p, Blocks.LIGHT.defaultBlockState(), 0);
                }
            });


        } else if (savedStates.size() > 0) {
            // restore all if power is not had
            Set<BlockPos> noLongerEyePositions = new HashSet<>(savedStates.keySet());
            noLongerEyePositions.forEach(eyePosition -> {
                BlockState state = savedStates.get(eyePosition);
                mc.level.setBlock(eyePosition, state, 0);
                savedStates.remove(eyePosition);
            });
        }
    }
}
