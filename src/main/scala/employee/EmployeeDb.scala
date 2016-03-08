package employee

import slick.driver.H2Driver.api._

import scala.concurrent.Future

object EmployeeDb {

  class Employees(tag: Tag) extends Table[(Int, String, Int)](tag, "EMPLOYEES") {
    def id = column[Int]("EMPLOYEE_ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def salary = column[Int]("SALARY")

    def * = (id, name, salary)
  }

  val employees = TableQuery[Employees]


  def allEmployees(implicit db: Database): Future[Seq[(Int, String, Int)]] =
    db.run(employees.result)

  def findById(employeeId: Int)
              (implicit db: Database): Future[Option[(Int, String, Int)]] = {
    val q = employees.findBy(_.id)
    db.run(q(employeeId).result.headOption)
  }

  def salaries(implicit db: Database): Future[Seq[Int]] = {
    val q = employees.map(_.salary)
    db.run(q.result)
  }

  def employeeNames(implicit db: Database): Future[Seq[String]] = {
    val q = employees.map(_.name)
    db.run(q.result)
  }

  private val highEarners = employees.filter(_.salary > 600000)

  def getHighEarners(implicit db: Database): Future[Seq[(Int, String, Int)]] =
    db.run(highEarners.result)

  def topEarners(implicit db: Database): Future[Seq[String]] = {
    val q = highEarners.sortBy(_.salary.desc).map(_.name).take(10)
    db.run(q.result)
  }

}
