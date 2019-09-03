package controllers

import java.time.LocalDateTime

import domain.entity.{DateFormatter, Event, Vote, VotingValue}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json

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

  //ここはパス
  implicit val mapWrites: Writes[Map[LocalDateTime, Seq[Vote]]] = (map: Map[LocalDateTime, Seq[Vote]]) => {

  }


  implicit val eventWrites: Writes[Event] = (
    (__ \ "id").write[Int] and
      (__ \ "eventName").write[String] and
      (__ \ "candidateDates" ).write[Map[LocalDateTime, Seq[Vote]]] and
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
}