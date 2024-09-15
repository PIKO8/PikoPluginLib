package ru.piko.pikopluginlib.Functions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

// This code was created by Code Monkey for C# Unity and translated using artificial intelligence for java spigot
// Этот код был создан Code Monkey для C# Unity и через ИИ был переведён для java spigot
public class FunctionTimer {

    private static final List<FunctionTimer> timerList = new ArrayList<>();

    // Лямда-функция была перенесена в конец для удобного использования на котлин
    // The lambda function has been moved to the end for convenient use on kotlin
    @Deprecated
    public static FunctionTimer create(Runnable action, int delayTicks) {
        return create(delayTicks, "", false, action);
    }

    // Лямда-функция была перенесена в конец для удобного использования на котлин
    // The lambda function has been moved to the end for convenient use on kotlin
    @Deprecated
    public static FunctionTimer create(Runnable action, int delayTicks, String functionName) {
        return create(delayTicks, functionName, false, action);
    }

    // Лямда-функция была перенесена в конец для удобного использования на котлин
    // The lambda function has been moved to the end for convenient use on kotlin
    @Deprecated
    public static FunctionTimer create(Runnable action, int delayTicks, String functionName, boolean stopAllWithSameName) {
        return create(delayTicks, functionName, stopAllWithSameName, action);
    }


    // Лямда-функция была перенесена в конец для удобного использования на котлин
    // The lambda function has been moved to the end for convenient use on kotlin
    public static FunctionTimer create(int delayTicks, String functionName, boolean stopAllWithSameName, Runnable action) {
        if (stopAllWithSameName) {
            stopAllTimersWithName(functionName);
        }

        FunctionTimer funcTimer = new FunctionTimer(action, delayTicks, functionName);
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(FunctionTimer.class), funcTimer::run, delayTicks);

        timerList.add(funcTimer);
        return funcTimer;
    }

    public static FunctionTimer create(int delayTicks, String functionName, Runnable action) {
        return create(delayTicks, functionName, false, action);
    }

    public static FunctionTimer create(int delayTicks, Runnable action) {
        return create(delayTicks, "", action);
    }

    public static void removeTimer(FunctionTimer funcTimer) {
        timerList.remove(funcTimer);
    }

    public static void stopAllTimersWithName(String functionName) {
        timerList.removeIf(timer -> timer.functionName.equals(functionName));
    }

    public static void stopFirstTimerWithName(String functionName) {
        timerList.removeIf(timer -> timer.functionName.equals(functionName));
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
}
