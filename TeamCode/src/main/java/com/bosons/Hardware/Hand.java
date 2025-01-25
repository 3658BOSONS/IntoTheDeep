package com.bosons.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Hand {
    public Servo claw = null;
    public Servo wrist = null;

    public enum Pose{
        open,
        close
    }

    public Hand(OpMode OPmode){
        claw = OPmode.hardwareMap.get(Servo.class,"claw");
        wrist = OPmode.hardwareMap.get(Servo.class,"wrist");
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


    public double GetClawPos(){
        return claw.getPosition();
    }
    
    public void setRotat(double TargetPos){
        if (TargetPos>1){
            TargetPos = 1;
        } else if (TargetPos<0) {
            TargetPos = 0;
        }
        wrist.setPosition(TargetPos);
    }

    public double GetWristPos(){
        return(wrist.getPosition());
    }

//this code is so skibidi

}
