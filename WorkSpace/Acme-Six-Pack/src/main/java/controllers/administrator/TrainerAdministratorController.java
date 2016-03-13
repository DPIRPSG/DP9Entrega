package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.form.ActorFormService;

import controllers.AbstractController;
import domain.form.ActorForm;

@Controller
@RequestMapping(value = "/trainer/administrator")
public class TrainerAdministratorController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private ActorFormService actorFormService;
	
	@Autowired
	private Validator actorFormValidator;

	// Constructors ----------------------------------------------------------

	public TrainerAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------


	// Creation ----------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView edit(){
		ModelAndView result;
		ActorForm trainer;
		
		trainer = actorFormService.createForm(null);
		
		result = createEditModelAndView(trainer);
		
		return result;
	}

	// Edition ----------------------------------------------------------

	
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid ActorForm actorForm, BindingResult binding) {
		actorFormValidator.validate(actorForm, binding);
		
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(actorForm);
		} else {
			try {
				actorFormService.saveForm(actorForm, true);
				result = new ModelAndView("redirect:/");
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
		result.addObject("urlAction", "trainer/administrator/register.do");

		return result;
	}
}
