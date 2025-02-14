package com.bosons.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Hand {
    public Servo claw = null;
    public Servo clawSpinny = null;
    public Servo wrist = null;

    public enum Pose{
        open,
        close
    }

    public Hand(OpMode OPmode){
        claw = OPmode.hardwareMap.get(Servo.class,"claw");
        clawSpinny = OPmode.hardwareMap.get(Servo.class,"clawSpinny");
        wrist = OPmode.hardwareMap.get(Servo.class,"wrist");
    }

    public void grip(Pose pose){
        switch (pose){
            case open: {
                setPosition(1.0);
                break;
            }
            case close: {
                setPosition(0.0);
                break;
            }
        }
    }

    public void setClawAngle(double angle){
        if(Math.abs(angle)>90){return;}
        double servoPosition = 0.5 + (angle/180);
        clawSpinny.setPosition(servoPosition);
    }

    public void setPosition(double TargetPos){//Dawg why did you make a wrapper for a single function, take this dudes linux from him 😭😭😭
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
