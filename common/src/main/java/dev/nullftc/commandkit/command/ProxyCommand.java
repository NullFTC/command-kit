package dev.nullftc.commandkit.command;

import java.util.function.Supplier;

/**
 * A command that defers construction of its internal command until the first time it runs.
 */
public class ProxyCommand implements Command {
    private final Supplier<Command> commandSupplier;
    private Command actual;

    public ProxyCommand(Supplier<Command> commandSupplier) {
        this.commandSupplier = commandSupplier;
    }

    @Override
    public void initialize() {
        actual = commandSupplier.get();
        actual.initialize();
    }

    @Override
    public void execute() {
        if (actual != null) {
            actual.execute();
        }
    }

    @Override
    public boolean isFinished() {
        return actual != null && actual.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        if (actual != null) {
            actual.end(interrupted);
        }
    }
}
