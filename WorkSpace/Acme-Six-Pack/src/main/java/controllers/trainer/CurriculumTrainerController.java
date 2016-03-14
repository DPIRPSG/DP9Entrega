package controllers.trainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.TrainerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.Trainer;

@Controller
@RequestMapping("/curriculum/trainer")
public class CurriculumTrainerController extends AbstractController {
	
	// Services ----------------------------------------------------------
	
	@Autowired
	private TrainerService trainerService;
	
	// Constructors --------------------------------------------------------
	
	public CurriculumTrainerController() {
		super();
	}
	
	// Listing --------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Trainer trainer;
		Curriculum curriculum;
		String profilePicture;
				
		trainer = trainerService.findByPrincipal();
		curriculum = trainer.getCurriculum();
		profilePicture = trainer.getPicture();
		
		result = new ModelAndView("curriculum/list");
		result.addObject("requestURI", "curriculum/trainer/list.do");
		result.addObject("curriculum", curriculum);
		result.addObject("profilePicture", profilePicture);

		return result;
	}

}
