package org.terraedu;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.terraedu.subsystem.Deposit;
import org.terraedu.subsystem.Hang;
import org.terraedu.subsystem.Intake;
import org.terraedu.subsystem.MecanumDrive;
import org.terraedu.subsystem.PinpointLocalizer;
import org.terraedu.util.Alliance;
import org.terraedu.util.TimingLogger;
import org.terraedu.util.control.GoBildaPinpointDriver;
import org.terraedu.util.wrappers.WSubsystem;
import org.terraedu.util.wrappers.positional.PMotor;
import org.terraedu.util.wrappers.positional.PServo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Robot extends WSubsystem {

    //#region Drive
    public DcMotorEx frontLeftMotor;
    public DcMotorEx frontRightMotor;
    public DcMotorEx backLeftMotor;
    public DcMotorEx backRightMotor;
    //#endregion

    //#region Hang
    public CRServo hangServoLeft, hangServoRight;
    //#endregion

    //#region Intake

    public Servo intakeLinkage, latch;
    public DcMotorEx intakeMotor, extendo;

    public RevColorSensorV3 colorIntake;
    public Motor.Encoder extendoEncoder;

    //#endregion

    //#region Outtake
    public Servo claw, pivot, outtakeLinkage, armLeft, armRight;
    public DcMotorEx liftLeft, liftRight;
    public RevColorSensorV3 colorDeposit;
    public Set<DcMotorEx> liftMotors = new HashSet<>();

    public Motor.Encoder liftEncoder;

    //#endregion

    //#region Robot Hardware

    private static Robot INSTANCE = null;
    public boolean enabled;

    List<LynxModule> allHubs;
    public LynxModule CONTROL_HUB;

    public double voltage = 0.0;
    public double idealVoltage = 13.7; // Change this as per tuning preferences
    private ElapsedTime voltageTimer;

    //#endregion

    //#region Hardware Objects
    public HardwareMap hardwareMap;
    private Telemetry telemetry;
    //#endregion

    public MecanumDrive drive;
    public Hang hang;
    public Intake intake;
    public Deposit deposit;
    public PinpointLocalizer localizer;


    public Alliance alliance = Alliance.BLUE;

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public static Robot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Robot();
        }
        INSTANCE.enabled = true;
        return INSTANCE;
    }

    public void init(HardwareMap hardwareMap, Telemetry telemetry, Alliance alliance) {
        this.hardwareMap = hardwareMap;
        this.alliance = alliance;

        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if (hub.isParent() && LynxConstants.isEmbeddedSerialNumber(hub.getSerialNumber())) {
                CONTROL_HUB = hub;
            }
        }

        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        voltageTimer = new ElapsedTime();

        //#region Hardware Mapping

        // --= Drivetrain =-- //

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "fl");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "fr");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "bl");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "br");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // --= Hang =-- //

        hangServoLeft = hardwareMap.get(CRServo.class, "hangl");
        hangServoRight = hardwareMap.get(CRServo.class, "hangr");

        hangServoLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        hangServoRight.setDirection(DcMotorSimple.Direction.FORWARD);

        // --= Intake =-- //
        intakeLinkage = new PServo(hardwareMap.get(Servo.class, "ilink"));
        latch = new PServo(hardwareMap.get(Servo.class, "latch"));
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        extendo = new PMotor(hardwareMap.get(DcMotorEx.class, "extendo"));
        extendoEncoder = new Motor(hardwareMap, "br").encoder;

        extendo.setDirection(DcMotorSimple.Direction.REVERSE);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        colorIntake = hardwareMap.get(RevColorSensorV3.class, "intakeColor");

        // --= Outtake =-- //

        armLeft = new PServo(hardwareMap.get(Servo.class, "arml"));
        armRight = new PServo(hardwareMap.get(Servo.class, "armr"));
        outtakeLinkage = new PServo(hardwareMap.get(Servo.class, "linkage"));
        pivot = new PServo(hardwareMap.get(Servo.class, "pivot"));
        claw = new PServo(hardwareMap.get(Servo.class, "claw"));

        liftLeft = new PMotor(hardwareMap.get(DcMotorEx.class, "lil"));
        liftRight = new PMotor(hardwareMap.get(DcMotorEx.class, "lir"));

        claw.setDirection(Servo.Direction.REVERSE);
        pivot.setDirection(Servo.Direction.REVERSE);
        outtakeLinkage.setDirection(Servo.Direction.REVERSE);
        armRight.setDirection(Servo.Direction.REVERSE);
        armLeft.setDirection(Servo.Direction.FORWARD);

        liftLeft.setDirection(DcMotorSimple.Direction.FORWARD); // TODO FIX THIS
        liftRight.setDirection(DcMotorSimple.Direction.REVERSE);

        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

//        colorDeposit = hardwareMap.get(RevColorSensorV3.class, "depositColor");
        liftEncoder = new Motor(hardwareMap, "fl").encoder;
        liftEncoder.setDirection(Motor.Direction.FORWARD);

        liftMotors.add(liftLeft);
        liftMotors.add(liftRight);

        //#endregion

        drive = new MecanumDrive(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
        hang = new Hang(this);
        intake = new Intake(this);
        deposit = new Deposit(this);
        localizer = new PinpointLocalizer(hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint"));
        localizer.setOffsets(38.1, 171.45);
        //-171.45 38.1
        localizer.setResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        localizer.setDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.FORWARD);


        voltage = hardwareMap.voltageSensor.iterator().next().getVoltage();

        if (Globals.SHOULD_LOG) {
            Terrabats.LOGGER.debug("Initialized Robot");
        }
    }

    @Override
    public void periodic() {
        long startIntake = System.nanoTime();
        intake.periodic();
        long endIntake = System.nanoTime();
        TimingLogger.log("periodic", "intake", (endIntake - startIntake) / 1_000_000.0);

        long startDeposit = System.nanoTime();
        deposit.periodic();
        long endDeposit = System.nanoTime();
        TimingLogger.log("periodic", "deposit", (endDeposit - startDeposit) / 1_000_000.0);

        long startHang = System.nanoTime();
        hang.periodic();
        long endHang = System.nanoTime();
        TimingLogger.log("periodic", "hang", (endHang - startHang) / 1_000_000.0);

        long startDrive = System.nanoTime();
        drive.periodic();
        long endDrive = System.nanoTime();
        TimingLogger.log("periodic", "drive", (endDrive - startDrive) / 1_000_000.0);

        long startLocalizer = System.nanoTime();
        localizer.periodic();
        long endLocalizer = System.nanoTime();
        TimingLogger.log("periodic", "localizer", (endLocalizer - startLocalizer) / 1_000_000.0);

        if (voltageTimer.seconds() > 5) {
            voltageTimer.reset();
            voltage = hardwareMap.voltageSensor.iterator().next().getVoltage();
        }
    }

    @Override
    public void read() {
        long startIntake = System.nanoTime();
        intake.read();
        TimingLogger.log("read", "intake", (System.nanoTime() - startIntake) / 1_000_000.0);

        long startDeposit = System.nanoTime();
        deposit.read();
        TimingLogger.log("read", "deposit", (System.nanoTime() - startDeposit) / 1_000_000.0);

        if (Globals.AUTO) {
            long startLocalizer = System.nanoTime();
            localizer.read();
            TimingLogger.log("read", "localizer", (System.nanoTime() - startLocalizer) / 1_000_000.0);
        }
    }

    @Override
    public void write() {
        long startHang = System.nanoTime();
        hang.write();
        TimingLogger.log("write", "hang", (System.nanoTime() - startHang) / 1_000_000.0);

        long startIntake = System.nanoTime();
        intake.write();
        TimingLogger.log("write", "intake", (System.nanoTime() - startIntake) / 1_000_000.0);

        long startDeposit = System.nanoTime();
        deposit.write();
        TimingLogger.log("write", "deposit", (System.nanoTime() - startDeposit) / 1_000_000.0);
    }

    public void clearBulkCache() {
        allHubs.forEach(LynxModule::clearBulkCache);
    }

    @Override
    public void reset() {
        localizer.reset();
    }
    public void hardwarereset() {
        liftEncoder.reset();
        extendoEncoder.reset();

    }
}
