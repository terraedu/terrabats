package opmode;

import static subsystem.Hang.hangState.in;
import static subsystem.Hang.hangState.out;
import static subsystem.Hang.hangState.stationary;
import static subsystem.Intake.intakeState.hover;
import static subsystem.Intake.intakeState.init;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import gamepad.GamepadHandler;
import subsystem.Drive;
import subsystem.Hang;
import subsystem.Intake;
import subsystem.Outtake;

@TeleOp(name="\uD83D\uDE08", group="teleop")
public class TerraBlue extends LinearOpMode {

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

    @Override
    public void runOpMode() throws InterruptedException {
        /**
         * Create Hardware
         */

        Drive drive = new Drive();
        Intake intake = new Intake();
        Outtake outtake = new Outtake();
        Hang hang = new Hang();
        ElapsedTime time = new ElapsedTime();
        GamepadHandler gph1 = new GamepadHandler(gamepad1);
        GamepadHandler gph2 = new GamepadHandler(gamepad2);

        /**
         * Init Hardware
         */
        drive.init(hardwareMap);
        hang.init(hardwareMap);
        outtake.init(hardwareMap);
        intake.init(hardwareMap);
        time.reset();

        /**
         * Set States
         */
        intake.setState(init);
        hang.setState(stationary);

        /**
         * Set Modes
         */
        hStatus = hangStatus.stationary;
        status = robotStatus.driving;

        waitForStart();

        while (opModeIsActive()) {

            /**
             * OUTTAKE
             */
            if(gph1.left_trigger && status == robotStatus.driving){
                outtake.goTo(400);
            }


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
             * the reason we use normal stock sdk gamepad 1 here is because we need it to be a toggle and i cant be bothered
             */
            while(gamepad1.dpad_up){ hang.startHang();}
            while(gamepad1.dpad_down){hang.reverseHang();}
            while(gamepad1.dpad_right){hang.stopHang();}

            outtake.setPower(gamepad2.left_stick_y);
            drive.move(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

//        intake.update();
            telemetry.addData("hangMode", hStatus);
            telemetry.addData("hangState", hang.currentstate);
            telemetry.addData("servoPower", hang.hangl.getPower());
            telemetry.addData("servorPower", hang.hangr.getPower());
            telemetry.addData("liftl", -(outtake.liftl.getCurrentPosition()));
//            telemetry.addData("liftr", outtake.liftr.getCurrentPosition());


//            outtake.oUpdate();
            gph1.update();
            gph2.update();
            telemetry.update();
        }
    }
}