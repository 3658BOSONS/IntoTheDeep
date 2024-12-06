package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800,30);
        Pose2d initialPose = new Pose2d(31.5, 70.5-8.375, Math.toRadians(90));
        Pose2d BlueNet = new Pose2d(55.0,55.0,Math.toRadians(45));
        Pose2d IntakeOne = new Pose2d(48.0,40,Math.toRadians(270));
        Pose2d IntakeTwo = new Pose2d(58.0,40,Math.toRadians(270));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(600, 600, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(initialPose)
                //bluenet
                .lineToY(48.0)
                .splineToLinearHeading(BlueNet,Math.toRadians(45))
                .lineToY(58.0)
                //intake one
                .setTangent(45.0)
                .lineToY(55.0)
                .splineToLinearHeading(IntakeOne,Math.toRadians(270))

                .splineToLinearHeading(BlueNet,Math.toRadians(45))
                .lineToY(58.0)

                .lineToY(55.0)
                .splineToLinearHeading(IntakeTwo,Math.toRadians(270))

                .splineToLinearHeading(BlueNet,Math.toRadians(45))
                .lineToY(58.0)
                //park
                //.setTangent(45.0)
                .lineToYLinearHeading(48.0,Math.toRadians(270))
                //.setTangent(59.0)
                .setTangent(Math.toRadians(0))
                .lineToXLinearHeading(-36.0,Math.toRadians(270))
                .strafeTo(new Vector2d(-36.0,70.5-8.375))
                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}