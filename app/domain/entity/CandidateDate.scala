
package domain.entity


//候補日のクラス
//候補日とそれに対応する参加者とその回答を保存する
//case class CandidateDate(date: Date, votingResults: Seq[Vote]) {
//
//}

//投票者の名前と参加可否を保存
case class Vote(name:  String, votingStatus: VotingValue) {

}


//参加可否のステータス
sealed abstract class VotingValue(value: Int)

object VotingValue {
  case object Maru extends VotingValue(value = 2)
  case object Batu extends VotingValue(value = 0)
  case object Sankaku extends VotingValue(value = 1)

  def from(value: Int):VotingValue = value match {
    case 2 => VotingValue.Maru
    case 0 => VotingValue.Batu
    case 1 => VotingValue.Sankaku
    case _ => sys.error(s"Unknown value: $value")
  }

  def toInt(value: VotingValue): Int =
    value match {
      case VotingValue.Maru => 2
      case VotingValue.Batu => 0
      case VotingValue.Sankaku => 1
    }
}
