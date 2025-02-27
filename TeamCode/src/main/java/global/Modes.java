package global;

public interface Modes {



    enum RobotStatus implements Mode.ModeType { DRIVING, PLACING, INTAKING, PLACING2}
    Mode robotStatus = new Mode(RobotStatus.class);

    enum TeleStatus implements Mode.ModeType {REDA, BLUEA}
    Mode teleStatus = new Mode(TeleStatus.class);


    enum Height implements Mode.ModeType {GROUND, lowrung, highrung, lowbasket, highbasket, currentHeight}

    Mode heightMode = new Mode(Height.class);


}

