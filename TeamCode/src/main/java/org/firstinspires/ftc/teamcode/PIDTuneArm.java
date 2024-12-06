package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.bosons.Hardware.Arm;
import com.bosons.Hardware.DriveTrain;
import com.bosons.Utils.Controller;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp(name="PIDTuneArm", group="Tuning")

public class PIDTuneArm extends OpMode{
    // Declare HardWare.
    public Arm arm = null;
    public static int tuningTarget = 0;
    public static int ExtendoTargo = 0;
    public static double P = 0;
    public static double I = 0;
    public static double D = 0;
    public static double F = 0;


    /*
     * Code to run ONCE when the Driver hits INIT
     */
    @Override
    public void init () {
        arm = new Arm(this,0.8);

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
        arm.setWristServo(0);
    }

    /*
     * Code to run REPEATEDLY after the Driver hits START but before they hit STOP
     */
    @Override
    public void loop () {
        arm.setPIDFCoefficients(P,I,D,F);
        arm.updatePidLoop(tuningTarget);
        arm.extendToTarget(ExtendoTargo,0.5);
    }

    /*
     *   Code to run ONCE after the Driver hits STOP
     */
    @Override
    public void stop () {

    }

}
