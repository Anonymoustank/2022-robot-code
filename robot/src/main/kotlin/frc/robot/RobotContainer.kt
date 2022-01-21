// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController
import frc.robot.commands.ExampleCommand
import frc.robot.subsystems.ExampleSubsystem
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import edu.wpi.first.wpilibj.Joystick

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.Subsystem
import edu.wpi.first.wpilibj2.command.button.JoystickButton

import frc.robot.subsystems.*
import frc.robot.commands.*

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the [Robot]
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private val m_exampleSubsystem: ExampleSubsystem = ExampleSubsystem()
    private val m_autoCommand: ExampleCommand = ExampleCommand(m_exampleSubsystem)

    val controller0 = Joystick(0)

    /** --- setup drivetrain --- **/
    val motorFrontLeft = WPI_TalonSRX(Constants.kDrivetrainFrontLeftPort)
    val motorBackLeft = WPI_TalonSRX(Constants.kDrivetrainBackLeftPort)
    val motorFrontRight = WPI_TalonSRX(Constants.kDrivetrainFrontRightPort)
    val motorBackRight = WPI_TalonSRX(Constants.kDrivetrainBackRightPort)

    val shooter = ShooterSubsystem(CANSparkMax(10, MotorType.kBrushless))

    /** The container for the robot. Contains subsystems, OI devices, and commands.  */
    init {
        // Configure the button bindings
        configureButtonBindings()
    }
    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a [GenericHID] or one of its subclasses ([ ] or [XboxController]), and then passing it to a [ ].
     */
    private fun configureButtonBindings() {
        JoystickButton(controller0, 1).whenHeld(ShooterBangBang(shooter, { 400.0 }))
    }// An ExampleCommand will run in autonomous

    /**
     * Use this to pass the autonomous command to the main [Robot] class.
     *
     * @return the command to run in autonomous
     */
    val autonomousCommand: Command
        get() =// An ExampleCommand will run in autonomous
            m_autoCommand
}