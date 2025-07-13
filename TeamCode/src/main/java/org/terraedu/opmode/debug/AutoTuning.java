package org.terraedu.opmode.debug;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.joml.Vector3f;
import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.command.auto.GotoCommand;
import org.terraedu.command.auto.GotoConfig;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Alliance;
import org.terraedu.util.Pose;
import org.terraedu.util.RobotMode;

@TeleOp(name = "Auto Tuning Tele")
public class AutoTuning extends CommandOpMode {
    private double loopTime = 0;
    private final Robot robot = Robot.getInstance();

    private ElapsedTime timer;
    public RobotMode status;

    GamepadEx gph1;
    GamepadEx gph2;

    private MecanumDrive drive;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();


        status = RobotMode.DRIVING;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Globals.AUTO = true;
        robot.init(hardwareMap, telemetry, Alliance.BLUE);
        robot.reset();

        gph1 = new GamepadEx(gamepad1);
        gph2 = new GamepadEx(gamepad2);

        drive = robot.drive;
        robot.deposit.setState(Deposit.FourBarState.INIT);
        robot.intake.setState(Intake.IntakeState.INIT);

        gph1.getGamepadButton(GamepadKeys.Button.A).toggleWhenPressed(
                new SequentialCommandGroup(
                        new GotoCommand(robot, new Pose(0, 0, Math.toRadians(0)))

                )
        );






    }

    @Override
    public void run() {
        super.run();
        robot.read();
        robot.periodic();
        robot.write();
        robot.clearBulkCache();

        double loop = System.nanoTime();
        telemetry.addData("hz ", 1000000000 / (loop - loopTime));
        telemetry.addData("X", robot.localizer.getCurrX());
        telemetry.addData("Y", robot.localizer.getCurrY());
        telemetry.addData("Heading", robot.localizer.getCurrHeading());


        loopTime = loop;
        telemetry.update();
    }
}

