package teleop;

import static global.General.gph1;
import static global.General.gph2;
import static global.General.log;
import static global.General.voltageScale;
import static global.Modes.RobotStatus.DRIVING;
import static global.Modes.RobotStatus.INTAKING;
import static global.Modes.RobotStatus.SAMPLE;
import static global.Modes.RobotStatus.SPECIMEN;
import static global.Modes.TeleStatus.REDA;
import static teleutil.button.Button.A;
import static teleutil.button.Button.B;
import static teleutil.button.Button.DPAD_DOWN;
import static teleutil.button.Button.DPAD_LEFT;
import static teleutil.button.Button.DPAD_RIGHT;
import static teleutil.button.Button.DPAD_UP;
import static teleutil.button.Button.LEFT_BUMPER;
import static teleutil.button.Button.LEFT_STICK_BUTTON;
import static teleutil.button.Button.LEFT_TRIGGER;
import static teleutil.button.Button.RIGHT_BUMPER;
import static teleutil.button.Button.RIGHT_STICK_BUTTON;
import static teleutil.button.Button.RIGHT_TRIGGER;
import static teleutil.button.Button.X;
import static teleutil.button.Button.Y;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TerraRedSingle", group = "TeleOp")
public class TerraRedSingle extends Tele {

    @Override
    public void initTele() {
        teleStatus.set(REDA);
        voltageScale = 1;

        Tele auto = this;
        auto.scan(true);

        intake.sampleScanner = sampleScanner;

        gph1.linkWithCancel(RIGHT_BUMPER, robotStatus.isMode(SPECIMEN), upSpecimen, robotStatus.isMode(DRIVING), high, down);
        gph1.linkWithCancel(RIGHT_TRIGGER, robotStatus.isMode(DRIVING), intakeOut, robotStatus.isMode(INTAKING), intakeIn, DRIVINGMODE);
        gph1.linkWithCancel(LEFT_TRIGGER, robotStatus.isMode(DRIVING), zestyFlick, updatePipeline);
        gph1.linkWithCancel(LEFT_BUMPER, robotStatus.isMode(DRIVING), specimenReady, grabSpecimen);
        gph1.link(DPAD_LEFT, intake::turretHorizontal);
        gph1.link(DPAD_UP, intake::turretReset);

        gph2.linkWithCancel(LEFT_TRIGGER, robotStatus.isMode(DRIVING), zestyFlick, ()-> intake.updatePipeline(20));
        gph2.link(RIGHT_TRIGGER, high);

        robotStatus.set(DRIVING);
    }

    @Override
    public void startTele() {intake.moveInit(); outtake.moveInit();}

    @Override
    public void loopTele() {
        drive.newMove(gph1.ly,gph1.rx,gph1.lx);
        if (teleStatus.modeIs(INTAKING)) {extendo.move(gph1.ry);}
        lift.move(gph2.rx);
        extendo.move(gph2.ry);
    }
}


