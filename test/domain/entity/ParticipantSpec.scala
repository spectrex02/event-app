package domain.entity

import org.scalatest.{FlatSpec, Matchers}

class ParticipantSpec extends FlatSpec with Matchers {
  "createVoting" should "return new event instance that is added your voting." in {
    val planner = Planner("test planner")
    val testEvent = planner.createEvent("test event", Seq(DateFormatter.string2date("2019-07-30 19:00"), DateFormatter.string2date("2019-07-31 19:00")), DateFormatter.string2date("2019-07-30 22:00"), "test comment.")

    val participant1 = Participant("participant1")
    val participant2 = Participant("participant2")
    assert(testEvent.candidateDates(DateFormatter.string2date("2019-07-30 19:00")) == Seq.empty[Vote])

    var updatedEvent: Event = participant1.createVoting(testEvent, DateFormatter.string2date("2019-07-30 19:00"), 2)
    assert(updatedEvent.candidateDates(DateFormatter.string2date("2019-07-30 19:00")) == Seq(Vote("participant1", VotingValue.Maru)))

    updatedEvent = participant2.createVoting(updatedEvent, DateFormatter.string2date("2019-07-30 19:00"), 1)
    assert(updatedEvent.candidateDates(DateFormatter.string2date("2019-07-30 19:00")) == Seq(Vote("participant1", VotingValue.Maru), Vote("participant2", VotingValue.Sankaku)))

    updatedEvent = participant1.createVoting(updatedEvent, DateFormatter.string2date("2019-07-31 19:00"), 0)
    assert(updatedEvent.candidateDates(DateFormatter.string2date("2019-07-31 19:00")) == Seq(Vote("participant1", VotingValue.Batu)))
  }

  "updateVoting" should "return new event instance that is updated by participants" in {
    val planner = Planner("test planner")
    val testEvent = planner.createEvent("test event", Seq(DateFormatter.string2date("2019-07-30 19:00"), DateFormatter.string2date("2019-07-31 19:00")), DateFormatter.string2date("2019-07-30 22:00"), "test comment.")

    val participant1 = Participant("participant1")
    val participant2 = Participant("participant2")

    //participant1-> (07-30, Maru)
    var updatedEvent: Event = participant1.createVoting(testEvent, DateFormatter.string2date("2019-07-30 19:00"), 2)
    //participant2-> (07-30, Sankaku)
    updatedEvent = participant2.createVoting(updatedEvent, DateFormatter.string2date("2019-07-30 19:00"), 1)
    //participant1-> (07-31, Batu)
    updatedEvent = participant1.createVoting(updatedEvent, DateFormatter.string2date("2019-07-31 19:00"), 0)

    //participant1-> (07-30, Batu)
    updatedEvent = participant1.updateVoting(updatedEvent, DateFormatter.string2date("2019-07-30 19:00"), 0)
    assert(updatedEvent.candidateDates(DateFormatter.string2date("2019-07-30 19:00")) == Seq(Vote("participant2", VotingValue.Sankaku), Vote("participant1", VotingValue.Batu)))

    //participant2-> (07-30, Maru)
    updatedEvent = participant2.updateVoting(updatedEvent, DateFormatter.string2date("2019-07-30 19:00"), 2)
    assert(updatedEvent.candidateDates(DateFormatter.string2date("2019-07-30 19:00")) == Seq(Vote("participant1", VotingValue.Batu), Vote("participant2", VotingValue.Maru)))
  }

  "deleteVoting" should "return new event instance from which some voting is deleted by user." in {
    val planner = Planner("test planner")
    val testEvent = planner.createEvent("test event", Seq(DateFormatter.string2date("2019-07-30 19:00"), DateFormatter.string2date("2019-07-31 19:00")), DateFormatter.string2date("2019-07-30 22:00"), "test comment.")

    val participant1 = Participant("participant1")
    val participant2 = Participant("participant2")

    //participant1-> (07-30, Maru)
    var updatedEvent: Event = participant1.createVoting(testEvent, DateFormatter.string2date("2019-07-30 19:00"), 2)
    //participant2-> (07-30, Sankaku)
    updatedEvent = participant2.createVoting(updatedEvent, DateFormatter.string2date("2019-07-30 19:00"), 1)
    //participant1-> (07-31, Batu)
    updatedEvent = participant1.createVoting(updatedEvent, DateFormatter.string2date("2019-07-31 19:00"), 0)

    updatedEvent = participant1.deleteVoting(updatedEvent, DateFormatter.string2date("2019-07-31 19:00"))
    assert(updatedEvent.candidateDates(DateFormatter.string2date("2019-07-31 19:00")) == Seq.empty[Vote])

    updatedEvent = participant1.deleteVoting(updatedEvent, DateFormatter.string2date("2019-07-30 19:00"))
    assert(updatedEvent.candidateDates(DateFormatter.string2date("2019-07-30 19:00")) == Seq(Vote("participant2", VotingValue.Sankaku)))

  }
}
