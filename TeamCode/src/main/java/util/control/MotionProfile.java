package util.control;
public class MotionProfile {

    double maxvel;
    double maxaccel;

    double acceleration_dt;
    double distance;
    double halfway_distance;
    double acceleration_distance;
    double deceleration_dt;
    double cruise_distance;
    double cruise_dt;
    double deceleration_time;
    double start = 0;

    public MotionProfile(double start, double end, double maxvel, double maxaccel) {
        acceleration_dt = maxvel / maxaccel;
        distance = end - start;
        this.start = start;

        if (distance < 0) {
            maxaccel = -maxaccel;
        }

        halfway_distance = distance / 2;
        acceleration_distance = 0.5 * maxaccel * Math.pow(acceleration_dt, 2);

        if (acceleration_distance > halfway_distance) {
            acceleration_dt = Math.sqrt(halfway_distance / (0.5 * maxaccel));
        }

        acceleration_distance = 0.5 * maxaccel * Math.pow(acceleration_dt, 2);

        maxvel = maxaccel * acceleration_dt;

        deceleration_dt = acceleration_dt;

        cruise_distance = distance - (2 * acceleration_distance);
        cruise_dt = cruise_distance / maxvel;
        deceleration_time = acceleration_dt + cruise_dt;

        this.maxvel = maxvel;
        this.maxaccel = maxaccel;
    }

    public double get(double time) {
        double cruise_current_dt;

        double entire_dt = acceleration_dt + cruise_dt + deceleration_dt;
        if (time > entire_dt) {
            return distance + start;
        }

        if (time < acceleration_dt) {
            return (0.5 * maxaccel * Math.pow(time, 2)) + start;
        }

        else if (time < deceleration_time) {
            acceleration_distance = 0.5 * maxaccel * Math.pow(acceleration_dt, 2);
            cruise_current_dt = time - acceleration_dt;

            return acceleration_distance + (maxvel * cruise_current_dt) + start;
        }

        else {
            acceleration_distance = 0.5 * maxaccel * Math.pow(acceleration_dt, 2);
            cruise_distance = maxvel * cruise_dt;
            deceleration_time = time - deceleration_time;

            return (acceleration_distance + cruise_distance + (maxvel * deceleration_time - 0.5 * maxaccel * Math.pow(deceleration_time, 2))) + start;
        }
    }
}