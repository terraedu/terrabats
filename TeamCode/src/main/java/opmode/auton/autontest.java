package opmode.auton;

import static util.purepursuit.Executor.followCurve;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;

import subsystem.Drive;
import util.purepursuit.CurvePoint;
import util.purepursuit.Executor;

public class autontest extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {

        ArrayList<CurvePoint> allPoints = new ArrayList<>();
        allPoints.add(new CurvePoint(0,0,1.0,1.0,50, Math.toRadians(50), 1));
        allPoints.add(new CurvePoint(180,180,1.0,1.0,50, Math.toRadians(50), 1));
        allPoints.add(new CurvePoint(200,180,1.0,1.0,50, Math.toRadians(50), 1));
        allPoints.add(new CurvePoint(280,50,1.0,1.0,50, Math.toRadians(50), 1));
        allPoints.add(new CurvePoint(180,0,1.0,1.0,50, Math.toRadians(50), 1));


        followCurve(allPoints, Math.toRadians(90));






    }
}
