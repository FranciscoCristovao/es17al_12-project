package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;

@Controller
@RequestMapping(value = "/hotels")
public class HotelController {
	private static Logger logger = LoggerFactory.getLogger(HotelController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String hotelForm(Model model) {
		logger.info("hotelForm");
		model.addAttribute("hotel", new HotelData());
		model.addAttribute("hotels", HotelInterface.getHotels());
		return "hotels";
	}

}