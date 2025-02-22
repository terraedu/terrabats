package robotparts.autoSubsystems;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;

import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

public class LiftSub extends Subsystem {
    // BOILERPLATE
    public static final LiftSub INSTANCE = new LiftSub();
    private LiftSub() {}

    public MotorEx lift;
    public PIDFController controller = new PIDFController(new PIDCoefficients(0.02, 0.0, 0.0));
    public String name = "lir";

    // USER CODE
    @Override
    public Command getDefaultCommand() { // runs internally automatically
        return new HoldPosition(lift, controller, this);
    }

    public Command down() {
        return new RunToPosition(lift, 0.0, controller, this);
    }

    public Command specimen() {
        return new RunToPosition(lift, 497, controller, this);
    }

    public Command placeHigh() {
        return new RunToPosition(lift, 2050, controller, this);
    }

    @Override
    public void initialize() {
        lift = new MotorEx(name);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}