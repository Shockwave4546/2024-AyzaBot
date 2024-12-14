package org.shockwave.subsystem.elevator

import com.revrobotics.spark.config.ClosedLoopConfig
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.wpilibj.Alert
import org.shockwave.MotorConstants
import org.shockwave.utils.PIDFGains

class ElevatorConstants {
  companion object {
    private const val ENCODER_INVERTED = false
    const val MOTOR_CAN_ID = 33
    const val REV_CONVERSION_FACTOR = 1.0

    val GAINS = PIDFGains(0.0, 0.0, 0.0)
    private const val MIN_OUTPUT = -1.0
    private const val MAX_OUTPUT = 1.0
    const val REV_TOLERANCE = 10.0

    const val MIN_REV = 0.0;
    const val MAX_REV = 100.0;

    val SHOULD_STOP_MOTOR_ALERT = Alert("The encoder is reporting an angle that will break the pivot", Alert.AlertType.kError)

    val MOTOR_CONFIG = SparkMaxConfig().apply {
      smartCurrentLimit(MotorConstants.NEO_CURRENT_LIMIT)
      idleMode(SparkBaseConfig.IdleMode.kBrake)

      encoder
        .positionConversionFactor(REV_CONVERSION_FACTOR)
        .inverted(ENCODER_INVERTED)

      closedLoop
        .pidf(GAINS.p, GAINS.i, GAINS.d, GAINS.ff)
        .outputRange(MIN_OUTPUT, MAX_OUTPUT)
        .feedbackSensor(ClosedLoopConfig.FeedbackSensor.kPrimaryEncoder)
    }
  }
}