package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;

@Controller
@RequestMapping(value = "/banks")
public class BankController {
	private static Logger logger = LoggerFactory.getLogger(BankController.class);
	@RequestMapping(method = RequestMethod.GET)
	public String bankForm(Model model) {
		logger.info("bankForm");
		model.addAttribute("banks", BankInterface.getBanks());
		return "banks";
	}
	

}
