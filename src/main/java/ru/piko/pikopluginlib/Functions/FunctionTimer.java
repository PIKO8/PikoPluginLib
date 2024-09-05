package ru.piko.pikopluginlib.Functions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

// This code was created by Code Monkey for C# Unity and translated using artificial intelligence for java spigot
// Этот код был создан Code Monkey для C# Unity и через ИИ был переведён для java spigot
public class FunctionTimer {

    private static final List<FunctionTimer> timerList = new ArrayList<>();

    public static FunctionTimer create(Runnable action, int delayTicks) {
        return create(action, delayTicks, "", false);
    }

    public static FunctionTimer create(Runnable action, int delayTicks, String functionName) {
        return create(action, delayTicks, functionName, false);
    }

    public static FunctionTimer create(Runnable action, int delayTicks, String functionName, boolean stopAllWithSameName) {
        if (stopAllWithSameName) {
            stopAllTimersWithName(functionName);
        }

        FunctionTimer funcTimer = new FunctionTimer(action, delayTicks, functionName);
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(FunctionTimer.class), funcTimer::run, delayTicks);

        timerList.add(funcTimer);
        return funcTimer;
    }

    public static void removeTimer(FunctionTimer funcTimer) {
        timerList.remove(funcTimer);
    }

    public static void stopAllTimersWithName(String functionName) {
        for (int i = 0; i < timerList.size(); i++) {
            if (timerList.get(i).functionName.equals(functionName)) {
                timerList.remove(i);
                i--;
            }
        }
    }

    public static void stopFirstTimerWithName(String functionName) {
        for (int i = 0; i < timerList.size(); i++) {
            if (timerList.get(i).functionName.equals(functionName)) {
                timerList.remove(i);
                return;
            }
        }
    }

    private Runnable action;
    private int delayTicks;
    private String functionName;

    public FunctionTimer(Runnable action, int delayTicks, String functionName) {
        this.action = action;
        this.delayTicks = delayTicks;
        this.functionName = functionName;
    }

    private void run() {
        action.run();
        removeTimer(this);
    }

    // Create a function to manually update (if necessary, though Spigot tasks are more automated)
    public static class FunctionTimerObject {

        private int ticks;
        private Runnable callback;

        public FunctionTimerObject(Runnable callback, int ticks) {
            this.callback = callback;
            this.ticks = ticks;
        }

        public boolean update() {
            ticks--;
            if (ticks <= 0) {
                callback.run();
                return true;
            }
            return false;
        }
    }

    // Create a FunctionTimerObject that must be manually updated
    public static FunctionTimerObject createObject(Runnable callback, int ticks) {
        return new FunctionTimerObject(callback, ticks);
    }
}
