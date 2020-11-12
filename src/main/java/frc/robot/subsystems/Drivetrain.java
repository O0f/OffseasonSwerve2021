/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.constants.DrivetrainConstants;
// import frc.robot.Controller;
// import frc.robot.VisionClient;

public class Drivetrain extends SubsystemBase {
  private static Drivetrain instance = new Drivetrain();

  private final AHRS gyro = new AHRS(SPI.Port.kMXP);
  private final SwervePod pod1;

  //private final DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(
  //    DrivetrainConstants.TRACK_WIDTH);

  //private final DifferentialDriveOdometry odometry;

  public Drivetrain() {
    gyro.reset();
    pod1 = new SwervePod(0);
    //odometry = new DifferentialDriveOdometry(getAngle());
  }
  
  public static Drivetrain getInstance() {
    return instance;
  }

  @Override
  public void periodic() {
  }

  public void drive(double rotX, double transMag, double transAngle) {
    pod1.thrust(transMag);
    pod1.spin(transMag, transAngle);
  }
  */
}
