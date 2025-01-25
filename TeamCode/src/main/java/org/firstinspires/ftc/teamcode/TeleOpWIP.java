package org.firstinspires.ftc.teamcode;

import static java.lang.Math.*;

import com.acmerobotics.dashboard.config.Config;
import com.bosons.AutoHardware.Wrist;
import com.bosons.Hardware.Arm;
import com.bosons.Hardware.DriveTrain;
import com.bosons.Hardware.Extender;
import com.bosons.Hardware.Hand;
import com.bosons.Utils.LEDcontroller;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.bosons.Utils.Controller;
import com.qualcomm.robotcore.hardware.DcMotor;

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
        highBucket,
        lowBucket,
        specimenHigh,
        specimenLow,
        home,
    }

    // Declare HardWare.
    public Controller driverA = null;
    public Arm arm = null;
    public Hand hand = null;
    public Extender extendo = null;
    public DriveTrain driveTrain = null;
    public Boolean isHomed = false;
    public double LedColor = 0.722;
    public boolean Reversed = true;
    public static double UpdateSpeed = 0.0001;
    public LEDcontroller indicator = null;
    pose currentPose = pose.home;
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
        if (Reversed){
            LedColor -= UpdateSpeed;
        } else {
            LedColor += UpdateSpeed;
        }
        if (LedColor>0.722-UpdateSpeed&& !Reversed){
            Reversed = true;
        }
        if (LedColor<0.277+UpdateSpeed&& Reversed){
            Reversed = false;
        }
        telemetry.addData("LedColor",LedColor);
        telemetry.addData("UpdateSpeed",UpdateSpeed);
        indicator.SetColor(LedColor);
    }

    /*
     * Code to run ONCE when the Driver hits START
     */
    @Override
    public void start () {
        //driverA.updateAll();
    }

    /*
     * Code to run REPEATEDLY after the Driver hits START but before they hit STOP
     */
    @Override
    public void loop () {
        double runtime = getRuntime();


        double x = -driverA.getAnalogValue(Controller.Joystick.LeftX) * 1.5;
        double y = driverA.getAnalogValue(Controller.Joystick.LeftY) * 1.5;
        double turn = -driverA.getAnalogValue(Controller.Joystick.RightX)/1.2;

        double p = Math.sqrt((x * x) + (y * y));
        double theta = Math.atan2(y, x);
        driveTrain.drive(p , theta, turn);

        if (driverA.onButtonPress(Controller.Button.rightBumper)){
            arm.Home();
        }

        if (Math.abs(arm.getCurrentPositionInDegrees())>30)
            arm.extendoServo(driverA.getTriggerValue(Controller.Trigger.Right));


        if (driverA.toggleButtonState(Controller.Button.a)){
           hand.setPose(Hand.Pose.open);
        }else{
            hand.setPose(Hand.Pose.close);
        }

        if (driverA.onButtonPress(Controller.Button.x)) {
            if (currentPose==pose.intake){
                currentPose = pose.home;
            }else{
                currentPose = pose.intake;
            }
        }



        /*
        if(driverA.onButtonPress(Controller.Button.rightBumper)){
            extendo.FORBIDDIN();
        }
        */

        double deltaTime = getRuntime() - runtime;

        telemetry.addData("MotorPowerRight",extendo.getPower()[0]);
        telemetry.addData("MotorPowerLeft",extendo.getPower()[1]);
        telemetry.addData("MotorTicksRight",extendo.getCurrentPosition()[0]);
        telemetry.addData("MotorTicksLeft",extendo.getCurrentPosition()[1]);
        telemetry.addData("ArmTicks",arm.getCurrentPosition());
        telemetry.addData("ArmPower",arm.getPower());
        telemetry.addData("ArmOffset",arm.getOffset());
        telemetry.addData("deltatime",deltaTime);

        switch (currentPose){
            case home:{
                extendo.ExtendToTarget(0);
                hand.setRotat(0.0);
                arm.setRotat(0);
                break;
            }
            case intake:{
                extendo.ExtendToTarget(0);
                arm.setRotat(-90);
                hand.setRotat(0.6);
                break;
            }
            case lowBucket:{
                break;
            }
            case highBucket:{
                break;
            }
            case specimenLow:{
                break;
            }
            case specimenHigh:{
                break;
            }

        }

        driverA.updateAll();

    }

    /*
     *   Code to run ONCE after the Driver hits STOP
     */
    @Override
    public void stop () {

    }

}
