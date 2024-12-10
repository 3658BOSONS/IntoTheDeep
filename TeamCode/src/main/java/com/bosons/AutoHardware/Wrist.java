package com.bosons.AutoHardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Wrist {
    public Servo wristServo;
    public Wrist(HardwareMap hwm){
        wristServo = hwm.get(Servo.class,"wrist");
    }

    public class intake implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            double targetPos = 0.5;
            wristServo.setPosition(targetPos);
            double currentPos = wristServo.getPosition();
            packet.put("Current State: ","WristIntake");
            packet.put("Wrist | targetPos: ", targetPos);
            packet.put("Wrist | currentPos: ", currentPos);
            return !(currentPos >= targetPos);
        }
    }

    public Action intake(){
        return new intake();
    }

    public class standby implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            double targetPos = 0.3;
            wristServo.setPosition(targetPos);
            double currentPos = wristServo.getPosition();
            packet.put("Current State: ","WristStandby");
            packet.put("Wrist | targetPos: ", targetPos);
            packet.put("Wrist | currentPos: ", currentPos);
            return !(currentPos >= targetPos);
        }
    }

    public Action standby(){
        return new standby();
    }

    public class bucket implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            double targetPos = 0.4;
            wristServo.setPosition(targetPos);
            double currentPos = wristServo.getPosition();
            packet.put("Current State: ","WristBucket");
            packet.put("Wrist | targetPos: ", targetPos);
            packet.put("Wrist | currentPos: ", currentPos);
            return !(currentPos >= targetPos);
        }
    }

    public Action bucket(){
        return new bucket();
    }

    public class straight implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            double targetPos = 0.5;
            wristServo.setPosition(targetPos);
            double currentPos = wristServo.getPosition();
            packet.put("Current State: ","WristStraight");
            packet.put("Wrist | targetPos: ", targetPos);
            packet.put("Wrist | currentPos: ", currentPos);
            return !(currentPos >= targetPos);
        }
    }

    public Action straight(){
        return new straight();
    }

    public class zero implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            double targetPos = 0.0;
            wristServo.setPosition(targetPos);
            double currentPos = wristServo.getPosition();
            packet.put("Current State: ","WristZero");
            packet.put("Wrist | targetPos: ", targetPos);
            packet.put("Wrist | currentPos: ", currentPos);
            return !(currentPos >= targetPos);
        }
    }

    public Action zero(){
        return new zero();
    }


}
