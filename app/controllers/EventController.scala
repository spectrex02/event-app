package controllers

import java.time.LocalDateTime

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
    //get all event data(Map[id: Int, event_name: String]) from datababse
    val eventList: Map[Int, String] = EventRepository.findAll()
    Created(PlayJsonFormats.eventListToJson(eventList))
  }

  def createEvent() = Action { implicit request: Request[AnyContent] =>
    val optEvent: Option[(Event, String)] = request.body.asJson match {
      case Some(v) => PlayJsonFormats.eventAndPlannerFromJson(v)
      case None => None
    }
    optEvent match {
      case Some(ep) => EventRepository.insertEvent(ep._1, ep._2)
      case None => new Status(FORBIDDEN)
    }
    Ok
  }

  def updateEvent() = Action { implicit request: Request[AnyContent] =>

    Ok
  }

  def deleteEvent() = Action { implicit request: Request[AnyContent] =>

  }

//  def updateEvent() = Action { implicit request: Request[AnyContent] =>
//    update event
//  }
}

