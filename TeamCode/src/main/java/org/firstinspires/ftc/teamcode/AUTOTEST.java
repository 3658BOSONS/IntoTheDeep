package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// road runner Imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;

//Non road runner Imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import RoadRunner.MecanumDrive;

//Boson Imports
import com.bosons.AutoHardware.Wrist;
import com.bosons.AutoHardware.Arm;
import com.bosons.AutoHardware.Intake;

@Config
@Autonomous(name = "AUTOTEST", group = "Dev")
public class AUTOTEST extends LinearOpMode {

    public SleepAction sleeb(int milliseconds){
        return new SleepAction(milliseconds/1000.0);
    }

    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        if (isStopRequested()) return;
        waitForStart();
        //Tool Definitions

        //HARDWARE DEFINITIONS

        Intake intake = new Intake(hardwareMap);
        Wrist wrist = new Wrist(hardwareMap);
        Arm arm = new Arm(this);

        //ACTION SHORTCUTS
        SequentialAction IntakeCube = new SequentialAction(
                wrist.intake(),
                intake.spinIn(),
                sleeb(100)
        );


        //EXECUTE ACTIONS
        Actions.runBlocking(
                new SequentialAction(
                        IntakeCube,
                        sleeb((50*1000))
                )
        );
    }
}
