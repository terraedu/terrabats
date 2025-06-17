package org.terraedu.util.wrappers;

public abstract class WSubsystem {
    public abstract void periodic();
    public abstract void read();
    public abstract void write();
    public abstract void reset();
}