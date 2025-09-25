package dev.piny.xaeroshare.client.screen;

import dev.piny.xaeroshare.client.Waypoint;
import dev.piny.xaeroshare.client.WaypointTools;
import dev.piny.xaeroshare.client.XaeroshareClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaypointSelectScreen extends Screen {
    public WaypointSelectScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getCurrentServerEntry() == null) {
            client.getToastManager().add(
                    SystemToast.create(client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("No server detected"), Text.of("Join a server to share waypoints"))
            );
            return;
        }

        assert client.world != null;
        String dimName = client.world.getRegistryKey().getValue().toString();
        List<Waypoint> waypoints = new ArrayList<>();
        Objects.requireNonNull(WaypointTools.getMinimapWorld(client.world.getRegistryKey())).getIterableWaypointSets().forEach(waypointSet -> waypointSet.getWaypoints().forEach(waypoint -> {
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
            ButtonWidget buttonWidget = ButtonWidget.builder(Text.of(wp.name()), (btn) -> {
                // When the button is clicked, we can display a toast to the screen.
                assert this.client != null;
                this.client.setScreen(
                        new PlayerSelectScreen(Text.of("Send waypoint to..."), wp)
                );
            }).dimensions(x, y, width, height).build();
            this.addDrawableChild(buttonWidget);
            y += height + padding;

            // If the next button would go off the screen, go to next column
            if (y + height > this.height) {
                y = 20;
                x += width + padding;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
        // We'll subtract the font height from the Y position to make the text appear above the button.
        // Subtracting an extra 10 pixels will give the text some padding.
        // textRenderer, text, x, y, color, hasShadow
//        context.drawText(this.textRenderer, "Click me why don't ya", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }
}