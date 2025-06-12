package subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import wrappers.positional.PServo;

public class Outtake {
    PServo arml, armr, linkage, pivot, claw;

    public enum outtakeState{
        init,
    }

    outtakeState currentOuttakeState;

    public void setState(outtakeState newState){
        this.currentOuttakeState = newState;
    }

    public void init() {
        arml = hardwareMap.get(PServo.class, "arml");
        armr = hardwareMap.get(PServo.class, "armr");
        linkage = hardwareMap.get(PServo.class, "linkage");
        pivot = hardwareMap.get(PServo.class, "pivot");
        claw = hardwareMap.get(PServo.class, "claw");

        arml.addPosition("init", 0);
        armr.addPosition("init", 0);
        linkage.addPosition("init", 0);
        pivot.addPosition("init", 0);
        claw.addPosition("init", 0);
    }

    public void moveInit() {
        arml.setPosition("init");
        armr.setPosition("init");
        linkage.setPosition("init");
        pivot.setPosition("init");
        claw.setPosition("init");
    }

    public void update(){
        switch(currentOuttakeState) {
            case init:
                moveInit();
                break;

            default:
                moveInit();
        }
    }
}
