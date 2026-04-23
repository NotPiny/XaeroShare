package dev.piny.xaeroshare.client;

import dev.piny.xaeroshare.client.command.LocationShareCommand;
import dev.piny.xaeroshare.client.command.WaypointShareCommand;
import dev.piny.xaeroshare.client.screen.WaypointSelectScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class XaeroshareClient implements ClientModInitializer {
    private static KeyMapping keyMapping;
    public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("xaeroshare");

    @Override
    public void onInitializeClient() {
        keyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.xaeroshare.share",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_ALT,
                KeyMapping.Category.MISC
        ));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            LocationShareCommand.register(dispatcher);
            WaypointShareCommand.register(dispatcher);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyMapping.consumeClick()) {
                if (client.getCurrentServer() == null) {
                    SystemToast.add(client.getToastManager(), SystemToast.SystemToastId.NARRATOR_TOGGLE,
                            Component.translatable("xaeroshare.text.toast.no_server.title"),
                            Component.translatable("xaeroshare.text.toast.no_server.description"));
                    return;
                }

                assert client.level != null;

                AtomicInteger waypointsFound = new AtomicInteger();
                Objects.requireNonNull(WaypointTools.getMinimapWorld(client.level.dimension())).getIterableWaypointSets().forEach(waypointSet -> waypointSet.getWaypoints().forEach(waypoint -> waypointsFound.getAndIncrement()));

                if (waypointsFound.get() == 0) {
                    SystemToast.add(client.getToastManager(), SystemToast.SystemToastId.NARRATOR_TOGGLE,
                            Component.translatable("xaeroshare.text.toast.no_waypoints.title"),
                            Component.translatable("xaeroshare.text.toast.no_waypoints.description"));
                    return;
                }

                Minecraft.getInstance().setScreen(
                        new WaypointSelectScreen(Component.empty())
                );
            }
        });
    }
}
