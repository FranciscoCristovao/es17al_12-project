package pt.ulisboa.tecnico.softeng.activity.presentation;

import java.util.List;

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
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ProviderData;

@Controller
@RequestMapping(value = "/providers/{code}/activities/{code2}/activityOffers")
public class OfferController {
	private static Logger logger = LoggerFactory.getLogger(OfferController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String offerForm(Model model, @PathVariable String code, @PathVariable String code2) {
		logger.info("offerForm");
		ProviderData provider = ActivityInterface.getProviderDataByCode(code);
		ActivityData activityData = ActivityInterface.getOffersDataByCodes(code, code2);
		if (provider == null){
			model.addAttribute("error", "Error: it does not exist a provider with the code " + code);
			model.addAttribute("provider", new ProviderData());
			model.addAttribute("providers", ActivityInterface.getProviders());
			return "providers";
		}
		else if(activityData == null){
			model.addAttribute("error", "Error: it does not exist an activity with the code " + code);
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("provider", provider);
			return "activities";
		}
		else{
			model.addAttribute("offer",new ActivityOfferData());
			model.addAttribute("activity", activityData);
			model.addAttribute("providerCode", code);
			return "activityOffers";
		}
	}
	@RequestMapping(method = RequestMethod.POST)
	public String offerSubmit(Model model, @ModelAttribute ActivityOfferData offer, @PathVariable String code, @PathVariable String code2) {
		logger.info("offerSubmit begin:{}, end:{} capacity:{}", offer.getBegin(), offer.getEnd(), offer.getCapacity());
		try {
			ActivityInterface.createActivityOffer(code, code2, offer);
		} catch (ActivityException oe) {
			model.addAttribute("error", "Error: it was not possible to create the offer");
			model.addAttribute("offer", offer);
			model.addAttribute("provider", ActivityInterface.getProviderDataByCode(code));
			return "activityOffers";
		}
		return "redirect:/providers/"+ code + "/activities/" + code2 + "/activityOffers";
	}
}