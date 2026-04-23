package dev.piny.xaeroshare.client.screen;

import dev.piny.xaeroshare.client.Waypoint;
import dev.piny.xaeroshare.client.WaypointTools;
import dev.piny.xaeroshare.client.XaeroshareClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaypointSelectScreen extends Screen {
    public WaypointSelectScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        Minecraft client = Minecraft.getInstance();
        if (client.getCurrentServer() == null) {
            SystemToast.add(client.getToastManager(), SystemToast.SystemToastId.NARRATOR_TOGGLE,
                    Component.literal("No server detected"),
                    Component.literal("Join a server to share waypoints"));
            return;
        }

        assert client.level != null;
        String dimName = client.level.dimension().identifier().toString();
        List<Waypoint> waypoints = new ArrayList<>();
        Objects.requireNonNull(WaypointTools.getMinimapWorld(client.level.dimension())).getIterableWaypointSets().forEach(waypointSet -> waypointSet.getWaypoints().forEach(waypoint -> {
            Waypoint wp = WaypointTools.convertWaypoint(waypoint, WaypointTools.dimId.getOrDefault(dimName, 0));
            waypoints.add(wp);
        }));
        int x = 20;
        int y = 20;
        int width = 200;
        int height = 20;
        int padding = 5;
        for (Waypoint wp : waypoints) {
            if (wp == null) {
                XaeroshareClient.LOGGER.warn("Encountered null waypoint, skipping...");
                continue;
            }
            Button buttonWidget = Button.builder(Component.literal(wp.name()), (btn) -> {
                assert this.minecraft != null;
                this.minecraft.setScreen(
                        new PlayerSelectScreen(Component.literal("Send waypoint to..."), wp)
                );
            }).bounds(x, y, width, height).build();
            this.addRenderableWidget(buttonWidget);
            y += height + padding;

            // If the next button would go off the screen, go to next column
            if (y + height > this.height) {
                y = 20;
                x += width + padding;
            }
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
    }
}