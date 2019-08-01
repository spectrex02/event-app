
package domain.entity

import java.time.LocalDateTime

//参加者のクラス
case class Participant(name: String) {

  //メソッド定義
  def createVoting(event: Event, date:  LocalDateTime, votingStatus: Int): Event = {
    //投票する
    val newVoting: Seq[Vote] = event.candidateDates(date) :+ Vote(name, Util.Int2VotingValue(votingStatus))
    event.copy(
      candidateDates = event.candidateDates + (date -> newVoting)
    )
  }

  def updateVoting(event: Event, date: LocalDateTime, votingStatus:  Int): Event = {
    //投票内容を変更する
    val newVoting: Seq[Vote] = event.candidateDates(date).filter(vote => vote.name != name)

    event.copy (
      candidateDates = (event.candidateDates - date) + (date -> (newVoting :+ Vote(name, Util.Int2VotingValue(votingStatus))))
      )
  }

  def deleteVoting(event: Event, date: LocalDateTime): Event = {
    //投票を削除する
    val newVoting: Seq[Vote] = event.candidateDates(date).filter(vote => vote.name != name)
    event.copy(
      candidateDates = (event.candidateDates - date) + (date -> newVoting)
    )
  }
}