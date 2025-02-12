package com.bosons.Hardware;

import static java.lang.Math.abs;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Extender {
    public Motor right;
    public Motor left;
    public double MaxPow = 1.0;
    private int LOffset = 0;
    private int ROffset = 0;
    private RevTouchSensor leftButton;
    private RevTouchSensor rightButton;
    private double PPR = 751.8;
    private int MAXPIP = (int) ((8000*PPR)/1993.6);

    OpMode opm;
    public Extender(OpMode opMode){
        opm = opMode;
        left = new Motor("leftLift",opMode);
        left.setPower(0.0);
        left.setTargetPosition(0);
        left.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.FORWARD);
        leftButton = opMode.hardwareMap.get(RevTouchSensor.class,"leftHome");

        right = new Motor("rightLift",opMode);
        right.setPower(0.0);
        right.setTargetPosition(0);
        right.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.REVERSE);
        rightButton = opMode.hardwareMap.get(RevTouchSensor.class,"rightHome");
    }

    public double getPowerLeft(){
        return(left.getPower());
    }
    public double getPowerRight(){
        return(right.getPower());
    }

    public int GetLeftPos(){
        return(left.getCurrentPosition());
    }

    public int GetRightPos(){
        return(right.getCurrentPosition());
    }



    public void setMaxPow(double pow){
        MaxPow = pow;
    }

    public double getMaxPow(){
        return MaxPow;
    }

    public int[] getCurrentPosition(){
        return(new int[]{right.getCurrentPosition(), left.getCurrentPosition()});
    }
    public void FORBIDDIN(float v){
        int pips = -8000;
        if (abs(pips - right.getCurrentPosition())<=10){
            right.setPower(0.0);
        }else{
            right.setPower(v);
        }
        if (abs(pips - left.getCurrentPosition())<=10){
            left.setPower(0.0);
        }else{
            left.setPower(v);
        }
        right.setTargetPosition(pips);
        left.setTargetPosition(pips);
    }
    public void Stop(){
        right.setPower(0.0);
        left.setPower(0.0);
    }

    public void ExtendToTarget(int pips){
        opm.telemetry.addData("leftHome",leftButton.isPressed());
        opm.telemetry.addData("rightHome",rightButton.isPressed());
        pips = (int) (pips * (PPR/1993.6));
        if (pips > MAXPIP){
            pips = (MAXPIP);
        }
        if (pips<0){
            pips = 0;
        }

        if (pips==0){
            if(leftButton.isPressed()){left.setPower(0.0);}
            else{left.setPower(MaxPow);}
            if(rightButton.isPressed()){right.setPower(0.0);}
            else{right.setPower(MaxPow);}
            if (left.getCurrentPosition()<=0&&!leftButton.isPressed()){left.setOffset(left.getOffset()-1);}
            if (right.getCurrentPosition()<=0&&!rightButton.isPressed()){right.setOffset(right.getOffset()-1);}
        }else{
            if (abs(pips - right.getCurrentPosition())<=10){
                right.setPower(0.0);
            }else{
                right.setPower(MaxPow);
            }
            if (abs(pips - left.getCurrentPosition())<=10){
                left.setPower(0.0);
            }else{
                left.setPower(MaxPow);
            }
        }


        right.setTargetPosition(pips);
        left.setTargetPosition(pips);
    }
}
