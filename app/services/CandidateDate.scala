
package services

import java.util.Date

//候補日関連のクラス
case class CandidateDate(date: Date, votingResults: Seq[Vote]) {

}

case class Vote(name:  String, votingStatus: VotingValue) {

}


sealed abstract class VotingValue(value:  Int)

object VotingValue {
  object Maru extends VotingValue(value = 2)
  object Batu extends VotingValue(value = 0)
  object Sankaku extends VotingValue(value = 1)
}
