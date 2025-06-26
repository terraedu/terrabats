package org.terraedu.constants;

public enum IntakePositions {
    INIT_LINKAGE(0.0);

    double position;

    IntakePositions(double position) {
        this.position = position;
    }

    public double get() {
        return position;
    }
}

