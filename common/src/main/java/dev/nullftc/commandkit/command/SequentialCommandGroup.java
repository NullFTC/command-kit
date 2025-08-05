package dev.nullftc.commandkit.command;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class SequentialCommandGroup implements Command {
    private final Queue<Command> commands = new LinkedList<>();
    private Command current;

    public SequentialCommandGroup(Command... commands) {
        Collections.addAll(this.commands, commands);
    }

    @Override public void initialize() {
        current = commands.poll();
        if (current != null) current.initialize();
    }

    @Override public void execute() {
        if (current == null) return;
        current.execute();
        if (current.isFinished()) {
            current.end(false);
            current = commands.poll();
            if (current != null) current.initialize();
        }
    }

    @Override public boolean isFinished() {
        return current == null;
    }

    @Override public void end(boolean interrupted) {
        if (current != null) current.end(true);
    }
}
