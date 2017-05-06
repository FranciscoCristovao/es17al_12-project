package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;


@Controller
@RequestMapping(value="/banks/{code}/clients/{id}")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@RequestMapping(value="/accounts", method = RequestMethod.GET)
	public String accountForm(Model model, @PathVariable String code, @PathVariable String id) {
		logger.info("showAccounts from client id:{}", id);
		
		BankData bankData = BankInterface.getBankDataByCode(code, CopyDepth.CLIENTS);
		ClientData clientData = BankInterface.getClientDataById(id, code,  ClientData.CopyDepth.ACCOUNTS);
		model.addAttribute("client", clientData);
		model.addAttribute("bank", bankData);
		model.addAttribute("account", new AccountData());
		model.addAttribute("accounts", clientData.getAccounts());
		
		return "accounts";
	}
}
