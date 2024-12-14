package org.shockwave

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import org.shockwave.subsystem.elevator.ElevatorIOSpark
import org.shockwave.subsystem.elevator.ElevatorSubsystem
import org.shockwave.utils.Tab

object RobotContainer {
  val elevator = ElevatorSubsystem(ElevatorIOSpark())

  val driverController = CommandXboxController(GlobalConstants.DRIVER_CONTROLLER_PORT)

  init {
    DriverStation.silenceJoystickConnectionWarning(true)
    Tab.MATCH.add(InstantCommand(elevator::zeroEncoder, elevator))
  }
}