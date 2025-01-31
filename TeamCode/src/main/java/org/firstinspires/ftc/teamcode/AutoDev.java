package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.bosons.AutoHardware.Extender;
import com.bosons.AutoHardware.Hand;
import com.bosons.Utils.LEDcontroller;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// road runner Imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;

//Non road runner Imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import RoadRunner.MecanumDrive;
//Boson Imports
import com.bosons.AutoHardware.Arm;

@Config
@Autonomous(name = "AutoDev", group = "Comp",preselectTeleOp = "TeleOp")
public class AutoDev extends LinearOpMode {

    public SleepAction sleeb(int milliseconds){
        return new SleepAction(milliseconds/1000.0);
    }
    public SleepAction sleeb2(int milliseconds){
        return new SleepAction(milliseconds/1000.0);
    }
    public SleepAction sleeb3(int milliseconds){
        return new SleepAction(milliseconds/1000.0);
    }
    @Override
    public void waitForStart() {
        Arm arm = new Arm(this);
        Hand hand = new Hand(hardwareMap);
        LEDcontroller indicator = new LEDcontroller("LedOne","LedTwo",this);
        while (arm.Homing){
            indicator.SetColor("red");
            Actions.runBlocking(new SequentialAction(hand.home(),arm.Home()));
        }
        indicator.SetColor("green");



        super.waitForStart();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        if (isStopRequested()) return;
        waitForStart();
        //Tool Definitions

        //POSITION DEFINITIONS
        Pose2d initialPose = new Pose2d(25+7.5, 53.5+(17.5/2), Math.toRadians(-90));
        Pose2d IntakeOne = new Pose2d(48.5,47.9,Math.toRadians(-90));
        Pose2d IntakeTwo = new Pose2d(59.0,47.9,Math.toRadians(90));
        Pose2d BlueNet = new Pose2d(46.0,46.0,Math.toRadians(45));

        //HARDWARE DEFINITIONS
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Extender exendo = new Extender(this);
        Hand hand = new Hand(hardwareMap);
        Arm arm = new Arm(this);
        //ACTION SHORTCUTS


        //set the arm to the default position
        ParallelAction homeArm = new ParallelAction(
                hand.close(),
                hand.home(),
                arm.ExtHome(),
                exendo.Zero(),
                arm.Zero()
        );

        ParallelAction homeArm2 = new ParallelAction(
                hand.close(),
                hand.home(),
                arm.ExtHome(),
                exendo.Zero(),
                arm.Zero()
        );

        ParallelAction homeArm3 = new ParallelAction(
                hand.close(),
                hand.home(),
                arm.ExtHome(),
                exendo.Zero(),
                arm.Zero()
        );
        //move arm to intake position and grab cube
        ParallelAction intakeSpecimen = new ParallelAction(
                hand.Specimen(),
                hand.close()
        );
        SequentialAction ExtendToHighBucket = new SequentialAction(
                new ParallelAction(
                        hand.home(),
                        hand.close()
                ),
                new SequentialAction(
                        arm.Bucket(),
                        arm.ExtFull(),
                        exendo.HighBucket(),
                        hand.Intake()
                )
        );
        SequentialAction ExtendToHighBucket2 = new SequentialAction(
                new ParallelAction(
                        hand.home(),
                        hand.close()
                ),
                new SequentialAction(
                        arm.Bucket(),
                        arm.ExtFull(),
                        exendo.HighBucket(),
                        hand.Intake()
                )
        );
        SequentialAction ExtendToHighBucket3 = new SequentialAction(
                new ParallelAction(
                        hand.home(),
                        hand.close()
                ),
                new SequentialAction(
                        arm.Bucket(),
                        arm.ExtFull(),
                        exendo.HighBucket(),
                        hand.Intake()
                )

        );



        //move arm to High bucket and drop specimen
        SequentialAction dumpInHighBucket = new SequentialAction(
                hand.close(),
                hand.Bucket(),
                arm.Bucket(),
                hand.open(),
                sleeb(200)
        );

        SequentialAction dumpInHighBucket2 = new SequentialAction(
                hand.close(),
                hand.Bucket(),
                arm.Bucket(),
                hand.open(),
                sleeb2(200)
        );

        SequentialAction dumpInHighBucket3 = new SequentialAction(
                hand.close(),
                hand.Bucket(),
                arm.Bucket(),
                hand.open(),
                sleeb3(200)
        );

        SequentialAction IntakeCube = new SequentialAction(
                hand.open(),
                hand.Intake(),
                arm.ExtFull(),
                hand.close()
        );

        SequentialAction IntakeCube2 = new SequentialAction(
                hand.Intake(),
                arm.ExtFull(),
                hand.close(),
                arm.ExtHome(),
                arm.Intake()
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
                .lineToY(42.0);

        TrajectoryActionBuilder inchForward2 = drive.actionBuilder(BlueNet)
                .lineToY(42.0);

        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(58,58,45.0))
                .lineToYLinearHeading(38.0,Math.toRadians(0))
                .setTangent(Math.toRadians(0))
                .lineToXLinearHeading(-36.0,Math.toRadians(-180))
                .strafeTo(new Vector2d(36,11));

        TrajectoryActionBuilder intakeOne = drive.actionBuilder(BlueNet)
                .setTangent(45.0)
                .lineToY(55.0)
                .splineToLinearHeading(IntakeOne,Math.toRadians(-90));
        TrajectoryActionBuilder intakeOneInch = drive.actionBuilder(IntakeOne)
                .lineToY(36.0);

        TrajectoryActionBuilder intakeTwo = drive.actionBuilder(BlueNet)
                .setTangent(45.0)
                .lineToY(55.0)
                .splineToLinearHeading(IntakeTwo,Math.toRadians(-90));

        TrajectoryActionBuilder intakeTwoInch = drive.actionBuilder(IntakeTwo)
                .lineToY(36.0);

        //EXECUTE ACTIONS
        Actions.runBlocking(
                new SequentialAction(
                        intakeSpecimen,
                        new ParallelAction(
                                Bucket1.build(),
                                ExtendToHighBucket,
                                hand.Bucket()
                        ),
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
                        new ParallelAction(
                                homeArm2,
                                Bucket2.build()
                        ),

                        //wrist.straight(),

                        ExtendToHighBucket3,
                        inchForward2.build(),
                        dumpInHighBucket2,
                        new ParallelAction(
                                homeArm3,
                                park.build()
                        )
                )
        );
    }
}
