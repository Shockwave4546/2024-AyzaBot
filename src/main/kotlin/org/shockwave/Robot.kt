package org.shockwave

import edu.wpi.first.wpilibj.Alert
import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.littletonrobotics.junction.LoggedRobot
import org.littletonrobotics.junction.Logger
import org.littletonrobotics.junction.networktables.NT4Publisher
import org.littletonrobotics.junction.wpilog.WPILOGWriter
import java.nio.file.Files
import java.nio.file.Paths

object Robot : LoggedRobot() {
  private val logFolderFound = Alert("Logging is enabled.", Alert.AlertType.kInfo)
  private val logFolderNotFound = Alert("Logging is disabled as the log folder doesn't exist!", Alert.AlertType.kWarning)
  private val garbageCollectorTimer = Timer()

  override fun robotInit() {
    when (GlobalConstants.ROBOT_TYPE) {
      RobotType.REAL -> {
        Logger.addDataReceiver(NT4Publisher())
        PowerDistribution() // Enables power distribution logging.

        val logFolderPath = Paths.get(GlobalConstants.LOG_FOLDER_PATH)
        if (Files.notExists(logFolderPath)) Files.createDirectories(logFolderPath) // Create /log folder if it's a new RIO
        if (Files.exists(logFolderPath)) {
          logFolderFound.set(true)
          Logger.addDataReceiver(WPILOGWriter())
        } else {
          logFolderNotFound.set(true)
        }
      }

      RobotType.SIM -> {
        Logger.addDataReceiver(NT4Publisher())
      }
    }

    Logger.recordMetadata("MavenGroup", MAVEN_GROUP)
    Logger.recordMetadata("MavenName", MAVEN_NAME)
    Logger.recordMetadata("BuildDate", BUILD_DATE)
    Logger.recordMetadata("GitSHA", GIT_SHA)
    Logger.recordMetadata("GitBranch", GIT_BRANCH)
    when (DIRTY) {
      0 -> Logger.recordMetadata("GitDirty", "All changes committed")
      1 -> Logger.recordMetadata("GitDirty", "Uncommitted changes")
      else -> Logger.recordMetadata("GitDirty", "Unknown")
    }

    Logger.start()

    CommandScheduler.getInstance().onCommandInitialize { command -> Logger.recordOutput("/ActiveCommands/${command.name}", true) }
    CommandScheduler.getInstance().onCommandFinish { command -> Logger.recordOutput("/ActiveCommands/${command.name}", false) }
    CommandScheduler.getInstance().onCommandInterrupt { command -> Logger.recordOutput("/ActiveCommands/${command.name}", false) }
  }

  override fun robotPeriodic() {
    if (garbageCollectorTimer.advanceIfElapsed(5.0)) {
      System.gc()
    }

    CommandScheduler.getInstance().run()
  }

  override fun disabledPeriodic() {
  }

  override fun autonomousInit() {
  }

  override fun autonomousPeriodic() {

  }

  override fun teleopInit() {

  }

  override fun teleopPeriodic() {

  }
}