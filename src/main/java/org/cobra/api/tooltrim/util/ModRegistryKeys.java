package org.cobra.api.tooltrim.util;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.cobra.api.tooltrim.ToolTrimsAPI;
import org.cobra.api.tooltrim.item.trim.ToolTrimMaterial;
import org.cobra.api.tooltrim.item.trim.ToolTrimPattern;

public class ModRegistryKeys {
    public static final RegistryKey<Registry<String>> STRING = of("string");
    public static final RegistryKey<Registry<ToolTrimMaterial>> TRIM_MATERIAL = of("trim_material");
    public static final RegistryKey<Registry<ToolTrimPattern>> TRIM_PATTERN = of("trim_pattern");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(Identifier.ofVanilla(id));
    }

    public static void register() {
        ToolTrimsAPI.LOGGER.info("Loaded Registry Keys!");
    }
}
