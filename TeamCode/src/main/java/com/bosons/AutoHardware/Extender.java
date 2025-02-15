package com.bosons.AutoHardware;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.bosons.Hardware.Motor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Arrays;

public class Extender {
    public Motor right;
    public Motor left;
    private RevTouchSensor leftButton;
    private RevTouchSensor rightButton;
    public double PPR = 751.8;
    public int MAXPIP = (int) ((8000*PPR)/1993.6);
    public double MaxPow=1.0;
    public Extender(OpMode opMode){
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
    public class HighBucket implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ExtBucket");
            int pips = 6500;
            pips = (int) (pips * (PPR/1993.6));
            if (pips > MAXPIP){
                pips = MAXPIP;
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
            telemetryPacket.put("extendoPower:   ", Arrays.toString(new double[]{right.getPower(), left.getPower()}));
            telemetryPacket.put("extendoTarget:  ", Arrays.toString(new int[]{right.getTargetPosition(), left.getTargetPosition()}));
            telemetryPacket.put("extendoCurrent: ", Arrays.toString(new int[]{right.getCurrentPosition() + left.getCurrentPosition()}));
            int acceptableExtensionError = 30;
           return !((right.burnCheck(acceptableExtensionError)&&left.burnCheck(acceptableExtensionError)));
        }
    }

    public Action HighBucket(){
        return new HighBucket();
    }

    public class Zero implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ExtZero");
            int pips = 0;
            pips = (int) (pips * (PPR/1993.6));
            if (pips > MAXPIP){
                pips = MAXPIP;
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
            telemetryPacket.put("extendoPower:   ", Arrays.toString(new double[]{right.getPower(), left.getPower()}));
            telemetryPacket.put("extendoTarget:  ", Arrays.toString(new int[]{right.getTargetPosition(), left.getTargetPosition()}));
            telemetryPacket.put("extendoCurrent: ", Arrays.toString(new int[]{right.getCurrentPosition() + left.getCurrentPosition()}));
            return !(leftButton.isPressed()&&rightButton.isPressed());
        }
    }

    public Action Zero(){
        return new Zero();
    }

    public class intake implements Action {


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ExtIntake");
            int pips = 100;
            pips = (int) (pips * (PPR/1993.6));
            if (pips > MAXPIP){
                pips = MAXPIP;
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
            telemetryPacket.put("extendoPower:   ", Arrays.toString(new double[]{right.getPower(), left.getPower()}));
            telemetryPacket.put("extendoTarget:  ", Arrays.toString(new int[]{right.getTargetPosition(), left.getTargetPosition()}));
            telemetryPacket.put("extendoCurrent: ", Arrays.toString(new int[]{right.getCurrentPosition() + left.getCurrentPosition()}));
             int acceptableExtensionError = 30;
           return ((right.burnCheck(acceptableExtensionError)&&left.burnCheck(acceptableExtensionError)));
        }
    }

    public Action intake(){
        return new intake();
    }

    public class Specimen implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ExtSpecimen");
            int pips = 0;
            pips = (int) (pips * (PPR/1993.6));
            if (pips > MAXPIP){
                pips = MAXPIP;
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
            telemetryPacket.put("extendoPower:   ",new double[]{right.getPower(), left.getPower()}.toString());
            telemetryPacket.put("extendoTarget:  ",new int[]{right.getTargetPosition(), left.getTargetPosition()}.toString());
            telemetryPacket.put("extendoCurrent: ",new int[]{right.getCurrentPosition() + left.getCurrentPosition()}.toString());
             int acceptableExtensionError = 30;
           return !((right.burnCheck(acceptableExtensionError)&&left.burnCheck(acceptableExtensionError)));
        }
    }

    public Action Specimen(){
        return new Specimen();
    }

    public class LowBucket implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","LowBucket");
            int pips = 0;
            pips = (int) (pips * (PPR/1993.6));
            if (pips > MAXPIP){
                pips = MAXPIP;
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
            telemetryPacket.put("extendoPower:   ", Arrays.toString(new double[]{right.getPower(), left.getPower()}));
            telemetryPacket.put("extendoTarget:  ", Arrays.toString(new int[]{right.getTargetPosition(), left.getTargetPosition()}));
            telemetryPacket.put("extendoCurrent: ", Arrays.toString(new int[]{right.getCurrentPosition() + left.getCurrentPosition()}));
             int acceptableExtensionError = 30;
           return !((right.burnCheck(acceptableExtensionError)&&left.burnCheck(acceptableExtensionError)));
        }
    }

    public Action LowBucket(){ return new LowBucket(); }
}
