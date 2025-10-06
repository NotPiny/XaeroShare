package dev.piny.xaeroshare.client.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class XaerosharePirateLangProvider extends FabricLanguageProvider {
    public XaerosharePirateLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "en_pt", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // Keybinds
        translationBuilder.add("key.xaeroshare.share", "Share marked spot");
        translationBuilder.add("key.category.minecraft.xaeroshare", "Map o' Share");

        // Toasts
        translationBuilder.add("xaeroshare.text.toast.no_server.title", "There be no server");
        translationBuilder.add("xaeroshare.text.toast.no_server.description", "Locate yerself a server to share waypoints");
        translationBuilder.add("xaeroshare.text.toast.no_waypoints.title", "There be no marked spots");
        translationBuilder.add("xaeroshare.text.toast.no_waypoints.description", "Ye have no marked spots to share");

        // GUI Text
        translationBuilder.add("xaeroshare.text.share_waypoint", "Share Marked Spot");
        translationBuilder.add("xaeroshare.text.enter_player_name", "Enter Matey's Name:");
        translationBuilder.add("xaeroshare.text.multiplayer_note", "(Use commas to add more mates)");
        translationBuilder.add("xaeroshare.text.player_name", "Matey's Name");

        // Messages
        translationBuilder.add("xaeroshare.text.shared_to_player", "Sendin' marked spot %s to %s...");
    }
}
