package dev.piny.xaeroshare.client;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class WaypointTools {
    private static final Path gameDir = FabricLoader.getInstance().getGameDir();
    private static final Path xaeroDir = gameDir.resolve("xaero/minimap");
    private static final Logger LOGGER = XaeroshareClient.LOGGER;
    public static final Map<String, Integer> dimId = Map.of(
            "minecraft:overworld", 0,
            "minecraft:the_nether", -1,
            "minecraft:the_end", 1
    );
    public static final Map<Integer, String> dimName = Map.of(
            0, "minecraft:overworld",
            -1, "minecraft:the_nether",
            1, "minecraft:the_end"
    );

    public static Waypoint[] findWaypoints(@NotNull String serverIP, @NotNull String dimName) {
        Path serverFolder = xaeroDir.resolve("Multiplayer_" + serverIP);

        if (!dimId.containsKey(dimName.toLowerCase())) {
            LOGGER.error("Dimension not recognized: {}", dimName.toLowerCase());
            return new Waypoint[0];
        }

        Path dimFolder = serverFolder.resolve("dim%" + dimId.get(dimName));
        Path waypointsPath;
        File[] files = dimFolder.toFile().listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null && files.length > 0) {
            waypointsPath = files[0].toPath();
        } else {
            LOGGER.error("No .txt waypoint files found in: {}", dimFolder);
            return new Waypoint[0];
        }
        File waypointsFile = waypointsPath.toFile();

        if (waypointsFile.exists()) {
            try {
                String content = Files.readString(waypointsPath);
                String[] lines = content.split("\n");

                // First pass: count valid waypoint lines
                int waypointCount = 0;
                for (String line : lines) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.startsWith("waypoint:")) {
                        waypointCount++;
                    }
                }

                // Second pass: parse waypoints
                Waypoint[] waypoints = new Waypoint[waypointCount];
                int waypointIndex = 0;

                for (String line : lines) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.startsWith("waypoint:")) {
                        String[] parts = trimmedLine.split(":");
                        if (parts.length == 14) {
                            String name = parts[1];
                            String initials = parts[2];
                            int x = Integer.parseInt(parts[3]);
                            int y = Integer.parseInt(parts[4]);
                            int z = Integer.parseInt(parts[5]);
                            int color = Integer.parseInt(parts[6]);
                            boolean disabled = Boolean.parseBoolean(parts[7]);
                            int type = Integer.parseInt(parts[8]);
                            String set = parts[9];
                            boolean rotateOnTp = Boolean.parseBoolean(parts[10]);
                            int tpYaw = Integer.parseInt(parts[11]);
                            int visibilityType = Integer.parseInt(parts[12]);
                            String destination = parts[13];
                            waypoints[waypointIndex] = new Waypoint(name, initials, x, y, z, color, disabled, type, set, rotateOnTp, tpYaw, visibilityType, destination, dimId.get(dimName));
                            waypointIndex++;
                        } else {
                            LOGGER.warn("Invalid waypoint format: {}", trimmedLine);
                        }
                    }
                }

                return waypoints;
            } catch (Exception e) {
                LOGGER.error("Error reading waypoints file: {}", e.getMessage());
                return new Waypoint[0];
            }
        } else {
            LOGGER.warn("Waypoints file does not exist: {}", waypointsPath);
        }

        return new Waypoint[0];
    }
}
