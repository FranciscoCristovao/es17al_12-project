package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ProviderData;

@Controller
@RequestMapping(value = "/providers/{code}/activities")
public class ActivityController {
	private static Logger logger = LoggerFactory.getLogger(ActivityController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String activityForm(Model model, @PathVariable String code) {
		logger.info("activityForm");
		ProviderData providerData = ActivityInterface.getProviderDataByCode(code);
		if(providerData != null){
			model.addAttribute("activity",new ActivityData());
			model.addAttribute("provider",providerData);
			return "activities";
		}
		else{
			model.addAttribute("error", "Error: it does not exist a provider with the code " + code);
			model.addAttribute("provider", new ProviderData());
			model.addAttribute("providers", ActivityInterface.getProviders());
			return "providers";
		}
	}
	@RequestMapping(method = RequestMethod.POST)
	public String ActivitySubmit(Model model, @ModelAttribute ActivityData activity, @PathVariable String code) {
		logger.info("activitySubmit name:{}, minAge:{} maxAge:{} capacity:{}", activity.getName(), activity.getMinAge(),
				activity.getMaxAge(),activity.getCapacity());
		try {
			ActivityInterface.createActivity(code, activity);
		} catch (ActivityException ae) {
			model.addAttribute("error", "Error: it was not possible to create the activity");
			model.addAttribute("activity", activity);
			model.addAttribute("provider", ActivityInterface.getProviderDataByCode(code));
			return "activities";
		}

		return "redirect:/providers/"+ code + "/activities";
	}
}