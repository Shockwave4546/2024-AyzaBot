package org.shockwave.subsystem.elevator

import com.revrobotics.REVLibError
import org.littletonrobotics.junction.LogTable
import org.littletonrobotics.junction.inputs.LoggableInputs
import org.shockwave.utils.PIDFGains

interface ElevatorIO {
  class ElevatorIOInputs : LoggableInputs {
    var rev = 0.0
    var appliedVolts = 0.0
    var current = 0.0
    var temp = 0.0

    override fun toLog(table: LogTable) {
      table.put("(1) Rev", rev)
      table.put("(2) AppliedVolts", appliedVolts)
      table.put("(3) Current", current)
      table.put("(4) Temp", temp)
    }

    override fun fromLog(table: LogTable) {
      rev = table.get("(1) Rev", rev)
      appliedVolts = table.get("(2) AppliedVolts", appliedVolts)
      current = table.get("(3) Current", current)
      temp = table.get("(4) Temp", temp)
    }
  }

  fun updateInputs(inputs: ElevatorIOInputs)

  fun setRevSetpoint(rev: Double): REVLibError

  fun setPIDF(pidf: PIDFGains): REVLibError

  fun zeroEncoder(): REVLibError

  fun stop()
}