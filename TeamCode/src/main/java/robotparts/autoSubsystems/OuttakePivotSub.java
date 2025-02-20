package robotparts.autoSubsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

public class OuttakePivotSub extends Subsystem {
    // BOILERPLATE
    public static final OuttakePivotSub INSTANCE = new OuttakePivotSub();
    private OuttakePivotSub() {}

    // USER CODE
    public Servo pivot;

    private final double INIT = 0;
    private final double SPECIMENREADY = 0.79;
    private final double PLACE = 0.49;
    private final double SWITCHAROO = 0.52;
    private final double BASKET = 0.3;

    public Command moveInit() {
        return new ServoToPosition(pivot, INIT, this);
    }

    public Command specimenReady() {
        return new ServoToPosition(pivot, SPECIMENREADY, this);
    }

    public Command upSpecimen() {
        return new ServoToPosition(pivot, PLACE, this);
    }

    public Command switcharooReady() {
        return new ServoToPosition(pivot, SWITCHAROO, this);
    }

    public Command placeHigh() {
        return new ServoToPosition(pivot, BASKET, this);
    }

    @Override
    public void initialize() {
        pivot = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "pivot");
        pivot.setDirection(Servo.Direction.FORWARD);
    }
}