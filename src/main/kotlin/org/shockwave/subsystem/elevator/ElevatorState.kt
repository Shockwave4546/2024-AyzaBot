package org.shockwave.subsystem.elevator

data class ElevatorState(val name: String, val rev: Double) {
  companion object {
    val STARTING = ElevatorState("Starting", 2.5)
  }
}
