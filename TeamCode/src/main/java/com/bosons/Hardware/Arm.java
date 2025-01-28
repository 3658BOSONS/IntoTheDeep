package com.bosons.Hardware;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

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
    public Servo ext = null;
    public DigitalChannel home;
    private int TicksInDegree = (int) 5281.1/360;

    public Arm(OpMode opMode){
        opm = opMode;
        arm = new Motor("arm",opMode);
        ext = opMode.hardwareMap.get(Servo.class,"armExt");
        arm.setPower(0.0);
        arm.setTargetPosition(0);
        arm.setConstants(DcMotor.RunMode.RUN_TO_POSITION, DcMotor.ZeroPowerBehavior.BRAKE, DcMotorSimple.Direction.FORWARD);
        home = (opMode.hardwareMap.get(DigitalChannel.class, "armHome"));
        home.setMode(DigitalChannel.Mode.INPUT);
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

    public void Home(){

        opm.telemetry.addData("Home Button", home.getState());
        if (Homing){
            if(home.getState()){
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
        if (Math.abs(TicksAsDegrees - arm.getCurrentPosition())<=10){
            arm.setPower(0.0);
        }else{
            arm.setPower(1.0);
        }
        opm.telemetry.addData("armPower",getPower());
    }
    public boolean getHomeState(){
        return !home.getState();
    }
    public void extendoServo(double extTarget){
        //extTarget=max(extTarget, 1.0);
        //extTarget=min(extTarget, 0.0);
        ext.setPosition(extTarget);
    }



    }
