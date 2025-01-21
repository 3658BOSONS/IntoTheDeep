package com.bosons.Utils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

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
