package subsystem;

import static global.Modes.heightMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import global.Mode;
import global.Modes;
import util.control.MotionProfile;
import util.control.MotionProfileGenerator;
import util.control.PIDFController;
import wrappers.positional.PMotor;

public class Lift {

        private PIDFController controller1;

        private MotionProfile profile;
        private ElapsedTime timer;
        double maxvel = 6000;
        double maxaccel = 6000;

        public double p = 0.02, i = 0.00, d = 0.00045;
        public double f = 0.125;
        double voltageCompensation;

        public double target = 0;
        private final double ticks_in_degrees = 700 / 180.0;
        public double power1;

        public PMotor slides1;
        public PMotor slides2;
        public VoltageSensor voltageSensor;



        public Lift(PMotor l1, PMotor l2, VoltageSensor voltageSensor) {
            this.slides1 = l1;
            this.slides2 = l2;
            this.voltageSensor = voltageSensor;

            controller1 = new PIDFController(p, i , d, f, false);
            slides1.setDirection(DcMotorSimple.Direction.REVERSE);
            slides1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slides1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slides2.setDirection(DcMotorSimple.Direction.FORWARD);
            slides2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slides2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            timer = new ElapsedTime();
            profile = MotionProfileGenerator.generateSimpleMotionProfile(1, 0, maxvel, maxaccel);
        }


        public void update() {

            int motorPos = slides1.getCurrentPosition();

            double pid1 = controller1.update(motorPos, target);


            voltageCompensation = 13.5 / voltageSensor.getVoltage();
            power1 = (pid1) * voltageCompensation;

            if (target == 0){
                slides1.setPower(-power1);
                slides2.setPower(-power1); //was at *0.3 pre push
            }
            else {
                slides1.setPower(-power1);
                slides2.setPower(-power1);
            }
        }

        public void runToPosition(int ticks) {
            target = ticks;

        }
//
//        public void runToPreset(Modes.Height level) {
//            if (level == Levels.INIT) {
//                runToPosition(0);
//            } else if (level == Levels.INTAKE) {
//                runToPosition(-15);
//            } else if (level == Levels.INTERMEDIATE) {
//                runToPosition(0);
//            } else if (level == Levels.LOCATING_TARGETS) {
//                runToPosition(0);
//            } else if (level == Levels.LOW_BASKET) {
//                runToPosition(1300);
//            } else if (level == Levels.HIGH_BASKET) {
//                runToPosition(2160);
//            } else if (level == Levels.LOW_RUNG) {
//                runToPosition(0);
//            } else if (level == Levels.HIGH_RUNG) {
//                runToPosition(960);
//            } else if (level == Levels.CLIMB_EXTENDED) {
//                runToPosition(0);
//            } else if (level == Levels.CLIMB_RETRACTED) {
//                runToPosition(0);
//            }
//        }

        public void setPower(float power) {
            slides1.setPower(power);
            slides2.setPower(power);
        }

        public void setPower(float power1, float power2) {
            slides1.setPower(power1);
            slides2.setPower(power2);
        }


        public int getPos() {
            return slides1.getCurrentPosition();
        }

    }
}
