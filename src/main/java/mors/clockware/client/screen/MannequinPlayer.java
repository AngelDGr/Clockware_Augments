package mors.clockware.client.screen;

import com.mojang.authlib.GameProfile;
import mors.clockware.Clockware_Main;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class MannequinPlayer extends AbstractClientPlayer {
    private final Minecraft minecraft;

    public MannequinPlayer(@NotNull Minecraft minecraft) {
        super(Objects.requireNonNull(minecraft.level), new GameProfile(UUID.randomUUID(), "Dummy"));
        this.minecraft=minecraft;
    }

    @Override
    public @NotNull PlayerSkin getSkin() {
        return new PlayerSkin(
                ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "textures/gui/mannequin.png"),
                null, null, null,
                Clockware_Util.isSlim(minecraft.player)? PlayerSkin.Model.SLIM: PlayerSkin.Model.WIDE, false
        );
    }
}
