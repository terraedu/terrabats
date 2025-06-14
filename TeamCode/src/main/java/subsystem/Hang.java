package subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import wrappers.continuous.CServo;

public class Hang {
    public CRServo hangl;
    public CRServo hangr;


    public enum hangState{
        stationary,
        out,
        in
    }

    public hangState currentHangState;

    public void init(HardwareMap hardwareMap) {

        hangl = hardwareMap.get(CRServo.class, "hangl");
        hangr = hardwareMap.get(CRServo.class, "hangr");


        hangl.setDirection(DcMotorSimple.Direction.FORWARD);
        hangr.setDirection(DcMotorSimple.Direction.FORWARD);
        hangr.setPower(0);
        hangl.setPower(0);
    }


    public void setPower(double power){
        hangl.setPower(power);
        hangr.setPower(power);
    }


    public void startHang(){
        hangl.setPower(1);
        hangr.setPower(1);
    }

    public void stopHang(){
        hangl.setPower(0);
        hangr.setPower(0);
    }

    public void reverseHang(){
        hangl.setPower(-1);
        hangr.setPower(-1);
    }


    public void setState(hangState newState){
        this.currentHangState = newState;
    }
    public String currentstate;
    public void update(){

        switch(currentHangState) {
            case stationary:
                stopHang();
                currentstate = "stationary";

                break;
            case out:
                startHang();
                currentstate = "out";


                break;
            case in:
                reverseHang();
                currentstate = "in";


                break;
        }

    }
}
