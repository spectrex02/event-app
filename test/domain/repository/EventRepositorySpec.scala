package domain.repository

import java.time.LocalDateTime

import org.scalatest.{FlatSpec, Matchers}
import scalikejdbc.AutoSession

class EventRepositorySpec extends FlatSpec with Matchers {
  "find" should "return option(event)" in {
    implicit val session = AutoSession
    assert(EventRepository.find(100) == Seq.empty[LocalDateTime])
  }
}
