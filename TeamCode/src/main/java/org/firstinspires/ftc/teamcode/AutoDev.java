package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// road runner Imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

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
        Pose2d initialPose = new Pose2d(11.8, 61.7, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Intake intake = new Intake(hardwareMap);
        Arm arm = new Arm(hardwareMap);




        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);


        Actions.runBlocking(
                new SequentialAction(
                        intake.spinIn(),
                        arm.bucketHigh(),
                        intake.spinOut(),
                        waitMilliseconds(3000),
                        arm.home(),
                        tab1.build()
                )
        );









    }
}
