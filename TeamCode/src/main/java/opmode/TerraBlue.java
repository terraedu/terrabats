package opmode;

import static subsystem.Hang.hangState.in;
import static subsystem.Hang.hangState.out;
import static subsystem.Hang.hangState.stationary;
import static subsystem.Intake.intakeState.hover;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import gamepad.GamepadHandler;
import subsystem.Drive;
import subsystem.Hang;
import subsystem.Intake;
import subsystem.Outtake;

@TeleOp(name="\uD83D\uDE08", group="teleop")
public class TerraBlue extends OpMode {

    enum robotStatus {
        driving,
        intaking,
        placing
    }

    enum hangStatus {
        out,
        stationary,
        in
    }

    hangStatus hStatus;
    robotStatus status;
    GamepadHandler gph1 = new GamepadHandler(gamepad1);
    GamepadHandler gph2 = new GamepadHandler(gamepad2);
    Drive drive = new Drive();
    Intake intake = new Intake();
    Outtake outtake = new Outtake();
    Hang hang = new Hang();

    ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {
        /**
         * Init Hardware
         */
        hang.init();
        outtake.init();
        intake.init();
        time.reset();

        /**
         * Set States
         */
        intake.setState(Intake.intakeState.init);

        /**
         * Set Modes
         */
        hStatus = hangStatus.stationary;
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
        /**
         * INTAKE
         */
        if(gph1.right_trigger && status == robotStatus.driving) {
            time.reset();
            intake.goTo(0);
            if(time.seconds() == 1) {
            intake.setState(hover);
            }
            status = robotStatus.intaking;
        }
        else if (gph1.right_trigger && status == robotStatus.intaking) {
            time.reset();

        }
        else if (gph1.right_trigger && status == robotStatus.placing) {        }

        /**
         * HANG
         */
        if(gph1.dpad_up && status == robotStatus.driving){
            hang.setState(out);
            hStatus = hangStatus.out;
        }else if(gph1.dpad_up && hStatus == hangStatus.out){
            hang.setState(stationary);
            hStatus = hangStatus.stationary;
        }else if(gph1.dpad_down && status == robotStatus.driving){
            hang.setState(in);
            hStatus = hangStatus.in;
        }


        drive.move(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);


//        intake.update();
        hang.update();
        gph1.update();
        gph2.update();

    }
}