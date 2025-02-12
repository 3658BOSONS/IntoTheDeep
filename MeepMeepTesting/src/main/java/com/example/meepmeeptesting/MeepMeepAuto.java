package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAuto {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800,30);
        Pose2d initialPose = new Pose2d(25+7.5, 53.5+(17.5/2), Math.toRadians(-90));
        Pose2d BlueNet = new Pose2d(47.5,47.5,Math.toRadians(45));//orign: 48.0
        Pose2d IntakeOne = new Pose2d(46.5,44.9,Math.toRadians(-90));
        //blue net again
        Pose2d IntakeTwo = new Pose2d(58.5,45.0,Math.toRadians(-90));
        Pose2d BlueNetLow = new Pose2d(55,55,Math.toRadians(45));
        Pose2d Park = new Pose2d(36.0,12,Math.toRadians(180));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width

                .setConstraints(50, 50, Math.PI, Math.PI, 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(initialPose)

                .splineToLinearHeading(BlueNet,Math.toRadians(45))

                .lineToY(42.0)

                .lineToY(45.0)
                .splineToLinearHeading(IntakeOne,Math.toRadians(-90))

                .lineToY(BlueNet.position.y)
                .splineToLinearHeading(BlueNet,Math.toRadians(45))

                //inch backward again

                .lineToY(45.0)
                .splineToLinearHeading(IntakeTwo,Math.toRadians(-90))

                .lineToY(BlueNetLow.position.y)
                .splineToLinearHeading(BlueNetLow,Math.toRadians(45))

                .lineToY(42.0)

                .splineToLinearHeading(Park,Math.toRadians(180))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}