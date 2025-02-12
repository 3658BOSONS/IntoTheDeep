package com.bosons.AutoHardware;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.bosons.Hardware.Motor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
    public NormalizedColorSensor home;
    private int TicksInDegree = (int) 5281.1/360;

    public Arm(OpMode opMode){
        arm = new Motor("arm",opMode);
        ext = opMode.hardwareMap.get(Servo.class,"armExt");
        arm.setPower(0.0);
        arm.setTargetPosition(0);
        arm.setConstants(DcMotor.RunMode.RUN_TO_POSITION, DcMotor.ZeroPowerBehavior.BRAKE, DcMotorSimple.Direction.FORWARD);
        home = (opMode.hardwareMap.get(NormalizedColorSensor.class, "armHome"));
        offset = 0;
    }

    public class ExtFull implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandOpen");
            double TargetPos = 1.0;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            ext.setPosition(TargetPos);
            return false;
        }
    }
    public Action ExtFull(){ return new ExtFull(); }

    public class ExtHalf implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandOpen");
            double TargetPos = 0.5;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            ext.setPosition(TargetPos);
            return false;
        }
    }
    public Action ExtHalf(){ return new ExtHalf(); }

    public class ExtHome implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","HandOpen");
            double TargetPos = 0.0;
            if (TargetPos>1){
                TargetPos = 1;
            } else if (TargetPos<0) {
                TargetPos = 0;
            }
            ext.setPosition(TargetPos);
            return false;
        }
    }
    public Action ExtHome(){ return new ExtHome(); }

    public class ParkOne implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmBucket");
            int degrees = -60;
            double TicksAsDegrees = degrees*TicksInDegree;
            if (degrees>=180) {
                arm.setTargetPosition((180 * TicksInDegree) + offset);
            }
            else{
                arm.setTargetPosition(degrees*TicksInDegree + offset);
            }
            if (abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
                arm.setPower(1.0);
            }else{
                arm.setPower(1.0);
            }
            if ((arm.getCurrentPosition()/TicksInDegree-offset) < -30){

            }
            int acceptableExtensionError = 30;
            return !(arm.burnCheck(acceptableExtensionError,false));
        }
    }
    public Action ParkOne(){ return new ParkOne(); }

    public class ParkThree implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmBucket");
            int degrees = -45;
            double TicksAsDegrees = degrees*TicksInDegree;
            if (degrees>=180) {
                arm.setTargetPosition((180 * TicksInDegree) + offset);
            }
            else{
                arm.setTargetPosition(degrees*TicksInDegree + offset);
            }
            if (abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
                arm.setPower(1.0);
            }else{
                arm.setPower(1.0);
            }
            if ((arm.getCurrentPosition()/TicksInDegree-offset) < -30){

            }
            int acceptableExtensionError = 30;
            return !(arm.burnCheck(acceptableExtensionError,false));
        }
    }
    public Action ParkThree(){ return new ParkThree(); }

    public class ParkTwo implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmBucket");
            int degrees = -174;
            double TicksAsDegrees = degrees*TicksInDegree;
            if (degrees>=180) {
                arm.setTargetPosition((180 * TicksInDegree) + offset);
            }
            else{
                arm.setTargetPosition(degrees*TicksInDegree + offset);
            }
            if (abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
                arm.setPower(1.0);
            }else{
                arm.setPower(1.0);
            }
            int acceptableExtensionError = 30;
            return !(arm.burnCheck(acceptableExtensionError,false));
        }
    }
    public Action ParkTwo(){ return new ParkTwo(); }

    public class Bucket implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.put("Current State: ","ArmBucket");
            int degrees = 150;
            double TicksAsDegrees = degrees*TicksInDegree;
            if (degrees>=180) {
                arm.setTargetPosition((180 * TicksInDegree) + offset);
            }
            else{
                arm.setTargetPosition(degrees*TicksInDegree + offset);
            }
            if (abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
                arm.setPower(1.0);
            }else{
                arm.setPower(1.0);
            }
            int acceptableExtensionError = 30;
            return !(arm.burnCheck(acceptableExtensionError,false));
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
                arm.setPower(0.5);
            }else{
                arm.setPower(1.0);
            }
            int acceptableExtensionError = 30;
            return !(arm.burnCheck(acceptableExtensionError,false));
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
                arm.setPower(1.0);
            }else{
                arm.setPower(1.0);
            }
            telemetryPacket.put("arm.getPower: ",arm.getPower());
            int acceptableExtensionError = 30;
            return !(arm.burnCheck(acceptableExtensionError,false));
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
                arm.setPower(1.0);
            }else{
                arm.setPower(1.0);
            }
            telemetryPacket.put("arm.getPower: ",arm.getPower());
            int acceptableExtensionError = 30;
            return !(arm.burnCheck(acceptableExtensionError,false));
        }
    }

    public Action Zero(){
        return new Zero();
    }


    public class Home implements Action{


        public double GetHomeDist(){
            if (home instanceof DistanceSensor){
                return (((DistanceSensor) home).getDistance(DistanceUnit.MM));
            }
            return 9999.9999;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (Homing){
                if(GetHomeDist() > 10.0){
                    arm.setPower(0.15);
                    if (!HomeInit){
                        arm.setTargetPosition(TicksInDegree*90);
                        HomeInit = true;
                    }
                }
                else{
                    offset = arm.getCurrentPosition();
                    arm.setPower(0.0);
                    arm.setTargetPosition(0);
                    Homing = false;
                    HomeInit = false;
                    return(false);
                }
                if(Math.abs(arm.getCurrentPosition() - arm.getTargetPosition()) < 30){
                    opm.telemetry.addData("TargetDistance",(arm.getCurrentPosition() - arm.getTargetPosition()));
                    arm.setTargetPosition((arm.getTargetPosition()*-1)*attempts);
                    attempts+=1;
                }
            }
            return !(((DistanceSensor) home).getDistance(DistanceUnit.MM) < 5);
        }
    }

    public Action Home(){
        return new Home();
    }
    }
