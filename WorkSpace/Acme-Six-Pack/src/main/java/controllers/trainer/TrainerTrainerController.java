package controllers.trainer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.TrainerService;
import services.form.ActorFormService;

import controllers.AbstractController;
import domain.Trainer;
import domain.form.ActorForm;

@Controller
@RequestMapping(value = "/trainer/trainer")
public class TrainerTrainerController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private ActorFormService actorFormService;
	
	@Autowired
	private Validator actorFormValidator;

	// Constructors ----------------------------------------------------------

	public TrainerTrainerController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(){
		ModelAndView result;
		Trainer trainer;
		
		trainer = trainerService.findByPrincipal();
		
		result = new ModelAndView("trainer/display");
		result.addObject("trainer", trainer);
		
		return result;
	}

	// Creation ----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(){
		ModelAndView result;
		ActorForm administrator;
		
		administrator = actorFormService.createForm();
		
		result = createEditModelAndView(administrator);
		
		return result;
	}

	// Edition ----------------------------------------------------------

	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid ActorForm actorForm, BindingResult binding) {
		actorFormValidator.validate(actorForm, binding);
		
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(actorForm);
		} else {
			try {
				actorFormService.saveForm(actorForm);
				result = new ModelAndView("redirect:display.do");
			} catch (Throwable oops) {
				String errorCode;
				errorCode = "actorForm.commit.error";
				result = createEditModelAndView(actorForm, errorCode);
			}
		}

		return result;
	}
	
	// Ancillary Methods
	// ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(ActorForm administrator) {
		ModelAndView result;

		result = createEditModelAndView(administrator, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(ActorForm administrator, String message) {
		ModelAndView result;

		result = new ModelAndView("actorForm/edit");
		result.addObject("actorForm", administrator);
		result.addObject("message", message);
		result.addObject("urlAction", "trainer/trainer/edit.do");

		return result;
	}
}
