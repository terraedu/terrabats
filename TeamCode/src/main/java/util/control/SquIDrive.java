package util.control;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import subsystem.Drive;

public class SquIDrive {
    DcMotor fr, br, fl, bl;

    double xTarget;
    double yTarget;
    double hTarget;

    double xCurrent;
    double yCurrent;
    double hCurrent;

    double x;
    double y;
    double h;
    double angle;

    private final SquIDController xPID = new SquIDController();
    private final SquIDController yPID = new SquIDController();
    private final SquIDController hPID = new SquIDController();

    public GoBildaPinpointDriver odo;


    public void init(){

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");

        //TODO FIX OFFSETS
        odo.setOffsets(-32, 55);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.resetPosAndIMU();


        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



    }

    private double lastAngle = 0;
    private double lastX = 0;
    private double lastY = 0;


    int counterH;
    int counterX;
    int counterY;

    private double nanAngle(double h) {
        if (Double.isNaN(h)) {
            counterH++;
            return lastAngle;
        } else if (counterH == 50) {
            h = 0;
            return h;
        } else {
            lastAngle = h;
            return h;
        }
    }
    private double nanX(double x) {
        if (Double.isNaN(x)) {
            counterX++;
            return lastX;
        } else if (counterX == 50) {
            h = 0;
            return h;
        } else {
            lastX = x;
            return x;
        }
    }
    private double nanY(double y) {
        if (Double.isNaN(y)) {
            counterY++;
            return lastY;
        } else if (counterY == 50) {
            h = 0;
            return h;
        } else {
            lastY = y;
            return y;
        }
    }

    public void goTo(double xTarget, double yTarget, double hTarget){

        this.xTarget = xTarget;
        this.yTarget = yTarget;
        this.hTarget = hTarget;
    }


    public void update(){
        odo.update();
        Pose2D pos = odo.getPosition();

        hCurrent = nanAngle(pos.getHeading(AngleUnit.DEGREES));
        xCurrent = nanX((pos.getY(DistanceUnit.MM)/10));
        yCurrent = nanY((pos.getX(DistanceUnit.MM)/10));

        x = xPID.calculate(xTarget, xCurrent);
        y = yPID.calculate(yTarget, yCurrent);
        h = hPID.calculateH(hTarget, hCurrent);

        double xRot = x * Math.cos(angle) - y * Math.sin(angle);
        double yRot = y * Math.cos(angle) - y * Math.sin(angle);

        fl.setPower(xRot + yRot + h);
        bl.setPower(xRot - yRot + h);
        fr.setPower(xRot - yRot - h);
        br.setPower(xRot + yRot - h);


    }
}
