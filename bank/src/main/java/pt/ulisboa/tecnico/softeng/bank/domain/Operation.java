package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Operation extends Operation_Base{
	public static enum Type {
		DEPOSIT, WITHDRAW
	};
	private static int counter = 0;

	public Operation(Type type, Account account, int value) {
		checkArguments(type, account, value);
		this.setReference(account.getBank().getCode() + Integer.toString(++Operation.counter));
		this.setType(type);
		this.setAccount(account);
		this.setValue(value);
		this.setTime(DateTime.now());
		this.getAccount().getBank().addLog(this);
	}

	private void checkArguments(Type type, Account account, int value) {
		if (type == null || account == null || value <= 0) {
			throw new BankException();
		}
	}
	
	public String revert() {
		switch (this.getType()) {
		case DEPOSIT:
			return this.getAccount().withdraw(this.getValue());
		case WITHDRAW:
			return this.getAccount().deposit(this.getValue());
		default:
			throw new BankException();

		}

	}
	public void delete(){
		setBank(null);
		setAccount(null);
		deleteDomainObject();
	}

}
