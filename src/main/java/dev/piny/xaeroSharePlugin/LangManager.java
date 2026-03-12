package dev.piny.xaeroSharePlugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class LangManager {

    private final Map<String, String> strings = new HashMap<>();
    private final JavaPlugin plugin;

    public LangManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private static final String[] BUNDLED_LANGS = {"en", "ru"};

    private void load() {
        // Extract all bundled lang files to the data folder if not already present
        for (String bundled : BUNDLED_LANGS) {
            plugin.saveResource("lang/" + bundled + ".json", false);
        }

        String lang = plugin.getConfig().getString("lang", "en");
        String resourcePath = "lang/" + lang + ".json";

        File langFile = new File(plugin.getDataFolder(), resourcePath);
        if (!langFile.exists()) {
            plugin.getLogger().warning("Language '" + lang + "' not found. Falling back to 'en'.");
            resourcePath = "lang/en.json";
            langFile = new File(plugin.getDataFolder(), resourcePath);
        }

        // Parse the JSON lang file
        try (Reader reader = new InputStreamReader(new FileInputStream(langFile), StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> loaded = gson.fromJson(reader, mapType);
            if (loaded != null) {
                strings.putAll(loaded);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load language file: " + langFile.getPath(), e);
        }
    }

    /**
     * Returns the translated string for the given key, or the key itself if not found.
     */
    public String get(String key) {
        return strings.getOrDefault(key, key);
    }
}
