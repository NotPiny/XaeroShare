package dev.piny.xaeroshare.client.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class XaeroshareEnglishLangProvider extends FabricLanguageProvider {
    public XaeroshareEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // Keybinds
        translationBuilder.add("key.xaeroshare.share", "Share waypoint");
        translationBuilder.add("key.category.minecraft.xaeroshare", "XaeroShare");

        // Toasts
        translationBuilder.add("xaeroshare.text.toast.no_server.title", "No server found");
        translationBuilder.add("xaeroshare.text.toast.no_server.description", "Join a server to share waypoints");
        translationBuilder.add("xaeroshare.text.toast.no_waypoints.title", "No waypoints found");
        translationBuilder.add("xaeroshare.text.toast.no_waypoints.description", "You have no waypoints to share");

        // GUI Text
        translationBuilder.add("xaeroshare.text.share_waypoint", "Share Waypoint");
        translationBuilder.add("xaeroshare.text.enter_player_name", "Enter Player Name:");
        translationBuilder.add("xaeroshare.text.multiplayer_note", "(Use commas to add more players)");
        translationBuilder.add("xaeroshare.text.player_name", "Player Name");

        // Messages
        translationBuilder.add("xaeroshare.text.shared_to_player", "Sending waypoint %s to %s...");
    }
}