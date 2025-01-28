package com.bosons.AutoHardware;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.bosons.Hardware.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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
    public class HighBucket implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            int pips = 100;
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
            return !(abs(left.getTargetPosition() - left.getCurrentPosition()) > 30);


        }
    }

    public Action HighBucket(){
        return new HighBucket();
    }

    public class Zero implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            int pips = 0;
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
            return !(abs(left.getTargetPosition() - left.getCurrentPosition()) > 30);


        }
    }

    public Action Zero(){
        return new Zero();
    }

    public class intake implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            int pips = 100;
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
            return !(abs(left.getTargetPosition() - left.getCurrentPosition()) > 30);


        }
    }

    public Action intake(){
        return new intake();
    }

    public class Specimen implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            int pips = 0;
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
            return !(abs(left.getTargetPosition() - left.getCurrentPosition()) > 30);


        }
    }

    public Action Specimen(){
        return new Specimen();
    }

    public class LowBucket implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            int pips = 0;
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
            return !(abs(left.getTargetPosition() - left.getCurrentPosition()) > 30);


        }
    }

    public Action LowBucket(){ return new LowBucket(); }
}
