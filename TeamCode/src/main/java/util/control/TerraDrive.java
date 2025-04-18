package util.control;



import com.arcrobotics.ftclib.controller.PDController;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class TerraDrive {
    public DcMotor fr, br, fl, bl;
    public GoBildaPinpointDriver odo;

    double xTarget;
    double yTarget;
    double hTarget;

    double xCurrent;
    double yCurrent;
    double hCurrent;

    double x;
    double y;
    double h;
    double xDist;
    double yDist;
    double angle;

    public static PDTroller xPID = new PDTroller(0.1,0);
    public static PDTroller yPID = new PDTroller(0.1,0);

    public static PDTroller hPID = new PDTroller(0.2 ,0);




    public void init(HardwareMap hardwareMap){
        fl  = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        bl  = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");

        odo.setOffsets(-50, 139);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.resetPosAndIMU();



        //TODO FIX THIS FOR CURRENT BOT IJAVVE ONE IS WEIRD

        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);

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
        angle = Math.atan2(yDist, xDist);


        double xRot = x * Math.cos(angle) - y * Math.sin(angle);
        double yRot = x * Math.cos(angle) - y * Math.sin(angle);
        h = hPID.calculateH(hTarget, hCurrent);


        //TODO FIX THIS FOR CURRENT BOT IJAVVE ONE IS WEIRD
        fl.setPower(xRot + yRot + h);
        bl.setPower(xRot - yRot + h);
        fr.setPower(xRot - yRot - h);
        br.setPower(xRot + yRot - h);


    }

    public void stop(){
        fl.setPower(0);
        bl.setPower(0);
        fr.setPower(0);
        br.setPower(0);
    }
}
