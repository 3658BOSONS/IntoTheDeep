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
@Autonomous(name = "Auto-Dev", group = "Dev")
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

        waitForStart();
        //Tool Definitions

        //POSITION DEFINITIONS
        Pose2d initialPose = new Pose2d(31.5, 70.5-8.375, Math.toRadians(90));
        Pose2d IntakeOne = new Pose2d(49.0,44,Math.toRadians(90));
        Pose2d IntakeTwo = new Pose2d(59.0,44,Math.toRadians(90));
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
                wrist.intake(),
                arm.home(),
                wrist.zero()
        );
        //set the arm to the default position again
        SequentialAction homeArm2 = new SequentialAction(
                intake.Stop(),
                wrist.straight(),
                arm.home(),
                wrist.zero()
        );
        SequentialAction homeArm3 = new SequentialAction(
                intake.Stop(),
                wrist.intake(),
                arm.home(),
                wrist.zero()
        );
        SequentialAction homeArm4 = new SequentialAction(
                intake.Stop(),
                wrist.intake(),
                arm.home(),
                wrist.zero()
        );
        SequentialAction homeArm5 = new SequentialAction(
                intake.Stop(),
                wrist.intake(),
                arm.home(),
                wrist.zero()
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
                intake.spinOut(),
                sleeb(500),
                wrist.straight(),
                sleeb(100)
        );
        SequentialAction dumpInHighBucket2 = new SequentialAction(
                wrist.straight(),
                arm.bucketHigh(),
                wrist.bucket(),
                intake.spinOut(),
                sleeb(500),
                wrist.straight(),
                sleeb(100)
        );
        SequentialAction dumpInHighBucket3 = new SequentialAction(
                wrist.straight(),
                arm.bucketHigh(),
                wrist.bucket(),
                intake.spinOut(),
                sleeb(500),
                wrist.straight(),
                sleeb(100)
        );

        SequentialAction IntakeCube = new SequentialAction(
                wrist.intake(),
                intake.spinIn(),
                arm.intakeActive()
        );
        SequentialAction IntakeCube2 = new SequentialAction(
                wrist.intake(),
                intake.spinIn(),
                arm.intakeActive()
        );



        //MOVEMENT ACTIONS
        TrajectoryActionBuilder Bucket1 = drive.actionBuilder(initialPose)
                .lineToY(48.0)
                .splineToLinearHeading(BlueNet,60.0);

        TrajectoryActionBuilder Bucket2 = drive.actionBuilder(IntakeOne)
                .lineToY(48.0)
                .splineToLinearHeading(BlueNet,60.0);

        TrajectoryActionBuilder Bucket3 = drive.actionBuilder(IntakeTwo)
                .lineToY(48.0)
                .splineToLinearHeading(BlueNet,60.0);

        TrajectoryActionBuilder inchForward = drive.actionBuilder(BlueNet)
                .lineToY(58.0);

        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(58,58,45.0))
                .lineToYLinearHeading(.0,Math.toRadians(0))
                .setTangent(Math.toRadians(0))
                .lineToXLinearHeading(-36.0,Math.toRadians(0))
                .strafeTo(new Vector2d(-36.0,70.5-8.375));

        TrajectoryActionBuilder intakeOne = drive.actionBuilder(BlueNet)
                .setTangent(45.0)
                .lineToY(55.0)
                .splineToLinearHeading(IntakeOne,Math.toRadians(270));
        TrajectoryActionBuilder intakeOneInch = drive.actionBuilder(IntakeOne)
                .lineToY(36.0);

        TrajectoryActionBuilder intakeTwo = drive.actionBuilder(BlueNet)
                .setTangent(45.0)
                .lineToY(55.0)
                .splineToLinearHeading(IntakeTwo,Math.toRadians(270));

        TrajectoryActionBuilder intakeTwoInch = drive.actionBuilder(IntakeTwo)
                .lineToY(36.0);

        //EXECUTE ACTIONS
        Actions.runBlocking(
                new SequentialAction(
                        intakeSpecimen,
                        //sleeb(500),
                        wrist.zero(),
                        Bucket1.build(),
                        wrist.straight(),
                        arm.bucketHigh(),
                        inchForward.build(),
                        dumpInHighBucket,
                        new ParallelAction(
                                homeArm,
                                intakeOne.build()
                        ),
                        //Do Intake Stuff
                        IntakeCube,
                        intakeOneInch.build(),
                        //bucket stuff
                        homeArm2,
                        Bucket2.build(),
                        wrist.straight(),
                        arm.bucketHigh(),
                        inchForward.build(),
                        dumpInHighBucket2,
                        new ParallelAction(
                                homeArm3,
                                intakeTwo.build()
                        ),
                        //Do Intake Stuff
                        IntakeCube2,
                        intakeTwoInch.build(),
                        //bucket stuff
                        homeArm4,
                        Bucket3.build(),
                        wrist.straight(),
                        arm.bucketHigh(),
                        inchForward.build(),
                        dumpInHighBucket3,
                        homeArm5,
                        park.build()
                )
        );
    }
}
