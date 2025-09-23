package dev.piny.xaeroshare.client.screen;

import dev.piny.xaeroshare.client.Waypoint;
import dev.piny.xaeroshare.client.WaypointTools;
import dev.piny.xaeroshare.client.XaeroshareClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

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

        String serverIP = client.getCurrentServerEntry().address;
        assert client.world != null;
        String dimName = client.world.getRegistryKey().getValue().toString();
        Waypoint[] waypoints = WaypointTools.findWaypoints(serverIP, dimName); // At least one, otherwise toast above would have shown by now
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

//        TextFieldWidget textFieldWidget = new TextFieldWidget(this.textRenderer, 40, 70, 120, 20, Text.of("Input"));
//        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Speak thy truth"), (btn) -> {
//            // When the button is clicked, we can display a toast to the screen.
//            assert this.client != null;
//            this.client.getToastManager().add(
//                    SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Turns out"), Text.of("Trans rights are human rights"))
//            );
//            XaeroshareClient.LOGGER.info(textFieldWidget.getText());
//        }).dimensions(40, 40, 120, 20).build();
//
//        // Register the button widget.
//        this.addDrawableChild(buttonWidget);
//        this.addDrawableChild(textFieldWidget);
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