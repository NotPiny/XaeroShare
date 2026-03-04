package dev.piny.xaeroshare.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.piny.xaeroshare.client.Waypoint;
import dev.piny.xaeroshare.client.WaypointTools;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.List;

public class LocationShareCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("locationshare")
                        .then(ClientCommandManager.argument("waypointName", StringArgumentType.word())
                                .then(ClientCommandManager.argument("dimension", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            MinecraftClient.getInstance().getNetworkHandler()
                                                    .getWorldKeys()
                                                    .forEach(key -> builder.suggest("\"" + key.getValue().toString() + "\""));
                                            return builder.buildFuture();
                                        })
                                        .then(ClientCommandManager.argument("x", IntegerArgumentType.integer())
                                                .then(ClientCommandManager.argument("y", IntegerArgumentType.integer())
                                                        .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
                                                                .then(ClientCommandManager.argument("players", StringArgumentType.greedyString())
                                                                        .suggests((context, builder) -> {
                                                                            String remaining = builder.getRemaining();
                                                                            String prefix = remaining.contains(" ")
                                                                                    ? remaining.substring(remaining.lastIndexOf(' ') + 1)
                                                                                    : remaining;

                                                                            MinecraftClient.getInstance().getNetworkHandler()
                                                                                    .getPlayerList()
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
        Identifier dimension = Identifier.of(StringArgumentType.getString(context, "dimension"));
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
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("msg " + player + " " + waypoint.toImportableString());
        });

        return 1;
    }
}