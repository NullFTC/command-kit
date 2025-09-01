package dev.nullftc.commandkit.command;

import java.util.function.BooleanSupplier;

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

    /** Run this command, then the next. */
    default Command andThen(Command next) {
        return new SequentialCommandGroup(this, next);
    }

    /** Run this command, then a simple runnable. */
    default Command andThen(Runnable action) {
        return new SequentialCommandGroup(this, new RunnableCommand(action));
    }

    /** Run this and another in parallel; both must finish. */
    default Command alongWith(Command other) {
        return new ParallelCommandGroup(this, other);
    }

    /** Run this and another in parallel; ends when *any* ends. */
    default Command raceWith(Command other) {
        return new ParallelRaceGroup(this, other);
    }

    /** Run this in parallel with others, but this command is the deadline. */
    default Command deadlineWith(Command... others) {
        return new DeadlineCommandGroup(this, others);
    }

    /** Repeat this command indefinitely. */
    default Command repeatedly() {
        return new RepeatCommand(this);
    }

    /** Run this command until a condition is true. */
    default Command until(BooleanSupplier condition) {
        return new ParallelRaceGroup(this, new WaitUntilCommand(condition));
    }

    /** Run this command only if a condition is true. */
    default Command onlyIf(BooleanSupplier condition) {
        return new ConditionalCommand(this, new RunnableCommand(() -> {}), condition);
    }

    /** Run this command unless a condition is true. */
    default Command unless(BooleanSupplier condition) {
        return new ConditionalCommand(new RunnableCommand(() -> {}), this, condition);
    }

    /** Run this command with a timeout in seconds. */
    default Command withTimeout(double seconds) {
        return new ParallelRaceGroup(this, new WaitCommand((long) seconds));
    }

    /** Run this command, but interrupt if the condition becomes true. */
    default Command withInterrupt(BooleanSupplier condition) {
        return new ParallelRaceGroup(this, new WaitUntilCommand(condition));
    }

    /** Run this command, then wait a fixed time before continuing. */
    default Command andThenWait(double seconds) {
        return new SequentialCommandGroup(this, new WaitCommand((long) seconds));
    }

    /** Run this command, then wait until a condition is true. */
    default Command andThenWaitUntil(BooleanSupplier condition) {
        return new SequentialCommandGroup(this, new WaitUntilCommand(condition));
    }

    /** Defer creation until runtime. */
    default Command asProxy() {
        return new ProxyCommand(() -> this);
    }
}
