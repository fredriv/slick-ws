package smarthome

import java.time.LocalDateTime

import slick.driver.H2Driver.api._

import scala.concurrent.Future

object SmarthomeDb {
  import Tables._

  val schema = (rooms.schema ++ sensors.schema ++ readings.schema)

  def allRooms(implicit db: Database): Future[Seq[Room]] = {
    val q = rooms

    db.run(q.result)
  }

  def findRoom(roomId: RoomId)(implicit db: Database): Future[Option[Room]] = {
    val q = rooms.filter(_.id === roomId)

    db.run(q.result.headOption)
  }

  def insertRoom(room: Room)
                (implicit db: Database): Future[Int] = {
    val q = rooms += room

    db.run(q)
  }

  def roomNames(implicit db: Database): Future[Seq[String]] = {
    val q = rooms.map(_.name)

    db.run(q.result)
  }

  def renameRoom(roomId: RoomId, newName: String)
                (implicit db: Database): Future[Int] = {
    val q = rooms.filter(_.id === roomId).map(_.name)

    db.run(q.update(newName))
  }

  def sensorsOfType(sensorType: String)
                   (implicit db: Database): Future[Seq[Sensor]] = {
    val q = sensors.filter(_.sensorType === sensorType)

    db.run(q.result)
  }

  def roomsWithSensor(sensorType: String)
                     (implicit db: Database): Future[Seq[Room]] = {
    val q = for {
      sensor <- sensors if sensor.sensorType === sensorType
      room <- sensor.room
    } yield room

    db.run(q.result)
  }

  def getReadings(roomId: RoomId, sensorType: String)
                 (implicit db: Database): Future[Seq[(LocalDateTime, BigDecimal)]] = {
    val q = for {
      reading <- getReadingsQuery(roomId, sensorType)
    } yield (reading.timestamp, reading.value)

    db.run(q.result)
  }

  def maxTemperature(roomId: RoomId)
                    (implicit db: Database): Future[Option[BigDecimal]] = {
    val q = getReadingsQuery(roomId, "Temperature").map(_.value).max

    db.run(q.result)
  }

  // Could be useful
  private def getReadingsQuery(roomId: RoomId, sensorType: String): Query[Readings, Reading, Seq] = {
    for {
      reading <- readings
      sensor <- reading.sensor
      if sensor.sensorType === sensorType
      if sensor.roomId === roomId
    } yield reading
  }
}
