package entity


import org.scalatest.{FlatSpec, Matchers}

class PlannerSpec extends FlatSpec with Matchers {
  "createEvent" should "return new event instance." in {
    val planner = Planner("test")
    val testEvent = planner.createEvent("test event", Seq(DateFormatter.string2date("2019-07-30 19:00"), DateFormatter.string2date("2019-07-31 19:00")), DateFormatter.string2date("2019-07-30 22:00"), "test comment.")
    assert(testEvent.eventName == "test event")
    assert(testEvent.candidateDates == Map(DateFormatter.string2date("2019-07-30 19:00") -> Seq.empty[Vote], DateFormatter.string2date("2019-07-31 19:00") -> Seq.empty[Vote]))
    assert(testEvent.deadline == DateFormatter.string2date("2019-07-30 22:00"))
    assert(testEvent.comment == "test comment.")
//    testEvent.eventStatus should be true
  }

  "updateEvent" should "return updated event instance." in {
    val planner = Planner("test")
    val testEvent = planner.createEvent("test event", Seq(DateFormatter.string2date("2019-08-21 00:00"), DateFormatter.string2date("2019-07-30 19:00"), DateFormatter.string2date("2019-07-31 19:00")), DateFormatter.string2date("2019-07-30 22:00"), "test comment.")
    val updatedEvent = planner.updateEvent(testEvent, newEventName = Option("updated test event"),
      newCandidateDates = Option(Seq(DateFormatter.string2date("2019-07-30 19:00"), DateFormatter.string2date("2019-07-01 18:00"))),
      newDeadline = Option(DateFormatter.string2date("2019-08-01 12:00")),
      newComment = Option("updated test comment"))
    assert(updatedEvent.eventName == "updated test event")
    assert(updatedEvent.deadline == DateFormatter.string2date("2019-08-01 12:00"))
    assert(updatedEvent.comment == "updated test comment")
    assert(updatedEvent.candidateDates == Map(DateFormatter.string2date("2019-07-30 19:00") -> Seq.empty[Vote], DateFormatter.string2date("2019-07-01 18:00") -> Seq.empty[Vote]))
  }
}
