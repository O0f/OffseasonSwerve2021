package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.*;
import frc.robot.constants.ControllerConstants;

public class Controller {
    // Instantiating the instance of Controller to be used system-wide
    private static Controller instance = new Controller();

    // Instantiating necessary joysticks and buttons
    private final Joystick driveStick;
    private final Joystick spinStick;
    //private final XboxController operator = new XboxController(ControllerConstants.OPERATOR_ID);

    // getInstance function for controller
    public static Controller getInstance() {
        return instance;
    }

    public Controller() {
        driveStick = new Joystick(ControllerConstants.DRIVE_STICK_ID);
        spinStick = new Joystick(ControllerConstants.SPIN_STICK_ID);

        SmartDashboard.putNumber("test", 1);
        SmartDashboard.putNumber("Drive Stick x", 2);
        SmartDashboard.putNumber("Drive Stick y", 3);
        SmartDashboard.putNumber("Spin Stick y", 4);
    }

    // Gets the joystick speed input for normal arcade drive
    public double getArcadeDriveSpeed() {
        double test = Math.sqrt(Math.pow(getStickAxis(driveStick, 0),2) + Math.pow(getStickAxis(driveStick, 1),2));
        SmartDashboard.putNumber("test", test);
        SmartDashboard.putNumber("Drive Stick x", getStickAxis(driveStick, 0));
        SmartDashboard.putNumber("Drive Stick y", getStickAxis(driveStick, 1));
        return getStickAxis(driveStick, 1);

    }

    // Gets the joystick rotation input for normal arcade drive
    public double getArcadeDriveRot() {
        SmartDashboard.putNumber("Spin Stick y", getStickAxis(spinStick, 1));
        return getStickAxis(spinStick, 1);
    }

    /**
     * Takes an input stick and its axis and returns a modified value with deadbands
     * 
     * @param stick The joystick you want to get an axis from
     * @param axisId 0 for the x-axis and 1 for the y-axis
    *  @return The modified output of the stick and its given axis
    */
    public double getStickAxis(Joystick stick, int axisId) { 
        double stickAxisOutput = 0;
        if(axisId == 0) {
            stickAxisOutput = stick.getX();
        } else if(axisId == 1) {
            stickAxisOutput = stick.getY();
        }
        if(!stick.getRawButton(1)){
            // Adds in deadband logic
            if (Math.abs(stickAxisOutput) < ControllerConstants.DEADBAND) {
                return 0.0;
            } else if (stickAxisOutput < 0) {
                return (stickAxisOutput + ControllerConstants.DEADBAND) * ControllerConstants.SLOW_ROT_MULT * Math.PI;
            } else {
                return (stickAxisOutput - ControllerConstants.DEADBAND) * ControllerConstants.SLOW_ROT_MULT * Math.PI;
            }
        } else {
            // Adds in deadband logic
            if (Math.abs(stickAxisOutput) < ControllerConstants.DEADBAND) {
                return 0.0;
            } else if (stickAxisOutput < 0) {
                return (stickAxisOutput + ControllerConstants.DEADBAND) * Math.PI;
            } else {
                return (stickAxisOutput - ControllerConstants.DEADBAND) * Math.PI;
            }
        }
    }

    public Boolean resetOdometry() {
        return driveStick.getRawButton(6);
    }
}