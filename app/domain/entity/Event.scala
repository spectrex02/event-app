
package domain.entity

import java.time.LocalDateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
//イベント関連のクラス
//case class Event(eventName: String, candidateDates: Seq[CandidateDate], deadline: Date, comment: String) {
case class Event(id: Int, eventName: String, candidateDates: Map[LocalDateTime, Seq[Vote]], deadline:  LocalDateTime, comment:  String) {
  //フィールド
  var eventStatus: Boolean = true

}

