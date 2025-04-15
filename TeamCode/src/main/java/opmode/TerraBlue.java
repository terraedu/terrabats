package opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import gamepad.GamepadHandler;
import subsystem.Intake;

@TeleOp(name="\uD83D\uDE08", group="teleop")
public class TerraBlue extends OpMode {

    enum robotStatus {
        driving,
        intaking,
        placing
    }

    robotStatus status;
    GamepadHandler gph1 = new GamepadHandler(gamepad1);
    GamepadHandler gph2 = new GamepadHandler(gamepad2);


    ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {
        /**
         * Init Hardware
         */
        Intake intake = new Intake();
        time.reset();

        /**
         * Set States
         */
        intake.setState(Intake.intakeState.init);

        /**
         * Set Modes
         */

        status = robotStatus.driving;

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

        if(gph1.right_trigger && status == robotStatus.driving) {      }
        else if (gph1.right_trigger && status == robotStatus.intaking) {        }
        else if (gph1.right_trigger && status == robotStatus.placing) {        }



        gph1.update();
        gph2.update();

    }
}