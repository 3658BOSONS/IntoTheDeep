package com.bosons.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Motor { //DCMotor Wrapper Class with added functionality and usb communication spam avoidance and speedometer

    private final DcMotor motor;
    private double lastPower;
    private int Offset;

    public void setOffset(int offset){
        this.Offset = offset;
    }

    public int getOffset(){
        return(this.Offset);
    }

    public Motor(String name, OpMode op)
    {
        this.Offset=0;
        motor = op.hardwareMap.get(DcMotor.class, name);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public Motor(String name, HardwareMap hwm)
    {
        motor = hwm.get(DcMotor.class, name);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setConstants(DcMotor.RunMode mode, DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor.Direction direction) {
        motor.setMode(mode);
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        motor.setDirection(direction);
    }



    public void setRunMode(DcMotor.RunMode mode) {
        motor.setMode(mode);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        motor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    public void setDirection(DcMotor.Direction direction) {
        motor.setDirection(direction);
    }

    public void setTargetPosition(int position) {
        motor.setTargetPosition(position+this.Offset);
    }

    public double getPower(){
        return lastPower;
    }

    public void setPower(double power) {
        if(power == lastPower)
            return;
        if(power < -1)
            power = -1;
        else if(power > 1)
            power = 1;
        motor.setPower(power);
        lastPower = power;
    }



    public void resetEncoder() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public int getCurrentPosition() {
        return motor.getCurrentPosition()-this.Offset;
    }
    public int getRawPosition() {
        return motor.getCurrentPosition();
    }


    public int getTargetPosition() {
        return motor.getTargetPosition();
    }
    public boolean burnCheck(int acceptableError) {
        int targetDist = Math.abs(getTargetPosition() - getCurrentPosition());//get positional error;
        if (targetDist <= acceptableError+this.Offset) {//check if error is acceptable;
            setPower(0);//shut off power to prevent burning;
            return true;//if within range of target;
        }
        else {
            return false;//if not within range of target;
        }
    }
    public boolean burnCheck(int acceptableError,Boolean checkMode) {
        int targetDist = Math.abs(getTargetPosition() - getCurrentPosition());//get positional error;
        if (targetDist <= acceptableError+this.Offset) {//check if error is acceptable;
            if (!checkMode){
                setPower(0);
            }
            //shut off power to prevent burning;
            return true;//if within range of target;
        }
        else {
            return false;//if not within range of target;
        }
    }
}