package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;

public class AccountData {
	public static enum CopyDepth{
		SHALLOW, OPERATIONS
	};
	
	private Bank bank;
	private Client client;
	private String iban;
	private int balance;
	private List<BankOperationData> operations = new ArrayList<BankOperationData>();
	
	public AccountData(){
	}
	
	public AccountData(Account account, CopyDepth depth){
		this.bank = account.getBank(); 
		this.client = account.getClient(); 
		this.iban = account.getIBAN();
		this.balance = account.getBalance();
		
		
		switch(depth){
			case OPERATIONS:
				for(Operation operation: account.getOperationSet()){
					this.operations.add(new BankOperationData(operation));
				}
				break;
			case SHALLOW:
				break;
			default:
				break;
		}
	}
	
	public Bank getBank(){
		return this.bank;
	}
	
	public Client getClient(){
		return this.client;
	}
	
	public String getIban(){
		return this.iban;
	}
	
	public int getBalance(){
		return this.balance;
	}
	
	public List<BankOperationData> getOperation(){
		return this.operations;
	}
	
}
