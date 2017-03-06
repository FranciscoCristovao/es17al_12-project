package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Account {
	private static int counter = 0;

	private final Bank bank;
	private final String IBAN;
	private final Client client;
	private int balance;

	public Account(Bank bank, Client client) {

		/*checkArguments();
		checkClient();*/
		
		this.bank = bank;
		this.IBAN = bank.getCode() + Integer.toString(++Account.counter);
		this.client = client;
		this.balance = 0;

		bank.addAccount(this);
	}
	
	/*private void checkArguments(){
		if(bank == null || client == null){
			throw new BankException();
		}
	}
	private void checkClient(){
		if(!bank.hasClient(client)){
			throw new BankException();
		}
	}*/
	private void checkAmount(int amount){
		if(amount <= 0){
			throw new BankException();
		}
	}
	
	Bank getBank() {
		return this.bank;
	}

	public String getIBAN() {
		return this.IBAN;
	}

	public Client getClient() {
		return this.client;
	}

	public int getBalance() {
		return this.balance;
	}

	public String deposit(int amount) {
		checkAmount(amount);
		this.balance = this.balance + amount;

		Operation operation = new Operation(Operation.Type.DEPOSIT, this, amount);
		return operation.getReference();
	}

	public String withdraw(int amount) {
		if (amount > this.balance) {
			throw new BankException();
		}

		this.balance = this.balance - amount;

		return new Operation(Operation.Type.WITHDRAW, this, amount).getReference();
	}

}
