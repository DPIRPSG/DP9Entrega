package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
		result.addObject("requestURI", "bulletin/administrator/list.do");
		result.addObject("bulletins", bulletins);
		result.addObject("gym", gym);

		return result;
	}

	// Creation ----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int gymId) {
		ModelAndView result;
		Bulletin bulletin;

		bulletin = bulletinService.create(gymId);
		result = createEditModelAndView(bulletin);

		return result;
	}

	// Edition ----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Bulletin bulletin, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(bulletin);
		} else {
			try {
				bulletinService.save(bulletin);
				result = new ModelAndView("redirect:list.do?" + "gymId="
						+ bulletin.getGym().getId());
			} catch (Throwable oops) {
				result = createEditModelAndView(bulletin, "bulletin.commit.error");
			}
		}

		return result;
	}

	// Ancillary Methods
	// ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(Bulletin bulletin) {
		ModelAndView result;

		result = createEditModelAndView(bulletin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Bulletin bulletin, String message) {
		ModelAndView result;
		Collection<Gym> gyms;

		gyms = gymService.findAll();

		result = new ModelAndView("bulletin/create");
		result.addObject("bulletin", bulletin);
		result.addObject("message", message);
		result.addObject("gyms", gyms);

		return result;
	}
}
