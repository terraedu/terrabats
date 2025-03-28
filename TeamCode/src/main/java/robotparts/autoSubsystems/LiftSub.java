package robotparts.autoSubsystems;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.control.coefficients.PIDCoefficients;

import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;

import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;

import java.util.ArrayList;
import java.util.List;

public class LiftSub extends Subsystem {
    // BOILERPLATE
    public static final LiftSub INSTANCE = new LiftSub();
    private LiftSub() {}

    public MotorEx lir;
    public MotorEx lil;
    public MotorGroup Lift;


    public PIDFController controller = new PIDFController(new PIDCoefficients(0.02, 0.0, 0.0));
    public String name = "lir";
    public String name2 = "lil";


    // USER CODE
    @Override
    public Command getDefaultCommand() { // runs internally automatically
        return new HoldPosition(Lift, controller, this);
    }

    public Command down() {
        return new RunToPosition(Lift, 0.0, controller, this);
    }

    public Command specimen() {
        return new RunToPosition(Lift, 675, controller, this);
    }

    public Command placeHigh() {
        return new RunToPosition(Lift, 2050, controller, this);
    }

    @Override
    public void initialize() {
        lir = new MotorEx(name);
        lir.setDirection(DcMotorSimple.Direction.REVERSE);
        lil = new MotorEx(name2);
        lil.setDirection(DcMotorSimple.Direction.FORWARD);
        Lift = new MotorGroup(lil, lir);

    }
}