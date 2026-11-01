package com.example.aesthetics;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.lwjgl.glfw.GLFW;

public class AestheticsClient implements ClientModInitializer {
    private static KeyBinding openKey;
    private static boolean bylaPelnaJasnosc = false;

    @Override
    public void onInitializeClient() {
        AestheticsConfig.load();

        // Klawisz V otwiera menu
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aesthetics.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "category.aesthetics"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) {
                client.setScreen(new AestheticsScreen(null));
            }
            if (client.player == null) return;

            // Pelna jasnosc w jaskiniach = efekt widzenia w nocy tylko u ciebie
            if (AestheticsConfig.fullbright) {
                client.player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.NIGHT_VISION, 400, 0, false, false, false));
                bylaPelnaJasnosc = true;
            } else if (bylaPelnaJasnosc) {
                client.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                bylaPelnaJasnosc = false;
            }
        });
    }
}
