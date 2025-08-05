package dev.nullftc.commandkit.command;

import java.util.Map;
import java.util.function.Supplier;

public class SwitchCommand<T> implements Command {
    private final Supplier<T> selector;
    private final Map<T, Command> commandMap;
    private final Command defaultCommand;

    private Command selected;

    public SwitchCommand(Supplier<T> selector, Map<T, Command> commandMap, Command defaultCommand) {
        this.selector = selector;
        this.commandMap = commandMap;
        this.defaultCommand = defaultCommand;
    }

    @Override
    public void initialize() {
        T key = selector.get();
        selected = commandMap.getOrDefault(key, defaultCommand);
        if (selected != null) {
            selected.initialize();
        }
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
