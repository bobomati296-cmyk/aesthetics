package com.example.aesthetics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class AestheticsScreen extends Screen {
    private final Screen parent;
    private ButtonWidget presetButton;
    private ButtonWidget fullbrightButton;

    public AestheticsScreen(Screen parent) {
        super(Text.literal("Ustawienia wyglądu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = this.height / 6;
        MinecraftClient mc = MinecraftClient.getInstance();

        // Jasnosc (to jest zwykla jasnosc z ustawien gry)
        addDrawableChild(new Suwak(cx - 100, y, 200, 20, "Jasność",
                mc.options.getGamma().getValue(), 1.0,
                v -> mc.options.getGamma().setValue(v)));

        // Pelna jasnosc w jaskiniach
        fullbrightButton = addDrawableChild(ButtonWidget.builder(napisFullbright(), b -> {
            AestheticsConfig.fullbright = !AestheticsConfig.fullbright;
            b.setMessage(napisFullbright());
        }).dimensions(cx - 100, y + 26, 200, 20).build());

        // Presety nieba
        presetButton = addDrawableChild(ButtonWidget.builder(napisPreset(), b -> {
            AestheticsConfig.preset = (AestheticsConfig.preset + 1) % SkyPreset.values().length;
            b.setMessage(napisPreset());
        }).dimensions(cx - 100, y + 60, 200, 20).build());

        // Nasycenie nieba: 0 - 200 (100 = normalnie)
        addDrawableChild(new Suwak(cx - 100, y + 86, 200, 20, "Nasycenie",
                AestheticsConfig.saturation, 200, v -> AestheticsConfig.saturation = (int) Math.round(v)));

        // Wlasne niebo: suwaki R, G, B (dzialaja gdy wybrany preset "Własne")
        addDrawableChild(new Suwak(cx - 100, y + 110, 200, 20, "Własne niebo – czerwony",
                AestheticsConfig.customR, 255, v -> AestheticsConfig.customR = (int) Math.round(v)));
        addDrawableChild(new Suwak(cx - 100, y + 134, 200, 20, "Własne niebo – zielony",
                AestheticsConfig.customG, 255, v -> AestheticsConfig.customG = (int) Math.round(v)));
        addDrawableChild(new Suwak(cx - 100, y + 158, 200, 20, "Własne niebo – niebieski",
                AestheticsConfig.customB, 255, v -> AestheticsConfig.customB = (int) Math.round(v)));

        addDrawableChild(ButtonWidget.builder(Text.literal("Gotowe"), b -> close())
                .dimensions(cx - 100, y + 184, 200, 20).build());
    }

    private Text napisFullbright() {
        return Text.literal("Pełna jasność w jaskiniach: " + (AestheticsConfig.fullbright ? "WŁ." : "WYŁ."));
    }

    private Text napisPreset() {
        return Text.literal("Niebo: " + SkyPreset.values()[AestheticsConfig.preset].nazwa);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 6 - 20, 0xFFFFFF);
    }

    @Override
    public void close() {
        AestheticsConfig.save();
        MinecraftClient.getInstance().setScreen(parent);
    }

    /** Prosty suwak: wartosc od 0 do max. */
    private static class Suwak extends SliderWidget {
        private final String nazwa;
        private final double max;
        private final Consumer<Double> przyZmianie;

        Suwak(int x, int y, int w, int h, String nazwa, double start, double max, Consumer<Double> przyZmianie) {
            super(x, y, w, h, Text.empty(), start / max);
            this.nazwa = nazwa;
            this.max = max;
            this.przyZmianie = przyZmianie;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.literal(nazwa + ": " + Math.round(this.value * max * (max <= 1.0 ? 100 : 1)) + (max <= 1.0 ? "%" : "")));
        }

        @Override
        protected void applyValue() {
            przyZmianie.accept(this.value * max);
        }
    }
}
