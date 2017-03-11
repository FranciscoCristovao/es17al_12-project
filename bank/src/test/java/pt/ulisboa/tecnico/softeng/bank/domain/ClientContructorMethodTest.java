package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class ClientContructorMethodTest {
	Bank bank;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
	}
	
	@Test(expected = BankException.class)
	public void clientNameCantBeNull() {
		Client client = new Client(bank, null);
	}
	
	@Test(expected = BankException.class)
	public void clientBankCantBeNull() {
		Client client = new Client(null, "Joao");
	}
	
	@Test(expected = BankException.class)
	public void clientNameCantBeEmpty() {
		Client client = new Client(bank, "");
	}
	
	@Test(expected = BankException.class)
	public void clientNameCantBeBlank() {
		Client client = new Client(bank, "    ");
	}
	
	@Test
	public void success() {
		Client client = new Client(this.bank, "António");

		Assert.assertEquals("António", client.getName());
		Assert.assertTrue(client.getID().length() >= 1);
		Assert.assertTrue(this.bank.hasClient(client));
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
