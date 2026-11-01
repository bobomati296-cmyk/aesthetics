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
        if (p == WLASNE) {
            return 0xFF000000 | (AestheticsConfig.customR << 16)
                    | (AestheticsConfig.customG << 8) | AestheticsConfig.customB;
        }
        return 0xFF000000 | p.rgb;
    }
}
