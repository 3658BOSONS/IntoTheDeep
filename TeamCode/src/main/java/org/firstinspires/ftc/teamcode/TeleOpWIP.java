package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.bosons.Hardware.Extender;
import com.bosons.Hardware.Motor;
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
    // Declare HardWare.
    public Controller driverA = null;
    public Motor arm = null;
    public Extender extendo = null;

    /*
     * Code to run ONCE when the Driver hits INIT
     */
    @Override
    public void init () {
        driverA = new Controller(gamepad1);
        extendo = new Extender(this);
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
        double runtime = getRuntime();
        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setPower(1);
        if (driverA.onButtonPress(Controller.Button.dPadUp)){
            extendo.ExtendToTarget(8000);
        }else if (driverA.onButtonPress(Controller.Button.dPadDown)){
            extendo.ExtendToTarget(0);
        }
        double deltaTime = getRuntime() - runtime;
        telemetry.addData("MotorPowerRight",extendo.getPower()[0]);
        telemetry.addData("MotorPowerLeft",extendo.getPower()[1]);
        telemetry.addData("MotorTicksRight",extendo.getCurrentPosition()[0]);
        telemetry.addData("MotorTicksLeft",extendo.getCurrentPosition()[1]);
        telemetry.addData("deltatime",deltaTime);
        //if (driverA.onButtonPress(Controller.Button.dPadDown)) {
        //    arm.extendToTarget(0,0.5);
        //}
        //if(driverA.toggleButtonState(Controller.Button.y)){
        //    driveTrain.setDrivePowerCoefficient(0.5);
        //    driveTrain.setTurnPowerCoefficient(0.5);
        //}
        //else{
        //    driveTrain.setDrivePowerCoefficient(1);
        //    driveTrain.setTurnPowerCoefficient(1);
        //}

        //setting up controller input to drivetrain output ratios//
        //------------------------------------------------------//


        //telemetry.addData("Smoothing? ",arm.isSmoothing());
        //YOU NEED THESE FOR CONTROLLER AND SAFETY CHECKS
        driverA.updateAll();

    }

    /*
     *   Code to run ONCE after the Driver hits STOP
     */
    @Override
    public void stop () {

    }

}
