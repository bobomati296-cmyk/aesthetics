package com.example.aesthetics;

public enum SkyPreset {
    DOMYSLNE("Domyślne", -1),
    ZACHOD("Zachód słońca", 0xFF7A3E),
    NOC("Fioletowa noc", 0x1B1040),
    PASTELOWE("Pastelowe", 0xF4C2E0),
    NEON("Neon", 0xFF2BD6),
    FANTAZJA("Fantazja", 0x7B2FF7),
    WLASNE("Własne", -2);

    public final String nazwa;
    private final int rgb;

    SkyPreset(String nazwa, int rgb) {
        this.nazwa = nazwa;
        this.rgb = rgb;
    }

    /** Zwraca kolor nieba (ARGB) albo null, jesli ma zostac domyslne. */
    public static Integer aktualnyKolor() {
        SkyPreset p = values()[AestheticsConfig.preset];
        if (p == DOMYSLNE) return null;
        int kolor;
        if (p == WLASNE) {
            kolor = (AestheticsConfig.customR << 16)
                    | (AestheticsConfig.customG << 8) | AestheticsConfig.customB;
        } else {
            kolor = p.rgb;
        }
        return 0xFF000000 | nasycenie(kolor, AestheticsConfig.saturation / 100f);
    }

    /** Miesza kolor z jego jasnoscia: factor 0 = szarosc, 1 = bez zmian, 2 = mocniej. */
    private static int nasycenie(int rgb, float factor) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        float szarosc = 0.299f * r + 0.587f * g + 0.114f * b;
        r = clamp(Math.round(szarosc + (r - szarosc) * factor));
        g = clamp(Math.round(szarosc + (g - szarosc) * factor));
        b = clamp(Math.round(szarosc + (b - szarosc) * factor));
        return (r << 16) | (g << 8) | b;
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
}
