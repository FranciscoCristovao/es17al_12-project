package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;

public class OperationConstructorMethodTest {
	private final Logger logger = LoggerFactory.getLogger(OperationConstructorMethodTest.class);

	private Bank bank;
	private Account account;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
	}
	
	@Test(expected = BankException.class)
	 public void operationTypeCantBeNull() {
		Operation operation = new Operation(null, account, 10);	
	}
	
	@Test(expected = BankException.class)
	 public void operationAccountCantBeNull() {
		Operation operation = new Operation(type, null, 10);	
	}
	
	@Test(expected = BankException.class)
	 public void operationValueCantBeNull() {
		Operation operation = new Operation(type, account, null);	
	}
	
	@Test(expected = BankException.class)
	 public void operationValueCantBeNegative() {
		Operation operation = new Operation(type, account, -1);	
	}
	@Test
	public void success() {
		Operation operation = new Operation(Type.DEPOSIT, this.account, 1000);

		Assert.assertTrue(operation.getReference().startsWith(this.bank.getCode()));
		Assert.assertTrue(operation.getReference().length() > Bank.CODE_SIZE);
		Assert.assertEquals(Type.DEPOSIT, operation.getType());
		Assert.assertEquals(this.account, operation.getAccount());
		Assert.assertEquals(1000, operation.getValue());
		Assert.assertTrue(operation.getTime() != null);
		Assert.assertEquals(operation, this.bank.getOperation(operation.getReference()));
		Assert.assertNotNull(operation.getType());
		Assert.assertNotNull(operation.getAccount());
		Assert.assertNotNull(operation.getValue());
		Assert.assertFalse(operation.getValue() < 0);
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
