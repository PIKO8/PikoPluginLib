package ru.piko.pikopluginlib.Commands.Gamerules;

public abstract class AGameRuleModification {

    public GameRule gameRule;

    public GameRule exitModify() {
        return gameRule;
    }
}
