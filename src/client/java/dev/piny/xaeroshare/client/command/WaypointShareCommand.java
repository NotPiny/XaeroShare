package dev.piny.xaeroshare.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.piny.xaeroshare.client.Waypoint;
import dev.piny.xaeroshare.client.WaypointTools;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import xaero.hud.minimap.world.MinimapWorld;

import java.util.List;

public class WaypointShareCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommands.literal("waypointshare")
                        .then(ClientCommands.argument("waypointName", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    ResourceKey<Level> dimension = Minecraft.getInstance().level.dimension();
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
                                .then(ClientCommands.argument("dimension", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            Minecraft.getInstance().getConnection()
                                                    .levels()
                                                    .forEach(key -> builder.suggest("\"" + key.identifier().toString() + "\""));
                                            return builder.buildFuture();
                                        })
                                        .then(ClientCommands.argument("players", StringArgumentType.greedyString())
                                                .suggests((context, builder) -> {
                                                    String remaining = builder.getRemaining();
                                                    String prefix = remaining.contains(" ")
                                                            ? remaining.substring(remaining.lastIndexOf(' ') + 1)
                                                            : remaining;

                                                    Minecraft.getInstance().getConnection()
                                                            .getOnlinePlayers()
                                                            .forEach(p -> {
                                                                String name = p.getProfile().name();
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
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, Identifier.parse(StringArgumentType.getString(context, "dimension")));
        List<String> players = List.of(StringArgumentType.getString(context, "players").split(" "));

        MinimapWorld world = WaypointTools.getMinimapWorld(dimension);
        world.getIterableWaypointSets().forEach(waypointSet -> waypointSet.getWaypoints().forEach(waypoint -> {
            Waypoint wp = WaypointTools.convertWaypoint(waypoint, WaypointTools.dimId.getOrDefault(dimension.identifier().toString(), 0));
            if (wp.name().equals(waypointName)) {
                // Send the waypoint to the server to be shared with the specified players
                players.forEach(player -> {
                    Minecraft.getInstance().player.connection.sendCommand("msg " + player + " " + wp.toImportableString());
                });
            }
        }));

        return 1;
    }
}
