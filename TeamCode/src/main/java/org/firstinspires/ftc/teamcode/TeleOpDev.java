package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.bosons.Utils.Controller;
import com.bosons.Hardware.DriveTrain;
import com.bosons.AutoHardware.Wrist;
import com.bosons.AutoHardware.Arm;
import com.bosons.AutoHardware.Intake;

import java.util.ArrayList;
import java.util.List;

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

public class TeleOpDev extends OpMode{
    // Declare HardWare.
    public Controller driverA = null;
    public DriveTrain driveTrain = null;
    public Intake intake = null;
    public Wrist wrist = null;
    public Arm arm = null;
    //RoadRunner Actions for teleop
    private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    /*
     * Code to run ONCE when the Driver hits INIT
     */
    @Override
    public void init () {
        intake = new Intake(hardwareMap);
        wrist = new Wrist(hardwareMap);
        arm = new Arm(hardwareMap);

        driveTrain = new DriveTrain(this);
        driverA = new Controller(gamepad1);

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

    }

    /*
     * Code to run REPEATEDLY after the Driver hits START but before they hit STOP
     */
    @Override
    public void loop () {

        //if (driverA.onButtonPress(Controller.Button.dPadUp)){
        //    arm.extendToTarget(2190,0.5);
        //}
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
        TelemetryPacket packet = new TelemetryPacket();
        double x = -driverA.getAnalogValue(Controller.Joystick.LeftX) * 1.5;
        double y = driverA.getAnalogValue(Controller.Joystick.LeftY) * 1.5;
        double turn = -driverA.getAnalogValue(Controller.Joystick.RightX)/1.2;

        double p = Math.sqrt((x * x) + (y * y));
        double theta = Math.atan2(y, x);
        driveTrain.drive(p , theta, turn); //updates the drivetrain inputs in the "DriveTrain" class
        //------------------------------------------------------//

        //arm.updatePidLoop();
        //arm.setPositionPolar(radius_arm,theta_arm);


        //Intake Controls
        if(driverA.onButtonHold(Controller.Button.a)){
            runningActions.add(
                new SequentialAction(
                    new InstantAction(() -> intake.spinIn())
                )
            );
        }
        else if(driverA.onButtonHold(Controller.Button.b)){
            runningActions.add(
                new SequentialAction(
                    new InstantAction(() -> intake.spinOut())
                )
            );
        }
        else{
            runningActions.add(
                new SequentialAction(
                    new InstantAction(() -> intake.Stop())
                )
            );
        }

        //Arm Controls
        if(driverA.toggleButtonState(Controller.Button.y)){
            runningActions.add(
                new SequentialAction(
                    new InstantAction(() -> arm.bucketHigh()),
                    new InstantAction(() -> wrist.bucket())
                )
            );

            driveTrain.setDrivePowerCoefficient(0.5);
            driveTrain.setTurnPowerCoefficient(0.5);
        }
        else if(driverA.toggleButtonState(Controller.Button.x)){
            runningActions.add(
                new SequentialAction(
                    new InstantAction(() -> arm.intakeStandby()),
                    new InstantAction(() -> wrist.standby())
                )
            );
            driveTrain.setDrivePowerCoefficient(1);
            driveTrain.setTurnPowerCoefficient(1);
        }
        else{
            runningActions.add(
                new SequentialAction(
                    new InstantAction(() -> arm.home()),
                    new InstantAction(() -> wrist.standby())
                )
            );
            driveTrain.setDrivePowerCoefficient(1);
            driveTrain.setTurnPowerCoefficient(1);
        }

        // update Running Actions
        // what this does is it checks if the action has finished yet
        // if not do it again on the next loop.
        List<Action> newActions = new ArrayList<>();
        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet)) {
                newActions.add(action);
            }
        }
        runningActions = newActions;

        dash.sendTelemetryPacket(packet);
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
