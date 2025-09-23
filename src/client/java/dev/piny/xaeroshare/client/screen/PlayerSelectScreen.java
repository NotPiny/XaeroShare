package dev.piny.xaeroshare.client.screen;

import dev.piny.xaeroshare.client.Waypoint;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class PlayerSelectScreen extends Screen {
    private final Waypoint sharedWaypoint;
    protected PlayerSelectScreen(Text title, Waypoint sharedWaypoint) {
        super(title);
        this.sharedWaypoint = sharedWaypoint;
    }

    @Override
    protected void init() {
        super.init();

        TextWidget instructionText = new TextWidget(this.width / 2 - 100, this.height / 2 - 40, 200, 20, Text.of("Enter Player Name:"), this.textRenderer);
        TextFieldWidget textField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Text.of("Player Name"));
        textField.setText("");
        ButtonWidget sendButton = ButtonWidget.builder(Text.of("Send Waypoint"), button -> {
            // Logic to send the waypoint to the specified player
            String playerName = textField.getText();
            if (!playerName.isEmpty()) {
                // Implement the logic to send the waypoint to the player
                // For example, you might want to send a chat message or use a custom packet
                assert this.client != null;
                assert this.client.player != null;
                this.client.player.networkHandler.sendChatCommand("msg " + playerName + " " + sharedWaypoint.toImportableString());
                this.client.player.sendMessage(Text.of("Sent waypoint " + sharedWaypoint.name() + " to " + playerName), false);
                this.client.setScreen(null); // Close the screen after sending
            }
        }).dimensions(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build();

        this.addDrawableChild(textField);
        this.addDrawableChild(instructionText);
        this.addDrawableChild(sendButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
}
