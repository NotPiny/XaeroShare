package dev.piny.xaeroshare.client;

import dev.piny.xaeroshare.client.screen.WaypointSelectScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class XaeroshareCommand {
    public XaeroshareCommand() { // Still ain't working :(
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommandManager.literal("xaeroshare").executes(context -> {
                MinecraftClient.getInstance().setScreen(new WaypointSelectScreen(Text.empty()));
                return 1;
            }))
        );
    }
}
