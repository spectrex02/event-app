
package entity

import java.time.LocalDateTime

//イベント企画者のクラス
case class Planner(name: String) {

  //メソッド定義
  def createEvent(eventName: String, candidateDates: Seq[LocalDateTime], deadline:  LocalDateTime, comment:  String):  Event = {
    //イベント作成
    Event(eventName, candidateDates.map(date => date -> Seq.empty[Vote]).toMap, deadline, comment)
  }

  def updateEvent(event: Event, newEventName: Option[String] = None,
                  newCandidateDates: Option[Seq[LocalDateTime]] = None,
                  newDeadline: Option[LocalDateTime] = None,
                  newComment:  Option[String] = None): Event = {

    //候補日の変更
    val newDate: Map[LocalDateTime, Seq[Vote]] = newCandidateDates.map{ dates: Seq[LocalDateTime] =>
      // Map henkan
      dates.map( date => date -> event.candidateDates.getOrElse(date, Seq.empty[Vote])).toMap
    }.getOrElse(event.candidateDates)


    event.copy(
      eventName = newEventName.getOrElse(event.eventName),
      candidateDates = newDate,
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