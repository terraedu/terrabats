package subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import wrappers.continuous.CServo;

public class Hang {
    CServo hangl, hangr;


    public enum hangState{
        stationary,
        out,
        in
    }

    hangState currentHangState;

    public void init() {
        hangl = hardwareMap.get(CServo.class, "hangl");
        hangr = hardwareMap.get(CServo.class, "hangr");


        hangl.setDirection(DcMotorSimple.Direction.FORWARD);
        hangr.setDirection(DcMotorSimple.Direction.REVERSE);
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

    public void update(){

        switch(currentHangState) {
            case stationary:
                stopHang();
                break;

            case out:
                startHang();
                break;

            case in:
                reverseHang();
                break;

            default:
                stopHang();
        }

    }
}
