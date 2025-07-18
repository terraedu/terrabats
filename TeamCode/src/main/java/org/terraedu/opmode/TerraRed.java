package org.terraedu.opmode;


import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.joml.Vector3f;
import org.terraedu.Robot;
import org.terraedu.command.bot.SetArmCommand;
import org.terraedu.command.bot.SetExtendoCommand;
import org.terraedu.command.bot.SetIntakeCommand;
import org.terraedu.command.bot.SetLiftCommand;
import org.terraedu.constants.IntakePositions;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.util.Alliance;
import org.terraedu.util.LinkageMode;
import org.terraedu.util.PlaceMode;
import org.terraedu.util.RobotMode;

import java.util.Set;

@TeleOp(name = "Red")
public class TerraRed extends CommandOpMode {

    private ElapsedTime timer;
    private double loopTime = 0;

    private double speedCoeff;
    private double turnspeedCoeff;
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

        robot.init(hardwareMap, telemetry, Alliance.RED);
        robot.reset();

        gph1 = new GamepadEx(gamepad1);
        gph2 = new GamepadEx(gamepad2);

        drive = robot.drive;
        robot.deposit.setState(Deposit.OuttakeState.START);
        robot.intake.setState(Intake.IntakeState.INIT);
        robot.deposit.setClawClosed(false);

        new SetExtendoCommand(robot.intake, 0);
        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.INIT_TURRET));

        //#region Command Registrar

//        // Y Button – Toggle Specimen Mode
//        gph1.getGamepadButton(GamepadKeys.Button.Y).and(new Trigger(() -> deposit == PlaceMode.SPECIMEN)).whenActive(
//                new SequentialCommandGroup(
//                        new InstantCommand(() -> robot.deposit.setClawClosed(true)),
//                        new InstantCommand(() -> robot.setAlliance(Alliance.REDY)),
//                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
//                        new WaitCommand(500),
//                        new InstantCommand(() -> status = RobotMode.DRIVING),
//                        new InstantCommand(() -> robot.deposit.setClawClosed(false)),
//                        new InstantCommand(() -> deposit = PlaceMode.SAMPLE),
//                        new InstantCommand(() -> status = RobotMode.DRIVING)
//
//
//
//                )
//        );
//        gph1.getGamepadButton(GamepadKeys.Button.Y).and(new Trigger(() -> deposit == PlaceMode.SAMPLE)).whenActive(
//
//                new SequentialCommandGroup(
//                        new InstantCommand(() -> robot.deposit.setClawClosed(true)),
//                        new InstantCommand(() -> robot.setAlliance(Alliance.RED)),
//                        new InstantCommand(() -> status = RobotMode.DRIVING),
//                        new SetExtendoCommand(robot.intake, 0),
//                        new WaitCommand(500),
//                        new InstantCommand(() -> robot.deposit.setClawClosed(false)),
//                        new InstantCommand(() -> deposit = PlaceMode.SPECIMEN),
//                        new InstantCommand(() -> status = RobotMode.DRIVING)
//
//
//
//                )
//        );
        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).whenActive(
                intakeSequence()
        );

        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1).whenActive(
                sampleDepositSequence()
        );

        gph1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenActive(
                new SequentialCommandGroup(
                        new SetIntakeCommand(robot.intake, Intake.IntakeState.INT_READY),
                        new WaitCommand(250),
                        new SetIntakeCommand(robot.intake, Intake.IntakeState.GRAB),
                        new WaitCommand(250),
                        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.INIT_TURRET)),
                        new SetIntakeCommand(robot.intake, Intake.IntakeState.TRANSFER),
                        new InstantCommand(() -> robot.deposit.setClawClosed(false)),
                        new SetExtendoCommand(robot.intake, 45)
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenActive(
                new SequentialCommandGroup(
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.TRANSFER),
                        new WaitCommand(250),
                        new InstantCommand(() -> robot.deposit.setClawClosed(true)),
                        new WaitCommand(350),
                        new InstantCommand(() -> robot.iclaw.setPosition(IntakePositions.OPEN_CLAW)),
                        new WaitCommand(150),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
                        new SetExtendoCommand(robot.intake, 0),
                        new SetLiftCommand(robot.deposit, 1750),
                        new SetArmCommand(robot.deposit, Deposit.OuttakeState.PLACE)

                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenActive(
                new SequentialCommandGroup(
                        new SetExtendoCommand(robot.intake, 325)
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(
                new SequentialCommandGroup(
                        new SetExtendoCommand(robot.intake, 200)
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.RIGHT_TURRET))
                )
        );

        gph1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.turret.setPosition(IntakePositions.LEFT_TURRET))
                )
        );

//        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1 && deposit == PlaceMode.SPECIMEN).and(new Trigger(() -> status == RobotMode.DRIVING)).whenActive(
//                toSpecimenMode()
//        );
//        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1 && deposit == PlaceMode.SPECIMEN).and(new Trigger(() -> status == RobotMode.PLACING)).whenActive(
//                toDrivingFromSpecimen()
//        );
//
//        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1 && deposit == PlaceMode.SAMPLE).and(new Trigger(() -> status == RobotMode.DRIVING)).whenActive(
//                sampleTransfer()
//        );
//        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1 && deposit == PlaceMode.SAMPLE).and(new Trigger(() -> status == RobotMode.PLACING)).whenActive(
//                samplePlace()
//        );

        // DPAD UP – Toggle Linkage between INIT and PLACE

//        gph1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
//                new SequentialCommandGroup(
//                        new InstantCommand(() -> link = LinkageMode.OUT)
//                )
//        );
//        gph1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
//                new SequentialCommandGroup(
//                        new InstantCommand(() -> status = RobotMode.PLACING),
//                        new InstantCommand(() -> link = LinkageMode.IN)
//                )
//        );

        // Right Trigger – Intake Logic (Auto-Intake If Driving)


//        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).and(new Trigger(() -> status == RobotMode.DRIVING)).whenActive(
//                startAutoIntake()
//
//        );
//        new Trigger(() -> gph1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1).and(new Trigger(() -> status == RobotMode.INTAKING)).whenActive(
//                manualEject()
//        );


//        gph1.getGamepadButton(GamepadKeys.Button.B).whenActive(
//                new InstantCommand(() -> robot.intake.setReading(false))
//        );
//
//        gph1.getGamepadButton(GamepadKeys.Button.A).whenActive(
//                new SequentialCommandGroup(
//                        new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
//                        new SetSpinCommand(robot.intake, -1),
//                        new WaitCommand(125),
//                        new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER)
//
//                )
//        );


        //#endregion
    }

    @Override
    public void run() {
        super.run();

        if (timer == null) {
            timer = new ElapsedTime();
        }

        robot.read();


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
        telemetry.addData("intake", robot.intake.getPosition());
        telemetry.addData("deposit", robot.deposit.getPosition());

        telemetry.addData("mode ", deposit);
        telemetry.addData("reading", robot.intake.getReading());

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

    private Command intakeSequence() {
        return new SequentialCommandGroup(
                new SetExtendoCommand(robot.intake, 250),
                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER)
        );
    }

    private Command sampleDepositSequence () {
        return new SequentialCommandGroup(

        );
    }


//    private Command toSpecimenMode() {
//        return new SequentialCommandGroup(
//                new InstantCommand(() -> robot.deposit.setClawClosed(false)),
//                new WaitCommand(250),
//                new SetLiftCommand(robot.deposit, 0),
//                new SetArmCommand(robot.deposit, Deposit.OuttakeState.SPECI),
//                new InstantCommand(() -> status = RobotMode.PLACING)
//
//        );
//    }
//    private Command toDrivingFromSpecimen() {
//        return new SequentialCommandGroup(
//                new InstantCommand(() -> robot.deposit.setClawClosed(true)),
//                new WaitCommand(250),
//                new SetLiftCommand(robot.deposit, 530),
//                new WaitCommand(250),
//                new SetArmCommand(robot.deposit, Deposit.OuttakeState.SPECIPLACE),
//                new InstantCommand(() -> status = RobotMode.DRIVING)
//
//        );
//    }
//
//    private Command sampleTransfer() {
//        return new SequentialCommandGroup(
//                new SetExtendoCommand(robot.intake, 100),
//                new WaitCommand(150),
//                new SetArmCommand(robot.deposit, Deposit.OuttakeState.TRANSFER),
//                new SetIntakeCommand(robot.intake, Intake.IntakeState.RELEASE),
//                new WaitCommand(150),
//                new SetSpinCommand(robot.intake, 1),
//                new SetExtendoCommand(robot.intake, 40),
//                new WaitCommand(300),
//                new InstantCommand(() -> robot.deposit.setSampleClosed(true)),
//                new SetSpinCommand(robot.intake, 0),
//                new SetExtendoCommand(robot.intake, 135),
//                new WaitCommand(150),
//                new SetLiftCommand(robot.deposit, 630),
//                new SetArmCommand(robot.deposit, Deposit.OuttakeState.PLACE),
//                new WaitCommand(300),
//                new InstantCommand(() -> robot.deposit.setClawClosed(true)),
//                new InstantCommand(() -> status = RobotMode.PLACING)
//
//
//        );
//    }
//
//    private Command samplePlace() {
//        return new SequentialCommandGroup(
//                new InstantCommand(() -> robot.deposit.setSampleClosed(false)),
//                new InstantCommand(() -> robot.deposit.setClawClosed(false)),
//                new WaitCommand(300),
//                new WaitCommand(300),
//                new SetArmCommand(robot.deposit, Deposit.OuttakeState.INIT),
//                new WaitCommand(300),
//                new SetLiftCommand(robot.deposit, 0),
//                new WaitCommand(200),
//                new SetExtendoCommand(robot.intake, 0),
//                new InstantCommand(() -> status = RobotMode.DRIVING)
//
//
//        );
//    }
//
//    private Command startAutoIntake() {
//        return new SequentialCommandGroup(
//                new InstantCommand(() -> robot.intake.setReading(true)),
//                new SetExtendoCommand(robot.intake, 475),// Max is 500
//                new WaitCommand(500),
//                new SetIntakeCommand(robot.intake, Intake.IntakeState.HOVER),
//                new SetSpinCommand(robot.intake, 1),
//                new WaitUntilCommand(robot.intake.getSupplier()),
//                new SetSpinCommand(robot.intake, 0),
//                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
//                new SetExtendoCommand(robot.intake, 0),
//                new SetSpinCommand(robot.intake, -.35),
//                new WaitCommand(150),
//                new SetSpinCommand(robot.intake, 0),
//                new InstantCommand(() -> status = RobotMode.INTAKING)
//
//
//        );
//    }
//
//    private Command cancelIntake() {
//        return new SequentialCommandGroup(
//                new SetSpinCommand(robot.intake, 0),
//                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
//                new SetExtendoCommand(robot.intake, 0),
//                new InstantCommand(() -> status = RobotMode.DRIVING)
//        );
//    }
//
//    private Command manualEject() {
//        return new SequentialCommandGroup(
//                new InstantCommand(() -> robot.intake.setReading(false)),
//                new SetIntakeCommand(robot.intake, Intake.IntakeState.INIT),
//                new SetExtendoCommand(robot.intake, 450),
//                new SetSpinCommand(robot.intake, -1),
//                new WaitCommand(750),
//                new SetSpinCommand(robot.intake, 0),
//                new SetExtendoCommand(robot.intake, 0),
//                new InstantCommand(() -> status = RobotMode.DRIVING)
//
//        );
//    }

}

