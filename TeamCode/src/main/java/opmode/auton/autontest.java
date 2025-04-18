package opmode.auton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import util.control.TerraDrive;

@Autonomous(name="auton", group="Autonomous")
public class autontest extends LinearOpMode {






    TerraDrive drive = new TerraDrive();


    @Override
    public void runOpMode() throws InterruptedException {

        drive.init(hardwareMap);

        waitForStart();



        drive.goTo(0,0,90);

        while(opModeIsActive()){

            drive.update();

        }

        if(isStopRequested()){
            drive.stop();
        }



    }

}
