package org.terraedu.command.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
public class GotoConfig {
    public static PIDFCoefficients xPID = new PIDFCoefficients();
    public static PIDFCoefficients yPID = new PIDFCoefficients();
    public static PIDFCoefficients hPID = new PIDFCoefficients();
}
