package smarthome

import java.time.LocalDateTime.parse

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import slick.driver.H2Driver.api._
import smarthome.Tables._

class SmarthomeDbTest extends FunSuite with BeforeAndAfter with ScalaFutures with ShouldMatchers {
  implicit override val patienceConfig = PatienceConfig(timeout = Span(5, Seconds))

  implicit var db: Database = _

  val livingRoom = Room(RoomId(1), "Living room")
  val kitchen = Room(RoomId(2), "Kitchen")

  before {
    db = Database.forConfig("h2mem1")
    db.run(SmarthomeDb.schema.create).futureValue
    insertTestData()
  }

  test("Getting all rooms works") {
    val allRooms = SmarthomeDb.allRooms.futureValue

    allRooms.size shouldBe 2
  }

  test("Finding a room works") {
    val room = SmarthomeDb.findRoom(RoomId(1)).futureValue

    room shouldBe Some(livingRoom)
  }

  test("Inserting a new room works") {
    val room = Room(RoomId(3), "Bedroom")
    val inserted = SmarthomeDb.insertRoom(room).futureValue
    val allRooms = SmarthomeDb.allRooms.futureValue

    inserted shouldBe 1
    allRooms.size shouldBe 3
  }

  test("Renaming room works") {
    val updated = SmarthomeDb.renameRoom(RoomId(2), "Bathroom").futureValue
    val room = SmarthomeDb.findRoom(RoomId(2)).futureValue

    updated shouldBe 1
    room.map(_.name) shouldBe Some("Bathroom")
  }

  test("Get names of rooms") {
    val names = SmarthomeDb.roomNames.futureValue

    names should contain allOf ("Living room", "Kitchen")
  }

  test("Find temperature sensors") {
    val sensors = SmarthomeDb.sensorsOfType("Temperature").futureValue

    sensors.size shouldBe 2
  }

  test("Find rooms with light sensor") {
    val result = SmarthomeDb.roomsWithSensor("Light").futureValue

    result.size shouldBe 1
    result should contain (livingRoom)
  }

  test("Find temperature readings for room") {
    val result = SmarthomeDb.getReadings(livingRoom.id, "Temperature").futureValue

    result.size shouldBe 2
  }

  test("Find max temperature") {
    val result = SmarthomeDb.maxTemperature(livingRoom.id).futureValue

    result shouldBe Some(BigDecimal(21))
  }

  def insertTestData(): Unit = {
    db.run(DBIO.seq(
      rooms ++= Seq(livingRoom, kitchen),
      sensors ++= Seq(Sensor(SensorId(1), livingRoom.id, "Temperature"),
        Sensor(SensorId(2), livingRoom.id, "Light"),
        Sensor(SensorId(3), kitchen.id, "Temperature")),
      readings ++= Seq(Reading(None, SensorId(1), parse("2016-03-09T05:30:00"), BigDecimal(17)),
        Reading(None, SensorId(1), parse("2016-03-09T13:30:00"), BigDecimal(21)),
        Reading(None, SensorId(2), parse("2016-03-09T05:00:00"), BigDecimal(0.2)),
        Reading(None, SensorId(3), parse("2016-03-09T05:30:00"), BigDecimal(17)))
    )).futureValue
  }

  after {
    db.close()
  }

}
