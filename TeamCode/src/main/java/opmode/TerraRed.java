package opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import subsystem.Intake;
import subsystem.Outtake;

@TeleOp(name="Red", group="teleop")
public class TerraRed extends OpMode {

    @Override
    public void init() {
        Outtake outtake = new Outtake();
        Intake intake = new Intake();

        telemetry.addData("status", "ready");
        
        outtake.moveInit();
    }

    @Override
    public void loop() {

    }
}