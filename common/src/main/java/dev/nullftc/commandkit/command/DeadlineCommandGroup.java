package dev.nullftc.commandkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeadlineCommandGroup implements Command {
    private final Command deadline;
    private final List<Command> others = new ArrayList<>();
    private final List<Command> all = new ArrayList<>();
    private boolean finished = false;

    public DeadlineCommandGroup(Command deadline, Command... others) {
        this.deadline = deadline;
        this.others.addAll(Arrays.asList(others));
        this.all.add(deadline);
        this.all.addAll(this.others);
    }

    @Override
    public void initialize() {
        for (Command cmd : all) {
            cmd.initialize();
        }
    }

    @Override
    public void execute() {
        deadline.execute();
        for (Command cmd : others) {
            cmd.execute();
        }

        if (deadline.isFinished()) {
            finished = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        for (Command cmd : all) {
            cmd.end(interrupted || finished);
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
