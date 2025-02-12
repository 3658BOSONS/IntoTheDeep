package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
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
@Autonomous(name = "AutoThreeDeezNutz", group = "Comp",preselectTeleOp = "TeleOp")
public class AutoThreeDeezNuts extends LinearOpMode {

    public SleepAction sleeb(int milliseconds){
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
        Pose2d BlueNet = new Pose2d(47.5,47.5,Math.toRadians(45));//orign: 48.0
        Pose2d BlueNet2 = new Pose2d(47.5,47.5,Math.toRadians(45));
        Pose2d BlueNet3 = new Pose2d(55,55,Math.toRadians(45));
        Pose2d IntakeOne = new Pose2d(47,44.9,Math.toRadians(-90));
        Pose2d IntakeTwo = new Pose2d(58.5,45.0,Math.toRadians(-90));


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

        ParallelAction homeArm4 = new ParallelAction(
                hand.close(),
                hand.home(),
                arm.ExtHome(),
                exendo.Zero(),
                arm.Zero()
        );

        ParallelAction homeArm5 = new ParallelAction(
                hand.close(),
                hand.home(),
                arm.ExtHome(),
                exendo.Zero(),
                arm.Zero()
        );

        ParallelAction homeArm6 = new ParallelAction(
                hand.close(),
                hand.home(),
                arm.ExtHome(),
                exendo.Zero(),
                arm.ParkThree()
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
        SequentialAction ExtendToLowBucket = new SequentialAction(
                new ParallelAction(
                        hand.home(),
                        hand.close()
                ),
                new SequentialAction(
                        arm.Bucket(),
                        arm.ExtHalf(),
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
                sleeb(200)
        );

        SequentialAction dumpinlowbucket = new SequentialAction(
                hand.close(),
                hand.Bucket(),
                arm.Bucket(),
                hand.open(),
                sleeb(200)
        );

        SequentialAction IntakeCube = new SequentialAction(
                arm.Intake(),
                hand.open(),
                new ParallelAction(
                        hand.Intake(),

                        arm.ExtFull()
                ),
                sleeb(500),
                hand.close(),
                sleeb(500)
        );

        SequentialAction IntakeCube2 = new SequentialAction(
                arm.Intake(),
                hand.open(),
                new ParallelAction(
                        hand.Intake(),

                        arm.ExtFull()
                ),
                sleeb(500),
                hand.close(),
                sleeb(500)
        );



        //MOVEMENT ACTIONS
        TrajectoryActionBuilder Bucket1 = drive.actionBuilder(initialPose)
                .splineToLinearHeading(BlueNet,Math.toRadians(45));

        TrajectoryActionBuilder Bucket2 = drive.actionBuilder(IntakeOne)
                .lineToY(BlueNet2.position.y)
                .splineToLinearHeading(BlueNet2,Math.toRadians(45));

        TrajectoryActionBuilder Bucket3 = drive.actionBuilder(IntakeTwo)
                .lineToY(BlueNet3.position.y)
                .splineToLinearHeading(BlueNet3,Math.toRadians(45));

        TrajectoryActionBuilder inchForward = drive.actionBuilder(BlueNet)
                .lineToY(42.0);

        TrajectoryActionBuilder inchForward2 = drive.actionBuilder(BlueNet2)
                .lineToY(42.0);

        TrajectoryActionBuilder inchForward3 = drive.actionBuilder(BlueNet3)
                .lineToY(42.0);

        TrajectoryActionBuilder park = drive.actionBuilder(IntakeOne)
                .setTangent(Math.toRadians(0))
                .turnTo(Math.toRadians(180));

        TrajectoryActionBuilder intakeOne = drive.actionBuilder(BlueNet)
                .lineToY(45.0)
                .splineToLinearHeading(IntakeOne,Math.toRadians(-90));

        TrajectoryActionBuilder intakeTwo = drive.actionBuilder(BlueNet2)
                .lineToY(45.0)
                .splineToLinearHeading(IntakeTwo,Math.toRadians(-90));

        TrajectoryActionBuilder intakeOneInch = drive.actionBuilder(IntakeOne)
                .lineToY(36.0);

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
                        sleeb(500),
                        dumpInHighBucket,
                        inchForward.build(),
                        new ParallelAction(
                                    homeArm,
                                    intakeOne.build()
                                ),
                        IntakeCube,
                        homeArm,
                        new ParallelAction(
                                Bucket2.build(),
                                ExtendToHighBucket,
                                hand.Bucket()
                        ),
                        sleeb(500),
                        dumpInHighBucket,
                        inchForward2.build(),
                        new ParallelAction(
                                homeArm,
                                intakeTwo.build()
                        ),
                        IntakeCube,
                        homeArm,
                        Bucket3.build(),
                        new ParallelAction(
                                ExtendToLowBucket,
                                hand.Bucket()
                        ),
                        sleeb(500),
                        dumpinlowbucket,
                        inchForward3.build(),
                        new ParallelAction(
                                homeArm,
                                park.build()
                        )


                )
        );
        }
    }
