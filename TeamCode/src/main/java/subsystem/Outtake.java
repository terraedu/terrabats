package subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import wrappers.positional.PServo;

public class Outtake extends OpMode {
    private PServo armr;
    private PServo arml;
    private PServo pivot;
    private PServo claw;

    @Override
    public void init() {
        armr = hardwareMap.get(PServo.class, "armr");
        arml = hardwareMap.get(PServo.class, "arml");
        pivot = hardwareMap.get(PServo.class, "pivot");
        claw = hardwareMap.get(PServo.class, "claw");
    }

    public void moveInit() {
//        armr.setPosition(0.3);
//        arml.setPosition(0.3);
//        pivot.setPosition(0.3);
        claw.setPosition(0.3);
    }

    @Override
    public void loop() {}
}
