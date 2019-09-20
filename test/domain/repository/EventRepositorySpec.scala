package domain.repository

import java.time.LocalDateTime

import domain.entity._
import org.scalatest.{FlatSpec, Matchers}
import scalikejdbc._

class EventRepositorySpec extends FlatSpec with Matchers {

  //connection
  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton("jdbc:postgresql://localhost:5000/eventapp", "tomoya", "tomoya")
  implicit val session = AutoSession

//    "insertEvent" should "return Boolean" in {
//      val newEvent = Event(-1, "insertEvent", CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-08-12T10:00:00"), Seq.empty[Vote]))), DateFormatter.string2date("2019-08-07T17:00:00"), "insert event test")
//      assert(EventRepository.insertEvent(newEvent, "tester for insert") == false)
//    }

//    "openEvent" should "reflect db" in {
//      assert(!EventRepository.openEvent(3))
//    }

//    "insertVoting" should "reflect db" in {
//      assert(EventRepository.insertVoting(3, DateFormatter.string2date("2019-08-12T10:00:00"), Vote("name1", VotingValue.from(2))) == false)
//    }


  "find" should "return event" in {
    val event = Event(3, "insertEvent", CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-08-12T10:00:00"), Seq(Vote("name1", VotingValue.from(2)))))), DateFormatter.string2date("2019-08-07T17:00:00"), "insert event test")
    assert(EventRepository.find(3).get == event)
  }

//  "findall" should "return event" in {
//    val a: Map[Int, String] =EventRepository.findAll()
//    assert(a == Map(1 -> "insertEvent"))
//  }

//


//  "updateVoting" should "reflect db" in {
//    assert(!EventRepository.updateVoting(1, DateFormatter.string2date("2019-09-11T00:00:00"), Vote("name1", VotingValue.from(0))))
//  }
//
//  "deleteVoting" should "reflect db" in {
//    assert(!EventRepository.deleteVoting(1, DateFormatter.string2date("2019-09-11T00:00:00"), Vote("name1", VotingValue.from(0))))
//  }
//

}
