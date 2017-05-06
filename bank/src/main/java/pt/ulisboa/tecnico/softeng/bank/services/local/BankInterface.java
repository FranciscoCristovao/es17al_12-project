package pt.ulisboa.tecnico.softeng.bank.services.local;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

public class BankInterface {
	
	@Atomic(mode = TxMode.READ)
	public static List<BankData> getBanks() {
		List<BankData> banks = new ArrayList<>();
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			banks.add(new BankData(bank, CopyDepth.SHALLOW));
		}
		return banks;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBank(BankData bankData) {
		new Bank(bankData.getName(), bankData.getCode() );
	}
	
	private static Bank getBankByCode(String code) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getCode().equals(code)) {
				return bank;
			}
		}
		return null;
	}
	
	private static Client getClientById(String id, String code) {
		for (Client client : getBankByCode(code).getClientSet()) {
			if (client.getID().equals(id)) {
				return client;
			}
		}
		return null;
	}
	
	@Atomic(mode = TxMode.READ)
	public static ClientData getClientDataById(String id, String code, ClientData.CopyDepth cd){
		Client c = getClientById(id,code);
		return new ClientData(c, cd);
	}
	
	@Atomic(mode = TxMode.READ)
	public static BankData getBankDataByCode(String code, BankData.CopyDepth cd) {
		Bank b = getBankByCode(code);
		return new BankData(b, cd);
	}
	
	
	//old
	@Atomic(mode = TxMode.WRITE)
	public static String processPayment(String IBAN, int amount) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getAccount(IBAN) != null) {
				return bank.getAccount(IBAN).withdraw(amount);
			}
		}
		throw new BankException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelPayment(String paymentConfirmation) {
		Operation operation = getOperationByReference(paymentConfirmation);
		if (operation != null) {
			return operation.revert();
		}
		throw new BankException();
	}

	@Atomic(mode = TxMode.READ)
	public static BankOperationData getOperationData(String reference) {
		Operation operation = getOperationByReference(reference);
		if (operation != null) {
			return new BankOperationData(operation);
		}
		throw new BankException();
	}

	private static Operation getOperationByReference(String reference) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			Operation operation = bank.getOperation(reference);
			if (operation != null) {
				return operation;
			}
		}
		return null;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createClient(String bankCode, ClientData client) {
		new Client(getBankByCode(bankCode), client.getName());
		
	}

}
