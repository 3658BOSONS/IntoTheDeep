package com.bosons.Utils;

import java.util.ArrayList;
import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

class Color {
    String name;
    int[] rgb;
    double pwm;

    public Color(String name, int[] rgb, double pwm) {
        this.name = name;
        this.rgb = rgb;
        this.pwm = pwm;
    }
}

public class LEDcontroller {
    public Servo LEDone = null;
    public Servo LEDtwo = null;

    public LEDcontroller(String nameOne,String nameTwo, OpMode op){
        LEDone = op.hardwareMap.get(Servo.class,nameOne);
        LEDtwo = op.hardwareMap.get(Servo.class,nameTwo);
    }

    public void SetColor(double pwm) {
        LEDone.setPosition(pwm);
        LEDtwo.setPosition(pwm);
        }
    }
