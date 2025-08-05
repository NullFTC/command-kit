package dev.nullftc.commandkit.command;

public class WaitCommand implements Command {
    private final long durationMillis;
    private long startTime;

    public WaitCommand(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    @Override public void initialize() {
        startTime = System.currentTimeMillis();
    }

    @Override public void execute() {}

    @Override public boolean isFinished() {
        return System.currentTimeMillis() - startTime >= durationMillis;
    }

    @Override public void end(boolean interrupted) {}
}