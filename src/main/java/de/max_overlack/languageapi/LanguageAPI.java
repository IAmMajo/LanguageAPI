package de.max_overlack.languageapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.max_overlack.languageapi.LanguageAPIPlugin;
import de.max_overlack.languageapi.RegisteredPlugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class LanguageAPI {

    private LanguageAPI() {}

    /**
     * You have to call this method in onEnable() before registering all messa-
     * ges of your plugin using registerMessage().
     * 
     * @param name The name of your plugin.
     */
    public static void registerPlugin(final String name) {
        final LanguageAPIPlugin plugin = LanguageAPIPlugin.getInstance();
        final File folder = new File(plugin.getDataFolder(), name);        
        if (folder.mkdir())
            return;

        final RegisteredPlugin registeredPlugin = new RegisteredPlugin();
        final Logger logger = plugin.getLogger();
        final Map<String, Map<String, String>> registeredPluginLanguages
        = registeredPlugin.getLanguages();
        final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        final Type type = new TypeToken<Map<String, String>>() {}.getType();
        plugin.getRegisteredPlugins().put(name, registeredPlugin);
        logger.info("Loading language files of plugin " + name);
        for (final File languageFile : folder.listFiles()) {
            final String languageFileName = languageFile.getName();
            try (final FileInputStream languageFileInput = 
                    new FileInputStream(languageFile);
                    final InputStreamReader languageFileInputReader =
                    new InputStreamReader(languageFileInput,
                            StandardCharsets.UTF_8);) {
                registeredPluginLanguages.put(languageFileName.split("\\.")[0],
                        gson.fromJson(languageFileInputReader, type));
            } catch (final IOException e) {
                logger.severe("Error while loading language file "
                        + languageFileName + " of plugin " + name + ":");
                e.printStackTrace();
            }
        }
    }

    /**
     * You have to call this method in onEnable() to register all messages of
     * your plugin. Call this method after registerPlugin() and before
     * saveMessages().
     * 
     * @param pluginName The name of your plugin.
     * @param name The name of the message.
     * @param message The default English version of the message. You can use
     *                color codes by using the character '&'. Placeholders are
     *                created by using the character '§'. Examples:
     *                <ul>
     *                  <li>"&aYour balance is &6§balance§&a!"</li>
     *                  <li>"This is a message, which contains multiple
     *                  placeholders: §placeholder1§, §placeholder2§ and
     *                  §placeholder3§"</li>
     *                </ul>
     */
    public static void registerMessage(final String pluginName,
            final String name, final String message) {
        final RegisteredPlugin registeredPlugin = LanguageAPIPlugin
        .getInstance().getRegisteredPlugins().get(pluginName);
        final Map<String, String> registeredPluginLanguage
        = registeredPlugin.getLanguages().get("en");
        if (registeredPluginLanguage.containsKey(name))
            return;

        registeredPluginLanguage.put(name, message);
        registeredPlugin.setModifiedTrue();
    }

    /**
     * You have to call this method in onEnable() after you have registered all
     * messages of your plugin using registerMessage().
     * 
     * @param pluginName The name of your plugin.
     */
    public static void saveMessages(final String pluginName) {
        final LanguageAPIPlugin plugin = LanguageAPIPlugin.getInstance();
        final RegisteredPlugin registeredPlugin
        = plugin.getRegisteredPlugins().get(pluginName);
        if (!registeredPlugin.isModified())
            return;

        final Logger logger = plugin.getLogger();
        final File languageFile = new File(new File(plugin.getDataFolder(),
                        pluginName), "en.json");
        logger.info("Creating en.json for plugin " + pluginName
                + " if it doesn't exist");
        try {
            languageFile.createNewFile();
        } catch (final IOException e) {
            logger.severe("Error while creating en.json for plugin "
                    + pluginName + ":");
            e.printStackTrace();
            return;
        }

        logger.info("Saving en.json for plugin " + pluginName);
        try (final FileOutputStream languageFileOutput =
                new FileOutputStream(languageFile);
                final OutputStreamWriter languageFileOutputWriter =
                new OutputStreamWriter(languageFileOutput,
                        StandardCharsets.UTF_8);) {
            new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
            .toJson(registeredPlugin.getLanguages().get("en"),
                    new TypeToken<Map<String, String>>() {}.getType(),
                    languageFileOutputWriter);
        } catch (final IOException e) {
            logger.severe("Error while saving en.json for plugin " + pluginName
                    + ":");
            e.printStackTrace();
        }
    }

    /**
     * @param pluginName The name of your plugin.
     * @param name The name of the message.
     * @param sender The CommandSender that the message should be displayed to.
     * @param placeholderReplacements Only needed if there are placeholders in
     *                                the message. The replacements for the
     *                                placeholders in the message. They have to
     *                                be in the same order like the placeholders
     *                                in the default English version of the
     *                                message.
     * @return The translated message as ComponentBuilder. Useful for creating
     *         things like clickable messages.
     */
    public static ComponentBuilder getMessageAsComponentBuilder(
            final String pluginName, final String name,
            final CommandSender sender,
            final String... placeholderReplacements) {
        return new ComponentBuilder("").append(getMessageAsBaseComponents(
                        pluginName, name, sender, placeholderReplacements));
    }

    /**
     * @param pluginName The name of your plugin.
     * @param name The name of the message.
     * @param sender The CommandSender that the message should be displayed to.
     * @param placeholderReplacements Only needed if there are placeholders in
     *                                the message. The replacements for the
     *                                placeholders in the message. They have to
     *                                be in the same order like the placeholders
     *                                in the default English version of the
     *                                message.
     * @return The translated message as BaseComponents. Useful for creating
     *         things like clickable messages.
     */
    public static BaseComponent[] getMessageAsBaseComponents(
            final String pluginName, final String name,
            final CommandSender sender,
            final String... placeholderReplacements) {
        return TextComponent.fromLegacyText(getMessage(pluginName, name, sender,
                        placeholderReplacements));
    }

    /**
     * @param pluginName The name of your plugin.
     * @param name The name of the message.
     * @param sender The CommandSender that the message should be displayed to.
     * @param placeholderReplacements Only needed if there are placeholders in
     *                                the message. The replacements for the
     *                                placeholders in the message. They have to
     *                                be in the same order like the placeholders
     *                                in the default English version of the
     *                                message.
     * @return The translated message.
     */
    public static String getMessage(final String pluginName, final String name,
            final CommandSender sender,
            final String... placeholderReplacements) {
        final Map<String, Map<String, String>> registeredPluginLanguages
        = LanguageAPIPlugin.getInstance().getRegisteredPlugins().get(pluginName)
        .getLanguages();
        final Map<String, String> placeholders
        = getPlaceholders(pluginName, name, placeholderReplacements);
        final Map<String, String> registeredPluginLanguage
        = registeredPluginLanguages.get(getLanguage(sender));
        final String messageEn
        = replacePlaceholders(registeredPluginLanguages.get("en").get(name),
                placeholders);
        if (registeredPluginLanguage == null)
            return messageEn;

        final String message = registeredPluginLanguage.get(name);
        if (message == null)
            return messageEn;

        return replacePlaceholders(message, placeholders);
    }

    /**
     * @param pluginName The name of your plugin.
     * @param messageName The name of the message.
     * @param placeholderReplacements Only needed if there are placeholders in
     *                                the message. The replacements for the
     *                                placeholders in the message. They have to
     *                                be in the same order like the placeholders
     *                                in the default English version of the
     *                                message.
     * @return All versions of the message. Useful for checking things like if
     *         someone clicked in a specific inventory that has different names
     *         in different languages. 
     */
    public static Set<String> getMessages(final String pluginName,
            final String messageName, final String... placeholderReplacements) {
        final Set<String> messages = new HashSet<>();
        final Map<String, String> placeholders = getPlaceholders(pluginName,
                messageName, placeholderReplacements);
        LanguageAPIPlugin.getInstance().getRegisteredPlugins().get(pluginName)
        .getLanguages().values().forEach(l -> {
            final String message = l.get(messageName);
            if (message != null)
                messages.add(replacePlaceholders(message, placeholders));
        });
        return messages;
    }

    /**
     * @param sender The CommandSender that you want to know the language of.
     * @return The current language of the CommandSender.
     */
    public static String getLanguage(final CommandSender sender) {
        if (!(sender instanceof Player))
            return "en";

        final String locale = ((Player) sender).getLocale();
        if (locale == null)
            return "en";

        return new Locale(locale.split("_")[0]).getLanguage();
    }

    private static Map<String, String> getPlaceholders(final String pluginName,
            final String messageName, final String... placeholderReplacements) {
        final Map<String, String> placeholders = new HashMap<>();
        final String[] splittedMessage = LanguageAPIPlugin.getInstance()
        .getRegisteredPlugins().get(pluginName).getLanguages().get("en")
        .get(messageName).split("§");
        for (byte i = 0; i < placeholderReplacements.length; i++)
            placeholders.put(splittedMessage[i * 2 + 1],
                    placeholderReplacements[i]);
        return placeholders;
    }

    private static String replacePlaceholders(String message,
            final Map<String, String> placeholders) {
        for (final String placeholder : placeholders.keySet())
            message = message.replace("§" + placeholder + "§",
                    placeholders.get(placeholder));
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}