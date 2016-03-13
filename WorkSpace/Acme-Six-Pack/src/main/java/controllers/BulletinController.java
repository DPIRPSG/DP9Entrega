package controllers;

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
@RequestMapping(value = "/bulletin")
public class BulletinController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private BulletinService bulletinService;
	
	@Autowired
	private GymService gymService;

	// Constructors ----------------------------------------------------------

	public BulletinController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int gymId, @RequestParam(required=false, defaultValue="") String keyword) {
		ModelAndView result;
		Collection<Bulletin> bulletins;
		Gym gym;
		String keywordToFind;

		bulletins = bulletinService.findAllByGymId(gymId);
		gym = gymService.findOne(gymId);
		
		if (!keyword.equals("")) {
			String[] keywordComoArray = keyword.split(" ");
			for (int i = 0; i < keywordComoArray.length; i++) {
				if (!keywordComoArray[i].equals("")) {
					keywordToFind = keywordComoArray[i];
					bulletins = bulletinService.findBySingleKeyword(keywordToFind, gymId);
					break;
				}
			}
		}
		
		result = new ModelAndView("bulletin/list");
		result.addObject("requestURI", "bulletin/list.do");
		result.addObject("bulletins", bulletins);
		result.addObject("gym", gym);

		return result;
	}
}
