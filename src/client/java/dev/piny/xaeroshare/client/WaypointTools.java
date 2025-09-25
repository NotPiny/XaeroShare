package dev.piny.xaeroshare.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.slf4j.Logger;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.path.XaeroPath;

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

    public static MinimapWorld getMinimapWorld(RegistryKey<World> dim) { // This is just taken from https://github.com/rfresh2/XaeroPlus/blob/1.20.1/common/src/main/java/xaeroplus/feature/waypoint/WaypointAPI.java#L19 :)
        MinimapSession minimapSession = BuiltInHudModules.MINIMAP.getCurrentSession();
        if (minimapSession == null) return null;
        MinimapWorld currentWorld = minimapSession.getWorldManager().getCurrentWorld();
        if (currentWorld == null) return null;
        if (currentWorld.getDimId() == dim) {
            return currentWorld;
        }
        var rootContainer = minimapSession.getWorldManager().getCurrentRootContainer();
        for (MinimapWorld world : rootContainer.getWorlds()) {
            if (world.getDimId() == dim) {
                return world;
            }
        }
        String dimensionDirectoryName = minimapSession.getDimensionHelper().getDimensionDirectoryName(dim);
        String worldNode = minimapSession.getWorldStateUpdater().getPotentialWorldNode(dim, true);
        XaeroPath containerPath = minimapSession.getWorldState()
                .getAutoRootContainerPath()
                .resolve(dimensionDirectoryName)
                .resolve(worldNode);
        return minimapSession.getWorldManager().getWorld(containerPath);
    }

    public static Waypoint convertWaypoint(xaero.common.minimap.waypoints.Waypoint wp, int dim) {
        return new Waypoint(
                wp.getName(),
                wp.getInitials(),
                wp.getX(),
                wp.getY(),
                wp.getZ(),
                wp.getColor(),
                wp.isDisabled(),
                wp.getWaypointType(),
                "gui.xaero_default", // I think this always this value and as far as I can tell there's no way to actually fetch its value
                wp.isRotation(),
                wp.getYaw(),
                wp.getVisibilityType(),
                "0", // This is yet again one of those values that I can't find any way to fetch
                dim
        );
    }

}
