package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800,30);
        Pose2d initialPose = new Pose2d(31.5-(16*3), 70.5-8.375, Math.toRadians(90));
        Pose2d BlueNet = new Pose2d(55.0,55.0,Math.toRadians(45));
        Pose2d IntakeOne = new Pose2d(48.0,44,Math.toRadians(90));
        Pose2d IntakeTwo = new Pose2d(58.0,44,Math.toRadians(90));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(1.7, 10000, 10000, 10000, 0.34741685376)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(initialPose)
                //bluenet

                .waitSeconds(1)
                .setTangent(Math.toRadians(180))
                .lineToX(-70+8.375)
                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}