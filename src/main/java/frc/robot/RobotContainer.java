package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.teleop.SwerveDefense;
import frc.robot.commands.teleop.SwerveDrive;
import frc.robot.commands.teleop.SwerveVision;
import frc.robot.commands.teleop.SwerveResetGyro;
import frc.robot.constants.ControllerConstants;

import frc.robot.subsystems.Drivetrain;
public class RobotContainer {

  private Joystick xboxController = new Joystick(ControllerConstants.XBOX_CONTROLLER_ID);

  private Controller controller;
  private Drivetrain drivetrain;

  public RobotContainer() {
    controller = Controller.getInstance();
    drivetrain = Drivetrain.getInstance();

    // TODO: Is there a way to not have 6 inputs and check driveModes another way?
    drivetrain.setDefaultCommand(new SwerveDrive(
      () -> controller.getForward(), 
      () -> controller.getStrafe(),
      () -> controller.getSpin(),
      () -> controller.isFieldCentricButtonPressed(),
      () -> controller.isRobotCentricButtonPressed(),
      () -> controller.isBackRobotCentricButtonPressed()));

    configureButtonBindings();
  }

  private void configureButtonBindings() {
    // Drivetrain buttons
    controller.getDefenseButton().whenHeld(new SwerveDefense());
    controller.getVisionButton().whenHeld(new SwerveVision(
      () -> controller.getForward(), 
      () -> controller.getStrafe()));

    controller.getResetGyroButton().whenHeld(new SwerveResetGyro());
  }
}
