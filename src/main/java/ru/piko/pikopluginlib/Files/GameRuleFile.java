package ru.piko.pikopluginlib.Files;

import java.io.File;

public class GameRuleFile extends CustomConfigFile {

    public GameRuleFile(File dataFolder) {
        super(dataFolder, "gamerules");
    }

    public void setGamerule(String name, String value) {
        conf.set(name, value);
    }

    public String getGamerule(String name) {
        return conf.getString(name, "false");
    }

}
