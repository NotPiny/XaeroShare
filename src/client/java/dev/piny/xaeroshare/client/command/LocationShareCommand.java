package dev.piny.xaeroshare.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.piny.xaeroshare.client.Waypoint;
import dev.piny.xaeroshare.client.WaypointTools;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.util.List;

public class LocationShareCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommands.literal("locationshare")
                        .then(ClientCommands.argument("waypointName", StringArgumentType.string())
                                .then(ClientCommands.argument("dimension", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            Minecraft.getInstance().getConnection()
                                                    .levels()
                                                    .forEach(key -> builder.suggest("\"" + key.identifier().toString() + "\""));
                                            return builder.buildFuture();
                                        })
                                        .then(ClientCommands.argument("x", IntegerArgumentType.integer())
                                                .then(ClientCommands.argument("y", IntegerArgumentType.integer())
                                                        .then(ClientCommands.argument("z", IntegerArgumentType.integer())
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
                                        )
                                )
                        )
        );
    }

    private static int execute(CommandContext<FabricClientCommandSource> context) {
        String waypointName = StringArgumentType.getString(context, "waypointName");
        Identifier dimension = Identifier.parse(StringArgumentType.getString(context, "dimension"));
        int x = IntegerArgumentType.getInteger(context, "x");
        int y = IntegerArgumentType.getInteger(context, "y");
        int z = IntegerArgumentType.getInteger(context, "z");
        List<String> players = List.of(StringArgumentType.getString(context, "players").split(" "));

        Waypoint waypoint = new Waypoint(
                waypointName,
                "X",
                x,
                y,
                z,
                0,
                false,
                0,
                "xaeroshare",
                false,
                0,
                0,
                "",
                WaypointTools.dimId.getOrDefault(dimension.toString(), 0)
        );

        players.forEach(player -> {
            Minecraft.getInstance().player.connection.sendCommand("msg " + player + " " + waypoint.toImportableString());
        });

        return 1;
    }
}