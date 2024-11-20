package com.bosons.AutoHardware;

import android.net.wifi.hotspot2.pps.HomeSp;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.bosons.Hardware.Motor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Arm {
    //opMode for telemetry and hardware map;

    //arm extension motors
    public Motor rightExtendoMotor;
    public Motor leftExtendoMotor;

    //Rotation motor and pid//
    public Motor rightRotationMotor;
    public Motor leftRotationMotor;

    public PIDController controller;
    public static double p = 0.0016, i = 0.01, d = 0.00005;
    public static double f = 0.15;
    public static int rotTarget = 0;
    //---------------------//
    public static int extensionTarget = 0;
    public int acceptableExtensionError = 30;
    public int maxExtensionTicks = 2185;
    public double Power;

    //Constants
    private final double ticks_in_degree = 22.755555555555556;
    private final double ticks_per_cm = 44.73039215686274;

    /*
    Motion Profiling bullshit
    private double slope;
    private double radius_0;
    private double theta_0;
    */

    //Motion Smoothing;
    private int thetaTicksInitial;
    private int thetaTicks;


    public Arm(HardwareMap hwm){

        rightExtendoMotor = new Motor("RightExt",hwm);
        rightExtendoMotor.setTargetPosition(0);
        rightExtendoMotor.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.REVERSE);

        leftExtendoMotor = new Motor("LeftExt",hwm);
        leftExtendoMotor.setTargetPosition(0);
        leftExtendoMotor.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.FORWARD);

        rightRotationMotor = new Motor("RightRot",hwm);
        rightRotationMotor.setConstants(DcMotor.RunMode.RUN_WITHOUT_ENCODER,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.FORWARD);

        leftRotationMotor = new Motor("LeftRot",hwm);
        leftRotationMotor.setConstants(DcMotor.RunMode.RUN_WITHOUT_ENCODER,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.REVERSE);

        controller = new PIDController(p,i,d);
        
        

    }

    public class intakeStandby implements Action{
        private final ElapsedTime smoothingTimer = new ElapsedTime();
        public boolean run(@NonNull TelemetryPacket packet){

            int radius = 65;
            int theta = 15;
            extensionTarget = (int)((radius-40.8)*44.73039215686274);//subtract the fixed length of the arm
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            if(extensionTarget<0){extensionTarget=0;}
            //convert theta (degrees) to ticks
            thetaTicks = (int)((theta+28)* (8192/360.0));//subtract the initial -28 degree position of the arm
            if(thetaTicks>2700){thetaTicks=2700;}
            if(thetaTicks<0){thetaTicks=0;}
            packet.put("ThetaTicks",thetaTicks);

            //set the change in ticks over the set interval
            thetaTicksInitial = leftRotationMotor.getCurrentPosition();
            double timeSlope = (thetaTicks-thetaTicksInitial)/(1.5*1000);//ticks per millisecond

            rotTarget = (int) (thetaTicksInitial + timeSlope * smoothingTimer.milliseconds());
            smoothingTimer.reset();


            if((timeSlope>0 && rotTarget>thetaTicks)||(timeSlope<0 && rotTarget<thetaTicks)||(timeSlope==0)){
                rotTarget = thetaTicks;
            }//prevent any overshooting
            
            if(extensionTarget<0){extensionTarget=0;}
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            //set target position
            rightExtendoMotor.setTargetPosition(extensionTarget);
            leftExtendoMotor.setTargetPosition(extensionTarget);
            //set power if it wont burn the motor
            if(!rightExtendoMotor.burnCheck(acceptableExtensionError)){
                rightExtendoMotor.setPower(0.5);
            }
            if(!leftExtendoMotor.burnCheck(acceptableExtensionError)){
                leftExtendoMotor.setPower(0.5);
            }
            rotTarget = thetaTicks;
            controller.setPID(p,i,d);
            int armPos = leftRotationMotor.getCurrentPosition();
            double pid = controller.calculate(armPos,rotTarget);
            double ff = Math.cos(Math.toRadians(rotTarget/22.755555555555556))*f;

            double power = (pid + ff)*0.5;

            packet.put("pos ",armPos);
            packet.put("target ",rotTarget);
            packet.put("armAngle ", (leftRotationMotor.getCurrentPosition()/ticks_in_degree)-28);
            packet.put("armLength ",((leftExtendoMotor.getCurrentPosition()+rightExtendoMotor.getCurrentPosition())/2.0)/(ticks_per_cm)+40.8);


            if(Math.abs(armPos-thetaTicks)<30&&(rightExtendoMotor.burnCheck(acceptableExtensionError)&&leftExtendoMotor.burnCheck(acceptableExtensionError))) {
                rightRotationMotor.setPower(0);
                leftRotationMotor.setPower(0);
                return false;
            }
            else {
                rightRotationMotor.setPower(power);
                leftRotationMotor.setPower(power);
                return true;
            }
        }
    }
    public Action intakeStandby(){
        return new intakeStandby();
    }

    public class intakeActive implements Action{
        private final ElapsedTime smoothingTimer = new ElapsedTime();

        public boolean run(@NonNull TelemetryPacket packet){

            extensionTarget = (int)((65-40.8)*44.73039215686274);//subtract the fixed length of the arm
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            if(extensionTarget<0){extensionTarget=0;}
            //convert theta (degrees) to ticks
            thetaTicks = (int)((-6+28)*22.755555555555556);//subtract the initial -28 degree position of the arm
            if(thetaTicks>2700){thetaTicks=2700;}
            if(thetaTicks<0){thetaTicks=0;}

            //set the change in ticks over the set interval
            thetaTicksInitial = leftRotationMotor.getCurrentPosition();
            double timeSlope = (thetaTicks-thetaTicksInitial)/(0.9*1000);//ticks per millisecond
            smoothingTimer.reset();
            
            if((timeSlope>0 && rotTarget>thetaTicks)||(timeSlope<0 && rotTarget<thetaTicks)||(timeSlope==0)){
                rotTarget = thetaTicks;
            }//prevent any overshooting
            
            if(extensionTarget<0){extensionTarget=0;}
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            //set target position
            rightExtendoMotor.setTargetPosition(extensionTarget);
            leftExtendoMotor.setTargetPosition(extensionTarget);
            //set power if it wont burn the motor
            if(!rightExtendoMotor.burnCheck(acceptableExtensionError)){
                rightExtendoMotor.setPower(0.5);
            }
            if(!leftExtendoMotor.burnCheck(acceptableExtensionError)){
                leftExtendoMotor.setPower(0.5);
            }

            rotTarget = thetaTicks;
            controller.setPID(p,i,d);
            int armPos = leftRotationMotor.getCurrentPosition();
            double pid = controller.calculate(armPos,rotTarget);
            double ff = Math.cos(Math.toRadians(rotTarget/22.755555555555556))*f;

            double power = (pid + ff)*0.5;

            packet.put("pos ",armPos);
            packet.put("target ",rotTarget);
            packet.put("armAngle ", (leftRotationMotor.getCurrentPosition()/ticks_in_degree)-28);
            packet.put("armLength ",((leftExtendoMotor.getCurrentPosition()+rightExtendoMotor.getCurrentPosition())/2.0)/(ticks_per_cm)+40.8);


            if(Math.abs(armPos-thetaTicks)<30&&(rightExtendoMotor.burnCheck(acceptableExtensionError)&&leftExtendoMotor.burnCheck(acceptableExtensionError))) {
                rightRotationMotor.setPower(0);
                leftRotationMotor.setPower(0);
                return false;
            }
            else {
                rightRotationMotor.setPower(power);
                leftRotationMotor.setPower(power);
                return true;
            }
        }
    }
    public Action intakeActive(){
        return new intakeActive();
    }

    public class bucketHigh implements Action{
        private final ElapsedTime smoothingTimer = new ElapsedTime();

        public boolean run(@NonNull TelemetryPacket packet){
            acceptableExtensionError = 5;
            extensionTarget = (int)((84.6-40.8)*44.73039215686274);//subtract the fixed length of the arm
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            if(extensionTarget<0){extensionTarget=0;}
            //convert theta (degrees) to ticks
            thetaTicks = (int)((90+28)*22.755555555555556);//subtract the initial -28 degree position of the arm
            if(thetaTicks>2700){thetaTicks=2700;}
            if(thetaTicks<0){thetaTicks=0;}

            //set the change in ticks over the set interval
            thetaTicksInitial = leftRotationMotor.getCurrentPosition();
            double timeSlope = (thetaTicks-thetaTicksInitial)/(0.3*1000);//ticks per millisecond
            smoothingTimer.reset();
            
            if((timeSlope>0 && rotTarget>thetaTicks)||(timeSlope<0 && rotTarget<thetaTicks)||(timeSlope==0)){
                rotTarget = thetaTicks;
            }//prevent any overshooting
            
            if(extensionTarget<0){extensionTarget=0;}
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            //set target position
            rightExtendoMotor.setTargetPosition(extensionTarget);
            leftExtendoMotor.setTargetPosition(extensionTarget);
            //set power if it wont burn the motor
            if(!rightExtendoMotor.burnCheck(acceptableExtensionError)){
                rightExtendoMotor.setPower(0.5);
            }
            if(!leftExtendoMotor.burnCheck(acceptableExtensionError)){
                leftExtendoMotor.setPower(0.5);
            }
            rotTarget = thetaTicks;

            controller.setPID(p,i,d);
            int armPos = leftRotationMotor.getCurrentPosition();
            double pid = controller.calculate(armPos,rotTarget);
            double ff = Math.cos(Math.toRadians(rotTarget/22.755555555555556))*f;

            double power = (pid + ff)*0.5;

            packet.put("pos ",armPos);
            packet.put("target ",rotTarget);
            packet.put("armAngle ", (leftRotationMotor.getCurrentPosition()/ticks_in_degree)-28);
            packet.put("armLength ",((leftExtendoMotor.getCurrentPosition()+rightExtendoMotor.getCurrentPosition())/2.0)/(ticks_per_cm)+40.8);



            if(Math.abs(armPos-thetaTicks)<100&&(rightExtendoMotor.burnCheck(acceptableExtensionError)&&leftExtendoMotor.burnCheck(acceptableExtensionError))) {
                rightRotationMotor.setPower(0);
                leftRotationMotor.setPower(0);
                acceptableExtensionError = 30;
                return false;
            }
            else {
                rightRotationMotor.setPower(power);
                leftRotationMotor.setPower(power);
                return true;
            }
        }
    }
    public Action bucketHigh(){
        return new bucketHigh();
    }


    public class specimenHigh implements Action{
        private final ElapsedTime smoothingTimer = new ElapsedTime();

        public boolean run(@NonNull TelemetryPacket packet){

            extensionTarget = (int)((84.6-40.8)*44.73039215686274);//subtract the fixed length of the arm
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            if(extensionTarget<0){extensionTarget=0;}
            //convert theta (degrees) to ticks
            thetaTicks = (int)((90+28)*22.755555555555556);//subtract the initial -28 degree position of the arm
            if(thetaTicks>2700){thetaTicks=2700;}
            if(thetaTicks<0){thetaTicks=0;}

            //set the change in ticks over the set interval
            thetaTicksInitial = leftRotationMotor.getCurrentPosition();
            double timeSlope = (thetaTicks-thetaTicksInitial)/(0.3*1000);//ticks per millisecond
            smoothingTimer.reset();
            
            if((timeSlope>0 && rotTarget>thetaTicks)||(timeSlope<0 && rotTarget<thetaTicks)||(timeSlope==0)){
                rotTarget = thetaTicks;
            }//prevent any overshooting

            if(extensionTarget<0){extensionTarget=0;}
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            //set target position
            rightExtendoMotor.setTargetPosition(extensionTarget);
            leftExtendoMotor.setTargetPosition(extensionTarget);
            //set power if it wont burn the motor
            if(!rightExtendoMotor.burnCheck(acceptableExtensionError)){
                rightExtendoMotor.setPower(0.5);
            }
            if(!leftExtendoMotor.burnCheck(acceptableExtensionError)){
                leftExtendoMotor.setPower(0.5);
            }

            rotTarget = thetaTicks;
            controller.setPID(p,i,d);
            int armPos = leftRotationMotor.getCurrentPosition();
            double pid = controller.calculate(armPos,rotTarget);
            double ff = Math.cos(Math.toRadians(rotTarget/22.755555555555556))*f;

            double power = (pid + ff)*0.5;

            packet.put("pos ",armPos);
            packet.put("target ",rotTarget);
            packet.put("armAngle ", (leftRotationMotor.getCurrentPosition()/ticks_in_degree)-28);
            packet.put("armLength ",((leftExtendoMotor.getCurrentPosition()+rightExtendoMotor.getCurrentPosition())/2.0)/(ticks_per_cm)+40.8);


            if(Math.abs(armPos-thetaTicks)<30&&(rightExtendoMotor.burnCheck(acceptableExtensionError)&&leftExtendoMotor.burnCheck(acceptableExtensionError))) {
                rightRotationMotor.setPower(0);
                leftRotationMotor.setPower(0);
                return false;
            }
            else {
                rightRotationMotor.setPower(power);
                leftRotationMotor.setPower(power);
                return true;
            }
        }
    }
    public Action specimenHigh(){
        return new specimenHigh();
    }
    public class home implements Action{
        private final ElapsedTime smoothingTimer = new ElapsedTime();
        public boolean run(@NonNull TelemetryPacket packet){


            rightRotationMotor.setPower(0);
            leftRotationMotor.setPower(0);
            int radius = 0;
            thetaTicks = 0;
            extensionTarget = (int)((radius-40.8)*44.73039215686274);//subtract the fixed length of the arm
            if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
            if(extensionTarget<0){extensionTarget=0;}
            rightExtendoMotor.setTargetPosition(extensionTarget);
            leftExtendoMotor.setTargetPosition(extensionTarget);
            //set power if it wont burn the motor
            if(!rightExtendoMotor.burnCheck(acceptableExtensionError)){
                rightExtendoMotor.setPower(0.5);
            }
            if(!leftExtendoMotor.burnCheck(acceptableExtensionError)){
                leftExtendoMotor.setPower(0.5);
            }
            double armPos = ((leftRotationMotor.getCurrentPosition()/ticks_in_degree)+28);

            double power = 0.0;//(pid + ff)*0.01;


            if (armPos<80.0){power = 0.1;}
            else{power=-0.3;}
            if (armPos <= -10.0){power = 0.0;}
            packet.put("pos ",armPos);
            packet.put("target ",rotTarget);
            packet.put("armAngle ", (leftRotationMotor.getCurrentPosition()/ticks_in_degree)-28);
            packet.put("armLength ",((leftExtendoMotor.getCurrentPosition()+rightExtendoMotor.getCurrentPosition())/2.0)/(ticks_per_cm)+40.8);
            packet.put("power",power);

            packet.put("RightBurnCheck",rightExtendoMotor.burnCheck(acceptableExtensionError));
            packet.put("LeftBurnCheck",leftExtendoMotor.burnCheck(acceptableExtensionError));
            packet.put("Is Arm Done Lowering",Math.abs(armPos-thetaTicks)<30);
            packet.put("armpos",Math.abs(armPos-thetaTicks));
            if(Math.abs(armPos-thetaTicks)<30) {
                rightRotationMotor.setPower(0);
                leftRotationMotor.setPower(0);
                if (rightExtendoMotor.burnCheck(acceptableExtensionError)&&leftExtendoMotor.burnCheck(acceptableExtensionError)){
                    return false;
                }
            }
            else {
                rightRotationMotor.setPower(power);
                leftRotationMotor.setPower(power);
            }
            return true;
        }
    }
    public Action home(){
        return new home();
    }
}