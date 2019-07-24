
package services

import java.util.Date
import java.util.Date

//イベント企画者のクラス
case class Planner(name: String) {

  //メソッド定義
  def createEvent(eventName: String, candidateDates: Seq[CandidateDate], deadline:  Date, comment:  String): Unit = {
    Event.apply(eventName, candidateDates, deadline, comment)
  }

  def updateEvent(event: Event, newEventName: Option[String] = None,
                  newCandidateDates: Option[Seq[CandidateDate]] = None,
                  NewDeadline: Option[Date] = None,
                  NewComment:  Option[String] = None): Unit = {

    //イベント名更新
    event.copy(
      eventName = newEventName.getOrElse(event.eventName)
    )

    event.copy(
      deadline = newDeadline.getOrElse(event.deadline)
    )

  }

  def deleteEvent(event: Event) = {

  }

  def closeEvent(event: Event): Unit = {
    event.eventStatus = false
  }

}