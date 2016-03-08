package smarthome

import java.time.LocalDateTime

import slick.driver.H2Driver.api._

import scala.concurrent.Future

object SmarthomeDb {
  import Tables._

  val schema = (rooms.schema ++ sensors.schema ++ readings.schema)

  def allRooms(implicit db: Database): Future[Seq[Room]] = {
    ???
  }

  def findRoom(roomId: RoomId)(implicit db: Database): Future[Option[Room]] = {
    ???
  }

  def insertRoom(room: Room)
                (implicit db: Database): Future[Int] = {
    ???
  }

  def roomNames(implicit db: Database): Future[Seq[String]] = {
    ???
  }

  def renameRoom(roomId: RoomId, newName: String)
                (implicit db: Database): Future[Int] = {
    ???
  }

  def sensorsOfType(sensorType: String)
                   (implicit db: Database): Future[Seq[Sensor]] = {
    ???
  }

  def roomsWithSensor(sensorType: String)
                     (implicit db: Database): Future[Seq[Room]] = {
    ???
  }

  def getReadings(roomId: RoomId, sensorType: String)
                 (implicit db: Database): Future[Seq[(LocalDateTime, BigDecimal)]] = {
    ???
  }

  def maxTemperature(roomId: RoomId)
                    (implicit db: Database): Future[Option[BigDecimal]] = {
    ???
  }

  // Could be useful
  private def getReadingsQuery(roomId: RoomId, sensorType: String): Query[Readings, Reading, Seq] = {
    ???
  }

}
