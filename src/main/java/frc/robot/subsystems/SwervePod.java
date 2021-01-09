package frc.robot.subsystems;

import javax.xml.namespace.QName;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.constants.SwervePodConstants;

public class SwervePod {

    private CANSparkMax driveController;
    private TalonSRX spinController;

    private int id;
    private int encoderOffset = SwervePodConstants.OFFSETS[id];

    private double lastEncoderPos;
    private double radianError;
    private double encoderError;
    private double driveCommand;
    private double lastTransAngle;
    private double initRadianDifference; //init pos versus now pos
    private boolean flipThrust;

    private double PI = Math.PI;
    
    public SwervePod(int id, CANSparkMax driveController, TalonSRX spinController) {
        this.id = id;
        this.driveController = driveController;
        this.spinController = spinController;
 
        this.lastTransAngle = 0;

        //spinMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);

        this.spinController.config_kP(0, SwervePodConstants.SPIN_PID_CONFIG[0][id], 0);
		this.spinController.config_kI(0, SwervePodConstants.SPIN_PID_CONFIG[1][id], 0);
		this.spinController.config_kD(0, SwervePodConstants.SPIN_PID_CONFIG[2][id], 0);
		this.spinController.config_kF(0, SwervePodConstants.SPIN_PID_CONFIG[3][id], 0);
    }

    public void thrust(double transMag) {
        if(flipThrust) { transMag = -transMag; }
        driveController.set(transMag);
    }

    public void spin(double transMag, double transAngle) {
        this.initRadianDifference += ((transAngle % (2 * PI)) - (this.lastTransAngle % (2 * PI)));
        double encoderSetPos = calcSpinPos(transAngle);
        //if(transMag != 0) {
        if(true) {
            spinController.set(ControlMode.Position, encoderSetPos);
            SmartDashboard.putNumber("encoderSetPos", encoderSetPos);
            lastEncoderPos = encoderSetPos;
        } else {
            spinController.set(ControlMode.Position, lastEncoderPos);
            SmartDashboard.putNumber("lastEncoderPos", lastEncoderPos);
        }
        SmartDashboard.putNumber("transAngle", transAngle);
        SmartDashboard.putNumber("lastTransAngle", lastTransAngle);
        SmartDashboard.putNumber("initRadianDifference", initRadianDifference);
        this.lastTransAngle = transAngle;
    }

    /**
     * @param angle desired angle of swerve pod in units of radians, range from -2PI to +2PI
     * @return
     */
    private double calcSpinPos(double angle) {
        int encoderPos = spinController.getSelectedSensorPosition(0) - encoderOffset;
        SmartDashboard.putNumber("SSP", spinController.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("encoderPos", encoderPos);
        double radianPos = tics2Rads(encoderPos);
        SmartDashboard.putNumber("angle", angle);
        radianError = angle - radianPos;
        SmartDashboard.putNumber("radPos", radianPos);

        if(Math.abs(radianError) > (5 * (PI / 2))) {
            System.out.println("Error: Overload");
            SmartDashboard.putNumber("radError 52", radianError);
        } else if(Math.abs(radianError) > (3 * (PI / 2))) {
            radianError -= Math.copySign(2 * PI, radianError);
            SmartDashboard.putNumber("radError 32", radianError);
        } else if(Math.abs(radianError) > (PI / 2)) {
            radianError -= Math.copySign(PI, radianError);
            flipThrust = !flipThrust;
            SmartDashboard.putNumber("radError 12", radianError);
        }
        SmartDashboard.putNumber("round test", (double)(Math.sin((1/8) * Math.PI)));
        System.out.print((double)(Math.sin((1/8) * Math.PI)));

        encoderError = rads2Tics(radianError);
        driveCommand = encoderError + encoderPos + encoderOffset/* + this.initRadianDifference*/;
        SmartDashboard.putNumber("DriveCommand", driveCommand);
        return driveCommand;
    }

    private int rads2Tics(double rads) {
        return (int)((4096 / (PI * 2)) * rads);
    }

    private double tics2Rads(int tics) {
        return ((PI * 2) / 4096) * tics;
    }
}
