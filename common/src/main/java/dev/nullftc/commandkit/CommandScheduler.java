package dev.nullftc.commandkit;

import java.util.*;

import dev.nullftc.commandkit.command.Command;
import dev.nullftc.commandkit.subsystem.Subsystem;

public class CommandScheduler {
    private static final CommandScheduler instance = new CommandScheduler();

    public static CommandScheduler getInstance() {
        return instance;
    }

    private final Set<Subsystem> subsystems = new HashSet<>();
    private final Set<Command> scheduledCommands = new HashSet<>();
    private final Map<Subsystem, Command> requirements = new HashMap<>();

    private CommandScheduler() {}

    public void registerSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    public void schedule(Command command, Subsystem... requiredSubsystems) {
        for (Subsystem requirement : requiredSubsystems) {
            Command occupying = requirements.get(requirement);
            if (occupying != null && occupying != command) {
                occupying.end(true);
                scheduledCommands.remove(occupying);
            }
            requirements.put(requirement, command);
        }

        scheduledCommands.add(command);
        command.initialize();
    }

    public void run() {
        for (Subsystem subsystem : subsystems) {
            subsystem.periodic();
        }

        Iterator<Command> iterator = scheduledCommands.iterator();
        while (iterator.hasNext()) {
            Command command = iterator.next();
            command.execute();

            if (command.isFinished()) {
                command.end(false);
                iterator.remove();
                requirements.values().removeIf(cmd -> cmd == command);
            }
        }
    }

    public void cancel(Command command) {
        if (scheduledCommands.remove(command)) {
            command.end(true);
            requirements.values().removeIf(cmd -> cmd == command);
        }
    }

    public void cancelAll() {
        for (Command command : scheduledCommands) {
            command.end(true);
        }
        scheduledCommands.clear();
        requirements.clear();
    }
}
