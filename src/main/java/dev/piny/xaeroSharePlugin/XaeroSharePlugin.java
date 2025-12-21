package dev.piny.xaeroSharePlugin;

import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class XaeroSharePlugin extends JavaPlugin implements Listener {
    private static final HashMap<UUID, String> pendingWaypoints = new HashMap<>();
    private static final Key CONFIRM_KEY = Key.key("xaeroshare:user_input/confirm");
    private static final Key IGNORE_KEY = Key.key("xaeroshare:user_input/ignore");

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onMessage(AsyncChatEvent event) {
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        if (!message.startsWith("xaero-waypoint")) return; // Only process waypoints being shared
        if (message.endsWith(":xaeroshare")) return; // Ignore messages sent by the mod

        pendingWaypoints.put(event.getPlayer().getUniqueId(), message);

        ArrayList<DialogInput> inputs = new ArrayList<>();

        for (Player player : event.getPlayer().getWorld().getPlayers()) {
            if (player == event.getPlayer()) continue; // Don't show self.
            String name = player.getName();
            inputs.add(DialogInput.bool(
                    name,
                    Component.text(name)
            ).build());
        }

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Select players to share waypoint with"))
                        .inputs(inputs)
                        .build()
                )
                .type(DialogType.confirmation(
                        ActionButton.create(
                                Component.text("Confirm", TextColor.color(0xAEFFC1)),
                                Component.text("Click to confirm your input."),
                                100,
                                DialogAction.customClick(CONFIRM_KEY, null)
                        ),

                        ActionButton.create(
                                Component.text("Ignore", TextColor.color(0xFFA0B1)),
                                Component.text("Click here to share with the whole server."),
                                100,
                                DialogAction.customClick(IGNORE_KEY, null)
                        )
                ))
        );

        event.getPlayer().showDialog(dialog);
        event.setCancelled(true);
    }

    @EventHandler
    public void handleDialogInput(PlayerCustomClickEvent event) {
        if (!event.getIdentifier().equals(CONFIRM_KEY) && !event.getIdentifier().equals(IGNORE_KEY)) {
            return;
        }

        if (event.getCommonConnection() instanceof PlayerGameConnection conn) {
            Player player = conn.getPlayer();

            String waypoint = pendingWaypoints.remove(player.getUniqueId());
            if (waypoint == null) {
                getLogger().warning("No pending waypoint for player " + player.getName());
                return;
            }

            if (event.getIdentifier().equals(CONFIRM_KEY)) {
                for (Player target : player.getWorld().getPlayers()) {
                    if (target == player) continue; // Don't send to self.
                    boolean accepted = Boolean.TRUE.equals(event.getDialogResponseView().getBoolean(target.getName()));
                    if (accepted) {
                        target.sendMessage(Component.text("<" + player.getName() + "> " + waypoint));
                    }
                }
            } else {
                // We love gaslighting the clients
                Bukkit.broadcast(Component.text("<" + player.getName() + "> " + waypoint));
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
