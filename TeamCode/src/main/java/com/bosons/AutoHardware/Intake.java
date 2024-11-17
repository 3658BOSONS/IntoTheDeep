package com.bosons.AutoHardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private final CRServo intakeServo;

    public Intake(HardwareMap hardwareMap) {
        intakeServo = hardwareMap.get(CRServo.class, "intake");
        intakeServo.setDirection(CRServo.Direction.REVERSE);
    }

    public class spinIn implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intakeServo.setPower(0.8);
                initialized = true;
            }

            double Pow = intakeServo.getPower();
            packet.put("Intake Speed: ", Pow);
            return !initialized;
        }
    }
    public Action spinIn() {
        return new spinIn();
    }

    public class spinOut implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intakeServo.setPower(-0.8);
                initialized = true;
            }

            double Pow = intakeServo.getPower();
            packet.put("Intake Speed: ", Pow);
            return !initialized;
        }
    }
    public Action spinOut() {
        return new spinOut();
    }

    public class Stop implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intakeServo.setPower(0.0);
                initialized = true;
            }

            double Pow = intakeServo.getPower();
            packet.put("Intake Speed: ", Pow);
            return !initialized;
        }
    }
    public Action Stop() {
        return new Stop();
    }
}
