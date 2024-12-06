package com.bosons.Hardware;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Arm {
    //opMode for telemetry and hardware map;
    public OpMode opm;

    //arm extension motors
    public Motor rightExtendoMotor;
    public Motor leftExtendoMotor;

    //Wrist/intakeActive servos
    private final Servo wristServo;
    private final CRServo intakeServo;

    //Rotation motor and pid//
    public Motor rightRotationMotor;
    public Motor leftRotationMotor;

    public PIDController controller;
    public static double p = 0.0016, i = 0.01, d = 0.00005;
    public static double f = 0.15;
    //public static double p = 0.004, i = 0.05, d = 0.0001;
    //public static double f = 0.19;
    public static int rotTarget = 0;
    //---------------------//
    public static int extensionTarget = 0;
    public int acceptableExtensionError = 5;
    public int maxExtensionTicks = 2185;
    public double Power;

    //Constants
    private final double ticks_in_degree = (8192/360.0);
    private final double ticks_per_cm = (2190/48.96);

    /*
    Motion Profiling bullshit
    private double slope;
    private double radius_0;
    private double theta_0;
    */

    //Motion Smoothing;
    private int thetaTicksInitial;
    private int thetaTicks;
    private final ElapsedTime smoothingTimer = new ElapsedTime();
    pose bucketHigh;
    pose bucketLow;
    pose specimenHigh;
    pose specimenLow;
    pose intakeActive;
    pose intakeStandby;
    public pose home;
    private double timeSlope;

    //
    private Mode liftState = Mode.Home;
    private Height liftHeight = Height.High;

    private Height intakeState = Height.Standby;
    private pose currentPose;
    private PIDFCoefficients retractedCoefficients = new PIDFCoefficients(0.0016,0.01,0.00005,0.15);
    private static PIDFCoefficients extendedCoefficients = new PIDFCoefficients(0.0016,0.01,0.00005,0.15);;//new PIDFCoefficients(0.004,0.05,0.0001,0.19);
    private PIDFCoefficients activeCoefficients = retractedCoefficients;
    private double coefficientSwapPoint = 62.7;

    public void setPIDFCoefficients(double P,double I,double D,double F){
        activeCoefficients = new PIDFCoefficients(P,I,D,F);
    }

    public Arm(OpMode op, double power){
        opm = op;
        Power = power;

        bucketHigh =  new pose(84.6,90,0.4);
        bucketLow  =  new pose(50,90,0.4);
        specimenHigh =  new pose(84.6,90,0.3);//REPLACE THE NUMBERS WITH THE RIGHT ONES
        specimenLow =  new pose(84.6,90,0.3);//REPLACE THE NUMBERS WITH THE RIGHT ONES
        intakeActive = new pose(65,-6,0.9);

        intakeStandby = new pose(65,15,0.5);
        home = new pose(40.8,-28,0);



        rightExtendoMotor = new Motor("RightExt",op);
        rightExtendoMotor.setTargetPosition(0);
        rightExtendoMotor.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.REVERSE);

        leftExtendoMotor = new Motor("LeftExt",op);
        leftExtendoMotor.setTargetPosition(0);
        leftExtendoMotor.setConstants(DcMotor.RunMode.RUN_TO_POSITION,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.FORWARD);



        rightRotationMotor = new Motor("RightRot",op);
        rightRotationMotor.setConstants(DcMotor.RunMode.RUN_WITHOUT_ENCODER,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.FORWARD);

        leftRotationMotor = new Motor("LeftRot",op);
        leftRotationMotor.setConstants(DcMotor.RunMode.RUN_WITHOUT_ENCODER,DcMotor.ZeroPowerBehavior.BRAKE,DcMotor.Direction.REVERSE);

        controller = new PIDController(p,i,d);
        opm.telemetry = new MultipleTelemetry(op.telemetry, FtcDashboard.getInstance().getTelemetry());

        wristServo = op.hardwareMap.get(Servo.class,"wrist");
        intakeServo = op.hardwareMap.get(CRServo.class,"intake");
        intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void setLiftState(Mode state){
        liftState = state;
    }
    public void setLiftHeight(Height height){
        liftHeight = height;
    }
    public enum Height{
        High,
        Low,
        Standby,
        Active
    }
    public enum Mode{
        Bucket,
        Specimen,
        Intake,
        Home
    }



    public void setWristServo(double pos){
        wristServo.setPosition(pos);
    }

    public void setIntakePower(double power){
        intakeServo.setPower(power);
    }

    public void updatePidLoop(int target){

        if(getArmLength()>coefficientSwapPoint){
            activeCoefficients = extendedCoefficients;
        }
        else{
            activeCoefficients = retractedCoefficients;
        }

        p = activeCoefficients.p;
        i = activeCoefficients.i;
        d = activeCoefficients.d;
        f = activeCoefficients.f;

        controller.setPID(p,i,d);
        int armPos = leftRotationMotor.getCurrentPosition();
        double pid = controller.calculate(armPos,target);
        double ff = Math.cos(Math.toRadians(target/ticks_in_degree))*f;

        double power = (pid + ff);//*0.5;
        opm.telemetry.addData("ArmPower*10K",power*10000);
        if(armPos>100||target>100) {
            rightRotationMotor.setPower(power);
            leftRotationMotor.setPower(power);
        }
        else {
            rightRotationMotor.setPower(0);
            leftRotationMotor.setPower(0);
        }
        opm.telemetry.addData("armPos",armPos);
        opm.telemetry.addData("target",target);
    }

    public void setPositionPolarSmooth(pose P, double seconds){//Slows down the movement over a set time;
        //convert r (cm) to ticks
        extensionTarget = (int)((P.radius-40.8)*ticks_per_cm);//subtract the fixed length of the arm
        if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
        if(extensionTarget<0){extensionTarget=0;}
        //convert theta (degrees) to ticks
        thetaTicks = (int)((P.theta+28)*ticks_in_degree);//subtract the initial -28 degree position of the arm
        if(thetaTicks>2700){thetaTicks=2700;}
        if(thetaTicks<0){thetaTicks=0;}

        //set the change in ticks over the set interval
        thetaTicksInitial = leftRotationMotor.getCurrentPosition();
        timeSlope = (thetaTicks-thetaTicksInitial)/(seconds*1000);//ticks per millisecond
        smoothingTimer.reset();
    }

    public void setPositionPolarAngVelo(pose P, double degPerSec){
        //convert r (cm) to ticks
        extensionTarget = (int)((P.radius-40.8)*ticks_per_cm);//subtract the fixed length of the arm
        if(extensionTarget>maxExtensionTicks){extensionTarget=maxExtensionTicks;}
        if(extensionTarget<0){extensionTarget=0;}
        //convert theta (degrees) to ticks
        thetaTicks = (int)((P.theta+28)*ticks_in_degree);//subtract the initial -28 degree position of the arm
        if(thetaTicks>2700){thetaTicks=2700;}
        if(thetaTicks<0){thetaTicks=0;}

        //set the change in ticks over the set interval
        thetaTicksInitial = leftRotationMotor.getCurrentPosition();
        timeSlope = (degPerSec/1000)*ticks_in_degree;//ticks per millisecond
        if(thetaTicksInitial>thetaTicks){
            timeSlope*=-1;
        }
        opm.telemetry.addData("timeSlope",timeSlope);
        smoothingTimer.reset();
    }


    public void updatePositionSmooth(){
        if(rotTarget!=thetaTicks) {
            // initial position +/- ticksPerMillisecond*millisecond
            rotTarget = (int) (thetaTicksInitial + timeSlope * smoothingTimer.milliseconds());
            if(rotTarget>2700){rotTarget=2700;}
            if(rotTarget<0){rotTarget=0;}
        }

        if((timeSlope>0 && rotTarget>thetaTicks)||(timeSlope<0 && rotTarget<thetaTicks)||(timeSlope==0)){
            rotTarget = thetaTicks;
        }//prevent any overshooting

        updatePosition();
    }

    public boolean isSmoothing(){
        opm.telemetry.addData("rotTarget",rotTarget);
        opm.telemetry.addData("thetaTicks",thetaTicks);
        return rotTarget != thetaTicks;
    }

    public void setPositionPolar(double r,double theta){
        //convert r (cm) to extension
        int extensionTicks = (int)((r-40.8)*ticks_per_cm);//subtract the fixed length of the arm
        if(extensionTicks>maxExtensionTicks){extensionTicks=maxExtensionTicks;}
        //convert theta (cm) to encoder
        int rotationTicks = (int)((theta+28)*ticks_in_degree);//subtract the initial -28 degree position of the arm
        if(rotationTicks>2700){rotationTicks=2700;}
        //set target positions
        extensionTarget = extensionTicks;
        rotTarget = rotationTicks;
    }

    public void updatePosition(){
        extendToTarget(extensionTarget,0.5);
        updatePidLoop(rotTarget);
        opm.telemetry.addData("armAngle ", getArmAngle());
        opm.telemetry.addData("armLength ",getArmLength());
    }

    public double getArmAngle(){
        return (leftRotationMotor.getCurrentPosition()/ticks_in_degree)-28;
    }
    public void resetArmAngle(){
        leftRotationMotor.resetEncoder();
    }
    public double getArmLength(){
        return ((leftExtendoMotor.getCurrentPosition()+rightExtendoMotor.getCurrentPosition())/2.0)/(ticks_per_cm)+40.8;
    }

    public void resetArmLength(){
        leftExtendoMotor.resetEncoder();
        rightExtendoMotor.resetEncoder();
    }

    public void extendToTarget(int counts, double power){
        //Clamp incoming target to limits
        if(counts<0){counts=0;}
        if(counts>maxExtensionTicks){counts=maxExtensionTicks;}
        //set target position
        rightExtendoMotor.setTargetPosition(counts);
        leftExtendoMotor.setTargetPosition(counts);
        opm.telemetry.addData("extenstion Target",counts);
        opm.telemetry.addData("RightBurnCheck",!rightExtendoMotor.burnCheck(acceptableExtensionError));
        opm.telemetry.addData("LeftBurnCheck",!leftExtendoMotor.burnCheck(acceptableExtensionError));
        opm.telemetry.addData("ExtendoMotorPower",rightExtendoMotor.getPower());
        opm.telemetry.addData("ExtendoMotorPower",leftExtendoMotor.getPower());
        opm.telemetry.addData("ExtendoMotorTargetPos",rightExtendoMotor.getTargetPosition());
        opm.telemetry.addData("ExtendoMotorTargetPos",leftExtendoMotor.getTargetPosition());
        opm.telemetry.addData("PassedInPower",power);
        //set power if it wont burn the motor
        if(!rightExtendoMotor.burnCheck(acceptableExtensionError)){
            opm.telemetry.addData("InsideRightBurnCheck",!rightExtendoMotor.burnCheck(acceptableExtensionError));
            rightExtendoMotor.setPower(power);
        }
        if(!leftExtendoMotor.burnCheck(acceptableExtensionError)){
            opm.telemetry.addData("InsideLeftBurnCheck",!leftExtendoMotor.burnCheck(acceptableExtensionError));
            leftExtendoMotor.setPower(power);
        }
    }

    public void Stop(){
        rightExtendoMotor.setPower(0);
        leftExtendoMotor.setPower(0);
    }

    public void positionArm(){
        pose targetPose = home;
        double targetAngularVelocity = 40;

        switch (liftState){
            case Home:{
                //do nothing because target pose is already set to home when intialized
                //and targetAngularVelocity is already 20
                break;
            }
            case Bucket:{
                switch (liftHeight){
                    case High:{
                        targetPose = bucketHigh;
                        break;
                    }
                    case Low:{
                        targetPose = bucketLow;
                        break;
                    }
                }
                targetAngularVelocity = 60;
                break;
            }
            case Specimen:{
                break;
            }
            case Intake:{
                switch (intakeState){
                    case Standby:{
                        targetPose = intakeStandby;
                        break;
                    }
                    case Active:{
                        targetPose = intakeActive;
                        break;
                    }
                }
                targetAngularVelocity = 40;
                break;
            }
        }
        if(targetPose == currentPose){
            return;
        }
        currentPose = targetPose;
        //smoothingTimer.reset();
        opm.telemetry.addData("Smoothing Timer Milliseconds",smoothingTimer.milliseconds());
        setPositionPolarAngVelo(targetPose,targetAngularVelocity);
        setWristServo(home.wrist);
    }

//    public void positionArm(Mode mode, Height height, double time){
//        switch (mode){
//            case Bucket:{
//                p = 0.004;
//                i = 0.05;
//                d = 0.0001;
//                f = 0.19;
//                switch (height){
//                    //set Arm pose based on the provided height
//                    //High
//                    case High:{
//                        setPositionPolarSmooth(bucketHigh,time);
//                        setWristServo(bucketHigh.wrist);
//                        return;
//                    }
//                    //Low
//                    case Low:{
//                        setPositionPolarSmooth(bucketLow,time);
//                        setWristServo(bucketLow.wrist);
//                        return;
//                    }
//                }
//                return;
//            }
//            case Specimen:{
//                p = 0.0016;
//                i = 0.01;
//                d = 0.00005;
//                f = 0.15;
//                switch (height){
//                    //set Arm pose based on the provided height
//                    case High:{
//                        setPositionPolarSmooth(specimenHigh,time);
//                        setWristServo(specimenHigh.wrist);
//                        return;
//                    }
//                    case Low:{
//                        setPositionPolarSmooth(specimenLow,time);
//                        setWristServo(specimenLow.wrist);
//                        return;
//                    }
//                }
//                return;
//            }
//            case Intake:{
//                 p = 0.004;
//                 i = 0.05;
//                 d = 0.0001;
//                 f = 0.19;
//                 //set Arm pose based on the provided height
//                switch (height){
//                    case Standby:{
//                        setPositionPolarSmooth(intakeStandby,time);
//                        setWristServo(intakeStandby.wrist);
//                        return;
//                    }
//                    case Active:{
//                        setPositionPolarSmooth(intakeActive,time);
//                        setWristServo(intakeActive.wrist);
//                        return;
//                    }
//                }
//            }
//            case Home:{
//                p = 0.0016;
//                i = 0.01;
//                d = 0.00005;
//                f = 0.15;
//                setPositionPolarSmooth(home,time);
//                setWristServo(home.wrist);
//            }
//        }
//    }

    public static class pose {
        public double theta;
        public double radius;
        public double x;
        public double y;
        public double wrist;
        public pose(double r, double angle, double wristServoPosition){
            radius = r;
            theta = angle;
            wrist = wristServoPosition;
            x = radius*Math.cos(theta);
            y = radius*Math.sin(theta);
        }
    }
}
