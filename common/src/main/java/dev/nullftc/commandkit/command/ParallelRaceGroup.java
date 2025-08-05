package dev.nullftc.commandkit.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParallelRaceGroup implements Command {
    private final List<Command> commands = new ArrayList<>();
    private boolean anyFinished = false;

    public ParallelRaceGroup(Command... commands) {
        Collections.addAll(this.commands, commands);
    }

    @Override public void initialize() {
        for (Command cmd : commands) {
            cmd.initialize();
        }
    }

    @Override public void execute() {
        for (Command cmd : commands) {
            if (!cmd.isFinished()) {
                cmd.execute();
                if (cmd.isFinished()) {
                    anyFinished = true;
                    cmd.end(false);
                }
            }
        }
    }

    @Override public boolean isFinished() {
        return anyFinished;
    }

    @Override public void end(boolean interrupted) {
        for (Command cmd : commands) {
            if (!cmd.isFinished()) {
                cmd.end(true);
            }
        }
    }
}

