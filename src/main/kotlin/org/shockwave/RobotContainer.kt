package org.shockwave

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.InstantCommand
import org.shockwave.subsystem.elevator.ElevatorIOSpark
import org.shockwave.subsystem.elevator.ElevatorSubsystem
import org.shockwave.utils.Tab

object RobotContainer {
  val elevator = ElevatorSubsystem(ElevatorIOSpark())

  init {
    DriverStation.silenceJoystickConnectionWarning(true)
    Tab.MATCH.add(InstantCommand(elevator::zeroEncoder, elevator))
  }
}