
package services

import java.util.Date

//候補日のクラス
//候補日とそれに対応する参加者とその回答を保存する
//case class CandidateDate(date: Date, votingResults: Seq[Vote]) {
//
//}

//投票者の名前と参加可否を保存
case class Vote(name:  String, votingStatus: VotingValue) {

}


//参加可否のステータス
sealed abstract class VotingValue(value:  Int) {
}

object VotingValue {
  object Maru extends VotingValue(value = 2) {
    val value: Int = 2
  }
  object Batu extends VotingValue(value = 0) {
    val value: Int = 0
  }
  object Sankaku extends VotingValue(value = 1) {
    val value: Int = 1
  }
}
