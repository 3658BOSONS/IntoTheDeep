package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.bosons.Hardware.Arm;
import com.bosons.Hardware.DriveTrain;
import com.bosons.Hardware.Extender;
import com.bosons.Hardware.Hand;
import com.bosons.Utils.LEDcontroller;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.bosons.Utils.Controller;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
@Config
@TeleOp(name="TeleOp", group="Dev")

public class TeleOpWIP extends OpMode {
    enum pose{
        intake,
        bucket,
        specimen,
        home,
        climb0,
        climb1

    }
    enum height{
        high,
        low
    }

    // Declare HardWare.
    public Controller driverA = null;
    public Arm arm = null;
    public Hand hand = null;
    public Extender extendo = null;
    public DriveTrain driveTrain = null;
    //public double LedColor = 0.722;
    //public boolean Reversed = true;
    //public static double UpdateSpeed = 0.0001;

    //public ElapsedTime LEDTimer = new ElapsedTime();
    public LEDcontroller indicator = null;
    pose currentPose = pose.home;
    height currentHeight = height.high;
    int rotationClicks = 0;
    int degreePerInterval = 45;

    /*
     * Code to run ONCE when the Driver hits INIT
     */

    @Override
    public void init () {
        hand = new Hand(this);
        arm = new Arm(this);
        driveTrain = new DriveTrain(this);
        indicator = new LEDcontroller("LedOne","LedTwo",this);
        driverA = new Controller(gamepad1);
        extendo = new Extender(this);
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the Driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop () {
        //if (Reversed){
        //    LedColor -= UpdateSpeed;
        //} else {
        //    LedColor += UpdateSpeed;
        //}
        //if (LedColor>0.722-UpdateSpeed&& !Reversed){
        //    Reversed = true;
        //}
        //if (LedColor<0.277+UpdateSpeed&& Reversed){
        //    Reversed = false;
        //}
        //telemetry.addData("LedColor",LedColor);
        //telemetry.addData("UpdateSpeed",UpdateSpeed);
        //indicator.SetColor(LedColor);

        indicator.SetColor("Azure");
    }

    /*
     * Code to run ONCE when the Driver hits START
     */
    @Override
    public void start () {
        //
    }

    /*
     * Code to run REPEATEDLY after the Driver hits START but before they hit STOP
     */
    @Override
    public void loop () {
        if (arm.Homing){
            if (driverA.onButtonPress(Controller.Button.dPadLeft)){
                arm.HomeOverride = true;
            }
            hand.setRotat(1.0);
            indicator.SetColor("red");
            if (arm.GetHomeState()){
                indicator.SetColor("green");
            }
        }
        arm.Home();

        telemetry.addData("armHomeDist",arm.GetHomeDist());
        double runtime = getRuntime();

        //Drive
        double x = -driverA.getAnalogValue(Controller.Joystick.LeftX) * 1.5;
        double y = driverA.getAnalogValue(Controller.Joystick.LeftY) * 1.5;
        double turn = -driverA.getAnalogValue(Controller.Joystick.RightX)/1.2;

        double p = Math.sqrt((x * x) + (y * y));
        double theta = Math.atan2(y, x);
        driveTrain.drive(p , theta, turn);

        //Arm controls
        if (Math.abs(arm.getCurrentPositionInDegrees())>30 && currentPose != pose.home) {
            //arm.extendoServo(driverA.getTriggerValue(Controller.Trigger.Right));
        }
        else{
            arm.extendoServo(0);
        }

        if (driverA.onButtonPress(Controller.Button.dPadUp)){
            currentHeight = height.high;
        } else if (driverA.onButtonPress(Controller.Button.dPadDown)) {
            currentHeight = height.low;
        }

        if (!driverA.toggleButtonState(Controller.Button.a)||Math.abs(arm.getCurrentPositionInDegrees())<90){
           hand.grip(Hand.Pose.close);
        }else{
            hand.grip(Hand.Pose.open);
        }

        if (driverA.onButtonPress(Controller.Button.x)) {
            if (currentPose==pose.intake){
                currentPose = pose.home;
            }else{
                currentPose = pose.intake;
            }
        }

        if (driverA.onButtonPress(Controller.Button.b)) {
            if (currentPose==pose.specimen){
                currentPose = pose.home;
            }else{
                currentPose = pose.specimen;
            }
        }

        if (driverA.onButtonPress(Controller.Button.y)) {
            if (currentPose==pose.bucket){
                currentPose = pose.home;
            }else{
                currentPose = pose.bucket;
            }
        }

        if(driverA.onButtonPress(Controller.Button.rightStickButton)){
            if(currentPose==pose.climb0){
                currentPose = pose.climb1;
            }
            else{
                currentPose = pose.climb0;
            }
        }


        if(driverA.onButtonPress(Controller.Button.rightBumper)){
            rotationClicks -= 1;
        }
        if (driverA.onButtonPress(Controller.Button.leftBumper)) {
            rotationClicks += 1;
        }
        if(Math.abs(rotationClicks) > 90/degreePerInterval){//clamp
            rotationClicks = 90/degreePerInterval * rotationClicks / Math.abs(rotationClicks);
        }
        if(currentPose == pose.intake){//set
            hand.setClawAngle(degreePerInterval * rotationClicks);
        }
        else{//reset
            rotationClicks = 0;
        }

        if(!arm.Homing) {
            switch (currentPose) {
                case home: {
                    indicator.SetColor("green");
                    extendo.ExtendToTarget(0);
                    hand.setRotat(1);
                    hand.setClawAngle(0);
                    arm.setRotat(0);
                    break;
                }
                case intake: {
                    indicator.SetColor("blue");
                    extendo.ExtendToTarget(0);
                    arm.setRotat(-105);
                    if(arm.getCurrentPositionInDegrees()<-50){
                        hand.setRotat(0.61);
                        arm.extendoServo(driverA.getTriggerValue(Controller.Trigger.Right));
                    }
                    else{
                        hand.setRotat(1);
                        hand.setClawAngle(0);
                        arm.extendoServo(0);
                    }
                    break;
                }
                case bucket: {
                    indicator.SetColor("yellow");
                    switch (currentHeight){
                        case low:{
                            extendo.ExtendToTarget(0);//8000
                            arm.setRotat(160);
                            if(arm.getCurrentPositionInDegrees()>50){
                                arm.extendoServo(1);
                                hand.setRotat(0.5);
                            }
                            else{
                                arm.extendoServo(0);
                                hand.setRotat(1);
                                hand.setClawAngle(0);
                            }
                            break;
                        }
                        case high:{
                            extendo.ExtendToTarget(6500);//8000
                            arm.setRotat(160);
                            hand.setClawAngle(0);
                            if(arm.getCurrentPositionInDegrees()>50){
                                arm.extendoServo(1);
                                hand.setRotat(0.5);
                            }
                            else{
                                arm.extendoServo(0);
                                hand.setRotat(1);
                            }
                            break;
                        }
                    }
                    break;
                }
                case specimen: {
                    indicator.SetColor("red");
                    switch (currentHeight){
                        case low:{
                            break;
                        }
                        case high:{
                            break;
                        }
                    }
                    extendo.ExtendToTarget(0);
                    arm.setRotat(-200);
                    hand.setRotat(0.8);
                    if(arm.getCurrentPositionInDegrees()<-50){
                        arm.extendoServo(driverA.getTriggerValue(Controller.Trigger.Right));
                    }

                    break;
                }
                case climb0:{
                    arm.setRotat(90);
                    hand.setRotat(1);
                    arm.extendoServo(0);
                    extendo.ExtendToTarget(6500);
                    break;
                }
                case climb1:{
                    arm.setRotat(90);
                    hand.setRotat(1);
                    arm.extendoServo(0);
                    extendo.ExtendToTarget(0);
                    break;
                }
            }
        }

        driverA.updateAll();
        double deltaTime = getRuntime() - runtime;

        telemetry.addData("MotorPowerRight",extendo.getPowerRight());
        telemetry.addData("MotorPowerLeft",extendo.getPowerLeft());
        telemetry.addData("MotorTicksRight",extendo.getCurrentPosition()[0]);
        telemetry.addData("MotorTicksLeft",extendo.getCurrentPosition()[1]);
        telemetry.addData("ArmTicks",arm.getCurrentPosition());
        telemetry.addData("ArmPower",arm.getPower());
        telemetry.addData("ArmOffset",arm.getOffset());
        telemetry.addData("deltatime",deltaTime);
        telemetry.addData("armDegrees",arm.getCurrentPositionInDegrees());
        telemetry.addData("rightTrigger",driverA.getTriggerValue(Controller.Trigger.Right));


    }

    /*
     *   Code to run ONCE after the Driver hits STOP
     */
    @Override
    public void stop () {

    }

}
