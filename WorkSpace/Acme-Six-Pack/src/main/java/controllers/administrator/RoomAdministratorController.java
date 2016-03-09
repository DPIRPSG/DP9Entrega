package controllers.administrator;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.GymService;
import services.RoomService;

import controllers.AbstractController;
import domain.Gym;
import domain.Room;

@Controller
@RequestMapping(value = "/room/administrator")
public class RoomAdministratorController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private RoomService roomService;
	
	@Autowired
	private GymService gymService;

	// Constructors ----------------------------------------------------------

	public RoomAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int gymId) {
		ModelAndView result;
		Collection<Room> rooms;
		Gym gym;

		rooms = roomService.findAllByGymId(gymId);
		gym = gymService.findOne(gymId);
		
		result = new ModelAndView("room/list");
		result.addObject("requestURI", "room/administrator/list.do");
		result.addObject("rooms", rooms);
		result.addObject("gym", gym);

		return result;
	}
}
