package opmode;

import static subsystem.Intake.intakeState.hover;
import static subsystem.Outtake.outtakeState.init;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import gamepad.GamepadHandler;
import subsystem.Drive;
import subsystem.Intake;
import subsystem.Outtake;

@TeleOp(name="\uD83D\uDE08", group="teleop") // 😈 is hilarious
public class TerraBlue extends OpMode {

    enum robotStatus {
        driving,
        intaking,
        basketing,
        specimining
    }

    robotStatus status;
    GamepadHandler gph1 = new GamepadHandler(gamepad1);
    GamepadHandler gph2 = new GamepadHandler(gamepad2);
    Drive drive = new Drive();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();

    ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {
        // init hardware
        time.reset();

        // set states
        intake.setState(Intake.intakeState.init);

        // set modes
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
        // have separate if-else loops for each subsystems?
        // consider as intake if-else loop ?
        if (gph1.right_trigger && status == robotStatus.driving) {
            time.reset();
            intake.goTo(0);

            if (time.seconds() == 1) {
                intake.setState(hover);
            }
            status = robotStatus.intaking;
        } else if (gph1.right_trigger && status == robotStatus.intaking) {
            // go back to init
        } else if (gph1.right_trigger && status == robotStatus.basketing) {
            // uhh idk do smth
        }

        // consider as outtake if-else loop ?
        if (gph1.right_bumper && status == robotStatus.driving) {
            // go up to high basket ? do smth
        } else if (gph1.right_bumper && status == robotStatus.basketing) {
            outtake.setState(init);
        }

        drive.move(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        outtake.update();
        intake.update();
        intake.iupdate();
        gph1.update();
        gph2.update();
    }
}