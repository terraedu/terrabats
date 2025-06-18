package org.terraedu.util.math;

/**
 * RGB Class which contains a red, green, and blue value.
 */
public class RGB {
    private final Number r;
    private final Number g;
    private final Number b;

    public RGB(Number r, Number g, Number b) {
        this.r = r;
        this.g = g;
        this.b = b;
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

    public RGBA rgba() {
        return new RGBA(r, g, b, 255);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RGB)) return false;

        RGB rgb = (RGB) o;

        if (!r.equals(rgb.r)) return false;
        if (!g.equals(rgb.g)) return false;
        return b.equals(rgb.b);
    }

    @Override
    public int hashCode() {
        int result = r.hashCode();
        result = 31 * result + g.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RGB{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}

