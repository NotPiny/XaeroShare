package dev.piny.xaeroshare.client;

import org.jetbrains.annotations.NotNull;

//waypoint:name:initials:x:y:z:color:disabled:type:set:rotate_on_tp:tp_yaw:visibility_type:destination
//Example: waypoint:Spawn (End):SE:99:49:0:3:false:0:gui.xaero_default:false:0:0:false
public record Waypoint(String name, String initials, int x, int y, int z, int color, boolean disabled, int type, String set, boolean rotateOnTp, int tpYaw, int visibilityType, String destination, int dimension) {
    @Override
    public @NotNull String toString() {
        return "waypoint:" + name + ":" + initials + ":" + x + ":" + y + ":" + z + ":" + color + ":" + disabled + ":" + type + ":" + set + ":" + rotateOnTp + ":" + tpYaw + ":" + visibilityType + ":" + destination;
    }

    // xaero-waypoint:[Name]:[Initials]:[X]:[Y]:[Z]:[Colour?]:[Disabled?]:[Unknown]:Internal-[Dimension]-waypoints
    // ^ This is what you send in chat for that [Player] shared a waypoint called [Name]
    public @NotNull String toImportableString() {
        return "xaero-waypoint:" + name + ":" + initials + ":" + x + ":" + y + ":" + z + ":" + color + ":" + disabled + ":0:Internal-" + WaypointTools.dimName.get(dimension).replace("minecraft:", "").toLowerCase() + "-waypoints";
    }
}
