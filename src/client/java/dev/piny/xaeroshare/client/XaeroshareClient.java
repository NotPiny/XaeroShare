package dev.piny.xaeroshare.client;

import dev.piny.xaeroshare.client.screen.WaypointSelectScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class XaeroshareClient implements ClientModInitializer {
    private static KeyBinding keyBinding;
    public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("xaeroshare");

    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.xaeroshare.share", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_RIGHT_ALT, // The keycode of the key
                "category.xaeroshare" // The translation key of the keybinding's category.
        ));

        MinecraftClient.getLauncherBrand();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                if (client.getCurrentServerEntry() == null) {
                    client.getToastManager().add(
                            SystemToast.create(client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("No server detected"), Text.of("Join a server to share waypoints"))
                    );
                    return;
                }

                assert client.world != null;

                AtomicInteger waypointsFound = new AtomicInteger();
                Objects.requireNonNull(WaypointTools.getMinimapWorld(client.world.getRegistryKey())).getIterableWaypointSets().forEach(waypointSet -> {
                    waypointSet.getWaypoints().forEach(waypoint -> {
                        waypointsFound.getAndIncrement();
                    });
                });

                if (waypointsFound.get() == 0) {
                    client.getToastManager().add(
                            SystemToast.create(client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("No waypoints found"), Text.of("You have no waypoints to share"))
                    );
                    return;
                }

                MinecraftClient.getInstance().setScreen(
                        new WaypointSelectScreen(Text.empty())
                );
            }
        });
    }
}
