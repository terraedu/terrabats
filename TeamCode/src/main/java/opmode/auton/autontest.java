package opmode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;

import util.control.TerraDrive;
import util.purepursuit.CurvePoint;
import util.purepursuit.Executor;
@Autonomous(name="ohadusdfgy", group="auto")
public class autontest extends LinearOpMode {
    TerraDrive drive = new TerraDrive();

    @Override
    public void runOpMode() throws InterruptedException {

        drive.init(hardwareMap);


        waitForStart();



            drive.goTo(0,10,0);


        while(opModeIsActive()){
            drive.update();
            telemetry.addData("Position", drive.odo.getPosition());
            telemetry.addData("XPower", drive.getXPower());
            telemetry.addData("YPower", drive.getYPower());
            telemetry.addData("HPower", drive.getHPower());


            telemetry.update();


        }

        if(isStopRequested()){
            drive.stop();
        }


    }

}
