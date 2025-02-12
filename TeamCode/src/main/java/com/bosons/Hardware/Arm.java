package com.bosons.Hardware;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.TeleOpWIP;

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
    public double MaxPow = 1.0;
    public Servo ext = null;
    public NormalizedColorSensor home;
    private int TicksInDegree = (int) 5281.1/360;
    public boolean HomeOverride = false;

    public Arm(OpMode opMode){
        opm = opMode;
        arm = new Motor("arm",opMode);
        ext = opMode.hardwareMap.get(Servo.class,"armExt");
        arm.setPower(0.0);
        arm.setTargetPosition(0);
        arm.setConstants(DcMotor.RunMode.RUN_TO_POSITION, DcMotor.ZeroPowerBehavior.BRAKE, DcMotorSimple.Direction.FORWARD);
        home = (opMode.hardwareMap.get(NormalizedColorSensor.class, "armHome"));
        offset = 0;
    }

    public int getOffset(){
        return offset;
    }
    public int getCurrentPosition(){
        return arm.getCurrentPosition()-offset;
    }
    public int getCurrentPositionInDegrees(){
        return (arm.getCurrentPosition()-offset)/TicksInDegree;
    }

    public double getPower(){
        return arm.getPower();
    }

    public double GetHomeDist(){
        if (home instanceof DistanceSensor){
            return (((DistanceSensor) home).getDistance(DistanceUnit.MM));
        }
        return 9999.9999;
    }

    public boolean GetHomeState(){
        return GetHomeDist() < 5;
    }

    public void setMaxPow(double pow){
        MaxPow = pow;
    }

    public void Home(){
        if (Homing){
            if(GetHomeDist() > 10.0||HomeOverride){
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
                attempts = 1;
                return;
            }
            if(Math.abs(arm.getCurrentPosition() - arm.getTargetPosition()) < 30){
                opm.telemetry.addData("TargetDistance",(arm.getCurrentPosition() - arm.getTargetPosition()));
                arm.setTargetPosition((arm.getTargetPosition()*-1)*attempts);
                attempts+=1;
            }
        }
    }
    public void setRotat(int degrees){
        //if (!home.getState()){
        //    offset = arm.getCurrentPosition();
        //}
        double TicksAsDegrees = degrees*TicksInDegree;
        if (degrees>=180) {
            arm.setTargetPosition((180 * TicksInDegree) + offset);
        }
        else{
            arm.setTargetPosition(degrees*TicksInDegree + offset);
        }
        if (Math.abs(TicksAsDegrees - arm.getCurrentPosition())<=30){
            arm.setPower(0.0);
        }else{
            arm.setPower(1.0);
        }
        opm.telemetry.addData("armPower",getPower());
    }

    public void extendoServo(double extTarget){
        //extTarget=max(extTarget, 1.0);
        //extTarget=min(extTarget, 0.0);
        ext.setPosition(extTarget);
    }



    }
