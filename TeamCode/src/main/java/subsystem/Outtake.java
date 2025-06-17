package subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import util.Pause;
import util.control.PDFController;
import wrappers.positional.PMotor;
import wrappers.positional.PServo;

public class Outtake {

    public PServo claw, pivot, linkage, armr, arml;
    public PMotor liftl, liftr;
    double oTarget;
    double out;

    public Pause sub = new Pause();

    private final PDFController oPDF = new PDFController(0.0000, 0.0000, -0.15);


    public enum outtakeState {
        init,
        grab,
        sample,
        reset,
        neutral

    }

    outtakeState currentOuttakeState;

    public void setState(outtakeState newState) {
        this.currentOuttakeState = newState;
    }

    public void init(HardwareMap hardwareMap) {
        arml = new PServo(hardwareMap.get(Servo.class, "arml"));
        armr = new PServo(hardwareMap.get(Servo.class, "armr"));
        linkage = new PServo(hardwareMap.get(Servo.class, "linkage"));
        pivot = new PServo(hardwareMap.get(Servo.class, "pivot"));
        claw = new PServo(hardwareMap.get(Servo.class, "claw"));

        liftl = new PMotor(hardwareMap.get(DcMotorEx.class, "lil"));
        liftr = new PMotor(hardwareMap.get(DcMotorEx.class, "lir"));

        //TODO set servo directions
        claw.setDirection(Servo.Direction.REVERSE);
        pivot.setDirection(Servo.Direction.REVERSE);
        linkage.setDirection(Servo.Direction.REVERSE);
        armr.setDirection(Servo.Direction.REVERSE);
        arml.setDirection(Servo.Direction.FORWARD);


        liftl.setDirection(DcMotorSimple.Direction.FORWARD);
        liftr.setDirection(DcMotorSimple.Direction.REVERSE);

        liftl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        arml.addPosition("init", .2);
        armr.addPosition("init", .2);
        linkage.addPosition("init", 0);
        pivot.addPosition("init", 0);
        claw.addPosition("init", 0);

        claw.addPosition("grab", 0.4);

        arml.addPosition("transfer", 0);
        armr.addPosition("transfer", 0);

        pivot.addPosition("place", 0.15);
        linkage.addPosition("place", 0.55);
        arml.addPosition("place", .6);
        armr.addPosition("place", .6);


    }


    public void goTo(double target) {
        this.oTarget = target;
    }

    public void pdfUpdate() {
        out = -(oPDF.calculate(oTarget, liftl.getCurrentPosition()));
        liftl.setPower(out);
        liftr.setPower(out);

    }

    public double getOut() {
        return out;
    }

    public void setPower(double power) {
        liftl.setPower(power);
        liftr.setPower(power);
    }

    public void moveInit() {
        arml.setPosition("init");
        armr.setPosition("init");
        linkage.setPosition("init");
        pivot.setPosition("init");
    }

    public void moveGrab() {
        claw.setPosition("grab");
    }

    public void moveRelease() {
        claw.setPosition("init");
    }

    public void movePlace() {
        armr.setPosition("place");
        arml.setPosition("place");
        pivot.setPosition("place");
    }

    public void moveRetract() {
        linkage.setPosition("init");
    }

    public void moveExtend() {
        linkage.setPosition("place");
    }

    public void update() {
        switch (currentOuttakeState) {
            case neutral:
                break;
            case init:
                moveRetract();
                moveInit();
                moveGrab();
                break;

            case grab:

                moveGrab();
                break;
            case sample:

                moveGrab();
                movePlace();
                sub.getTime();
                if (!sub.pause(500)) return;
                moveExtend();
                sub.reset();
                break;
            case reset:
                moveRelease();
                moveRetract();
                if (!sub.pause(500)) return;
                moveInit();
                sub.reset();
                break;


        }
    }
}
