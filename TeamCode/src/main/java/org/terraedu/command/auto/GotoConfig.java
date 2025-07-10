package org.terraedu.command.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
public class GotoConfig {
    public static PIDFCoefficients xPID = new PIDFCoefficients(0.2,0,0,0);
    public static PIDFCoefficients yPID = new PIDFCoefficients(0,0,0,0);
    public static PIDFCoefficients hPID = new PIDFCoefficients(0,0,0,0);
}
