package domain.repository

import java.io.ByteArrayInputStream
import java.sql.Timestamp
import java.{io, sql}

import domain.entity._
import java.time.LocalDateTime
import java.util.Date

import controllers.PlayJsonFormats.Voting
import scalikejdbc._

import scala.collection.immutable

object EventRepository {

  implicit val session = AutoSession
  //一件取得
  def find(id: Int)(implicit session: DBSession): Option[Event]= {
    //候補日以外のイベントのフィールドを取得
    val emptyEvent: Option[Event] = sql"select * from event where id = ${id}".map{
      rs => Event(id = rs.get("id"),
                  eventName = rs.get("event_name"),
//          candidateDates = rs.string("candidate_dates").split(",").toSeq.map(date => DateFormatter.string2date(date) -> Seq.empty[Vote]).toMap,
          candidateDates = CandidateDates(rs.string("candidate_dates").split(",").toSeq.map {date =>
                                                                                                    Candidate(DateFormatter.string2date(date), Seq.empty[Vote]) }),
        deadline = DateFormatter.string2date(rs.string("deadline")),
        comment = rs.get("comment"))
    }.single().apply()

    //rebuild vote
    val votingSeq: Seq[(LocalDateTime, Vote)] = sql"select * from vote where id = ${id}".map{ rs =>
      (DateFormatter.string2date(rs.string("voting_date")),Vote(rs.get("participant_name"), VotingValue.from(rs.int("voting_status"))))
    }.list().apply()

    val a: Map[LocalDateTime, Seq[(LocalDateTime, Vote)]] = votingSeq.groupBy { case (d: LocalDateTime, v: Vote) => d }
    val aa: Map[LocalDateTime, Seq[Vote]] = a.map { case (d: LocalDateTime, s: Seq[(LocalDateTime, Vote)]) => d -> s.map { case (date: LocalDateTime, vote: Vote) => vote}.toSeq }
    val actualCandidateDates: CandidateDates = CandidateDates(aa.map { case (date: LocalDateTime, votes: Seq[Vote]) => Candidate(date, votes)}.toSeq)


    emptyEvent match {
//      case Some(e) => if(v2 == Map.empty[LocalDateTime, Seq[Vote]]) Some(e) else Some(e.copy(candidateDates = v2))
      case Some(e) =>if(actualCandidateDates == Seq.empty[Candidate]) Some(e) else Some(e.copy(candidateDates = actualCandidateDates))
      case None => None
    }
  }
  //全件取得
  def findAll()(implicit session: AutoSession): Map[Int, String] = sql"select id, event_name from event where status = true".map { rs =>
      ((rs.int("id") -> rs.string("event_name")))
    }.list().apply().toMap
//    eventId.map(id => find(id).get)


  //新規イベントをDBに追加
  def insertEvent(event: Event, plannerName: String)(implicit session: AutoSession): Boolean = {
//    val dates: Array[Timestamp] = event.candidateDates.keys.map(date => Timestamp.valueOf(date)).toArray
//    val dates: String = event.candidateDates.keys.map(date => DateFormatter.date2string(date)).mkString(",")
    val dates: String = event.candidateDates.collect().map(date => DateFormatter.date2string(date)).mkString(",")
    val dl: String = DateFormatter.date2string(event.deadline)

    sql"insert into event (event_name, candidate_dates, deadline, comment, planner) values (${event.eventName}, ${dates}, ${dl}, ${event.comment}, ${plannerName})".execute().apply()
  }

//  def insertEvent2(e: Event, plannerName: String): Boolean = {
//    val dates: Array[String] = e.candidateDates.keys.map(date => DateFormatter.date2string(date)).toArray
//
//
////    withSQL {
////      insert.into(Event).columns(
////        column.event_name,
////        column.candidate_dates,
////        column.deadline,
////        column.comment,
////        column.planner
////      ).values(
////        e.eventName,
////        dates,
////        DateFormatter.date2string(e.deadline),
////        e.comment,
////        plannerName
////      )
////    }.execute().apply()
//  }
  //イベント情報の更新をデータベースに反映させる
//  def updateEvent(event: Event, plannerName: String)(implicit session: AutoSession): Boolean = {
//    sql"update table event set (event_name, candidate_dates, deadline, comment, planner) values ($event.eventName, $dates, $event.deadline, $event.comment)where event_name = $event.eventName and id = $event.id".execute().apply()
//  }


  def updateEventName(event: Event)(implicit session: AutoSession): Boolean = sql"update event set event_name = ${event.eventName} where id = ${event.id}".execute().apply()

  def updateDeadline(event: Event)(implicit session: AutoSession): Boolean = sql"update event set deadline = ${event.deadline} where id = ${event.id}".execute().apply()

  def updateComment(event: Event)(implicit session: AutoSession): Boolean = sql"update event set comment = ${event.comment} where id = ${event.id}".execute().apply()

  def updatePlanner(event: Event, planner: String)(implicit session: AutoSession): Boolean = sql"update event set planner = ${planner} where id = ${event.id}".execute().apply()

  def updateCandidateDates(newEvent: Event): Boolean = {
    val dates = newEvent.candidateDates.collect().map(date => DateFormatter.date2string(date)).mkString(",")
    sql"update  event set candidate_dates = ${dates} where id = ${newEvent.id}".execute().apply()
    //vote table update
  }
  //イベントの削除をDBに反映
  def deleteEvent(eventId: Int)(implicit session: AutoSession): Boolean = {
    val eventResult = sql"delete from event where id = ${eventId}".execute().apply()
    val voteResult = sql"delete from vote where id = ${eventId}".execute().apply()
    Seq(eventResult, voteResult).find(r => true) match {
      case Some(r) => true
      case None => false
    }
  }
  //イベントの投票期間締め切り
  def closeEvent(eventId: Int)(implicit session: AutoSession): Boolean = sql"update event set status = false where id = ${eventId}".execute().apply()
  def openEvent(eventId: Int)(implicit session: AutoSession): Boolean = sql"update event set status = true where id = ${eventId}".execute().apply()

  //投票に関するクエリを実行するメソッドたち
  //投票をDBに反映させる
  def insertVoting(eventId: Int, votingDate: LocalDateTime, vote: Vote)(implicit session: AutoSession): Boolean = {
    val date: String = DateFormatter.date2string(votingDate)
    sql"insert into vote (id, participant_name, voting_date, voting_status) values (${eventId}, ${vote.name}, ${date}, ${VotingValue.toInt(vote.votingStatus)})".execute().apply()

  }

  //投票内容の変更をDBに反映させる
  def updateVoting(eventId: Int, votingDate: LocalDateTime, vote: Vote)(implicit session: AutoSession): Boolean = {
    val date = DateFormatter.date2string(votingDate)
    val status = Util.VotingValue2Int(vote.votingStatus)
    sql"update vote set voting_status = ${status} where id = ${eventId} and participant_name = ${vote.name} and voting_date = ${date}".execute().apply()
  }

  //投票内容の削除をDBに反映
  def deleteVoting(voting: Voting)(implicit session: AutoSession): Boolean = {
    val date: Seq[String] = voting.votes.map { p => DateFormatter.date2string(p.date)}
    val result: Option[Boolean] = date.map { d: String => sql"delete from vote where id = ${voting.id} and participant_name = ${voting.participant} and voting_date = ${d}".execute().apply() }.find(r => true)
    result match {
      case Some(r) => true
      case None => false
    }
  }
}
