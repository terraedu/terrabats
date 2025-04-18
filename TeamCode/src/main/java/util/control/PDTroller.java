package util.control;

import static mathutil.MathFunctions.angleWrap;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PDTroller {
    double p;
    double d;

    double error;

    double derivative;


    double hError;

    double out;

    double lastError = 0;

    ElapsedTime timer = new ElapsedTime();

    public PDTroller(double p, double d) {

        this.p = p;
        this.d = d;
    }


    public double calculate(double setpoint, double current) {

        error = setpoint - current;

        derivative = (error - lastError) / timer.seconds();

        out = (p * error) + (d * derivative);



        timer.reset();

        lastError = error;
        return out;



    }

    public double calculateH(double setpoint, double current) {

       hError = Math.toDegrees(angleWrap(Math.toRadians(setpoint - current)));

       derivative = (hError - lastError) / timer.seconds();

       out = (p*error) + (d*derivative);

       timer.reset();
       lastError = hError;
        return out;



    }
}
