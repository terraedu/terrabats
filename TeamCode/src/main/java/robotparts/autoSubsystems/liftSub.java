package robotparts.autoSubsystems;

import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;

import com.rowanmcalpin.nextftc.core.control.controllers.PIDController;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

public class liftSub extends Subsystem {
    // BOILERPLATE
    public static final liftSub INSTANCE = new liftSub();
    private liftSub() { }

    public MotorEx lift;

    public PIDFController controller = new PIDFController(new PIDCoefficients(0.005, 0.0, 0.0));

    public String name = "lir";


    @Override
    public void initialize() {
        lift = new MotorEx(name);
    }

    public Command toLow() {
        return new RunToPosition(lift, // MOTOR TO MOVE
                0.0, // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }
    public Command toHigh() {
        return new RunToPosition(lift, // MOTOR TO MOVE
                0.0, // TARGET POSITION, IN TICKS
                controller, // CONTROLLER TO IMPLEMENT
                this); // IMPLEMENTED SUBSYSTEM
    }
}
