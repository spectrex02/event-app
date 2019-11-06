
package domain.entity

import java.time.LocalDateTime

//参加者のクラス
case class Participant(name: String) {

  //メソッド定義
  def createVoting(event: Event, date:  LocalDateTime, votingStatus: Int): Event = {
    //投票する
    event.copy(
//      candidateDates = event.candidateDates + (date -> newVoting)
      candidateDates = event.candidateDates.update(date, Vote(name, VotingValue.from(votingStatus)))
    )
  }

  def updateVoting(event: Event, date: LocalDateTime, votingStatus:  Int): Event = {
    //投票内容を変更する
    event.copy (
      candidateDates = event.candidateDates.replace(date, Vote(name, VotingValue.from(votingStatus)))
      )
  }

  def deleteVoting(event: Event, date: LocalDateTime): Event = {
    //投票を削除する
    event.copy(
      candidateDates = event.candidateDates.delete(date, name)
    )
  }
}