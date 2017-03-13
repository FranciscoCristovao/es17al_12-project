package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankHasAccountMethodTest {
	Bank bank;
	Client client;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
	}
	
	@Test(expected = BankException.class)
	public void invalidArguments(){
		bank.getAccount(null);
	}
	
	@Test(expected = BankException.class)
	public void bankWithoutAccounts(){
		bank.getAccount("1111");
	}
	
	@Test(expected = BankException.class)
	public void noSuchAccount(){
		Account account1 = new Account(bank, client);
		Account account2 = new Account(bank, client);
		bank.addAccount(account1);
		bank.addAccount(account2);
		bank.getAccount("2344");
	}
	
	@Test
	public void success() {
		Account account = new Account(this.bank, this.client);

		Account result = this.bank.getAccount(account.getIBAN());

		Assert.assertEquals(account, result);
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
