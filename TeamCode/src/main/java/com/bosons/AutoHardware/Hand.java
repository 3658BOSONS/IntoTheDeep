package com.bosons.AutoHardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hand {
    public Servo claw = null;
    public Servo wrist = null;
    public Servo spinny = null;

    public Hand(HardwareMap hwm){
        claw = hwm.get(Servo.class,"claw");
        wrist = hwm.get(Servo.class,"wrist");
        spinny=hwm.get(Servo.class,"clawSpinny");
        claw.setPosition(0.0);
        wrist.setPosition(1.0);
        spinny.setPosition(0.5);
    }

    public class SpinZero implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandOpen");
            double TargetPos = 0.5;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            spinny.setPosition(TargetPos);
            return false;
        }
    }

    public class SpinPickup implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandOpen");
            double TargetPos = 0.25;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            spinny.setPosition(TargetPos);
            return false;
        }
    }

    public class open implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandOpen");
            double TargetPos = 1.0;
                if (TargetPos>1){
                    TargetPos = 1;
                } else if (TargetPos<0) {
                    TargetPos = 0;
                }
                claw.setPosition(TargetPos);
                return false;
        }
    }
    public Action open(){ return new open(); }

    public class close implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandClose");
            double TargetPos = 0.0;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            claw.setPosition(TargetPos);
            return false;
        }
    }
    public Action close(){ return new close(); }

    public class Bucket implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandBucket");
            double TargetPos = 0.5;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            wrist.setPosition(TargetPos);
            return false;
        }
    }
    public Action Bucket(){ return new Bucket(); }

    public class Intake implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandIntake");
            double TargetPos = 0.6;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            wrist.setPosition(TargetPos);
            return false;
        }
    }
    public Action Intake(){ return new Intake(); }

    public class home implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandIntake");
            double TargetPos = 1.0;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            wrist.setPosition(TargetPos);
            return false;
        }
    }
    public Action home(){ return new home(); }

    public class Specimen implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandSpecimen");
            double TargetPos = 1.0;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            wrist.setPosition(TargetPos);
            return false;
        }
    }
    public Action Specimen(){ return new Specimen(); }

    public class Zero implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandSpecimen");
            double TargetPos = 0.5;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            wrist.setPosition(TargetPos);
            return false;
        }
    }
    public Action Zero(){ return new Zero(); }


}
