package org.terraedu.command.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
public class GotoConfig {

    public static PIDFCoefficients xPID = new PIDFCoefficients(0.21,0,0.03,0);
    public static PIDFCoefficients yPID = new PIDFCoefficients( 0.21,0,0.03,0);
    public static PIDFCoefficients xlPID = new PIDFCoefficients(0.16,0,0.018,0);
    public static PIDFCoefficients ylPID = new PIDFCoefficients(0.16,0,0.018,0);
    public static PIDFCoefficients hPID = new PIDFCoefficients(-4,0,-0.17,0);
}
