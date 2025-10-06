package dev.piny.xaeroshare.client;

import dev.piny.xaeroshare.client.lang.XaeroshareEnglishLangProvider;
import dev.piny.xaeroshare.client.lang.XaeroshareCatLangProvider;
import dev.piny.xaeroshare.client.lang.XaerosharePirateLangProvider;
import dev.piny.xaeroshare.client.lang.XaeroshareShakespeareanLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class XaeroshareDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(XaeroshareEnglishLangProvider::new);
        pack.addProvider(XaerosharePirateLangProvider::new); // Gotta have pirate lang support
        pack.addProvider(XaeroshareCatLangProvider::new);
        pack.addProvider(XaeroshareShakespeareanLangProvider::new);
    }
}
