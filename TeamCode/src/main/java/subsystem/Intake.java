package subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import wrappers.positional.PServo;

public class Intake{

    PServo iarmr, iarml, ipivot;

    public enum intakeState{

        init,
        grab,
        hover

    };

    intakeState currentIntakeState;

    double setActionTime = 1;


    public void init() {

       iarmr = hardwareMap.get(PServo.class, "iarmr");
       iarml = hardwareMap.get(PServo.class, "iarml");
       ipivot = hardwareMap.get(PServo.class, "ipivot");


   }

   public void setState(intakeState newState){
        this.currentIntakeState = newState;
   }

   public void update(){

        switch(currentIntakeState) {
            case init:
            if (currentIntakeState == intakeState.init) {
                //do thing

            }
            break;
            case hover:
                if (currentIntakeState == intakeState.hover) {
                    //do thing

                }
                break;
            case grab:
                if (currentIntakeState == intakeState.grab) {
                    //do thing

                }
                break;
        }
   }
}
