package mors.clockware.block;

import com.mojang.serialization.MapCodec;
import mors.clockware.client.screen.ClockwareMenu;
import mors.clockware.client.screen.ClockwareMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class RipperdocTableBlock extends Block {
    public static final MapCodec<RipperdocTableBlock> CODEC = simpleCodec(RipperdocTableBlock::new);

    @Override
    public @NotNull MapCodec<? extends RipperdocTableBlock> codec() {
        return CODEC;
    }

    public RipperdocTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return new ClockwareMenuProvider((i, inventory, player) ->
                new ClockwareMenu(i, inventory, null), Component.translatable("gui.clockware.ripperdoc.block"),
                -1);
    }
}
