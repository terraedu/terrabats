
package teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static global.General.gph1;
import static global.General.gph2;
import static global.General.voltageScale;
import static global.Modes.RobotStatus.DRIVING;
import static global.Modes.RobotStatus.INTAKING;
import static global.Modes.RobotStatus.SAMPLE;
import static global.Modes.RobotStatus.SPECIMEN;
import static global.Modes.TeleStatus.BLUEA;
import static global.Modes.TeleStatus.REDA;
import static teleutil.button.Button.DPAD_DOWN;
import static teleutil.button.Button.DPAD_LEFT;
import static teleutil.button.Button.DPAD_UP;
import static teleutil.button.Button.LEFT_BUMPER;
import static teleutil.button.Button.LEFT_STICK_BUTTON;
import static teleutil.button.Button.LEFT_TRIGGER;
import static teleutil.button.Button.RIGHT_BUMPER;
import static teleutil.button.Button.RIGHT_TRIGGER;

@TeleOp(name = "TerraBlue", group = "TeleOp")
public class TerraBlue extends Tele {

    @Override
    public void initTele() {
        teleStatus.set(BLUEA);
        voltageScale = 1;

        Tele auto = this;
        auto.scan(true);

//        intake.yoloScanner = (YoloScanner) yoloScanner;
//        intake.sampleScanner = sampleScanner;

        gph1.link(RIGHT_TRIGGER, ()-> extendo.liftAdjust(5));
        gph1.link(LEFT_TRIGGER, ()-> extendo.liftAdjust(-5));
        gph1.link(LEFT_BUMPER, intake::turretLeft);
        gph1.link(RIGHT_BUMPER, intake::turretRight);
        gph1.link(DPAD_LEFT, intake::turretHorizontal);
        gph1.link(DPAD_UP, intake::turretReset);
        gph1.link(DPAD_DOWN, () -> intake.updatePipeline(20));

        gph2.linkWithCancel(RIGHT_TRIGGER, robotStatus.isMode(DRIVING), intakeOut, robotStatus.isMode(INTAKING), intakeIn, changeDriving);
        gph2.link(LEFT_TRIGGER, switcharoo);
        gph2.linkWithCancel(RIGHT_BUMPER, robotStatus.isMode(SPECIMEN), upSpecimen, robotStatus.isMode(SAMPLE), high, down);
        gph2.linkWithCancel(LEFT_BUMPER, robotStatus.isMode(DRIVING), specimenReady, grabSpecimen);
        gph2.link(LEFT_STICK_BUTTON, zestyFlick);

        robotStatus.set(DRIVING);
    }

    @Override
    public void startTele() {intake.moveInit(); outtake.moveInit();}

    @Override
    public void loopTele() {
        drive.newMove(gph1.ly,gph1.rx,gph1.lx);
        if (teleStatus.modeIs(INTAKING)) {extendo.move(gph1.ry);}
    }
}
