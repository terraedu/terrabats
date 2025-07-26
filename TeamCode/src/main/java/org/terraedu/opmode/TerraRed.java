package org.terraedu.opmode;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.joml.Vector3f;
import org.terraedu.Globals;
import org.terraedu.Robot;
import org.terraedu.command.auto.HeadingCommand;
import org.terraedu.command.auto.SetPointCommand;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetDepositLinkageCommand;
import org.terraedu.command.bot.SetExtendoCommand;
import org.terraedu.command.bot.SetIntakeCommand;
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.command.bot.SetSpinCommand;
import org.terraedu.constants.IntakePositions;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Hang;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Alliance;
import org.terraedu.util.LinkageMode;
import org.terraedu.util.PlaceMode;
import org.terraedu.util.Pose;
import org.terraedu.util.RobotMode;

@TeleOp(name = "Red")
public class TerraRed extends CommandOpMode {

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

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Globals.SHOULD_LOG = true;

        robot.init(hardwareMap, telemetry, Alliance.RED);
        robot.reset();

        gph1 = new GamepadEx(gamepad1);
        gph2 = new GamepadEx(gamepad2);

        drive = robot.drive;
        robot.deposit.setState(Deposit.FourBarState.INIT);
        robot.deposit.setLinkage(Deposit.LinkageState.INIT);
        robot.intake.setState(Intake.IntakeState.INIT);
        robot.latch.setPosition(IntakePositions.CLOSE_LATCH);


        //#region Command Registrar

        // Y Button – Toggle Specimen Mode
        gph1.getGamepadButton(GamepadKeys.Button.X).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.setAlliance(Alliance.REDY)),
                        new SetArmCommand(robot.deposit, Deposit.FourBarState.INIT)




                )
        );
        gph1.getGamepadButton(GamepadKeys.Button.Y).whenActive(

                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.setAlliance(Alliance.RED)),
                        new SetExtendoCommand(robot.intake, 0)




                )
        );

        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1).whenActive(
                toSpecimenMode()
        );
        new Trigger(()-> gph1.getButton(GamepadKeys.Button.LEFT_BUMPER)).whenActive(
                toDrivingFromSpecimen()
        );

//        new Trigger(()-> gph1.getButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)).whenActive(
//                sampleTransfer()
//        );
//        new Trigger(() -> gph1.getButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)).whenActive(
//                samplePlace()
//        );

        // DPAD UP – Toggle Linkage between INIT and PLACE

        gph1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                new SequentialCommandGroup(
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE),
                        new InstantCommand(() -> link = LinkageMode.OUT)
                )
        );
        gph1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                new SequentialCommandGroup(
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE),
                        new InstantCommand(() -> link = LinkageMode.OUT)
                )
        );
        gph1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                new SequentialCommandGroup(
                        new InstantCommand(() -> status = RobotMode.PLACING),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                        new InstantCommand(() -> link = LinkageMode.IN)
                )
        );

        // Right Trigger – Intake Logic (Auto-Intake If Driving)


        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).and(new Trigger(() -> status == RobotMode.DRIVING)).whenActive(
                startAutoIntake()

        );
        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).and(new Trigger(() -> status == RobotMode.INTAKING)).whenActive(
                manualEject()
        );


        gph1.getGamepadButton(GamepadKeys.Button.B).whenActive(
                new InstantCommand(() -> robot.intake.setReading(false))
        );

        gph1.getGamepadButton(GamepadKeys.Button.A).whenActive(
                new SequentialCommandGroup(
                        new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                        new WaitCommand( 450),
                        new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER)

                )
        );


        gph1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenHeld(
                new InstantCommand(() -> {
                    robot.hang.setState(Hang.HangState.IN);
                })
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenHeld(
                new InstantCommand(() -> {
                    robot.hang.setState(Hang.HangState.OUT);
                })
        );
        gph1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenHeld(
                new InstantCommand(() -> {
                    robot.hang.setState(Hang.HangState.STATIONARY);
                })
        );
        gph1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(()-> Globals.AUTO = true),
                        new InstantCommand(()-> robot.localizer.reset())

                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenHeld(
                new SequentialCommandGroup(
                        new InstantCommand(()-> Globals.AUTO = true),
                        new HeadingCommand(robot,new Pose(0,0,0),1)
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

        double loop = System.nanoTime();
//        telemetry.addData("serov", robot.intakeLinkage.getPosition());
//        telemetry.addData("target", robot.intake.getTarget());
        telemetry.addData("pos", robot.deposit.getPosition());
        telemetry.addData("mode ", deposit);
//        telemetry.addData("reading", robot.intake.getReading());

        telemetry.addData("hz ", 1000000000 / (loop - loopTime));
        loopTime = loop;
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

    private Command toSpecimenMode() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                new WaitCommand(250),
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                new SetLiftCommand(robot.deposit, 0),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECI),
                new InstantCommand(() -> status = RobotMode.PLACING)

        );
    }


    private Command toggleSpecimenLinkage() {
        return new ConditionalCommand(

                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE),
                new SequentialCommandGroup(
                        new InstantCommand(() -> status = RobotMode.PLACING),
                        new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT)
                ),
                () -> status == RobotMode.PLACING && robot.deposit.getLinkageState() == Deposit.LinkageState.INIT
        );
    }

    private Command toDrivingFromSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                new WaitCommand(250),
                new SetLiftCommand(robot.deposit, 410),
                new WaitCommand(250),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.SPECIPLACE),
                new InstantCommand(() -> status = RobotMode.DRIVING)

        );
    }

    private Command sampleTransfer() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.intake.setReading(false)),
                new SetExtendoCommand(robot.intake, 100),
                new WaitCommand(150),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.TRANSFER),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.RELEASE),
                new WaitCommand(150),
                new SetSpinCommand(robot.intake, 1),
                new SetExtendoCommand(robot.intake, 40),
                new WaitCommand(300),
                new InstantCommand(() -> robot.deposit.setSampleClosed(true)),
                new SetSpinCommand(robot.intake, 0),
                new SetExtendoCommand(robot.intake, 135),
                new WaitCommand(150),
                new SetLiftCommand(robot.deposit, 630),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.PLACE),
                new WaitCommand(300),
                new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.PLACE),
                new InstantCommand(() -> status = RobotMode.PLACING)


        );
    }

    private Command samplePlace() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.deposit.setSampleClosed(false)),
                new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                new WaitCommand(300),
                new SetDepositLinkageCommand(robot.deposit, Deposit.LinkageState.INIT),
                new WaitCommand(300),
                new SetArmCommand(robot.deposit, Deposit.FourBarState.INIT),
                new WaitCommand(300),
                new SetLiftCommand(robot.deposit, 0),
                new WaitCommand(200),
                new SetExtendoCommand(robot.intake, 0),
                new InstantCommand(() -> status = RobotMode.DRIVING)


        );
    }

    private Command startAutoIntake() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.intake.setReading(true)),
                new SetExtendoCommand(robot.intake, 640),// Max is 660
                new WaitCommand(700),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER),
                new SetSpinCommand(robot.intake, 1),
                new WaitUntilCommand(robot.intake.getSupplier()),
                new SetSpinCommand(robot.intake, 0),
                new SetExtendoCommand(robot.intake, 0),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                new InstantCommand(() -> status = RobotMode.INTAKING)


        );
    }

    private Command cancelIntake() {
        return new SequentialCommandGroup(
                new SetSpinCommand(robot.intake, 0),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                new SetExtendoCommand(robot.intake, 0),
                new InstantCommand(() -> status = RobotMode.DRIVING)
        );
    }

    private Command manualEject() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.intake.setReading(false)),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
                new SetExtendoCommand(robot.intake, 550),
                new SetSpinCommand(robot.intake, -1),
                new WaitCommand(750),
                new SetSpinCommand(robot.intake, 0),
                new SetExtendoCommand(robot.intake, 0),
                new InstantCommand(() -> status = RobotMode.DRIVING)

        );
    }

}

