package dev.nullftc.commandkit.command;

/**
 * Represents a unit of robot behavior that can be scheduled and executed.
 *
 * <p>Commands define robot actions that run over time and can be composed
 * into sequences or parallel groups. Each command has a defined lifecycle:
 *
 * <ul>
 *   <li>{@link #initialize()} is called once when the command starts</li>
 *   <li>{@link #execute()} is called repeatedly while the command is active</li>
 *   <li>{@link #isFinished()} determines when the command ends normally</li>
 *   <li>{@link #end(boolean)} is called once when the command ends or is interrupted</li>
 * </ul>
 */
public interface Command {
    void initialize();
    void execute();
    void end(boolean interrupted);
    boolean isFinished();
}
