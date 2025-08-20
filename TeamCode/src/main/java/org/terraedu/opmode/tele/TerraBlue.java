package org.terraedu.opmode.tele;

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
import org.terraedu.command.teleop.SetExampleCommand;
import org.terraedu.constants.IntakePositions;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Modes.LinkageMode;
import org.terraedu.util.Modes.PlaceMode;
import org.terraedu.util.Modes.RobotMode;
import org.terraedu.util.system.Alliance;

@TeleOp(name = "Blue")
public class TerraBlue extends CommandOpMode {

    private ElapsedTime timer;
    private double loopTime = 0;
    public PlaceMode deposit;
    public LinkageMode link;
    public RobotMode status;
    private final Robot robot = Robot.getInstance();

    GamepadEx gph1;
    GamepadEx gph2;

    private MecanumDrive drive;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();


        deposit = PlaceMode.SPECIMEN;
        status = RobotMode.DRIVING;
        link = LinkageMode.IN;

        Globals.AUTO = false;
        Globals.SHOULD_LOG = true;

        robot.init(hardwareMap, telemetry, Alliance.BLUE);
        robot.reset();

        gph1 = new GamepadEx(gamepad1);
        gph2 = new GamepadEx(gamepad2);

        drive = robot.drive;

        robot.deposit.setState(Deposit.DepositState.INIT);
        robot.deposit.setLinkage(Deposit.LinkageState.INIT);
        robot.intake.setState(Intake.IntakeState.INIT);
        robot.latch.setPosition(IntakePositions.CLOSE_LATCH);
        //#region Command Registrar
        //TODO USE BOOLEANS FOR MODES INSTEAD

        // Y Button – Toggle Specimen Mode
        gph1.getGamepadButton(GamepadKeys.Button.X).whenActive(
                new SequentialCommandGroup(

                    new SetExampleCommand()


                )
        );

        //#endregion
    }


    @Override
    public void run() {
        super.run();

        if (timer == null) {
            timer = new ElapsedTime();
        }

        robot.read();

//        robot.deposit.setPower(gamepad2.left_stick_y);


        double turn = joystickScalar(gamepad1.left_stick_x, 0.01);

        Vector3f driveVec = new Vector3f(
                (float) joystickScalar(-gamepad1.right_stick_x, 0.001),
                (float) joystickScalar(gamepad1.right_stick_y, 0.001),
                0f
        );

        drive.set(driveVec, turn);
        robot.periodic();
        robot.write();
        robot.clearBulkCache();

//        double loop = System.nanoTime();
//        telemetry.addData("serov", robot.intakeLinkage.getPosition());
//        telemetry.addData("target", robot.intake.getTarget());
//        telemetry.addData("pos", robot.deposit.getPosition());
//        telemetry.addData("mode ", deposit);
//        telemetry.addData("reading", robot.intake.getReading());
//        telemetry.addData("hz ", 1000000000 / (loop - loopTime));
//        loopTime = loop;
        telemetry.update();
    }

    private double joystickScalar(double num, double min) {
        return joystickScalar(num, min, 0.66, 4);
    }

    private double joystickScalar(double n, double m, double l, double a) {
        return Math.signum(n) * m
                + (1 - m) *
                (Math.abs(n) > l ?
                        Math.pow(Math.abs(n), Math.log(l / a) / Math.log(l)) * Math.signum(n) :
                        n / a);
    }



}

