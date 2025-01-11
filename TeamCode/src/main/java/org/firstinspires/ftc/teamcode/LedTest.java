package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.bosons.Utils.Controller;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


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
@TeleOp(name="LedTest", group="Dev")

public class LedTest extends OpMode{
    // Declare HardWare.
    public Controller driverA = null;
    public Servo indicator = null;
    public double LedColor = 0.277;
    public boolean Reversed = true;
    public static double UpdateSpeed = 0.001;
    /*
     * Code to run ONCE when the Driver hits INIT
     */
    @Override
    public void init () {
        driverA = new Controller(gamepad1);
        indicator = this.hardwareMap.get(Servo.class,"Led");
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the Driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop () {
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

        if (Reversed){
            LedColor -= UpdateSpeed;
        } else {
            LedColor += UpdateSpeed;
        }
        if (LedColor>0.722-UpdateSpeed){
            Reversed = true;
        }
        if (LedColor<0.277+UpdateSpeed){
            Reversed = false;
        }
        telemetry.addData("LedColor",LedColor);
        indicator.setPosition(LedColor);
    }

    /*
     *   Code to run ONCE after the Driver hits STOP
     */
    @Override
    public void stop () {

    }

}
