package subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.openftc.easyopencv.OpenCvPipeline;

import util.control.PDFController;
import wrappers.positional.PMotor;
import wrappers.positional.PServo;

public class Outtake extends OpMode {
    PServo arml, armr, linkage, pivot, claw;
    PMotor liftl, liftr;

    double oTarget;


    private final PDFController oPDF = new PDFController(0.0, 0.0,0);


    public enum outtakeState{
        init,
    }

    outtakeState currentOuttakeState;

    public void setState(outtakeState newState){
        this.currentOuttakeState = newState;
    }

    @Override
    public void init() {
        arml = hardwareMap.get(PServo.class, "arml");
        armr = hardwareMap.get(PServo.class, "armr");
        linkage = hardwareMap.get(PServo.class, "linkage");
        pivot = hardwareMap.get(PServo.class, "pivot");
        claw = hardwareMap.get(PServo.class, "claw");

        liftl = hardwareMap.get(PMotor.class, "lil");
        liftr = hardwareMap.get(PMotor.class, "lir");

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

    @Override
    public void loop() {

    }


    public void goTo(double target){
        this.oTarget = target;
    }

    public void oUpdate(){
        double out = oPDF.calculate(oTarget, liftl.getCurrentPosition());
        liftl.setPower(out);
        liftr.setPower(out);

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
