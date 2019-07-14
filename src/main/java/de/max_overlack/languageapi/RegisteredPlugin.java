package de.max_overlack.languageapi;

import java.util.HashMap;
import java.util.Map;

public final class RegisteredPlugin {

    private final Map<String, Map<String, String>> languages;
    private boolean modified;

    public RegisteredPlugin() {
        this.languages = new HashMap<>();
        this.modified = false;
        this.languages.put("en", new HashMap<>());
    }

    public Map<String, Map<String, String>> getLanguages() {
        return this.languages;
    }

    public boolean isModified() {
        return this.modified;
    }

    public void setModifiedTrue() {
        this.modified = true;
    }

}