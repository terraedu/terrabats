package subsystem;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import wrappers.positional.PServo;

public class Intake extends OpMode {
    public PServo armr, arml, latch;

    @Override
    public void init() {
        armr = hardwareMap.get(PServo.class, "armr");
        arml = hardwareMap.get(PServo.class, "armr");
        latch = hardwareMap.get(PServo.class, "armr");
    }

    public void moveInit() {
//        armr.setPosition(0.3);
//        armr.setPosition(0.3);
//        armr.setPosition(0.3);
    }

    @Override
    public void loop() {}
}
