package smarthome

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneOffset}

import slick.driver.H2Driver.api._

object Tables {

  class Rooms(tag: Tag) extends Table[Room](tag, "ROOMS") {
    def id   = column[RoomId] ("ID", O.PrimaryKey)
    def name = column[String] ("NAME")

    def * = (id, name) <> (Room.tupled, Room.unapply)
  }

  val rooms = TableQuery[Rooms]


  class Sensors(tag: Tag) extends Table[Sensor](tag, "SENSORS") {
    def id         = column[SensorId] ("ID", O.PrimaryKey)
    def roomId     = column[RoomId]   ("ROOM_ID")
    def sensorType = column[String]   ("SENSOR_TYPE")

    def * = (id, roomId, sensorType) <> (Sensor.tupled, Sensor.unapply)

    def room = foreignKey("ROOM_FK", roomId, rooms)(_.id)
  }

  val sensors = TableQuery[Sensors]


  implicit val dateTimeMapper = MappedColumnType.base[LocalDateTime, Timestamp](
    { date => Timestamp.from(date.toInstant(ZoneOffset.ofHours(0))) },
    { timestamp => timestamp.toLocalDateTime }
  )

  class Readings(tag: Tag) extends Table[Reading](tag, "READINGS") {
    def id        = column[Option[Int]]   ("ID", O.PrimaryKey, O.AutoInc)
    def sensorId  = column[SensorId]      ("SENSOR_ID")
    def timestamp = column[LocalDateTime] ("TIMESTAMP")
    def value     = column[BigDecimal]    ("VALUE")

    def * = (id, sensorId, timestamp, value) <> (Reading.tupled, Reading.unapply)

    def sensor = foreignKey("SENSOR_FK", sensorId, sensors)(_.id)
  }

  val readings = TableQuery[Readings]

}
