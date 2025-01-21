package com.bosons.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Extender {
    public Motor right;
    public Motor left;
    public Extender(OpMode opMode){
        left = new Motor("LeftExt",opMode);
        left.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.FORWARD);

        right = new Motor("RightExt",opMode);
        right.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.FORWARD);
    }
    public double[] getPower(){
        return(new double[]{right.getPower(), left.getPower()});
    }

    public int[] getCurrentPosition(){
        return(new int[]{right.getCurrentPosition(), left.getCurrentPosition()});
    }
    public void ExtendToTarget(int pips){
        if (left.getCurrentPosition()>7900){
            left.setPower(0.1);
        } else if (left.getCurrentPosition()>100) {
            left.setPower(1);
        }else{
            left.setPower(0.1);
        }
        if (right.getCurrentPosition()>7900){
            right.setPower(0.1);
        } else if (right.getCurrentPosition()>100) {
            right.setPower(1);
        }else{
            right.setPower(0.1);
        }
    }
}
