package util.control;

import static mathutil.MathFunctions.angleWrap;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PDFController {
    double Kp;
    double Kd;
    double Kf;

    double error;
    double lError;
    double out;


    double der;


    ElapsedTime time = new ElapsedTime();


    public PDFController(double Kp, double Kd, double Kf) {
        this.Kp = Kp;
        this.Kd = Kd;
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
}
