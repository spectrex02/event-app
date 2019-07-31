package domain.repository

import java.sql

import domain.entity._
import java.time.LocalDateTime

import scalikejdbc._

object EventRepository {

  implicit val session = AutoSession

  def find(id: Int)(implicit session: DBSession): Seq[LocalDateTime] = {
    //候補日以外のイベントのフィールドを取得
    val tmp: Option[Event] = sql"select * from event where id = $id".map{
      rs => Event(id = rs.get("id"),
                  eventName = rs.get("event_name"),
        candidateDates = rs.array("candidate_dates").asInstanceOf[Array[LocalDateTime]].toSeq.map(date => date -> Seq.empty[Vote]).toMap,
        deadline = rs.get("deadline"),
        comment = rs.get("comment"))
    }.single().apply()

    sql"select * from vote where event_id = $id".map{

    }
  }
}
