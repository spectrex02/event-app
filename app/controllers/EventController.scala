package controllers

import java.time.LocalDateTime

import domain.entity.{DateFormatter, Event, Vote, VotingValue}
import play.api.libs.json._
import play.api.libs.functional.syntax._

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

  implicit val eventReads: Reads[Event] = (
    (__ \ "id").read[Int] and
      (__ \ "eventName").read[String] and
      (__ \ "candidateDates").read[Map[LocalDateTime, Seq[Vote]]] and
      (__ \ "deadline").read[LocalDateTime] and
      (__ \ "comment").read[String]
    )(Event.apply _)

  implicit val voteReads: Reads[Vote] = (
    (__ \ "participant").read[String] and
      (__ \ "status").read[VotingValue]
    )(Vote)

  implicit val votingValueReads: Reads[VotingValue] = (value: JsValue) =>
    JsSuccess(VotingValue.apply(value.as[Int]))

  implicit val mapReads: Reads[Map[LocalDateTime, Seq[Vote]]] = (value: JsValue) =>
    JsSuccess(value.as[Map[String, Seq[Vote]]].map { case (k, v) =>
        DateFormatter.string2date(k) -> v
    })


  //  def toJson(): JsObject = {}
  //  def fromJson(value: JsObject): Event = {
  //
  //  }
}