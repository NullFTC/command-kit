package dev.nullftc.commandkit.command;

import java.util.function.BooleanSupplier;

public class WaitUntilCommand implements Command {
    private final BooleanSupplier condition;

    public WaitUntilCommand(BooleanSupplier condition) {
        this.condition = condition;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {}

    @Override
    public boolean isFinished() {
        return condition.getAsBoolean();
    }

    @Override
    public void end(boolean interrupted) {}
}
