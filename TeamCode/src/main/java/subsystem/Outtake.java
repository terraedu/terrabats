package subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import util.PauseTimer;
import util.control.PDFController;
import wrappers.positional.PMotor;
import wrappers.positional.PServo;

public class Outtake {

    public PServo claw, pivot, linkage, armr, arml;
    public PMotor liftl, liftr;
    ElapsedTime time = new ElapsedTime();
    double oTarget;

    PauseTimer pausetimer = new PauseTimer();

    private final PDFController oPDF = new PDFController(0.0001, 0.0,0);


    public enum outtakeState{
        init,
        grab,
        specimen,
    }

    outtakeState currentOuttakeState;

    public void setState(outtakeState newState){
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


        arml.addPosition("init", .3);
        armr.addPosition("init", .3);
        linkage.addPosition("init", 0);
        pivot.addPosition("init", 0);
        claw.addPosition("init", 0);

        claw.addPosition("grab", 0.4);

        pivot.addPosition("place", 0.15);
        linkage.addPosition("place", 0.55);
        arml.addPosition("place", 1);
        armr.addPosition("place", 1);


    }




    public void goTo(double target){
        this.oTarget = target;
    }

    public void pdfUpdate(){
        double out = -(oPDF.calculate(oTarget, (liftl.getCurrentPosition())));
        liftl.setPower(out);
        liftr.setPower(out);

    }

    public void setPower(double power){
        liftl.setPower(power);
        liftr.setPower(power);
    }

    public void moveInit() {
        arml.setPosition("init");
        armr.setPosition("init");
        linkage.setPosition("init");
        pivot.setPosition("init");
        claw.setPosition("init");
    }

    public void moveGrab() {
        claw.setPosition("grab");
    }

    public void movePlace() {
        armr.setPosition("place");
        arml.setPosition("place");
        pivot.setPosition("place");
    }

    public void moveExtend(){
        linkage.setPosition("place");
    }

    public void update(){
        switch(currentOuttakeState) {
            case init:
                moveInit();
                break;

            case grab:

                moveGrab();
                break;
            case specimen:
                time.reset();
                moveGrab();
                movePlace();
                pausetimer.addPause(1);
                moveExtend();
                break;
        }
    }
}
