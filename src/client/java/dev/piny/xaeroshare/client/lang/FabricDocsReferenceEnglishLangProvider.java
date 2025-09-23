package dev.piny.xaeroshare.client.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class FabricDocsReferenceEnglishLangProvider extends FabricLanguageProvider {
    public FabricDocsReferenceEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // Add your translations here - example translations for XaeroShare
        translationBuilder.add("key.xaeroshare.share", "Share waypoint");
        translationBuilder.add("category.xaeroshare", "XaeroShare");
    }
}