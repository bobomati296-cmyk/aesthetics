package com.example.aesthetics.mixin;

import com.example.aesthetics.SkyPreset;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    // Podmieniamy kolor nieba, gdy wybrany jest preset
    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void aesthetics$niebo(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        Integer kolor = SkyPreset.aktualnyKolor();
        if (kolor != null) {
            cir.setReturnValue(kolor);
        }
    }
}
