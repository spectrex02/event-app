package controllers

import java.time.LocalDateTime

import domain.entity.{DateFormatter, Event, Vote, VotingValue}
import play.api.libs.functional.syntax._
import play.api.libs.json._

class EventController {

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


  implicit val voteReads: Reads[Vote] = (
    (__ \ "participant").read[String] and
      (__ \"status").read[VotingValue]
    )(Vote)

  implicit val votingValueReads: Reads[VotingValue] = (value: JsValue) =>
    JsSuccess(VotingValue.apply(value.as[Int]))

  implicit val mapReads: Reads[Map[LocalDateTime, Seq[Vote]]] = (value: JsValue) =>
    JsSuccess(value.as[Map[String, Seq[Vote]]].map { case (k, v) =>
      DateFormatter.string2date(k) -> v
    })

  implicit val eventReads: Reads[Event] = (
    (__ \ "id").read[Int] and
      (__ \ "eventName").read[String] and
      (__ \ "candidateDates").read[Map[LocalDateTime, Seq[Vote]]] and
      (__ \ "deadline").read[LocalDateTime] and
      (__ \ "comment").read[String]
    )(Event.apply _)


  implicit val votingValueWrites: Writes[VotingValue] = (v: VotingValue) =>
    Json.toJson(VotingValue.toInt(v))

  implicit val voteWrites: Writes[Vote] = (
    (__ \ "participant").write[String] and
      (__ \ "status").write[VotingValue]
    )(unlift(Vote.unapply))

  /*
     it's a wrapper class which helps to convert Map -> Array
   */
  case class CandidateDate(date: LocalDateTime, votes: Seq[Vote])

  implicit val candidateDateWrites: Writes[CandidateDate] = (o: CandidateDate) =>
    Json.obj(
      "date" -> DateFormatter.date2string(o.date),
      "votes" -> o.votes
    )

  //ここはパスあとで
  implicit val candidateDatesWrites: Writes[Map[LocalDateTime, Seq[Vote]]] = (o: Map[LocalDateTime, Seq[Vote]]) =>
    Json.toJson(o.map(v => CandidateDate(v._1, v._2)))

  implicit val eventWrites: Writes[Event] = (
    (__ \ "id").write[Int] and
      (__ \ "eventName").write[String] and
      (__ \ "candidateDates").write[Map[LocalDateTime, Seq[Vote]]] and
      (__ \ "deadline").write[LocalDateTime] and
      (__ \ "comment").write[String]
  )(unlift(Event.unapply))


    def fromJson(value: JsValue): Option[Event] = {
      val eventResult : JsResult[Event] = value.validate[Event]
      eventResult match {
        case e: JsSuccess[Event] => e.asOpt
        case e: JsError => None
      }
    }

  //イベントの変更のJsonのやりとり

  //投票内容のJsonのやりとり
  //投票結果のJsonのやりとり
  //投票のJsonの構造
}
