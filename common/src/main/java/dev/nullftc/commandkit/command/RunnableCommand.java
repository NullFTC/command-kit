package dev.nullftc.commandkit.command;

public class RunnableCommand implements Command {
    private final Runnable runnable;

    public RunnableCommand(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        this.runnable.run();
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return true;
    }
}
