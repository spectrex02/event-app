
package entity

import java.time.LocalDateTime
//イベント関連のクラス
//case class Event(eventName: String, candidateDates: Seq[CandidateDate], deadline: Date, comment: String) {
case class Event(eventName: String, candidateDates: Map[LocalDateTime, Seq[Vote]], deadline:  LocalDateTime, comment:  String) {
  //フィールド
  var eventStatus: Boolean = true

}