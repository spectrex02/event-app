package services

import scala.util.Random
import java.util.Date
class ManageEvent() {
  //メソッド
  def generateId(): Int = {
    //乱数を生成してその値をイベントIDとする
    val r = new Random
    val id = Math.abs(r.nextInt())
    id
  }

  def Date2String(date: Date): String = {
   //文字列を日付に変換する
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd H:M:S")
    format.format(date)
  }

  def String2Date(date: String): Date = {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd H:M:S")
    format.parse(date)
  }
}
