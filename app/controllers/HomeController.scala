package controllers

import javax.inject.Inject
import play.api.mvc._
import views.html

class HomeController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def hello() = Action(Ok("Event App is running..\n"))

}
