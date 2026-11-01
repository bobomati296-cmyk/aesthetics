package com.example.aesthetics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class AestheticsConfig {
    public static boolean fullbright = false;
    public static int preset = 0;       // numer w SkyPreset
    public static int customR = 120;
    public static int customG = 170;
    public static int customB = 255;
    public static int saturation = 100; // 0 = szarosc, 100 = normalnie, 200 = mocno

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("aesthetics.json");

    private static class Data {
        boolean fullbright; int preset; int customR; int customG; int customB;
        Integer saturation;
    }

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                Data d = GSON.fromJson(Files.readString(FILE), Data.class);
                if (d != null) {
                    fullbright = d.fullbright;
                    preset = Math.max(0, Math.min(d.preset, SkyPreset.values().length - 1));
                    customR = d.customR; customG = d.customG; customB = d.customB;
                    if (d.saturation != null) {
                        saturation = Math.max(0, Math.min(d.saturation, 200));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Data d = new Data();
            d.fullbright = fullbright; d.preset = preset;
            d.customR = customR; d.customG = customG; d.customB = customB;
            d.saturation = saturation;
            Files.writeString(FILE, GSON.toJson(d));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
