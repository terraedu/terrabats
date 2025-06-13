package util.control;

import static mathutil.MathFunctions.angleWrap;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class PDFDrive {
    double Kp;
    double Kd;
    double Kf;

    double hError;
    double error;
    double lError;
    double lhError;
    double out;


    double der;

    TerraDrive odo = new TerraDrive();

    ElapsedTime time = new ElapsedTime();


    public PDFDrive(double Kp, double Kd) {
        this.Kp = Kp;
        this.Kd = Kd;
        //TODO LINEAR EQUATION TO CALCULATE FF VALUE (FIGURE THIS OUT)
    }


    public double calculate(double setpoint, double current) {

        double vx = odo.getVelocity().getX(DistanceUnit.MM);
        double vy = odo.getVelocity().getY(DistanceUnit.MM);

        double m = 0.5;
        double x = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2)); // magnitude of velocity vector
        double b = 0;


        error = setpoint - current;
        der = (error - lError) / time.seconds();
        Kf = (m * x) + b;

        out = (Kp * error) + (Kd * der) + Kf;


        lError = error;
        time.reset();

        return out;



    }

    public double calculateH(double setpoint, double current) {

       hError = Math.toDegrees(angleWrap(Math.toRadians(setpoint - current)));

       der = (hError - lhError) / time.seconds();
       out = (Kp * error) + (Kd * der) + Kf;

       lhError = hError;
       time.reset();

        return out;



    }
}
