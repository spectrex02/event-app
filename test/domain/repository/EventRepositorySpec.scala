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
  //  "find" should "return option(event)" in {
  //    assert(EventRepository.find(100).get == Event(100, "test event", Map(DateFormatter.string2date("2019-08-01 00:00") -> Seq.empty[Vote], DateFormatter.string2date("2019-08-10 00:00") -> Seq.empty[Vote]), DateFormatter.string2date("2019-07-31 00:00"), "test comment"))
  //  }
  //
  //  "findAll" should "return Seq[Event]" in {
  //    assert(EventRepository.findAll() == Seq(Event(100, "test event", Map(DateFormatter.string2date("2019-08-01 00:00") -> Seq.empty[Vote], DateFormatter.string2date("2019-08-10 00:00") -> Seq.empty[Vote]), DateFormatter.string2date("2019-07-31 00:00"), "test comment")))
  //  }

  //  "insertEvent" should "return Boolean" in {
  //    val newEvent = Event(-1, "insertEvent", CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-08-12T10:00:00"), Seq.empty[Vote]))), DateFormatter.string2date("2019-08-07T17:00:00"), "insert event test")
  ////    Map(DateFormatter.string2date("2019-08-12 10:00") -> Seq.empty[Vote])
  //    assert(EventRepository.insertEvent(newEvent, "tester for insert") == false)
  //  }

  //  "deleteEvent" should "return  bool" in {
  //    assert(EventRepository.deleteEvent(2) == false)
  //  }
  "updateCandidateDates" should "reflect db" in {
    val newEvent = Event(1, "insertEvent", CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-08-12T10:00:00"), Seq.empty[Vote]))), DateFormatter.string2date("2019-08-07T17:00:00"), "insert event test")
    val updatedEvent : Event = newEvent.copy(candidateDates = CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-09-11T00:00:00"), Seq.empty[Vote]))))
    assert(!EventRepository.updateCandidateDates(updatedEvent))
  }

  "find" should "return event" in {
    val newEvent = Event(1, "insertEvent", CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-08-12T10:00:00"), Seq.empty[Vote]))), DateFormatter.string2date("2019-08-07T17:00:00"), "insert event test")
    val event : Event = newEvent.copy(candidateDates = CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-09-11T00:00:00"), Seq.empty[Vote]))))
    assert(event == EventRepository.find(1))
  }
  "findall" should "return event" in {
    val a: Map[Int, String] =EventRepository.findAll()
    assert(a == (1 -> "insertEvent"))
  }


//  "insertVoting" should "reflect db" in {
//    assert(EventRepository.insertVoting(1, DateFormatter.string2date("2019-09-11T00:00:00"), Vote("name1", VotingValue.from(2))) == false)
//  }

  "updateVoting" should "reflect db" in {
    assert(!EventRepository.updateVoting(1, DateFormatter.string2date("2019-09-11T00:00:00"), Vote("name1", VotingValue.from(0))))
  }

  "deleteVoting" should "reflect db" in {
    assert(!EventRepository.deleteVoting(1, DateFormatter.string2date("2019-09-11T00:00:00"), Vote("name1", VotingValue.from(0))))
  }
}
