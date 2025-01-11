package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAuto {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800,30);
        Pose2d initialPose = new Pose2d(31.5, 70.5-8.375, Math.toRadians(90));
        Pose2d BlueNet = new Pose2d(55.0,55.0,Math.toRadians(45));
        Pose2d IntakeOne = new Pose2d(48.0,40,Math.toRadians(270));
        Pose2d IntakeTwo = new Pose2d(58.0,40,Math.toRadians(270));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(initialPose)
                //bucket 1
                .lineToY(48.0)
                .splineToLinearHeading(BlueNet,60.0)
                //inch forward
                .lineToY(58.0)
                //intake one
                .setTangent(45.0)
                .lineToY(55.0)
                .splineToLinearHeading(IntakeOne,Math.toRadians(270))
                //inch forward 2
                .lineToY(36.0)
                //bucket 2
                .lineToY(48.0)
                .splineToLinearHeading(BlueNet,60.0)
                //inch forward 2
                .lineToY(56.0)

                //park
                .lineToYLinearHeading(38.0,Math.toRadians(0))
                .setTangent(Math.toRadians(0))
                .lineToXLinearHeading(-36.0,Math.toRadians(0))
                .strafeTo(new Vector2d(-36.0,70.5-8.375))
                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}