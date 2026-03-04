package dev.piny.xaeroshare.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.piny.xaeroshare.client.Waypoint;
import dev.piny.xaeroshare.client.WaypointTools;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xaero.hud.minimap.world.MinimapWorld;

import java.util.List;

public class WaypointShareCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("waypointshare")
                        .then(ClientCommandManager.argument("waypointName", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    RegistryKey<World> dimension = MinecraftClient.getInstance().world.getRegistryKey();
                                    MinimapWorld world = WaypointTools.getMinimapWorld(dimension);
                                    if (world == null) return builder.buildFuture();
                                    world.getIterableWaypointSets().forEach(waypointSet -> waypointSet.getWaypoints().forEach(waypoint -> {
                                        if (waypoint.getName().contains(" ")) {
                                            builder.suggest("\"" + waypoint.getName() + "\"");
                                            return;
                                        }

                                        builder.suggest(waypoint.getName());
                                    }));
                                    return builder.buildFuture();
                                })
                                .then(ClientCommandManager.argument("dimension", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            MinecraftClient.getInstance().getNetworkHandler()
                                                    .getWorldKeys()
                                                    .forEach(key -> builder.suggest("\"" + key.getValue().toString() + "\""));
                                            return builder.buildFuture();
                                        })
                                        .then(ClientCommandManager.argument("players", StringArgumentType.greedyString())
                                                .suggests((context, builder) -> {
                                                    String remaining = builder.getRemaining();
                                                    String prefix = remaining.contains(" ")
                                                            ? remaining.substring(remaining.lastIndexOf(' ') + 1)
                                                            : remaining;

                                                    MinecraftClient.getInstance().getNetworkHandler()
                                                            .getPlayerList()
                                                            .forEach(p -> {
                                                                String name = p.getProfile().getName();
                                                                if (name.startsWith(prefix)) {
                                                                    String alreadyTyped = remaining.contains(" ")
                                                                            ? remaining.substring(0, remaining.lastIndexOf(' ') + 1)
                                                                            : "";
                                                                    builder.suggest(alreadyTyped + name);
                                                                }
                                                            });
                                                    return builder.buildFuture();
                                                })
                                                .executes(ctx -> execute(ctx))
                                        )
                                )
                        )
        );
    }

    public static int execute(CommandContext<FabricClientCommandSource> context) {
        String waypointName = StringArgumentType.getString(context, "waypointName");
        RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(StringArgumentType.getString(context, "dimension")));
        List<String> players = List.of(StringArgumentType.getString(context, "players").split(" "));

        MinimapWorld world = WaypointTools.getMinimapWorld(dimension);
        world.getIterableWaypointSets().forEach(waypointSet -> waypointSet.getWaypoints().forEach(waypoint -> {
            Waypoint wp = WaypointTools.convertWaypoint(waypoint, WaypointTools.dimId.getOrDefault(dimension.getValue().toString(), 0));
            if (wp.name().equals(waypointName)) {
                // Send the waypoint to the server to be shared with the specified players
                players.forEach(player -> {
                    MinecraftClient.getInstance().player.networkHandler.sendChatCommand("msg " + player + " " + wp.toImportableString());
                });
            }
        }));

        return 1;
    }
}
