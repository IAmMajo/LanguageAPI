# LanguageAPI
Allows plugin developers to easily support multiple languages.

This is the version for Spigot. [Click here to go to the version for BungeeCord!](https://github.com/Majoeins/LanguageAPI/tree/bungeecord)

The version for Spigot is available on SpigotMC [here](https://www.spigotmc.org/resources/languageapi.62198/) and the version for BungeeCord is available on SpigotMC [here](https://www.spigotmc.org/resources/languageapi-bungeecord.62199/).

## What does it do?
With the help of this plugin players see messages sent by plugins that use this plugin in the same language that their client has. If a message is not available in that language it will be displayed in English.

For every plugin that uses this plugin a new folder is created inside the "plugins\LanguageAPI" directory. In those folders there always is a file called "en.json", which contains the default English version of all messages of the respective plugin. To add support for another language to the plugin you can clone the file and rename the cloned file to something like "de.json" (to add support for German). Then translate all messages inside the file. Because the files are written in the JSON format, they are compatible with translation platforms like [Crowdin](https://crowdin.com/) (also used by Mojang), which make translation much easier.

## How to use it?
You have to register all messages of your plugin in onEnable(). This is done the following way:

```java
@Override
public void onEnable() {
    LanguageAPI.registerPlugin("ExamplePlugin");

    LanguageAPI.registerMessage("ExamplePlugin", "test",
            "This is the message \"test\"!");
    LanguageAPI.registerMessage("ExamplePlugin", "balance",
            "&aYour balance is &6§balance§$&a!");
    LanguageAPI.registerMessage("ExamplePlugin", "placeholders",
            "This is a message, which contains multiple placeholders:"
            + " §placeholder1§, §placeholder2§ and §placeholder3§");

    LanguageAPI.saveMessages("ExamplePlugin");
}
```

As you can see you can use color codes by using the character '&'. Placeholders are created by using the character '§'.

To get a translated message anywhere do the following:

```java
LanguageAPI.getMessage("ExamplePlugin", "test", CommandSender);
LanguageAPI.getMessage("ExamplePlugin", "balance", CommandSender, "429.18");
LanguageAPI.getMessage("ExamplePlugin", "placeholders", CommandSender,
        "replacement1", "replacement2", "replacement3");
```

Please notice that the replacements have to be in the same order like the placeholders in the default English version of a message.

On top of the methods already shown here there are additional methods available. The methods getMessageAsComponentBuilder() and getMessageAsBaseComponents() return a translated message as ComponentBuilder or BaseComponents. Both methods are useful for creating things like clickable messages. The method getMessages() returns all versions of a message, which is useful for checking things like if someone clicked in a specific inventory that has different names in different languages. The method getLanguage() return the current language of a CommandSender.

You can find more information about all methods in the javadoc.

**Do not forget to add "LanguageAPI" to the dependencies in plugin.yml!**

## License
### ISC License
Copyright &copy; 2018-2019 by Max Josef Overlack

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.