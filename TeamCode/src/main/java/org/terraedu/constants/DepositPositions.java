package org.terraedu.constants;

import com.acmerobotics.dashboard.config.Config;

@Config
public class DepositPositions {
    public static double INIT_LINKAGE = 0.04;
    public static double INIT_ARM = 0.2;
    public static double INIT_PIVOT = 0.01;
    public static double CLAW_GRAB = 0.45;
    public static double SAMPLE_GRAB = 0.4;
    public static double CLAW_INIT = 0.0;
    public static double ARM_TRANSFER = 0.0;
    public static double PLACE_PIVOT = 0.15;
    public static double SPECI_PIVOT = 0.25;
    public static double TRANSFER_PIVOT = 0.45;

    public static double PLACE_LINKAGE = 0.51;
    public static double SPECI_ARM = 0.778;
    public static double PLACE_ARM = 0.53;
}

//public enum DepositPositions {
//    INIT_LINKAGE(0.0),
//    ARM_INIT(.2),
//    PIVOT_INIT(0.05),
//    CLAW_INIT(0),
//    CLAW_GRAB(0.5),
//    ARM_TRANSFER(0),
//    PIVOT_PLACE(0.15),
//    PIVOT_SPECI(0.25),
//    LINKAGE_PLACE(0.55),
//    ARM_SPECI(0.8),
//    ARM_PLACE(0.6);
//
//    double position;
//
//    DepositPositions(double position) {
//        this.position = position;
//    }
//
//    public double get() {
//        return position;
//    }
//}