package org.cobra.api.tooltrim.data;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.obfuscate.DontObfuscate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.cobra.api.tooltrim.item.trim.ToolTrimMaterial;
import org.cobra.api.tooltrim.item.trim.ToolTrimPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@DontObfuscate
public abstract class TrimProvider implements DataProvider {
    private final DataOutput.PathResolver trimMaterialPathResolver;
    private final DataOutput.PathResolver trimPatternPathResolver;

    private final Map<Identifier, JsonObject> trimMaterials = new HashMap<>();
    private final Map<Identifier, JsonObject> trimPatterns = new HashMap<>();

    public TrimProvider(FabricDataOutput output) {
        this.trimMaterialPathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "tool/trim_material");
        this.trimPatternPathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "tool/trim_pattern");
    }

    protected abstract void generate();

    /**
     * Registers a new trim material.
     *
     * @param assetName            The ID of the material.
     * @param ingredient    The item or tag representing the material's ingredient.
     * @param trimMaterialRegistryKey           The registry key for the material.
     * @param keyTranslatable The translation key for localization.
     * @param modId          The namespace for this material (default: "minecraft").
     */
    public final void createTrimMaterial(String assetName, ItemConvertible ingredient, RegistryKey<ToolTrimMaterial> trimMaterialRegistryKey, String keyTranslatable, String modId) {

        String namespace = modId != null && !modId.isEmpty() ? modId : "minecraft";
        Identifier identifier = Identifier.of(namespace, assetName);

        JsonObject json = new JsonObject();
        json.addProperty("asset_name", assetName);
        json.addProperty("ingredient", ingredient.asItem().getRegistryEntry().registryKey().getValue().toString());
        Text description = Text.translatable(keyTranslatable);
        json.addProperty("description", description.getString());
        json.addProperty("material_key", trimMaterialRegistryKey.getValue().toString());
        trimMaterials.put(identifier, json);
    }

    /**
     * Registers a new trim pattern.
     *
     * @param assetId            The ID of the pattern.
     * @param templateItem  The item used as a template.
     * @param keyTranslatable The translation key for localization.
     * @param decal         Whether the pattern is a decal.
     * @param modId          The namespace for this pattern (default: "minecraft").
     */
    public final void createTrimPattern(String assetId, ItemConvertible templateItem, String keyTranslatable, boolean decal, RegistryKey<ToolTrimPattern> trimPatternRegistryKey, String modId) {

        String namespace = modId != null && !modId.isEmpty() ? modId : "minecraft";
        Identifier identifier = Identifier.of(namespace, assetId);

        JsonObject json = new JsonObject();
        json.addProperty("asset_name", assetId);
        json.addProperty("template_item", templateItem.asItem().getRegistryEntry().registryKey().getValue().toString());
        Text description = Text.translatable(keyTranslatable);
        json.addProperty("description", description.getString());
        json.addProperty("decal", decal);
        json.addProperty("pattern_key", trimPatternRegistryKey.getValue().toString());
        trimPatterns.put(identifier, json);
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        generate();
        return CompletableFuture.allOf(
                writeData(writer, trimMaterials, trimMaterialPathResolver),
                writeData(writer, trimPatterns, trimPatternPathResolver)
        );
    }

    private CompletableFuture<?> writeData(DataWriter writer, Map<Identifier, JsonObject> data, DataOutput.PathResolver resolver) {
        return CompletableFuture.allOf(
                data.entrySet().stream()
                        .map(entry -> DataProvider.writeToPath(writer, entry.getValue(), resolver.resolveJson(entry.getKey())))
                        .toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public String getName() {
        return "Trim Definitions";
    }
}
