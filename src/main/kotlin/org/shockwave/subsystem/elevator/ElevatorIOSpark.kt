package org.shockwave.subsystem.elevator

import com.revrobotics.REVLibError
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.ClosedLoopConfig
import com.revrobotics.spark.config.SparkMaxConfig
import org.shockwave.utils.PIDFGains

class ElevatorIOSpark : ElevatorIO {
  private val motor = SparkMax(ElevatorConstants.MOTOR_CAN_ID, SparkLowLevel.MotorType.kBrushless)
  private val encoder = motor.absoluteEncoder
  private val pid = motor.closedLoopController

  init {
    motor.configure(ElevatorConstants.MOTOR_CONFIG, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters)
  }

  override fun updateInputs(inputs: ElevatorIO.ElevatorIOInputs) {
    inputs.rev = encoder.position
    inputs.appliedVolts = motor.appliedOutput * motor.busVoltage
    inputs.current = motor.outputCurrent
    inputs.temp = motor.motorTemperature
  }

  override fun setRevSetpoint(rev: Double): REVLibError = pid.setReference(rev, SparkBase.ControlType.kPosition)

  override fun setPIDF(pidf: PIDFGains): REVLibError = motor.configure(
    SparkMaxConfig().apply(
      ClosedLoopConfig().pidf(pidf.p, pidf.i, pidf.d, pidf.ff)
    ),
    SparkBase.ResetMode.kNoResetSafeParameters,
    SparkBase.PersistMode.kPersistParameters
  )

  override fun zeroEncoder(): REVLibError = motor.encoder.setPosition(0.0)

  override fun stop() = motor.stopMotor()
}