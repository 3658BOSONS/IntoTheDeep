package com.bosons.AutoHardware;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.bosons.Hardware.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Arm {
    //opMode for telemetry and hardware map;
    public OpMode opm;
    private int offset;
    public Boolean Homing = true;
    private Boolean HomeInit = false;
    //arm extension motors
    public Motor arm;
    private int attempts = 1;
    public Servo ext = null;
    public DigitalChannel homeSwitch;
    private int TicksInDegree = (int) 5281.1/360;

    public Arm(OpMode opMode){
        arm = new Motor("arm",opMode);
        ext = opMode.hardwareMap.get(Servo.class,"armExt");
        arm.setPower(0.0);
        arm.setTargetPosition(0);
        arm.setConstants(DcMotor.RunMode.RUN_TO_POSITION, DcMotor.ZeroPowerBehavior.BRAKE, DcMotorSimple.Direction.FORWARD);
        homeSwitch = (opMode.hardwareMap.get(DigitalChannel.class, "armHome"));
        homeSwitch.setMode(DigitalChannel.Mode.INPUT);
        offset = 0;
    }


    public class Bucket implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmBucket");
            int degrees = 160;
            double TicksAsDegrees = degrees*TicksInDegree;
            if (degrees>=180) {
                arm.setTargetPosition((180 * TicksInDegree) + offset);
            }
            else{
                arm.setTargetPosition(degrees*TicksInDegree + offset);
            }
            if (abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
                arm.setPower(0.0);
            }else{
                arm.setPower(1.0);
            }
            return !(abs(arm.getCurrentPosition() - (degrees*TicksInDegree + offset)) > 30);
        }
    }

    public Action Bucket(){
        return new Bucket();
    }


    public class Specimen implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmSpecimen");
            int degrees = -180;
            double TicksAsDegrees = degrees*TicksInDegree;
            if (degrees>=180) {
                arm.setTargetPosition((180 * TicksInDegree) + offset);
            }
            else{
                arm.setTargetPosition(degrees*TicksInDegree + offset);
            }
            if (abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
                arm.setPower(0.0);
            }else{
                arm.setPower(1.0);
            }
            return !(abs(arm.getCurrentPosition() - (degrees*TicksInDegree + offset)) > 30);
        }
    }

    public Action Specimen(){
        return new Specimen();
    }

    public class Intake implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmIntake");
            int degrees = -100;
            double TicksAsDegrees = degrees*TicksInDegree;
            if (degrees>=180) {
                arm.setTargetPosition((180 * TicksInDegree) + offset);
            }
            else{
                arm.setTargetPosition(degrees*TicksInDegree + offset);
            }
            if (abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
                arm.setPower(0.0);
            }else{
                arm.setPower(1.0);
            }
            return !(abs(arm.getCurrentPosition() - (degrees*TicksInDegree + offset)) > 30);
        }
    }

    public Action Intake(){
        return new Intake();
    }

    public class Zero implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmZero");
            int degrees = 0;
            double TicksAsDegrees = degrees*TicksInDegree;
            if (degrees>=180) {
                arm.setTargetPosition((180 * TicksInDegree) + offset);
            }
            else{
                arm.setTargetPosition(degrees*TicksInDegree + offset);
            }
            if (abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
                arm.setPower(0.0);
            }else{
                arm.setPower(1.0);
            }
            return !(abs(arm.getCurrentPosition() - (degrees*TicksInDegree + offset)) > 30);
        }
    }

    public Action Zero(){
        return new Zero();
    }


    public class Home implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmHome");
            if (Homing){
                if(homeSwitch.getState()){
                    arm.setPower(0.15);
                    if (!HomeInit){
                        arm.setTargetPosition(TicksInDegree*45);
                        HomeInit = true;
                    }
                }
                else{
                    offset = arm.getCurrentPosition();
                    arm.setPower(0.0);
                    arm.setTargetPosition(0);
                    Homing = false;
                    HomeInit = false;
                    attempts = 1;
                }
                if(abs(arm.getCurrentPosition() - arm.getTargetPosition()) < 30){
                    opm.telemetry.addData("TargetDistance",(arm.getCurrentPosition() - arm.getTargetPosition()));
                    arm.setTargetPosition((arm.getTargetPosition()*-1)*attempts);
                    attempts+=1;
                }
            }
            return !homeSwitch.getState();
        }
    }

    public Action Home(){
        return new Home();
    }
    }
