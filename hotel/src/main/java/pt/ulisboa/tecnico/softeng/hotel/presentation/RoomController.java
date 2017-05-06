package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData.CopyDepth;

@Controller
@RequestMapping(value = "/hotels/rooms") /*path maybe incorrect*/
public class RoomController {
	private static Logger logger = LoggerFactory.getLogger(RoomController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String showRooms(Model model, @PathVariable String hotelCode) {
		logger.info("showRooms code:{}", hotelCode);
		
		//HotelData to be implemented
		//HotelData hotelData = HotelInterface.getHotelDataByCode(); 

		/*if (hotelData == null) {
			model.addAttribute("error", "Error: it does not exist a hotel with the code " + hotelCode);
			model.addAttribute("room", new HotelData());
			model.addAttribute("rooms", HotelInterface.getRooms(hotel, depth);
			return "rooms";
		} else {
			model.addAttribute("room", new RoomData());
			model.addAttribute("hotel", brokerData);
			return "adventures";
		}*/
		return null; //delete after implementations
	}
}
