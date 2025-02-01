package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAuto {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800,30);
        Pose2d initialPose = new Pose2d(25+7.5, 53.5+(17.5/2), Math.toRadians(-90));
        Pose2d BlueNet = new Pose2d(45.0,45.0,Math.toRadians(45));
        Pose2d IntakeOne = new Pose2d(48.0,40,Math.toRadians(-90));
        Pose2d IntakeTwo = new Pose2d(58.0,40,Math.toRadians(-90));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(initialPose)
                //bucket 1
                .splineToLinearHeading(BlueNet,Math.toRadians(45.0))
                //inch forward
                .setTangent(Math.toRadians(45))
                .lineToY(55.0)

                //intake one
                .lineToY(45.0)
                .splineToLinearHeading(IntakeOne,Math.toRadians(270))


                //bucket 2
                .setTangent(Math.toRadians(45))
                .splineToLinearHeading(BlueNet,Math.toRadians(45.0))
                .lineToY(55.0)

                //inch forward 2
                .lineToY(45.0)

                //park
                .setTangent(Math.toRadians(0))
                .lineToXLinearHeading(36.0,Math.toRadians(180))
                .strafeTo(new Vector2d(36.0,12))
                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}