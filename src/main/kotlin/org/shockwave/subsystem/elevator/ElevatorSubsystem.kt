package org.shockwave.subsystem.elevator

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.shockwave.utils.TunablePIDF

class ElevatorSubsystem(private val elevator: ElevatorIO) : SubsystemBase() {
  private val inputs = ElevatorIO.ElevatorIOInputs()
  private var desiredState = ElevatorState.STARTING

  private val key = "Elevator"
  private val pidf = TunablePIDF("AdvantageKit/$key/Tuning/", ElevatorConstants.GAINS)

  override fun periodic() {
    elevator.updateInputs(inputs)
    Logger.processInputs(key, inputs)

    pidf.periodic(elevator::setPIDF) { value ->
      val clamped = MathUtil.clamp(value, ElevatorConstants.MIN_REV, ElevatorConstants.MAX_REV)
      elevator.setRevSetpoint(clamped)
      this.desiredState = ElevatorState("Manual", clamped)
    }

    if (shouldStopPivot()) {
      ElevatorConstants.SHOULD_STOP_MOTOR_ALERT.text = "Elevator is at a dangerous angle, ${inputs.rev} > ${ElevatorConstants.MAX_REV}"
      ElevatorConstants.SHOULD_STOP_MOTOR_ALERT.set(true)
    }

    Logger.recordOutput("$key/DesiredState/1.Name", desiredState.name)
    Logger.recordOutput("$key/DesiredState/2.Rev", desiredState.rev)
    Logger.recordOutput("$key/DesiredState/3.At Goal", atDesiredState())
    Logger.recordOutput("$key/Should Stop Elevator", shouldStopPivot())
  }

  fun setDesiredState(desiredState: ElevatorState) {
    if (pidf.isManualMode()) return
    if (shouldStopPivot()) return

    val clamped = MathUtil.clamp(desiredState.rev, ElevatorConstants.MIN_REV, ElevatorConstants.MAX_REV)
    elevator.setRevSetpoint(clamped)
    this.desiredState = desiredState
  }

  fun atDesiredState() = atDesiredState(desiredState)

  fun atDesiredState(state: ElevatorState) = inputs.rev in state.rev - ElevatorConstants.REV_TOLERANCE..state.rev + ElevatorConstants.REV_TOLERANCE

  fun zeroEncoder() = elevator.zeroEncoder()

  private fun shouldStopPivot() = inputs.rev > ElevatorConstants.MAX_REV
}