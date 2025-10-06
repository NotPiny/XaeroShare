package dev.piny.xaeroshare.client.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class XaeroshareCatLangProvider extends FabricLanguageProvider {
    public XaeroshareCatLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "lol_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) { // TURNS OUT IT'S STUPIDLY HARD TO WRITE LOLCAT 😭
        // Keybinds
        translationBuilder.add("key.xaeroshare.share", "Ai can sharez map pointz");
        translationBuilder.add("key.category.minecraft.xaeroshare", "Map pointz sharerz");

        // Toasts
        translationBuilder.add("xaeroshare.text.toast.no_server.title", "No serverz foundz");
        translationBuilder.add("xaeroshare.text.toast.no_server.description", "Joinz a sever to sharez map pointz");
        translationBuilder.add("xaeroshare.text.toast.no_waypoints.title", "No map pointz foundz!1!!11!!");
        translationBuilder.add("xaeroshare.text.toast.no_waypoints.description", "U srsly haz no map pointz to sharez???");

        // GUI Text
        translationBuilder.add("xaeroshare.text.share_waypoint", "Sharez map pointz");
        translationBuilder.add("xaeroshare.text.enter_player_name", "Plz enter ur frendz namez:");
        translationBuilder.add("xaeroshare.text.multiplayer_note", "(Use commaz to add more frendz)");
        translationBuilder.add("xaeroshare.text.player_name", "Frendz namez");

        // Messages
        translationBuilder.add("xaeroshare.text.shared_to_player", "Ai iz sending map pointz %s to %s...");
    }
}
