package com.bosons.Utils;

import com.acmerobotics.roadrunner.SleepAction;

public class Sleep {

    public SleepAction Milliseconds(int milliseconds){
        return new SleepAction(milliseconds/1000.0);
    }
    public SleepAction seconds(int seconds){
        return new SleepAction(seconds);
    }


}
