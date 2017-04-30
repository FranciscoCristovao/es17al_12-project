package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;



@Controller
@RequestMapping(value = "/providers")
public class ProviderController {
	private static Logger logger = LoggerFactory.getLogger(ProviderController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String providerForm(Model model) {
		logger.info("providerForm");
		model.addAttribute("provider");
		model.addAttribute("providers", ActivityInterface.getProviders());
		return "providers";
	}
}
