package domain.repository

import java.{io, sql}

import domain.entity._
import java.time.LocalDateTime

import scalikejdbc._

object EventRepository {

  implicit val session = AutoSession
  //一件取得
  def find(id: Int)(implicit session: DBSession): Option[Event]= {
    //候補日以外のイベントのフィールドを取得
    val tmp: Option[Event] = sql"select * from event where id = $id".map{
      rs => Event(id = rs.get("id"),
                  eventName = rs.get("event_name"),
        candidateDates = rs.array("candidate_dates").asInstanceOf[Array[LocalDateTime]].toSeq.map(date => date -> Seq.empty[Vote]).toMap,
        deadline = rs.get("deadline"),
        comment = rs.get("comment"))
    }.single().apply()

    //Map[LocalDateTime, Seq[Vote]]
    val vv: List[(LocalDateTime, Vote)] = sql"select * from vote where event_id = $id".map{ rs =>
      rs.localDateTime("voting_date") -> Vote(rs.get("participant_name"), Util.Int2VotingValue(rs.int("voting_status")))
    }.list().apply()

    val v2: Map[LocalDateTime, List[Vote]] = vv.groupBy{ case (date: LocalDateTime, vote: Vote) =>
      date
    }.mapValues(_.map(_._2))

    tmp match {
      case Some(e) => Some(e.copy(candidateDates = v2))
      case None => None
    }
  }
  //全件取得
  def findAll()(implicit session: AutoSession): Seq[Event] = {
    val eventId: Seq[Int] = sql"select id from event where status = true".map(_.int("id")).list().apply()
    eventId.map(id => find(id).get)
  }

  //新規イベントをDBに追加
  def insertEvent(event: Event, plannerName: String)(implicit session: AutoSession): SQLExecution = {
    val dates = event.candidateDates.keys.map(date => DateFormatter.date2string(date))
    val dl = DateFormatter.date2string(event.deadline)
    sql"insert into event (event_name, candidate_dates, deadline, comment, planner) values ($event.eventName, $dates, $dl, $event.comment, $plannerName)".execute()
  }

  //イベント情報の更新をデータベースに反映させる
  def updateEvent(event: Event)(implicit session: AutoSession) = {
    sql"update table event set where event_name = $event.eventName, id = $event.id"
  }
  //イベントの削除をDBに反映
  def deleteEvent(eventId: Int)(implicit session: AutoSession): SQLExecution = sql"delete from event where id = $eventId".execute()
  //イベントの投票期間締め切り
  def closeEvent(eventId: Int)(implicit session: AutoSession): SQLExecution = sql"update table event set status = false where id = $eventId".execute()

  //投票に関するクエリを実行するメソッドたち
  //投票をDBに反映させる
  def insertVoting(eventId: Int, votingDate: LocalDateTime, vote: Vote) = {
    val date = DateFormatter.date2string(votingDate)
    sql"insert into vote (id, participant_name, voting_date, voting_status) values ($eventId, $vote.name, $date, $vote.voting_status)".execute()
  }

  //投票内容の変更をDBに反映させる
  def updateVoting(eventId: Int, votingDate: LocalDateTime, vote: Vote) = {
    val date = DateFormatter.date2string(votingDate)
    val status = Util.VotingValue2Int(vote.votingStatus)
    sql"update table event set voting_status = $status where id = $eventId and participant_name = $vote.name and voting_date = $date".execute()
  }

  //投票内容の削除をDBに反映
  def deleteVoting(eventId: Int, votingDate: LocalDateTime, vote: Vote) = {
    val date = DateFormatter.date2string(votingDate)
    sql"delete from event where id = $eventId and participant_name = $vote.name and voting_date = $date".execute()
  }
}
