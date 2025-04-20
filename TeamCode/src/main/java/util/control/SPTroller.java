package util.control;

import static mathutil.MathFunctions.angleWrap;

import com.qualcomm.robotcore.util.ElapsedTime;

public class SPTroller {
    double p;
    double d;
    double hError;
    double error;
    double lError;
    double lhError;
    double out;


    double der;


    ElapsedTime time = new ElapsedTime();


    public SPTroller(double p, double d) {
        this.p = p;
        this.d = d;
    }

    public double calculate(double setpoint, double current) {

        error = setpoint - current;
        der = (error - lError) / time.seconds();

        out = (p * error) + (d * der);


        lError = error;
        time.reset();

        return out;



    }

    public double calculateH(double setpoint, double current) {

       hError = Math.toDegrees(angleWrap(Math.toRadians(setpoint - current)));

       der = (hError - lhError) / time.seconds();
       out = (p * error) + (d * der);

       lhError = hError;
       time.reset();

        return out;



    }
}
