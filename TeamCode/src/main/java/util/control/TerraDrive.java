package util.control;



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

    double xDist;
    double yDist;

    double x;
    double y;
    double h;
    double angle;

    private final PDFDrive xPDF = new PDFDrive(0.0, 0.0,0);
    private final PDFDrive yPDF = new PDFDrive(0.0, 0.0,0);
    private final PDFDrive hPDF = new PDFDrive(0, 0,0);



    public void init(HardwareMap hardwareMap){
        fl  = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        bl  = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");

        //TODO FIX OFFSETS
        odo.setOffsets(0, 0);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
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
        xCurrent = nanX((pos.getY(DistanceUnit.CM)));
        yCurrent = nanY((pos.getX(DistanceUnit.CM)));

        x = xPDF.calculate(xTarget, xCurrent);
        y = yPDF.calculate(yTarget, yCurrent);
        h = hPDF.calculateH(hTarget, hCurrent);
        xDist = xTarget-xCurrent;
        yDist = yTarget-yCurrent;
        angle = Math.atan2(xDist, yDist);

        double xRot = x * Math.cos(angle) - y * Math.sin(angle);
        double yRot = x * Math.sin(angle) - y * Math.cos(angle);

        fl.setPower(xRot - yRot - h);
        bl.setPower(xRot + yRot - h);
        fr.setPower(xRot + yRot + h);
        br.setPower(xRot - yRot + h);


    }

    public double getXPower(){
        return x;
    }
    public double getYPower(){
        return y;
    }
    public double getHPower(){
        return h;
    }

    public double getXRot(){
        return getXRot();
    }


    public void stop(){
        fl.setPower(0);
        bl.setPower(0);
        fr.setPower(0);
        br.setPower(0);
    }
}
