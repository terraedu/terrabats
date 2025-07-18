package org.terraedu.constants;

import com.acmerobotics.dashboard.config.Config;

@Config
public class DepositPositions {
    //edit
    public static double INIT_ARM = 0.5; // This is the inner limit of the arm (on the back) This should be the transfer position or close to it
    public static double SAMPLE_TRANSFER = 0.325;
    public static double INIT_START = 0.4;
    public static double SPECI_ARM = 0.7;
    public static double SPECI_GRAB = 0;
    public static double SAMPLE_PLACE = 0.0;


    public static double INIT_PIVOT = 0.15;// the pivot is a skipathon, you need to set the positions, make sure to go slow though because its a high gear ratio
    public static double TRANSFER_PIVOT = 0.2;
    public static double SPECI_PIVOT = 0;

    public static double CLAW_GRAB = 0.15;
    public static double CLAW_INIT = 0.35;
    public static double SAMPLE_GRAB = 0.4;
    public static double PLACEHIGH_PIVOT = 0.42;
    public static double PLACE_ARM = 1;
}