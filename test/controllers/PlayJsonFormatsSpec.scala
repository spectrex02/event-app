package controllers

import domain.entity.{Candidate, CandidateDates, DateFormatter, Event, Vote, VotingValue}
import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json._
import PlayJsonFormats._
import play.api.libs.EventSource.EventDataExtractor

class PlayJsonFormatsSpec extends FlatSpec with Matchers {
  "fromJson" should "return Option[Event] class instance." in {
    val json: JsValue = Json.parse(
      """
        |{
        |    "id": 1,
        |    "eventName": "example_event",
        |    "candidateDates":[
        |    {
        |        "date": "2019-08-30T19:00:00",
        |        "votes": [
        |                {
        |                    "participant": "name1",
        |                    "status": 1
        |                },
        |                {
        |                    "participant": "name2",
        |                    "status": 2
        |                }
        |            ]
        |        },
        |    {
        |        "date": "2019-09-01T19:00:00",
        |        "votes": [
        |            {
        |                "participant": "name1",
        |                "status":1
        |            },
        |            {
        |                "participant": "name2",
        |                "status": 0
        |            }
        |            ]
        |      }
        |     ],
        |    "deadline": "2019-08-29T19:00:00",
        |    "comment": "example comment"
        |  }
      """.stripMargin)

    println(json)
//    assert(PlayJsonFormats.eventFromJson(json).getOrElse(Event(0,"none", CandidateDates(Seq.empty[Candidate]), DateFormatter.string2date("2019-09-09T14:00:00"), "none")).id == 1)
    val a: Option[Event] = PlayJsonFormats.eventFromJson(json)
    println(a)
    println(a.get)
    assert(PlayJsonFormats.eventFromJson(json).get.id ==1)
  }

  "FromJson" should "return Option[Event] class instance." in {
    val json: JsValue = Json.parse(
      """
        |{
        |    "id": 1,
        |    "eventName": "example_event",
        |    "candidateDates":[
        |    {
        |        "date": "2019-08-30T19:00:00",
        |        "votes": [
        |                {
        |                    "participant": "name1",
        |                    "status": 1
        |                },
        |                {
        |                    "participant": "name2",
        |                    "status": 2
        |                }
        |            ]
        |        },
        |    {
        |        "date": "2019-09-01T19:00:00",
        |        "votes": [
        |            {
        |                "participant": "name1",
        |                "status":1
        |            },
        |            {
        |                "participant": "name2",
        |                "status": 0
        |            }
        |            ]
        |      }
        |     ],
        |    "deadline": "2019-08-29T19:00:00",
        |    "comment": "example comment"
        |  }
      """.stripMargin)

    println(json)
//    assert(PlayJsonFormats.eventFromJson(json).getOrElse(Event(0,"none", CandidateDates(Seq.empty[Candidate]), DateFormatter.string2date("2019-09-09T14:00:00"), "none")).id == 1)
    val a: Option[Event] = PlayJsonFormats.eventFromJson(json)
    println(a)
//    println(a.get)
    assert(PlayJsonFormats.eventFromJson(json).getOrElse(CandidateDates(Seq.empty[Candidate])) != CandidateDates(Seq.empty[Candidate]))
  }


  "votingValueReads" should "return jsvalue" in {
    val json: JsValue = Json.parse(
      """{
            "participant":"name1",
            "status": 2
          }
      """.stripMargin)
    println(json)
    val voteResult: JsResult[Vote] = json.validate[Vote]
    println("hoge")
    println(voteResult)
    val vote: Vote = voteResult match {
      case v: JsSuccess[Vote] => v.get
      case e: JsError => Vote("none", VotingValue.Batu)
    }
    println(vote)
    assert(vote == Vote("name1", VotingValue.Maru))
  }

  "candidateReads" should "returm jsvalue" in {
    val json = Json.parse(
      """
        {
          "date": "2019-08-30T19:00:00",
          "votes": [
          {
            "participant": "name1",
            "status": 1
          },
          {
            "participant": "name2",
            "status": 2
          }
          ]
        }
      """.stripMargin)
    val candidate = json.validate[Candidate] match {
      case v: JsSuccess[Candidate] => v.get
      case e: JsError => Candidate(DateFormatter.string2date("2019-09-09T10:00:00"), Seq.empty[Vote])
    }
    assert(candidate == Candidate(DateFormatter.string2date("2019-08-30T19:00:00"),
      Seq(Vote("name1", VotingValue.from(1)), Vote("name2", VotingValue.from(2)))))
  }


//  "candidateDatesReads" should "return jsvalue" in {
//    val json = Json.parse(
//      """
//        |{
//        |"candidateDates":[
//        |    {
//        |        "date": "2019-08-30T19:00:00",
//        |        "votes": [
//        |                {
//        |                    "participant": "name1",
//        |                    "status": 1
//        |                },
//        |                {
//        |                    "participant": "name2",
//        |                    "status": 2
//        |                }
//        |            ]
//        |        },
//        |    {
//        |        "date": "2019-09-01T19:00:00",
//        |        "votes": [
//        |            {
//        |                "participant": "name1",
//        |                "status":1
//        |            },
//        |            {
//        |                "participant": "name2",
//        |                "status": 0
//        |            }
//        |            ]
//        |      }
//        |     ]
//        |}
//      """.stripMargin)
//    val  a = json.validate[CandidateDates]
//    val candidateDates = json.validate[CandidateDates] match {
//      case v: JsSuccess[CandidateDates] => v.get
//      case e: JsError => CandidateDates(Seq.empty[Candidate])
//    }
//
//    assert(candidateDates != CandidateDates(Seq.empty[Candidate]))
//  }

  "toJson" should "return json format of vote class" in {
    val vote: Vote = Vote("name1", VotingValue.Maru)
    val json: JsValue = Json.toJson(vote)
//    print(json)
    assert(json == Json.parse(
      """
        |{
        | "participant": "name1",
        | "status":2
        |}
      """.stripMargin))
  }

  "toJson" should "return json format of event class" in {
    val event: Event = Event(1, "test_event",
        CandidateDates(Seq(Candidate(DateFormatter.string2date("2019-08-30T19:00:00"),
                                      Seq(Vote("name1", VotingValue.from(1)), Vote("name2", VotingValue.from(2)))),
          Candidate(DateFormatter.string2date("2019-09-01T19:00:00"),
            Seq(Vote("name1", VotingValue.from(1)), Vote("name2", VotingValue.from(0)))))
        ),
        DateFormatter.string2date("2019-08-29T19:00:00"),
        "test_comment")

    val a: JsValue = Json.parse(
      """
        |{
        |    "id": 1,
        |    "eventName": "test_event",
        |    "candidateDates":[
        |    {
        |        "date": "2019-08-30T19:00:00",
        |        "votes": [
        |                {
        |                    "participant": "name1",
        |                    "status": 1
        |                },
        |                {
        |                    "participant": "name2",
        |                    "status": 2
        |                }
        |            ]
        |        },
        |    {
        |        "date": "2019-09-01T19:00:00",
        |        "votes": [
        |            {
        |                "participant": "name1",
        |                "status":1
        |            },
        |            {
        |                "participant": "name2",
        |                "status": 0
        |            }
        |            ]
        |      }
        |     ],
        |    "deadline": "2019-08-29T19:00:00",
        |    "comment": "test_comment"
        |  }
      """.stripMargin)

    val json = Json.toJson(event)
    assert(json == a)
  }

  "votingFromJson" should "return voting class instance" in {
    val json: JsValue = Json.parse(
      """
        |{
        |    "id": 1,
        |    "participant": "name1",
        |    "votes":[
        |              {
        |                "date": "2019-09-04T19:00:00",
        |                "status": 2
        |              },
        |              {
        |                "date": "2019-09-05T20:00:00",
        |                "status": 0
        |              }
        |            ]
        |  }
      """.stripMargin)

    assert(PlayJsonFormats.votingFromJson(json).get == Voting(1, "name1",
      Seq(ParticipateStatus(DateFormatter.string2date("2019-09-04T19:00:00"), VotingValue.Maru),
        ParticipateStatus(DateFormatter.string2date("2019-09-05T20:00:00"), VotingValue.Batu))))
  }

}
