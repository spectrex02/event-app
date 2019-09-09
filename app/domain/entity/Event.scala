
package domain.entity

import java.time.LocalDateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
//イベント関連のクラス
//case class Event(eventName: String, candidateDates: Seq[CandidateDate], deadline: Date, comment: String) {

case class Candidate(date: LocalDateTime, votes: Seq[Vote])

case class CandidateDates(candidates: Seq[Candidate]) {

  def findBy(date: LocalDateTime): Seq[Vote] = { candidates.find(_.date == date) match {
      case v: Some[Candidate] => v.get.votes
      case _ => Seq.empty[Vote]
    }
  }

  //replace some old candidate to new candidate
  def replace(date: LocalDateTime, newVote: Vote): CandidateDates = {
    val newVotes: Seq[Vote] = findBy(date).filter(_.name != newVote.name) :+ newVote
    val newCandidate: Candidate = Candidate(date, newVotes)
    CandidateDates(candidates.filter(_.date != newCandidate.date) :+ newCandidate)
  }

  //update new voting
  def update(date: LocalDateTime, newVote: Vote): CandidateDates = {
    val newVotes: Seq[Vote] = findBy(date) :+ newVote
    val newCandidate = Candidate(date, newVotes)
    CandidateDates(candidates.filter(_.date != newCandidate.date) :+ newCandidate)
  }

  def delete(date: LocalDateTime, name: String): CandidateDates = {
    val newCandidate = Candidate(date, findBy(date).filter(_.name != name))
    CandidateDates(candidates.filter(_.date != date) :+ newCandidate)
  }

  def collect(): Seq[LocalDateTime] = candidates.map(c => c.date)
}

case class Event(id: Int, eventName: String, candidateDates: CandidateDates, deadline:  LocalDateTime, comment:  String) {
  //フィールド
  var eventStatus: Boolean = true

}

