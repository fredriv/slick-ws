package smarthome

import java.time.LocalDateTime

import slick.driver.H2Driver.api.MappedTo

// Value classes act as wrapper classes to get type safety but avoid allocating runtime objects:
// http://docs.scala-lang.org/overviews/core/value-classes.html
case class RoomId(value: Int) extends AnyVal with MappedTo[Int]
case class SensorId(value: Int) extends AnyVal with MappedTo[Int]

case class Room(id: RoomId, name: String)
case class Sensor(id: SensorId, roomId: RoomId, sensorType: String)
case class Reading(id: Option[Int], sensorId: SensorId, timestamp: LocalDateTime, value: BigDecimal)

