package com.bosons.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    public Servo claw = null;

    public enum Pose{
        open,
        close
    }

    public Claw(OpMode OPmode){
        claw = OPmode.hardwareMap.get(Servo.class,"claw");

    }


    public void setPose(Pose pose){
        switch (pose){
            case open: {
                setPosition(0.0);
            }
            case close: {
                setPosition(1.0);
                break;
            }
        }
    }

    public void setPosition(double TargetPos){
        if (TargetPos>1){
            TargetPos = 1;
        } else if (TargetPos<0) {
            TargetPos = 0;
        }
        claw.setPosition(TargetPos);
    }


    public double GetCurrentPos(){
        return claw.getPosition();
    }

//this code is so skibidi

}
