package controllers

import java.time.LocalDateTime

import domain.entity.{Candidate, CandidateDates, DateFormatter, Event, Vote, VotingValue}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import javax.inject.Inject
import play.api.mvc._

import scala.collection.JavaConverters._


class EventController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
//  def createEvent() = Action { implicit request =>
//    val eventResult = request.body.asJson
//    Ok()
//  }
}

object EventReadWrites {
  //jsonへの変換
  /*
  eventの抽象構文木の例
  {
    "id": 1,
    "eventName": "example_event",
    "candidateDates" [
      {
        date: "2019",
        votes: [
              {
                "participant": "name1",
                "status": 1
              },
               ...
            ]
        }
      },
      {
        date: "2019",
        vote: {
        }
      },

     ]
    "deadline": "2019-08-29",
    "comment": "example comment"
  }
   */
//  case class CandidateDates(date: LocalDateTime, votes: Seq[Vote])

  /*
     it's a wrapper class which helps to convert Map -> Array
   */

  implicit val voteReads: Reads[Vote] = (
    (__ \ "participant").read[String] and
      (__ \"status").read[VotingValue]
    )(Vote)

  implicit val votingValueReads: Reads[VotingValue] = (value: JsValue) =>
    JsSuccess(VotingValue.apply(value.as[Int]))

  implicit val candidateReads: Reads[Candidate] = (
    (__ \ "date").read[LocalDateTime] and
      (__ \ "vote").read[Seq[Vote]]
  )(Candidate.apply _)

//  implicit val candidateSeqReads: Reads[Seq[Candidate]] = Reads.seq(candidateReads)

  implicit val candidateDatesReads: Reads[CandidateDates] =
    __.read[Seq[Candidate]].map(CandidateDates)


//  implicit val candidateDatesReads: Reads[CandidateDates] = (v: JsValue) =>
//    v.

  implicit val eventReads: Reads[Event] = (
    (__ \ "id").read[Int] and
      (__ \ "eventName").read[String] and
      (__ \ "candidateDates").read[CandidateDates] and
      (__ \ "deadline").read[LocalDateTime] and
      (__ \ "comment").read[String]
    )(Event.apply _)

  implicit val votingValueWrites: Writes[VotingValue] = (v: VotingValue) =>
    Json.toJson(VotingValue.toInt(v))

  implicit val voteWrites: Writes[Vote] = (
    (__ \ "participant").write[String] and
      (__ \ "status").write[VotingValue]
    )(unlift(Vote.unapply))

  implicit val candidateWrites: Writes[Candidate] = (
    (__ \ "date").write[LocalDateTime] and
      (__ \ "votes").write[Seq[Vote]]
  )(unlift(Candidate.unapply))

  implicit val candidateDatesWrites: Writes[CandidateDates] = (c: CandidateDates) =>
    Json.toJson(c.candidate)
//    Json.toJson(c.asInstanceOf[Seq[Candidate]])
//  val a: CandidateDates => Seq[Candidate] = (c: CandidateDates) => c.candidate

//  implicit val candidateDateWrites: Writes[CandidateDate] = (o: CandidateDate) =>
//    Json.obj(
//      "date" -> DateFormatter.date2string(o.date),
//      "votes" -> o.votes
//    )

  //ここはパスあとで
//  implicit val candidateDatesWrites: Writes[Map[LocalDateTime, Seq[Vote]]] = (o: Map[LocalDateTime, Seq[Vote]]) =>
//    Json.toJson(o.map(v => CandidateDate(v._1, v._2)))

  implicit val eventWrites: Writes[Event] = (
    (__ \ "id").write[Int] and
      (__ \ "eventName").write[String] and
      (__ \ "candidateDates").write[CandidateDates] and
      (__ \ "deadline").write[LocalDateTime] and
      (__ \ "comment").write[String]
  )(unlift(Event.unapply))

    def EventfromJson(value: JsValue): Option[Event] = {
      val eventResult : JsResult[Event] = value.validate[Event]
      eventResult match {
        case e: JsSuccess[Event] => e.asOpt
        case e: JsError => None
      }
    }


  //イベント一覧取得
  //json構造
  /*
  {
    [
      {
        "id": 1,
        "eventName": "test1"
      },
      {
        "id": 2,
        "eventName": "test2"
      }
    ]
  }
   */


  //イベントの変更のJsonのやりとり

  //投票内容のJsonのやりとり

  //投票のJsonの構造
  /*
  {
    "id": 1,
    "participant": "name1",
    "votes":[
              {
                "date": "2019-09-04T19:00:00",
                "status": 2
              },
              {
                "date": "2019-09-05T20:00:00",
                "status": 0
              }
            ]
  }
   */
  case class ParticipateStatus(date: LocalDateTime, status: VotingValue)

  case class Voting(id: Int, participant: String, votes: Seq[ParticipateStatus])

  implicit val participateStatusReads: Reads[ParticipateStatus] = (
    (__ \ "date").read[LocalDateTime] and
      (__ \ "status").read[VotingValue]
  )(ParticipateStatus.apply _)

  implicit val votingReads: Reads[Voting] = (
    (__ \ "id").read[Int] and
      (__ \ "participant").read[String] and
      (__ \ "votes").read[Seq[ParticipateStatus]]
  )(Voting.apply _)

  def votingFromJson(value: JsValue): Option[Voting] = {
    val votingResult: JsResult[Voting] = value.validate[Voting]
    votingResult match {
      case v: JsSuccess[Voting] => v.asOpt
      case e: JsError => None
    }
  }

  //投票結果のJsonのやりとり
  //Jsonの構造
  /*
  {[
    {
      "date": "2019-09-04T19:00:00",
      "result": 10
     },
     {
      "date": "2019-09-05T19:00:00",
      "result": 5
     }
    ]
  }
   */

  case class VotingResult(date: LocalDateTime, result: Int)
  implicit val votingResultWrites: Writes[VotingResult] = (
    (__ \ "date").write[LocalDateTime] and
      (__ \ "result").write[Int]
  )(unlift(VotingResult.unapply))

}
