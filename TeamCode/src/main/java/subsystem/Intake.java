package subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import util.control.PDFController;
import wrappers.positional.PMotor;
import wrappers.positional.PServo;

public class Intake{
    double itarget;
    double icurrent;

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

    public void init() {
       ilink = hardwareMap.get(PServo.class, "ilink");
       latch = hardwareMap.get(PServo.class, "latch");
       im = hardwareMap.get(PMotor.class, "im");
       extendo = hardwareMap.get(PMotor.class, "extendo");

       im.setDirection(DcMotorSimple.Direction.FORWARD);
       extendo.setDirection(DcMotorSimple.Direction.REVERSE);

       im.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

       ilink.addPosition("init", 0);
       latch.addPosition("init", 0);
    }

    public void moveInit() {
        ilink.setPosition("init");
        ilink.setPosition("init");
    }

    public void goTo(double target){
        this.itarget = target;
    }

    public void iupdate(){
        double out = iPDF.calculate(itarget, extendo.getCurrentPosition());
        extendo.setPower(out);
    }

    public void update(){
        switch(currentIntakeState) {
            case init:
                itarget = 0;
                moveInit();
            break;

            case hover:
                itarget = 0;
                break;

            case grab:
                itarget = 0;
                break;

            default:
                itarget = 0;
                moveInit();
        }
   }
}
