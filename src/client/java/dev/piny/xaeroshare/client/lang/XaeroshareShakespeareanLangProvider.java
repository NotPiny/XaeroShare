package dev.piny.xaeroshare.client.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class XaeroshareShakespeareanLangProvider extends FabricLanguageProvider {
    public XaeroshareShakespeareanLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "enws", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // Keybinds
        translationBuilder.add("key.xaeroshare.share", "Message thy atlas mark");
        translationBuilder.add("key.category.minecraft.xaeroshare", "Atlas Transmission");

        // Toasts
        translationBuilder.add("xaeroshare.text.toast.no_server.title", "Thy server is amiss");
        translationBuilder.add("xaeroshare.text.toast.no_server.description", "Locate thyself a server to share atlas marks");
        translationBuilder.add("xaeroshare.text.toast.no_waypoints.title", "Thy atlas marks are unfound");
        translationBuilder.add("xaeroshare.text.toast.no_waypoints.description", "Thou hast no atlas marks to share");

        // GUI Text
        translationBuilder.add("xaeroshare.text.share_waypoint", "Message Atlas Mark");
        translationBuilder.add("xaeroshare.text.enter_player_name", "Pray, enter thy companion's name:");
        translationBuilder.add("xaeroshare.text.multiplayer_note", "(Use commas to add more companions)");
        translationBuilder.add("xaeroshare.text.player_name", "Companion's Name");

        // Messages
        translationBuilder.add("xaeroshare.text.shared_to_player", "Sending atlas mark %s to %s...");
    }
}
