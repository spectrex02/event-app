
package services

import java.util.Date

//参加者のクラス
case class Participant(name: String) {

  //メソッド定義
  def createVoting(event: Event, date:  Date, votingStatus: Int): Unit = {
    //投票する
    val vote: Vote = Vote.apply(name, Int2VotingValue(votingStatus))
    val votingResults: Seq[Vote] = event.candidateDates(date)

    val newCandidateDates: Map[Date, Seq[Vote]] = event.candidateDates + (date -> (votingResults :+ vote))
    event.copy(
      candidateDates = newCandidateDates
    )
  }

  def updateVoting(event: Event, date: Date, votingStatus:  Int): Unit = {
    //投票内容を変更する
    var votingResults: Seq[Vote] = event.candidateDates.getOrElse(date, Seq.empty)

    if(votingResults == Seq.empty) createVoting(event, date, votingStatus)
    else {
      for(i <- 0 until votingResults.length) {
        if(votingResults(i).name == name) {
          val newVote = Vote.apply(name, Int2VotingValue(votingStatus))
          val newVotingResults = votingResults updated (i, newVote)
          val newCandidateDates: Map[Date, Seq[Vote]] = event.candidateDates + (date -> newVotingResults)
          event.copy(
            candidateDates = newCandidateDates
          )

        }
      }
    }
  }

  def deleteVoting(event: Event, date: Date): Unit = {
    //投票を削除する
    val newVotingResults: Seq[Vote] = event.candidateDates(date).filter(_.name != name)
    val newCandidateDates: Map[Date, Seq[Vote]] = event.candidateDates + (date -> newVotingResults)
    event.copy(
      candidateDates = newCandidateDates
    )
  }

  def Int2VotingValue(votingStatus: Int): VotingValue = {
    votingStatus match {
    case 2 => VotingValue.Maru
    case 0 => VotingValue.Batu
    case 1 => VotingValue.Sankaku

    }
  }
}