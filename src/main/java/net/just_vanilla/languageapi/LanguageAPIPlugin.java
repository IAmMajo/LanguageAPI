package net.just_vanilla.languageapi;

import java.util.HashMap;
import java.util.Map;
import net.just_vanilla.languageapi.RegisteredPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LanguageAPIPlugin extends JavaPlugin {

    private static LanguageAPIPlugin instance;
    private final Map<String, RegisteredPlugin> registeredPlugins;

    public static LanguageAPIPlugin getInstance() {
        return instance;
    }

    public LanguageAPIPlugin() {
        instance = this;
        this.registeredPlugins = new HashMap<>();
    }

    public Map<String, RegisteredPlugin> getRegisteredPlugins() {
        return this.registeredPlugins;
    }

    @Override
    public void onEnable() {
        this.getDataFolder().mkdir();
    }

}