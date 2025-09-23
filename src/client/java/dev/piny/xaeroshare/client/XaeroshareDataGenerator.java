package dev.piny.xaeroshare.client;

import dev.piny.xaeroshare.client.lang.FabricDocsReferenceEnglishLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class XaeroshareDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(FabricDocsReferenceEnglishLangProvider::new);
    }
}
