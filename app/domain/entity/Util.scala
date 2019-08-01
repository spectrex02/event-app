package domain.entity

object Util {
  def Int2VotingValue(votingStatus: Int): VotingValue = {
    votingStatus match {
      case 2 => VotingValue.Maru
      case 0 => VotingValue.Batu
      case 1 => VotingValue.Sankaku
    }
  }

  def VotingValue2Int(votingValue: VotingValue): Int = {
    votingValue match {
      case VotingValue.Maru => 2
      case VotingValue.Sankaku => 1
      case VotingValue.Batu => 0
    }
  }
}
