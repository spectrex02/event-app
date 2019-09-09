package domain.entity


import org.scalatest.{FlatSpec, Matchers}

class PlannerSpec extends FlatSpec with Matchers {
  "createEvent" should "return new event instance." in {
    val planner = Planner("test")
    val testEvent = planner.createEvent("test event", Seq(DateFormatter.string2date("2019-07-30T19:00:00"), DateFormatter.string2date("2019-07-31T19:00:00")), DateFormatter.string2date("2019-07-30T22:00:00"), "test comment.")
    assert(testEvent.eventName == "test event")
//    assert(testEvent.candidateDates == Map(DateFormatter.string2date("2019-07-30T19:00:00") -> Seq.empty[Vote], DateFormatter.string2date("2019-07-31T19:00:00") -> Seq.empty[Vote]))
    assert(testEvent.candidateDates == CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-07-30T19:00:00"), Seq.empty[Vote]), Candidate(DateFormatter.string2date("2019-07-31T19:00:00"), Seq.empty[Vote]))))
    assert(testEvent.deadline == DateFormatter.string2date("2019-07-30T22:00:00"))
    assert(testEvent.comment == "test comment.")
//    testEvent.eventStatus should be true
  }

  "updateEvent" should "return updated event instance." in {
    val planner = Planner("test")
    val testEvent = planner.createEvent("test event", Seq(DateFormatter.string2date("2019-08-21T00:00:00"), DateFormatter.string2date("2019-07-30T21:00:00"), DateFormatter.string2date("2019-07-31T19:00:00")), DateFormatter.string2date("2019-07-30T22:00:00"), "test comment.")
    val updatedEvent = planner.updateEvent(testEvent, newEventName = Some("updated test event"),
      newCandidateDates = Some(Seq(DateFormatter.string2date("2019-07-30T19:00:00"), DateFormatter.string2date("2019-07-01T18:00:00"))),
      newDeadline = Some(DateFormatter.string2date("2019-08-01T12:00:00")),
      newComment = Some("updated test comment"))
    assert(updatedEvent.eventName == "updated test event")
    assert(updatedEvent.deadline == DateFormatter.string2date("2019-08-01T12:00:00"))
    assert(updatedEvent.comment == "updated test comment")
//    assert(updatedEvent.candidateDates == Map(DateFormatter.string2date("2019-07-30T19:00:00") -> Seq.empty[Vote], DateFormatter.string2date("2019-07-01T18:00:00") -> Seq.empty[Vote]))
    assert(updatedEvent.candidateDates == CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-07-30T19:00:00"), Seq.empty[Vote]), Candidate(DateFormatter.string2date("2019-07-01T18:00:00"), Seq.empty[Vote]))))

  }
}
