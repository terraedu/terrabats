package automodules;

import static global.Modes.RobotStatus.DRIVING;
import static global.Modes.RobotStatus.INTAKING;
import static global.Modes.RobotStatus.SAMPLE;
import static global.Modes.RobotStatus.SPECIMEN;
import static global.Modes.robotStatus;

import org.firstinspires.ftc.teamcode.R;

import auton.Auto;
import robot.RobotUser;
import robotparts.RobotPart;

public interface AutoModuleUser extends RobotUser {

    AutoModule DRIVINGMODE = new AutoModule().setStartCode(()-> robotStatus.set(DRIVING));
    AutoModule SPECIMENMODE = new AutoModule().setStartCode(()-> robotStatus.set(SPECIMEN));
    AutoModule SAMPLEMODE = new AutoModule().setStartCode(()-> robotStatus.set(SAMPLE));

    // fat automodules
    AutoModule intakeOut = new AutoModule(
            intake.stageTransfer(0.1),
            intake.turretSwitcharoo(0.1),
            RobotPart.pause(0.15),
            extendo.stageLift(1, 30),
            intake.turretReset(0.1),
            intake.linkEnd(0.1),
            intake.intakeSeek(0.1),
            RobotPart.pause(0.35),
            intake.clawRelease(0.1)
    ).setStartCode(()-> robotStatus.set(INTAKING));

    AutoModule clearIntake = new AutoModule(
            intake.stageTransfer(0.1),
            intake.turretSwitcharoo(0.1),
            RobotPart.pause(0.15),
            extendo.stageLift(1, 15),
            extendo.stageLift(1, -1),
            intake.init(0.1)
    );

    AutoModule intakeIn = new AutoModule(
            intake.linkEnd(0.1),
            intake.intake(0.1).attach(intake.clawRELEASE(0.1)),
            intake.clawGRAB(0.1),
            intake.turretReset(0.1),
//            intake.electricSlide(0.1),
            RobotPart.pause(0.2),
            intake.stageTransfer(0.1),
            intake.clawAdjust(0.1),
            RobotPart.pause(0.2),
//            intake.clawGRAB(0.1),
            intake.linkStart(0.1),
            extendo.stageLift(1, -1).attach(outtake.switcharooReady(0.1)),
            intake.turretSwitcharoo(0.1),
            intake.clawGRAB(0.1),
            intake.armInit(0.1),
            intake.switcharoo(0.1),
            outtake.clawGrab(0.1)
    ).setStartCode(()-> robotStatus.set(DRIVING));

    AutoModule switcharoo = new AutoModule(
            intake.clawAdjust(0.1).attach(intake.zestyFlick(0.1)),
            intake.clawGrab(0.1),
            intake.init(0.1),
            RobotPart.pause(0.1),
            intake.turretReset(0.1),
            intake.switcharoo(0.1),
            outtake.clawGrab(0.1)
    );

    AutoModule zestiestFlick = new AutoModule(
            intake.clawRelease(0.1),
            intake.intakeSeek(0.1),
            intake.intake(0.1),
            intake.intakeSeek(0.1)
    );

    AutoModule specimenReady = new AutoModule(
//            intake.specimenReady(0.1),
            outtake.specimenReady(0.1),
//                // clingy claw :) - comment out
//                outtake.clawGrab(0.1),
//                outtake.clawRelease(0.1),
//                outtake.clawGrab(0.1),
//                outtake.clawRelease(0.1),
//                outtake.clawGrab(0.1),
//                outtake.clawRelease(0.1)
            intake.smallInit(0.1),
            intake.linkSeek(0.1),
            intake.clawRELEASE(0.1)
    ).setStartCode(()-> robotStatus.set(INTAKING));

    AutoModule grabSpecimen = new AutoModule(
//            intake.clawGRAB(0.1),
//            intake.yoinkSpecimen(0.2),
//            RobotPart.pause(0.05),
//            intake.transferSpecimen(0.1),
//            RobotPart.pause(0.2),
//            outtake.clawGrab(0.1),
//            outtake.clawRelease(0.1),
//            outtake.clawGrab(0.1)
            intake.clawGRAB(0.1),
            intake.transferSpecimen(0.1),
            RobotPart.pause(0.35),
            outtake.clawGrab(0.1)
    ).setStartCode(()-> robotStatus.set(SPECIMEN));

    AutoModule upSpecimen = new AutoModule(
            intake.clawRelease(0.1),
            lift.stageLift(1, 12.5).attach(outtake.upSpecimen(0.1))
    ).setStartCode(()-> robotStatus.set(SAMPLE));

    AutoModule high = new AutoModule(
            intake.clawRelease(0.1),
            RobotPart.pause(0.2),
            lift.stageLift(1, 41).attach(outtake.placeHigh(0.1)),
            intake.init(0.1)
    ).setStartCode(()-> robotStatus.set(SAMPLE));

    AutoModule down = new AutoModule(
            outtake.clawRelease(0.1),
            outtake.init(0.1),
            lift.stageLift(1, 0),
            intake.init(0.1)
    ).setStartCode(()-> robotStatus.set(DRIVING));

    AutoModule zestyFlick = new AutoModule(
            outtake.clawRelease(0.1),
            RobotPart.pause(0.1),
            intake.zestyFlick(0.1).attach(intake.clawRelease(0.1)),
            RobotPart.pause(0.1),
            intake.init(0.1)
    );



    // mini automodules
    AutoModule updatePipeline = new AutoModule(
            intake.turretReset(0.1),
            intake.updatePipeline(0.1),
            drive.alignSampleRight(0, -0.4, 0),
            drive.alignSampleLeft(0, 0.4, 0)
    );


    AutoModule clawDown = new AutoModule(
            intake.clawDown(0.1),
            intake.clawRelease(0.1)
    );

    AutoModule clawUp = new AutoModule(
            intake.linkEnd(0.1),
            intake.clawGrab(0.1),
            RobotPart.pause(0.1),
            intake.clawUp(0.1)
    );

    AutoModule clawRelease = new AutoModule(
            intake.clawRelease(0.1)
    );
}