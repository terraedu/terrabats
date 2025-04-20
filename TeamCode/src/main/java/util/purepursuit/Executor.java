package util.purepursuit;

import static mathutil.MathFunctions.angleWrap;
import static mathutil.MathFunctions.lineCircleIntersection;
import static util.purepursuit.Constants.movement_heading;
import static util.purepursuit.Constants.movement_x;
import static util.purepursuit.Constants.movement_y;
import static util.purepursuit.Robot.worldAngleRad;
import static util.purepursuit.Robot.worldXPosition;
import static util.purepursuit.Robot.worldYPosition;

import com.qualcomm.robotcore.util.Range;

import org.opencv.core.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mathutil.MathFunctions;

public class Executor {

    public static void followCurve(ArrayList<CurvePoint> allPoints, double followAngle){
//        for(int i = 0; i < allPoints.size() - 1; i++)
        CurvePoint followMe = getFollowPointPath(allPoints, new Point(worldXPosition, worldYPosition), allPoints.get(0).followDist);

        goToPosition(followMe.x, followMe.y, followMe.speed, followAngle, followMe.headingSpeed);

    }

    public static CurvePoint getFollowPointPath(ArrayList<CurvePoint> pathPoints, Point robotPos, double followRadius){

        CurvePoint followMe = new CurvePoint(pathPoints.get(0));

        for(int i = 0; i < pathPoints.size() - 1; i ++){

            CurvePoint startLine = pathPoints.get(i);
            CurvePoint endLine = pathPoints.get(i+1);

            ArrayList<Point> intersections = lineCircleIntersection(robotPos, followRadius, startLine.toPoint(), endLine.toPoint());

            double closestAngle = 10000000;

            for(Point thisIntersection : intersections){

                double angle = Math.atan2(thisIntersection.y - worldYPosition, thisIntersection.x - worldXPosition);
                double deltaAngle = Math.abs(MathFunctions.angleWrap(angle - worldAngleRad));

                if(deltaAngle < closestAngle){

                    closestAngle = deltaAngle;
                    followMe.setPoint(thisIntersection);


                }

            }

        }
        return followMe;

    }



    public static void goToPosition(double x, double y, double h, double speed, double headingSpeed){

        double distanceToTarget = Math.hypot(x-worldXPosition, y-worldYPosition);

        double absoluteAngleToTarget = Math.atan2(y - worldYPosition, x-worldXPosition);

        double relativeAngleToPoint = angleWrap(absoluteAngleToTarget - (worldAngleRad - Math.toRadians(90)));

        double relativeXToPoint = Math.cos(relativeAngleToPoint) * distanceToTarget;
        double relativeYToPoint = Math.sin(relativeAngleToPoint) * distanceToTarget;

        double movementXPower = relativeXToPoint / (Math.abs(relativeXToPoint)) + Math.abs(relativeYToPoint);
        double movementYPower = relativeYToPoint / (Math.abs(relativeXToPoint)) + Math.abs(relativeYToPoint);

        movement_x = movementXPower + speed;
        movement_y = movementYPower + speed;

        double relativeHeadingAngle = relativeAngleToPoint - Math.toRadians(180) - h;
        movement_heading = Range.clip(relativeHeadingAngle/Math.toRadians(30), -1,1) * headingSpeed;

        if(distanceToTarget < 10) {
            movement_heading = 0;
        }

    }

}
