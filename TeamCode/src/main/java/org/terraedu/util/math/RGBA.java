package org.terraedu.util.math;

/**
 * RGBA class which represents a color with a given alpha.
 */
public class RGBA {
    private final Number r;
    private final Number g;
    private final Number b;
    private final Number a;

    public RGBA(Number r, Number g, Number b, Number a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Number getR() {
        return r;
    }

    public Number getG() {
        return g;
    }

    public Number getB() {
        return b;
    }

    public Number getA() {
        return a;
    }

    public RGB rgb() {
        return new RGB(r, g, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RGBA)) return false;

        RGBA rgba = (RGBA) o;

        if (!r.equals(rgba.r)) return false;
        if (!g.equals(rgba.g)) return false;
        if (!b.equals(rgba.b)) return false;
        return a.equals(rgba.a);
    }

    @Override
    public int hashCode() {
        int result = r.hashCode();
        result = 31 * result + g.hashCode();
        result = 31 * result + b.hashCode();
        result = 31 * result + a.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RGBA{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
