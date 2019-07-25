
package services

import java.util.Date
import java.util.Date

//イベント企画者のクラス
case class Planner(name: String) {

  //メソッド定義
  def createEvent(eventName: String, candidateDates: Map[Date, Seq[Vote]], deadline:  Date, comment:  String):  Event = {
    //イベント作成
    Event.apply(eventName, candidateDates, deadline, comment)
  }

  def updateEvent(event: Event, newEventName: Option[String] = None,
                  newCandidateDates: Option[Map[Date, Seq[Vote]]] = None,
                  newDeadline: Option[Date] = None,
                  newComment:  Option[String] = None): Unit = {

    //イベント名更新
    event.copy(
      eventName = newEventName.getOrElse(event.eventName)
    )

    //イベント投票期限変更
    event.copy(
      deadline = newDeadline.getOrElse(event.deadline)
    )

    //イベント説明変更
    event.copy(
      comment = newComment.getOrElse(event.comment)
    )

    //イベント候補日変更
    //変更したら今まで投票したものが消えてしまう
    //どうするべきか


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