package controllers

import java.time.LocalDateTime

import controllers.PlayJsonFormats._
import domain.entity.{Candidate, CandidateDates, DateFormatter, Event, Vote, VotingValue}
import domain.repository.EventRepository
import play.api.libs.functional.syntax._
import play.api.libs.json._
import javax.inject.Inject
import play.api.mvc._
import scalikejdbc.AutoSession

import scala.collection.JavaConverters._



class EventController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  implicit val session = AutoSession

  //get one event
  def  get(id : Int) = Action { implicit request: Request[AnyContent] =>
    //get data from database and generate an event class instance
    val event: Option[Event] = EventRepository.find(id)

    event match {
      case Some(e) => Created(PlayJsonFormats.eventToJson(e))
      case None => NotFound
    }
  }

  def getAll() = Action { implicit request: Request[AnyContent] =>
    //get all event data(Map[id: Int, event_name: String]) from database
    val eventList: Map[Int, String] = EventRepository.findAll()
    Created(PlayJsonFormats.eventListToJson(eventList))
  }

  def createEvent() = Action { implicit request: Request[AnyContent] =>
    val optEvent: Option[(Event, String)] = request.body.asJson match {
      case Some(v) => PlayJsonFormats.eventAndPlannerFromJson(v)
      case None => None
    }
    println(optEvent)
    val result: Boolean = optEvent match {
      case Some(ep) => EventRepository.insertEvent(ep._1, ep._2)
      case None => true
    }
    result match {
      case true => new Status(FORBIDDEN)
      case false => Ok("create new event.\n")
    }
  }

  def updateEvent() = Action { implicit request: Request[AnyContent] =>

    Ok
  }

  def deleteEvent(id: Int) = Action { implicit request: Request[AnyContent] =>
    val queryResult: Boolean = EventRepository.deleteEvent(id)
    queryResult match {
      case true => new Status(BAD_REQUEST)
      case false => Ok("event deleted.\n")
    }
  }

  def createVote() = Action { implicit request: Request[AnyContent] =>
    //def insertVoting(eventId: Int, votingDate: LocalDateTime, vote: Vote)
//    case class ParticipateStatus(date: LocalDateTime, status: VotingValue)
//    case class Voting(id: Int, participant: String, votes: Seq[ParticipateStatus])
    //pay attention to difference between insertVoting and votingFromJson
    val votingOpt: Option[Voting] = request.body.asJson match {
      case Some(v) => PlayJsonFormats.votingFromJson(v)
      case None => None
    }
    val voting: Voting = votingOpt match {
      case Some(v) => v
      case None => sys.error("Found unknown value.")
    }

    //苦肉の策
    val eventId: Int = voting.id
    val participantName: String = voting.participant
    val resultSeq: Seq[Boolean] = voting.votes.map { dateAndStatus =>
      EventRepository.insertVoting(eventId, dateAndStatus.date, Vote(participantName, dateAndStatus.status))
    }

    resultSeq.forall(r => true) match {
      case true => new Status(BAD_REQUEST)
      case false => Ok("create new vote.")
    }
  }


  //delete
  def deleteVote() = Action { implicit request: Request[AnyContent] =>
    val deleteRequest: Option[Voting] = request.body.asJson match {
      case Some(value) => PlayJsonFormats.votingFromJson(value)
      case None => None
    }
    val result: Boolean = deleteRequest match {
      case Some(v) => EventRepository.deleteVoting(v)
      case None => true
    }
    result match {
      case true => new Status(BAD_REQUEST)
      case false => Ok("deleted voting.")
    }
  }

  def result(id: Int) = Action { implicit request: Request[AnyContent] =>

    Ok
  }
//  def updateEvent() = Action { implicit request: Request[AnyContent] =>
//    update event
//  }
}

