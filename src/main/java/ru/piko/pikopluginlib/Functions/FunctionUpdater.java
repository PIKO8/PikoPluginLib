package ru.piko.pikopluginlib.Functions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class FunctionUpdater {

    private static final List<FunctionUpdater> updaterList = new ArrayList<>();

    public static FunctionUpdater create(java.util.function.BooleanSupplier updateFunc) {
        return create(updateFunc, "", true, false);
    }

    public static FunctionUpdater create(java.util.function.BooleanSupplier updateFunc, String functionName) {
        return create(updateFunc, functionName, true, false);
    }

    public static FunctionUpdater create(java.util.function.BooleanSupplier updateFunc, String functionName, boolean active) {
        return create(updateFunc, functionName, active, false);
    }

    public static FunctionUpdater create(java.util.function.BooleanSupplier updateFunc, String functionName, boolean active, boolean stopAllWithSameName) {

        if (stopAllWithSameName) {
            stopAllUpdatersWithName(functionName);
        }

        FunctionUpdater functionUpdater = new FunctionUpdater(updateFunc, functionName, active);
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getProvidingPlugin(FunctionUpdater.class), functionUpdater::update, 0, 1);

        updaterList.add(functionUpdater);
        return functionUpdater;
    }

    private static void removeUpdater(FunctionUpdater funcUpdater) {
        updaterList.remove(funcUpdater);
    }

    public static void destroyUpdater(FunctionUpdater funcUpdater) {
        if (funcUpdater != null) {
            funcUpdater.destroySelf();
        }
    }

    public static void stopUpdaterWithName(String functionName) {
        for (FunctionUpdater updater : updaterList) {
            if (updater.functionName.equals(functionName)) {
                updater.destroySelf();
                return;
            }
        }
    }

    public static void stopAllUpdatersWithName(String functionName) {
        for (int i = 0; i < updaterList.size(); i++) {
            FunctionUpdater updater = updaterList.get(i);
            if (updater.functionName.equals(functionName)) {
                updater.destroySelf();
                i--;
            }
        }
    }

    private String functionName;
    private boolean active;
    private java.util.function.BooleanSupplier updateFunc;

    public FunctionUpdater(java.util.function.BooleanSupplier updateFunc, String functionName, boolean active) {
        this.updateFunc = updateFunc;
        this.functionName = functionName;
        this.active = active;
    }

    public void pause() {
        active = false;
    }

    public void resume() {
        active = true;
    }

    private void update() {
        if (!active) return;
        if (updateFunc.getAsBoolean()) {
            destroySelf();
        }
    }

    public void destroySelf() {
        removeUpdater(this);
    }
}