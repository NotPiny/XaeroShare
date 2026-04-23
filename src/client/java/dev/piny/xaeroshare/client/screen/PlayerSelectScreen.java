package dev.piny.xaeroshare.client.screen;

import dev.piny.xaeroshare.client.Waypoint;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerSelectScreen extends Screen {
    private final Waypoint sharedWaypoint;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    protected PlayerSelectScreen(Component title, Waypoint sharedWaypoint) {
        super(title);
        this.sharedWaypoint = sharedWaypoint;
    }

    @Override
    protected void init() {
        super.init();

        StringWidget instructionText = new StringWidget(this.width / 2 - 100, this.height / 2 - 60, 200, 20, Component.translatable("xaeroshare.text.enter_player_name"), this.font);
        StringWidget multiPlayerNoteText = new StringWidget(this.width / 2 - 100, this.height / 2 - 40, 200, 20, Component.translatable("xaeroshare.text.multiplayer_note"), this.font);
        EditBox textField = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Component.translatable("xaeroshare.text.player_name"));
        textField.setValue("");
        Button sendButton = Button.builder(Component.translatable("xaeroshare.text.share_waypoint"), button -> {
            // Logic to send the waypoint to the specified player
            String playerNameInput = textField.getValue();
            if (!playerNameInput.isEmpty()) {
                assert this.minecraft != null;
                assert this.minecraft.player != null;
                this.minecraft.player.sendSystemMessage(Component.translatable("xaeroshare.text.shared_to_player", sharedWaypoint.name(), playerNameInput.replace(",", ", ")));
                if (!playerNameInput.contains(",")) {
                    this.minecraft.player.connection.sendCommand("msg " + playerNameInput + " " + sharedWaypoint.toImportableString());
                } else {
                    String[] players = playerNameInput.split(",");
                    for (int i = 0; i < players.length; i++) {
                        String player = players[i].trim();
                        int delay = i * 100; // 100 ms between each message
                        scheduler.schedule(() -> {
                            assert this.minecraft != null;
                            assert this.minecraft.player != null;
                            this.minecraft.player.connection.sendCommand("msg " + player + " " + sharedWaypoint.toImportableString());
                        }, delay, TimeUnit.MILLISECONDS);
                    }
                }
                this.minecraft.setScreen(null); // Close the screen after sending
            }
        }).bounds(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build();

        this.addRenderableWidget(textField);
        this.addRenderableWidget(multiPlayerNoteText);
        this.addRenderableWidget(instructionText);
        this.addRenderableWidget(sendButton);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
    }
}
