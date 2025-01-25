package com.bosons.Utils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;
import java.util.Map;

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

    public void SetColor(String colorName) {
        LEDone.setPosition(setColorPWM(colorName));
        LEDtwo.setPosition(setColorPWM(colorName));
    }
    public static double setColorPWM(String colorName) {
        Map<String, Double> colorMap = new HashMap<>();
        colorMap.put("red", 0.279);
        colorMap.put("orange", 0.333);
        colorMap.put("yellow", 0.388);
        colorMap.put("sage", 0.444);
        colorMap.put("green", 0.500);
        colorMap.put("azure", 0.555);
        colorMap.put("blue", 0.611);
        colorMap.put("indigo", 0.666);
        colorMap.put("violet", 0.722);
        colorMap.put("white", 1.000);

        // Get the PWM value associated with the color name
        Double pwmValue = colorMap.get(colorName.toLowerCase());

        // If the color name is not found, return -1 to indicate an invalid color
        if (pwmValue == null) {
            System.out.println("Invalid color name!");
            return -1;
        }

        return pwmValue;
    }
}
