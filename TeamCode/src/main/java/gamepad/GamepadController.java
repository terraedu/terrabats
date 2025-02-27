package gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadController implements Controller {
    Gamepad gamepad;
    Runnable aHandler = null, bHandler = null, xHandler = null, yHandler = null;
    Runnable leftTriggerHandler = null, rightTriggerHandler = null;

    Runnable leftBumperHandler = null, rightBumperHandler = null;
    Runnable leftStickButtonHandler = null, rightStickButtonHandler = null;
    Runnable dpadDownHandler = null, dpadLeftHandler = null, dpadRightHandler = null, dpadUpHandler = null;
    boolean pA = false, pB = false, pX = false, pY = false;
    boolean pLeftBumper = false, pRightBumper = false;
    boolean pLeftTrigger = false, pRightTrigger = false;

    boolean pLeftStickButton = false, pRightStickButton = false;
    boolean pDpadDown = false, pDpadLeft = false, pDpadRight = false, pDpadUp = false;

    public GamepadController(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    @Override
    public Controller clickEvent(Button button, Runnable handler) {
        switch (button) {
            case A: this.aHandler = handler; break;
            case B: this.bHandler = handler; break;
            case X: this.xHandler = handler; break;
            case Y: this.yHandler = handler; break;
            case LEFT_BUMPER: this.leftBumperHandler = handler; break;
            case RIGHT_BUMPER: this.rightBumperHandler = handler; break;
            case LEFT_TRIGGER: this.leftTriggerHandler = handler; break;
            case RIGHT_TRIGGER: this.rightTriggerHandler = handler; break;
            case LEFT_STICK_BUTTON: this.leftStickButtonHandler = handler; break;
            case RIGHT_STICK_BUTTON: this.rightStickButtonHandler = handler; break;
            case DPAD_DOWN: this.dpadDownHandler = handler; break;
            case DPAD_LEFT: this.dpadLeftHandler = handler; break;
            case DPAD_RIGHT: this.dpadRightHandler = handler; break;
            case DPAD_UP: this.dpadUpHandler = handler; break;
        }
        return this;
    }



    @Override
    public void update() {
        if (!this.pA && this.gamepad.a && this.aHandler != null) this.aHandler.run();
        if (!this.pB && this.gamepad.b && this.bHandler != null) this.bHandler.run();
        if (!this.pX && this.gamepad.x && this.xHandler != null) this.xHandler.run();
        if (!this.pY && this.gamepad.y && this.yHandler != null) this.yHandler.run();
        if (!this.pLeftBumper && this.gamepad.left_bumper && this.leftBumperHandler != null) this.leftBumperHandler.run();
        if (!this.pRightBumper && this.gamepad.right_bumper && this.rightBumperHandler != null) this.rightBumperHandler.run();
        if (!this.pLeftTrigger && this.gamepad.left_trigger > 0 && this.leftTriggerHandler != null) this.leftTriggerHandler.run();
        if (!this.pRightTrigger && this.gamepad.right_trigger > 0 && this.rightBumperHandler != null) this.rightBumperHandler.run();
        if (!this.pLeftStickButton && this.gamepad.left_stick_button && this.leftStickButtonHandler != null) this.leftStickButtonHandler.run();
        if (!this.pRightStickButton && this.gamepad.right_stick_button && this.rightStickButtonHandler != null) this.rightStickButtonHandler.run();
        if (!this.pDpadDown && this.gamepad.dpad_down && this.dpadDownHandler != null) this.dpadDownHandler.run();
        if (!this.pDpadLeft && this.gamepad.dpad_left && this.dpadLeftHandler != null) this.dpadLeftHandler.run();
        if (!this.pDpadRight && this.gamepad.dpad_right && this.dpadRightHandler != null) this.dpadRightHandler.run();
        if (!this.pDpadUp && this.gamepad.dpad_up && this.dpadUpHandler != null) this.dpadUpHandler.run();
        this.pA = this.gamepad.a;
        this.pB = this.gamepad.b;
        this.pX = this.gamepad.x;
        this.pY = this.gamepad.y;
        this.pLeftBumper = this.gamepad.left_bumper;
        this.pRightBumper = this.gamepad.right_bumper;
        this.pLeftStickButton = this.gamepad.left_stick_button;
        this.pRightStickButton = this.gamepad.right_stick_button;
        this.pDpadDown = this.gamepad.dpad_down;
        this.pDpadLeft = this.gamepad.dpad_left;
        this.pDpadRight = this.gamepad.dpad_right;
        this.pDpadUp = this.gamepad.dpad_up;
    }
}
