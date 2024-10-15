package ru.piko.pikopluginlib.Functions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class FunctionPeriodic {

    private static List<FunctionPeriodic> funcList = new ArrayList<>();
    private static final Object initLock = new Object();

    public static FunctionPeriodic createGlobal(long timer, String functionName, BooleanSupplier action) {
        return create(timer, functionName, false, false, false, action);
    }

    public static FunctionPeriodic create(long timer, String functionName, BooleanSupplier action) {
        return create(timer, functionName, false, false, false, action);
    }

    public static FunctionPeriodic create(long timer, BooleanSupplier action) {
        return create(timer, "", false, false, false, action);
    }

    public static FunctionPeriodic create(long timer, String functionName, boolean stopAllWithSameName, BooleanSupplier action) {
        return create(timer, functionName, false, false, stopAllWithSameName, action);
    }

    public static FunctionPeriodic create(long timer, String functionName, boolean useUnscaledDeltaTime, boolean triggerImmediately, boolean stopAllWithSameName, BooleanSupplier action) {
        synchronized (initLock) {
            if (funcList.isEmpty()) {
                funcList = new ArrayList<>();
            }
        }

        if (stopAllWithSameName) {
            stopAllFunc(functionName);
        }

        FunctionPeriodic functionPeriodic = new FunctionPeriodic(action, timer, functionName, useUnscaledDeltaTime);
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getProvidingPlugin(FunctionPeriodic.class), functionPeriodic::run, 0, 1);

        funcList.add(functionPeriodic);

        if (triggerImmediately) action.getAsBoolean();

        return functionPeriodic;
    }

    public static void removeTimer(FunctionPeriodic funcTimer) {
        funcList.remove(funcTimer);
    }

    public static void stopTimer(String functionName) {
        for (FunctionPeriodic func : funcList) {
            if (func.functionName.equals(functionName)) {
                func.destroySelf();
                return;
            }
        }
    }

    public static void stopAllFunc(String functionName) {
        for (int i = 0; i < funcList.size(); i++) {
            if (funcList.get(i).functionName.equals(functionName)) {
                funcList.get(i).destroySelf();
                i--;
            }
        }
    }

    public static boolean isFuncActive(String name) {
        for (FunctionPeriodic func : funcList) {
            if (func.functionName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private long timer;
    private long baseTimer;
    private boolean useUnscaledDeltaTime;
    private String functionName;
    private BooleanSupplier action;
    private long lastUpdateTime;

    private FunctionPeriodic(BooleanSupplier action, long timer, String functionName, boolean useUnscaledDeltaTime) {
        this.action = action;
        this.timer = timer;
        this.functionName = functionName;
        this.useUnscaledDeltaTime = useUnscaledDeltaTime;
        baseTimer = timer;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void skipTimerTo(long timer) {
        this.timer = timer;
    }

    public void setBaseTimer(long baseTimer) {
        this.baseTimer = baseTimer;
    }

    public long getBaseTimer() {
        return baseTimer;
    }

    private void run() {
        if (useUnscaledDeltaTime) {
            timer -= System.currentTimeMillis() - lastUpdateTime;
        } else {
            timer -= 1; // assuming 1 tick = 1 millisecond
        }
        lastUpdateTime = System.currentTimeMillis();

        if (timer <= 0) {
            if (!action.getAsBoolean()) {
                destroySelf();
            } else {
                timer += baseTimer;
            }
        }
    }

    public void destroySelf() {
        removeTimer(this);
    }
}