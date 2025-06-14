package subsystem;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import util.control.PDFController;
import wrappers.positional.PMotor;
import wrappers.positional.PServo;

public class Intake {
    double iTarget;

    private final PDFController iPDF = new PDFController(0.0, 0.0,0);


    PServo ilink, latch;
    PMotor im, extendo;

    public enum intakeState{
        init,
        grab,
        hover
    };

    intakeState currentIntakeState;

    public void setState(intakeState newState){
        this.currentIntakeState = newState;
    }

    double setActionTime = 1;


    public void init(HardwareMap hardwareMap) {
//       ilink = new PServo(hardwareMap.get(Servo.class, "ilink"));
//        latch = new PServo(hardwareMap.get(PServo.class, "latch"));
//        im = new PMotor(hardwareMap.get(DcMotorEx.class, "im"));
        extendo = new PMotor(hardwareMap.get(DcMotorEx.class, "extendo"));

        //TODO set servo directions
//        im.setDirection(DcMotorSimple.Direction.FORWARD);
//       extendo.setDirection(DcMotorSimple.Direction.REVERSE);
//
//       im.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//       extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//       ilink.addPosition("init", 0);
//       latch.addPosition("init", 0);
    }

    public void moveInit() {
//        ilink.setPosition("init");
//        ilink.setPosition("init");
    }

    public void goTo(double target){
        this.iTarget = target;
    }

    public void pdfUpdate(){
        double out = iPDF.calculate(iTarget, extendo.getCurrentPosition());
        extendo.setPower(out);
    }

    public void update(){
        switch(currentIntakeState) {
            case init:
                moveInit();
            break;

            case hover:
                break;

            case grab:
                break;

            default:
                moveInit();
        }
   }
}
