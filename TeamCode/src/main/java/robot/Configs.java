package robot;

import static robot.RobotConfig.setConfig;

public class Configs implements RobotUser{
    RobotConfig IntoTheDeep = new RobotConfig(intake, outtake, lift, extendo, drive, camera);
    public void setCurrentConfig(){setConfig(IntoTheDeep);}
}