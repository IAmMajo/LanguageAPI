package de.max_overlack.languageapi;

import de.max_overlack.languageapi.RegisteredPlugin;
import java.util.HashMap;
import java.util.Map;
import net.md_5.bungee.api.plugin.Plugin;

public final class LanguageAPIPlugin extends Plugin {

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