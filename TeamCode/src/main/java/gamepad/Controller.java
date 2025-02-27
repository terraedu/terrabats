package gamepad;

public interface Controller {
    public static enum Button{
        A, B, X, Y,
        LEFT_BUMPER, RIGHT_BUMPER,
        LEFT_TRIGGER, RIGHT_TRIGGER,
        LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON,
        DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT, DPAD_UP
    }

    public Controller clickEvent(Button button, Runnable handler);
    public void update();
}
