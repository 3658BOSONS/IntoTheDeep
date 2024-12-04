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
@Autonomous(name = "Auto", group = "Dev")
public class AutoDev extends LinearOpMode {

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
        Pose2d initialPose = new Pose2d(31.5, 70.5-8.375, Math.toRadians(90));
        Pose2d BlueNet = new Pose2d(55.0,55.0,Math.toRadians(45));

        //HARDWARE DEFINITIONS
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Intake intake = new Intake(hardwareMap);
        Wrist wrist = new Wrist(hardwareMap);
        Arm arm = new Arm(hardwareMap);

        //ACTION SHORTCUTS


        //set the arm to the default position
        SequentialAction homeArm = new SequentialAction(
                intake.Stop(),
                wrist.standby(),
                arm.home()
        );
        //move arm to intake position and grab cube
        ParallelAction intakeSpecimen = new ParallelAction(
                wrist.intake(),
                intake.spinIn()
        );
        //move arm to High bucket and drop specimen
        SequentialAction dumpInHighBucket = new SequentialAction(
                wrist.straight(),
                arm.bucketHigh(),
                wrist.bucket(),
                //sleeb(1000),
                intake.spinOut(),
                sleeb(500)
        );



        //MOVEMENT ACTIONS
        TrajectoryActionBuilder Bucket1 = drive.actionBuilder(initialPose)
                .lineToY(48.0)
                .splineToLinearHeading(BlueNet,60.0);

        TrajectoryActionBuilder inchForward = drive.actionBuilder(BlueNet)
                .lineToY(58.0);

        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(58,58,45.0))
                .setTangent(45.0)
                .lineToYLinearHeading(36.0,Math.toRadians(180))
                .strafeTo(new Vector2d(-36.0,48.0))
                .strafeTo(new Vector2d(-36.0,70.5-8.375));

        //EXECUTE ACTIONS
        Actions.runBlocking(
                new SequentialAction(
                        intakeSpecimen,
                        //sleeb(500),
                        wrist.zero(),
                        Bucket1.build(),
                        arm.bucketHigh(),
                        wrist.bucket(),
                        inchForward.build(),
                        dumpInHighBucket,
                        homeArm,
                        wrist.zero(),
                        park.build()
                        )
        );








    }
}
