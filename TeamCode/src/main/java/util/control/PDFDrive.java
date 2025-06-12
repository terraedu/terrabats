package util.control;

import static mathutil.MathFunctions.angleWrap;

import com.qualcomm.robotcore.util.ElapsedTime;

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


    ElapsedTime time = new ElapsedTime();


    public PDFDrive(double Kp, double Kd, double Kf) {
        this.Kp = Kp;
        this.Kd = Kd;
        //TODO LINEAR EQUATION TO CALCULATE FF VALUE (FIGURE THIS OUT)
        this.Kf = Kf;
    }


    public double calculate(double setpoint, double current) {

        error = setpoint - current;
        der = (error - lError) / time.seconds();

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
