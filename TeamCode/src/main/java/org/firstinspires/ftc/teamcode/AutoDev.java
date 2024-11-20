package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// road runner Imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;

//Non road runner Imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import RoadRunner.MecanumDrive;


import com.bosons.AutoHardware.Arm;
import com.bosons.AutoHardware.Intake;
import com.qualcomm.robotcore.util.ElapsedTime;


@Config
@Autonomous(name = "Auto", group = "Dev")
public class AutoDev extends LinearOpMode {
    public ElapsedTime timer = new ElapsedTime();


    public SleepAction waitMilliseconds(int milliseconds){
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

        //POSITION DEFINITIONS
        Pose2d initialPose = new Pose2d(31.5, 70.5-8.375, Math.toRadians(90));
        Pose2d BlueNet = new Pose2d(55.0,55.0,Math.toRadians(45));

        //HARDWARE DEFINITIONS
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Intake intake = new Intake(hardwareMap);
        Arm arm = new Arm(hardwareMap);

        //ACTION SHORTCUTS
        SequentialAction putInHighBucket = new SequentialAction(
                arm.bucketHigh(),
                intake.spinOut(),
                waitMilliseconds(2000),
                intake.Stop(),
                arm.home()
        );

        //MOVEMENT ACTIONS
        TrajectoryActionBuilder Fucket1 = drive.actionBuilder(initialPose)
                .lineToY(48.0)
                .turnTo(Math.toRadians(45))
                .splineToLinearHeading(BlueNet,0.0);


        //EXECUTE ACTIONS
        Actions.runBlocking(
                new SequentialAction(
                        Fucket1.build(),
                        putInHighBucket
                )
        );








    }
}
