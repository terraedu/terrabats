package opmode.auton;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import util.control.SquIDrive;


public class autontest extends LinearOpMode {

    SquIDrive drive = new SquIDrive();

    @Override
    public void runOpMode() throws InterruptedException {

        drive.init();

        drive.goTo(1,2,3);

        while(opModeIsActive()){

            drive.update();

        }



    }

}
