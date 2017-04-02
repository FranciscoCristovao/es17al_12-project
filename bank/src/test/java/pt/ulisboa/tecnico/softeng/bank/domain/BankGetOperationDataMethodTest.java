package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankGetOperationDataMethodTest {
	Account a1;
	
	@Before
	public void setUp() {
		Bank b1 = new Bank("Money", "BK02");
		Client client = new Client(b1, "António");
		this.a1 = new Account(b1, client);
	}

	@Test
	public void success() {
		Operation op = new Operation(Type.DEPOSIT, a1, 1000);
		BankOperationData opData = Bank.getOperationData(op.getReference());
		
		Assert.assertEquals(Type.DEPOSIT.toString(),opData.getType());
		Assert.assertEquals(op.getReference(),opData.getReference());
		Assert.assertEquals(1000,opData.getValue());
		Assert.assertEquals(this.a1.getIBAN(),opData.getIban());
		Assert.assertEquals(op.getTime(),opData.getTime());
	}
	
	@Test
	public void successSeveralBanks() {
		new Operation(Type.DEPOSIT, this.a1, 1000);
		
		Bank b2 = new Bank("Dollar", "AK01");
		Account a2 = new Account(b2, new Client(b2, "António"));
		
		new Operation(Type.DEPOSIT, a2, 100);
		Operation op = new Operation(Type.WITHDRAW, a2, 100);
		
		BankOperationData opData = Bank.getOperationData(op.getReference());
		
		Assert.assertEquals(Type.WITHDRAW.toString(),opData.getType());
		Assert.assertEquals(op.getReference(),opData.getReference());
		Assert.assertEquals(100,opData.getValue());
		Assert.assertEquals(a2.getIBAN(),opData.getIban());
		Assert.assertEquals(op.getTime(),opData.getTime());
	}

	@Test(expected = BankException.class)
	public void nullReference() {
		new Operation(Type.DEPOSIT, a1, 1000);
		Bank.getOperationData(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		new Operation(Type.DEPOSIT, a1, 1000);
		Bank.getOperationData("");
	}

	@Test(expected = BankException.class)
	public void blankReference() {
		new Operation(Type.DEPOSIT, a1, 1000);
		Bank.getOperationData("    ");
	}
	
	@Test(expected = BankException.class)
	public void emptySetOfOperations() {
		Bank.getOperationData("AB56");
	}

	@Test(expected = BankException.class)
	public void operationDoesNotExist(){
		new Operation(Type.DEPOSIT, this.a1, 1000);
		new Operation(Type.DEPOSIT, this.a1, 10);
		Bank.getOperationData("AB56");
	}
	
	@Test(expected = BankException.class)
	public void severalOperationsDoNotMatch() {
		new Operation(Type.DEPOSIT, this.a1, 100);
		new Operation(Type.WITHDRAW, this.a1, 1000);
		
		Bank b2 = new Bank("Dollar", "AK01");
		Account a2 = new Account(b2,new Client(b2, "António"));
		
		new Operation(Type.DEPOSIT, a2, 100);
		new Operation(Type.WITHDRAW, a2, 100);
		
		Bank.getOperationData("AB56");
	}
	

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}