package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankCancelPaymentMethodTest {
	Bank bank;
	Account account1;
	
	@Before
	public void setUp() {
		this.bank = new Bank("Money", "CP01");
		this.account1 = new Account(this.bank, new Client(this.bank,"António"));
		this.account1.deposit(100);
	}
	
	
	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.cancelPayment(null);
	}
	
	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.cancelPayment("");
	}
	
	@Test(expected = BankException.class)
	public void whiteSpaceReference() {
		Bank.cancelPayment("        ");
	}
	
	@Test(expected = BankException.class)
	public void cancelPaymentWithReferenceToDeposit(){
		String reference= this.account1.deposit(50);
		Bank.cancelPayment(reference);
	}
	
	@Test(expected = BankException.class)
	public void cancelWhenThereAreNoBanks(){
		String reference = this.account1.withdraw(50);
		Bank.banks.clear();
		Bank.cancelPayment(reference);
	}
	
	@Test(expected = BankException.class)
	public void cancelPaymentWithNonExistantReference(){
		Bank.cancelPayment("NONEXISTANTREFERENCE");
	}
	
	@Test
	public void successCancelation(){
		
		String reference = this.account1.withdraw(50);
		
		String cancelRef = Bank.cancelPayment(reference);
		
		Operation operation=this.bank.getOperation(cancelRef);
		Assert.assertEquals(50,operation.getValue());
		Assert.assertEquals(Operation.Type.DEPOSIT,operation.getType());
		Assert.assertEquals(account1, operation.getAccount());
		Assert.assertEquals(cancelRef, operation.getReference());
	}
	
	
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}
	
	
}
