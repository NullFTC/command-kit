package dev.nullftc.commandkit.command;

public class RepeatCommand implements Command {
    private final Command toRepeat;
    private final int repeatCount;
    private int currentCount = 0;
    private boolean commandFinished = false;

    public RepeatCommand(Command toRepeat, int repeatCount) {
        this.toRepeat = toRepeat;
        this.repeatCount = repeatCount;
    }

    public RepeatCommand(Command toRepeat) {
        this(toRepeat, -1);
    }

    @Override
    public void initialize() {
        currentCount = 0;
        toRepeat.initialize();
        commandFinished = false;
    }

    @Override
    public void execute() {
        if (!commandFinished) {
            toRepeat.execute();
            if (toRepeat.isFinished()) {
                toRepeat.end(false);
                currentCount++;
                if (repeatCount != -1 && currentCount >= repeatCount) {
                    commandFinished = true;
                } else {
                    toRepeat.initialize();
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return commandFinished;
    }

    @Override
    public void end(boolean interrupted) {
        toRepeat.end(true);
    }
}
