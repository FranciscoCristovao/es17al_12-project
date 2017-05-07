package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{code}/clients/{id}/accounts/{iban}/makeOperation")
public class MakeOperationController {
	private static Logger logger = LoggerFactory.getLogger(MakeOperationController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String accountForm(Model model, @PathVariable String code, @PathVariable String id, @PathVariable String iban) {
		BankData bankData = BankInterface.getBankDataByCode(code, CopyDepth.CLIENTS);
		if(bankData == null){
			model.addAttribute("error", "Error: it does not exist a bank with the code " + code);
			model.addAttribute("bank", new BankData());
			model.addAttribute("banks", BankInterface.getBanks());
			return "banks";
		}
		
		ClientData clientData = BankInterface.getClientDataById(id, code,  ClientData.CopyDepth.ACCOUNTS);
		if (clientData == null) {
			model.addAttribute("error", "Error: it does not exist a client with the id " + id);
			model.addAttribute("client", new ClientData());
			model.addAttribute("bank", bankData);
			return "clients";
		}
		
		AccountData accountData = BankInterface.getAccountDataByIban(code, id,iban,  AccountData.CopyDepth.OPERATIONS);
		if(accountData==null){
			model.addAttribute("error", "Error: it does not exist an account with the iban " + iban);
			model.addAttribute("account", new AccountData());
			model.addAttribute("client", clientData);
			return "clients";
		}
		else{
			model.addAttribute("client", clientData);
			model.addAttribute("bank", bankData);
			model.addAttribute("operation", new BankOperationData());
			model.addAttribute("account", accountData);
			
			return "makeOperation";
		}
	}
	
	@RequestMapping(value="/deposit", method = RequestMethod.POST)
	public String submitDeposit(Model model, @PathVariable String code, @PathVariable String id, @PathVariable String iban, @ModelAttribute BankOperationData operationData) {
		logger.info("deposit account:{} value:{}",iban, operationData.getValue() );
		operationData.setType("deposit");
		try {
			BankInterface.makeOperation(code,id,iban, operationData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to make this deposit");
			model.addAttribute("operation", new BankOperationData());	
			return "makeOperation";
		}
		return "redirect:/banks/"+code+"/clients/"+id+"/accounts/"+iban+"/makeOperation";
	}
	
	@RequestMapping(value="/withdraw", method = RequestMethod.POST)
	public String submitWithdrawal(Model model, @PathVariable String code, @PathVariable String id, @PathVariable String iban, @ModelAttribute BankOperationData operationData) {
		logger.info("deposit account:{} value:{}",iban, operationData.getValue() );
		operationData.setType("withdraw");
		try {
			BankInterface.makeOperation(code,id,iban, operationData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to make this withdrawal");
			model.addAttribute("operation", new BankOperationData());	
			return "makeOperation";
		}
		return "redirect:/banks/"+code+"/clients/"+id+"/accounts/"+iban+"/makeOperation";
	}


}
