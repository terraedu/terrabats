package subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.openftc.easyopencv.OpenCvPipeline;

import util.control.PDFController;
import wrappers.positional.PMotor;
import wrappers.positional.PServo;

public class Outtake {

    PServo arml, armr, linkage, pivot, claw;
    public PMotor liftl, liftr;

    double oTarget;


    private final PDFController oPDF = new PDFController(0.0001, 0.0,0);


    public enum outtakeState{
        init,
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
        liftl.setDirection(DcMotorSimple.Direction.FORWARD);
        liftr.setDirection(DcMotorSimple.Direction.REVERSE);

        liftl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        arml.addPosition("init", 0);
        armr.addPosition("init", 0);
        linkage.addPosition("init", 0);
        pivot.addPosition("init", 0);
        claw.addPosition("init", 0);
    }




    public void goTo(double target){
        this.oTarget = target;
    }

    public void oUpdate(){
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

    public void update(){
        switch(currentOuttakeState) {
            case init:
                moveInit();
                break;

            default:
                moveInit();
        }
    }
}
