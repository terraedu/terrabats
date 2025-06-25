package org.terraedu.constants;

public enum DepositPositions {
    INIT_LINKAGE(0.0),
    ARM_INIT(.2),
    PIVOT_INIT(0),
    CLAW_INIT(0),
    CLAW_GRAB(0.5),
    ARM_TRANSFER(0),
    PIVOT_PLACE(0.15),
    PIVOT_SPECI(0.25),
    LINKAGE_PLACE(0.55),
    ARM_SPECI(0.75),
    ARM_PLACE(0.6);

    double position;

    DepositPositions(double position) {
        this.position = position;
    }

    public double get() {
        return position;
    }
}