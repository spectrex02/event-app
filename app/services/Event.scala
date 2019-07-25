
package services

import java.util.Date

//イベント関連のクラス
//case class Event(eventName: String, candidateDates: Seq[CandidateDate], deadline: Date, comment: String) {
case class Event(eventName: String, candidateDates: Map[Date, Seq[Vote]], deadline:  Date, comment:  String) {
  //フィールド
  var eventStatus: Boolean = true

}