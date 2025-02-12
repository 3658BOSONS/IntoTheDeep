package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.bosons.Hardware.Arm;
import com.bosons.Hardware.DriveTrain;
import com.bosons.Hardware.Extender;
import com.bosons.Hardware.Hand;
import com.bosons.Utils.Controller;
import com.bosons.Utils.LEDcontroller;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
@TeleOp(name="WHO FUCKED THE ARM", group="MANUAL OVERRIDE")

public class WhoFuckedTheArm extends OpMode {
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
    public Controller driverB = null;
    public Arm arm = null;
    public Hand hand = null;
    public Extender extendo = null;
    public DriveTrain driveTrain = null;
    public double LedColor = 0.722;
    public boolean Reversed = true;
    public static double UpdateSpeed = 0.0001;

    public ElapsedTime LEDTimer = new ElapsedTime();
    public LEDcontroller indicator = null;
    pose currentPose = pose.home;
    height currentHeight = height.high;

    /*
     * Code to run ONCE when the Driver hits INIT
     */

    @Override
    public void init () {
        hand = new Hand(this);
        arm = new Arm(this);
        driveTrain = new DriveTrain(this);
        indicator = new LEDcontroller("LedOne","LedTwo",this);
        driverB = new Controller(gamepad2);
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

        if(LEDTimer.seconds()>=2 && LEDTimer.seconds()<4) {
            indicator.SetColor("azure");
        } else if (LEDTimer.seconds()>=4) {
            indicator.SetColor(0);
            LEDTimer.reset();
        }

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
            indicator.SetColor("red");
            if (arm.GetHomeState()){
                indicator.SetColor("green");
            }
        }

        if (driverB.onButtonHold(Controller.Button.a)){
            extendo.FORBIDDIN(((driverB.getTriggerValue(Controller.Trigger.Left)+(driverB.getTriggerValue(Controller.Trigger.Right)/2))));
        }else{
            extendo.Stop();
        }

        arm.Home();

        double runtime = getRuntime();
        
        double deltaTime = getRuntime() - runtime;

        telemetry.addData("MotorPowerRight",extendo.getPowerRight());
        telemetry.addData("MotorPowerLeft",extendo.getPowerLeft());
        telemetry.addData("ArmTicks",arm.getCurrentPosition());
        telemetry.addData("ArmPower",arm.getPower());
        telemetry.addData("ArmOffset",arm.getOffset());
        telemetry.addData("deltatime",deltaTime);
        telemetry.addData("armDegrees",arm.getCurrentPositionInDegrees());
        telemetry.addData("rightTrigger",driverB.getTriggerValue(Controller.Trigger.Right));
        driverB.updateAll();

    }

    /*
     *   Code to run ONCE after the Driver hits STOP
     */
    @Override
    public void stop () {

    }

}
