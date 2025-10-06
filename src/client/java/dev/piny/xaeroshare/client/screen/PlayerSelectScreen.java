package dev.piny.xaeroshare.client.screen;

import dev.piny.xaeroshare.client.Waypoint;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerSelectScreen extends Screen {
    private final Waypoint sharedWaypoint;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    protected PlayerSelectScreen(Text title, Waypoint sharedWaypoint) {
        super(title);
        this.sharedWaypoint = sharedWaypoint;
    }

    @Override
    protected void init() {
        super.init();

        TextWidget instructionText = new TextWidget(this.width / 2 - 100, this.height / 2 - 60, 200, 20, Text.translatable("xaeroshare.text.enter_player_name"), this.textRenderer);
        TextWidget multiPlayerNoteText = new TextWidget(this.width / 2 - 100, this.height / 2 - 40, 200, 20, Text.translatable("xaeroshare.text.multiplayer_note"), this.textRenderer);
        TextFieldWidget textField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Text.translatable("xaeroshare.text.player_name"));
        textField.setText("");
        ButtonWidget sendButton = ButtonWidget.builder(Text.translatable("xaeroshare.text.share_waypoint"), button -> {
            // Logic to send the waypoint to the specified player
            String playerNameInput = textField.getText();
            if (!playerNameInput.isEmpty()) {
                assert this.client != null;
                assert this.client.player != null;
                this.client.player.sendMessage(Text.translatable("xaeroshare.text.shared_to_player", sharedWaypoint.name(), playerNameInput.replace(",", ", ")), false);
                if (!playerNameInput.contains(",")) {
                    this.client.player.networkHandler.sendChatCommand("msg " + playerNameInput + " " + sharedWaypoint.toImportableString());
                } else {
                    String[] players = playerNameInput.split(",");
                    for (int i = 0; i < players.length; i++) {
                        String player = players[i].trim();
                        int delay = i * 100; // 100 ms between each message
                        scheduler.schedule(() -> {
                            assert this.client != null;
                            assert this.client.player != null;
                            this.client.player.networkHandler.sendChatCommand("msg " + player + " " + sharedWaypoint.toImportableString());
                        }, delay, TimeUnit.MILLISECONDS);
                    }
                }
                this.client.setScreen(null); // Close the screen after sending
            }
        }).dimensions(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build();

        this.addDrawableChild(textField);
        this.addDrawableChild(multiPlayerNoteText);
        this.addDrawableChild(instructionText);
        this.addDrawableChild(sendButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
}
