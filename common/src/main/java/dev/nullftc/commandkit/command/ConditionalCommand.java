package dev.nullftc.commandkit.command;

import java.util.function.BooleanSupplier;

/**
 * A command that chooses between two commands based on a boolean condition at runtime.
 */
public class ConditionalCommand implements Command {
    private final Command onTrue;
    private final Command onFalse;
    private final BooleanSupplier condition;

    private Command selected;

    public ConditionalCommand(Command onTrue, Command onFalse, BooleanSupplier condition) {
        this.onTrue = onTrue;
        this.onFalse = onFalse;
        this.condition = condition;
    }

    @Override
    public void initialize() {
        selected = condition.getAsBoolean() ? onTrue : onFalse;
        selected.initialize();
    }

    @Override
    public void execute() {
        if (selected != null) {
            selected.execute();
        }
    }

    @Override
    public boolean isFinished() {
        return selected != null && selected.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        if (selected != null) {
            selected.end(interrupted);
        }
    }
}
