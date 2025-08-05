package dev.nullftc.commandkit.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParallelCommandGroup implements Command {
    private final List<Command> commands = new ArrayList<>();
    private final Set<Command> finished = new HashSet<>();

    public ParallelCommandGroup(Command... commands) {
        Collections.addAll(this.commands, commands);
    }

    @Override public void initialize() {
        for (Command cmd : commands) {
            cmd.initialize();
        }
    }

    @Override public void execute() {
        for (Command cmd : commands) {
            if (!finished.contains(cmd)) {
                cmd.execute();
                if (cmd.isFinished()) {
                    cmd.end(false);
                    finished.add(cmd);
                }
            }
        }
    }

    @Override public boolean isFinished() {
        return finished.size() == commands.size();
    }

    @Override public void end(boolean interrupted) {
        for (Command cmd : commands) {
            if (!finished.contains(cmd)) {
                cmd.end(true);
            }
        }
    }
}
