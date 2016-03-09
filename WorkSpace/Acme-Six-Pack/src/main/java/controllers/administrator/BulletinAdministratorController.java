package controllers.administrator;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BulletinService;
import services.GymService;
import controllers.AbstractController;
import domain.Bulletin;
import domain.Gym;

@Controller
@RequestMapping(value = "/bulletin/administrator")
public class BulletinAdministratorController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private BulletinService bulletinService;
	
	@Autowired
	private GymService gymService;

	// Constructors ----------------------------------------------------------

	public BulletinAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int gymId) {
		ModelAndView result;
		Collection<Bulletin> bulletins;
		Gym gym;

		bulletins = bulletinService.findAllByGymId(gymId);
		gym = gymService.findOne(gymId);
		
		result = new ModelAndView("bulletin/list");
		result.addObject("requestURI", "bulletin/administrator/list.do");
		result.addObject("bulletins", bulletins);
		result.addObject("gym", gym);

		return result;
	}
}
