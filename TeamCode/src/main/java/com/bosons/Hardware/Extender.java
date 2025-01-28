package com.bosons.Hardware;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.bosons.AutoHardware.Arm;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Extender {
    public Motor right;
    public Motor left;
    public Extender(OpMode opMode){
        left = new Motor("leftLift",opMode);
        left.setPower(0.0);
        left.setTargetPosition(0);
        left.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.FORWARD);

        right = new Motor("rightLift",opMode);
        right.setPower(0.0);
        right.setTargetPosition(0);
        right.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.REVERSE);
    }

    public double[] getPower(){
        return(new double[]{right.getPower(), left.getPower()});
    }

    public int[] getCurrentPosition(){
        return(new int[]{right.getCurrentPosition(), left.getCurrentPosition()});
    }
    public void FORBIDDIN(){
        int pips = -8000;
        if (abs(pips - right.getCurrentPosition())<=10){
            right.setPower(0.0);
        }else{
            right.setPower(1.0);
        }
        if (abs(pips - left.getCurrentPosition())<=10){
            left.setPower(0.0);
        }else{
            left.setPower(1.0);
        }
        right.setTargetPosition(pips);
        left.setTargetPosition(pips);
    }
    public void ExtendToTarget(int pips){
        if (pips > 8000){
            pips = 8000;
        }
        if (pips<0){
            pips = 0;
        }
        if (abs(pips - right.getCurrentPosition())<=10){
            right.setPower(0.0);
        }else{
            right.setPower(1.0);
        }
        if (abs(pips - left.getCurrentPosition())<=10){
            left.setPower(0.0);
        }else{
            left.setPower(1.0);
        }
        right.setTargetPosition(pips);
        left.setTargetPosition(pips);
    }
}
