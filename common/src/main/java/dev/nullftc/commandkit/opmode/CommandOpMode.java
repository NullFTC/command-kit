package dev.nullftc.commandkit.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import dev.nullftc.commandkit.CommandScheduler;
import dev.nullftc.commandkit.command.Command;

public abstract class CommandOpMode extends LinearOpMode {
    private boolean started = false;

    public abstract void prerun();

    public abstract void run();

    public void end() {}

    public void schedule(Command command) {
        CommandScheduler.getInstance().schedule(command);
    }

    @Override
    public void runOpMode() {
        prerun();

        while (!isStarted() && !isStopRequested()) {
            CommandScheduler.getInstance().run();
        }

        started = true;

        run();

        while (opModeIsActive() && !isStopRequested()) {
            CommandScheduler.getInstance().run();
            execute();
        }

        end();

        CommandScheduler.getInstance().cancelAll();
    }

    public void execute() {}

    public boolean isStartedFlag() {
        return started;
    }

    public static CommandOpMode currentOpMode;
}
