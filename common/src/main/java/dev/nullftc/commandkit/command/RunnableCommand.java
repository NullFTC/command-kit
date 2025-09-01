package dev.nullftc.commandkit.command;

import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;

public class RunnableCommand implements Command {

    private final Runnable runnable;
    private final BooleanSupplier condition;

    public RunnableCommand(Runnable runnable, BooleanSupplier condition) {
        this.runnable = runnable;
        this.condition = condition;
    }

    /**
     * Runs a block forever (never finishes)
     */
    public static RunnableCommand forever(Runnable runnable) {
        return new RunnableCommand(runnable, () -> false);
    }

    /**
     * Runs a block until the condition returns true
     */
    public static RunnableCommand idler(BooleanSupplier condition) {
        return new RunnableCommand(() -> {}, condition);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        runnable.run();
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return condition.getAsBoolean();
    }
}
