package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.bosons.AutoHardware.Arm;
import com.bosons.AutoHardware.Intake;
import com.bosons.AutoHardware.Wrist;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import RoadRunner.MecanumDrive;

/*
* .setTangent(Math.toRadians(180))
                .lineToX(-70+8.375)
                .build());
* */
@Config
@Autonomous(name = "Park", group = "Comp",preselectTeleOp = "TeleOp")
public class AutoPark extends LinearOpMode {

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

        //POSITION DEFINITIONS
        Pose2d initialPose = new Pose2d(31.5-(16*3), 70.5-8.375, Math.toRadians(90));

        //HARDWARE DEFINITIONS
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(5,5),5));        //ACTION SHORTCUTS

        TrajectoryActionBuilder park = drive.actionBuilder(initialPose)
                .setTangent(Math.toRadians(180))
                .lineToX(-61.625);

        //EXECUTE ACTIONS
        Actions.runBlocking(
                new SequentialAction(
                        park.build()
                )
        );
    }
}

