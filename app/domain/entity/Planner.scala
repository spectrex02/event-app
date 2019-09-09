
package domain.entity

import java.time.LocalDateTime

//イベント企画者のクラス
case class Planner(name: String) {

  //メソッド定義
  def createEvent(eventName: String, candidateDates: Seq[LocalDateTime], deadline:  LocalDateTime, comment:  String):  Event = {
    //イベント作成
    Event(-1, eventName, CandidateDates(candidateDates.map(date => Candidate(date, Seq.empty[Vote]))), deadline, comment)
  }

  def updateEvent(event: Event, newEventName: Option[String] = None,
                  newCandidateDates: Option[Seq[LocalDateTime]] = None,
                  newDeadline: Option[LocalDateTime] = None,
                  newComment:  Option[String] = None): Event = {

      //候補日の変更
//    val newDate: Map[LocalDateTime, Seq[Vote]] = newCandidateDates.map{ dates: Seq[LocalDateTime] =>
//      // Map henkan
//      dates.map( date => date -> event.candidateDates.getOrElse(date, Seq.empty[Vote])).toMap
//    }.getOrElse(event.candidateDates)
      val newDates: Option[CandidateDates] = newCandidateDates match {
        case Some(dates) => Some(CandidateDates(dates.map(d => Candidate(d, Seq.empty[Vote]))))
        case None => None
      }
      println(newDates.getOrElse(event.candidateDates))
      println(event.candidateDates)
    event.copy(
      eventName = newEventName.getOrElse(event.eventName),
      candidateDates = newDates.getOrElse(event.candidateDates),
      deadline = newDeadline.getOrElse(event.deadline),
      comment = newComment.getOrElse(event.comment)
    )
  }

  def deleteEvent(event: Event) : Unit = {
    //イベントの削除
    event.eventStatus = false
    //DB操作
  }

  def closeEvent(event: Event): Unit = {
    event.eventStatus = false
  }

}