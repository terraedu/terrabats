package robotparts.autoSubsystems;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

public class ExtendoSub extends Subsystem {
    // BOILERPLATE
    public static final ExtendoSub INSTANCE = new ExtendoSub();
    private ExtendoSub() {}

    public MotorEx extendo;
    public PIDFController controller = new PIDFController(new PIDCoefficients(0.01, 0.0, 0.0001));
    public String name = "eil";

    public Command back() {
        return new RunToPosition(extendo, 0.0, controller, this);
    }

    public Command extend1500() {
        return new RunToPosition(extendo, 1200, controller, this);
    }

    public Command placeHigh() {
        return new RunToPosition(extendo, 2050, controller, this);
    }

    @Override
    public void initialize() {
        extendo = new MotorEx(name);
        extendo.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}