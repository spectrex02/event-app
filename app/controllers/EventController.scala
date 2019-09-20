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

  def updateEventName() = Action { implicit request: Request[AnyContent] =>
    val updateRequest: Option[(Int, String)] = request.body.asJson match {
      case Some(value) => PlayJsonFormats.eventNameFromJson(value)
      case None => None
    }
    val result = updateRequest match {
      case Some(value) => EventRepository.updateEventName(value._1, value._2)
      case None => true
    }
    result match {
      case true => new Status(INTERNAL_SERVER_ERROR)
      case false => Ok("updated event name.")
    }
  }

  def updateComment() = Action { implicit request: Request[AnyContent] =>
    val updateRequest: Option[(Int, String)] = request.body.asJson match {
      case Some(value) => PlayJsonFormats.eventCommentFromJson(value)
      case None => None
    }
    val result = updateRequest match {
      case Some(value) => EventRepository.updateComment(value._1, value._2)
      case None => true
    }
    result match {
      case true => new Status(INTERNAL_SERVER_ERROR)
      case false => Ok("updated event comment.")
    }
  }

  def updateDeadLine() = Action { implicit request: Request[AnyContent] =>
    val updateRequest: Option[(Int, LocalDateTime)] = request.body.asJson match {
      case Some(value) => PlayJsonFormats.eventDeadLineFromJson(value)
      case None => None
    }
    val result = updateRequest match {
      case Some(value) => EventRepository.updateDeadline(value._1, value._2)
      case None => true
    }
    result match {
      case true => new Status(INTERNAL_SERVER_ERROR)
      case false => Ok("updated event deadline.")
    }
  }

  def updateCandidateDates() = Action { implicit request: Request[AnyContent] =>
    val updateRequest: Option[(Int, CandidateDates)] = request.body.asJson match {
      case Some(value) => PlayJsonFormats.eventCandidateDatesFromJson(value)
      case None => None
    }
    val result = updateRequest match {
      case Some(value) => EventRepository.updateCandidateDates(value._1, value._2)
      case None => true
    }
    result match {
      case true => new Status(INTERNAL_SERVER_ERROR)
      case false => Ok("updated candidate dates.")
    }
  }

  def deleteEvent(id: Int) = Action { implicit request: Request[AnyContent] =>
    val queryResult: Boolean = EventRepository.deleteEvent(id)
    queryResult match {
      case true => new Status(BAD_REQUEST)
      case false => Ok("event deleted.\n")
    }
  }

  def closeEvent(id: Int) = Action { implicit request: Request[AnyContent] =>
    EventRepository.closeEvent(id) match {
      case true => new Status(INTERNAL_SERVER_ERROR)
      case false => Ok("close this event.")
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
    val voting: Boolean = votingOpt match {
      case Some(v) => EventRepository.insertVoting(v)
      case None => true
    }
    voting match {
      case true => new Status(BAD_REQUEST)
      case false => Ok("insert voting")
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

  def updateVote() = Action { implicit request: Request[AnyContent] =>
    val updateRequest: Option[Voting] = request.body.asJson match {
      case Some(value) => PlayJsonFormats.votingFromJson(value)
      case None => None
    }
    val result = updateRequest match {
      case Some(value) => EventRepository.updateVoting(value)
      case None => true
    }
    result match {
      case true => new Status(BAD_REQUEST)
      case false => Ok("updated vote.")
    }
  }

