package ru.piko.pikopluginlib.Commands.Gamerules;

import ru.piko.pikopluginlib.Files.GameRuleFile;
import ru.piko.pikopluginlib.Main;

public class GameRuleStandardSave extends AGameRuleModification {

    private final String pluginId;
    private final GameRuleFile gameRuleFile;

    public GameRuleStandardSave(String pluginId) {
        this.pluginId = pluginId;
        this.gameRuleFile = new GameRuleFile(Main.Companion.getPlugin().getApi().getPlugins().get(pluginId).getPlugin().getDataFolder());
    }

    public GameRuleStandardSave setStandardSetValue() {
        gameRule.setSetValue(string -> gameRuleFile.setGamerule(gameRule.getName(), string));
        return this;
    }

    public GameRuleStandardSave setStandardGetValue() {
        gameRule.setGetValue(() -> gameRuleFile.getGamerule(gameRule.getName()));
        return this;
    }

    public GameRule all() {
        setStandardSetValue();
        setStandardGetValue();
        return gameRule;
    }

    public String getPluginId() {
        return pluginId;
    }

    public GameRuleFile getGameRuleFile() {
        return gameRuleFile;
    }
}
