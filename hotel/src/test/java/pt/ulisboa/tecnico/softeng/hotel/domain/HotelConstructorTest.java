package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		Assert.assertEquals("Londres", hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
	}
	
	@Test
	public void sameCode() {
		Hotel hotel = new Hotel("XPTO123", "Londres");
		try{
			 new Hotel("XPTO123", "Londres");
			 Assert.fail();
		}
		catch(HotelException he){
			Assert.assertEquals(1, Hotel.hotels.size());
			Assert.assertTrue(Hotel.hotels.contains(hotel));
		}
	}
	
	@Test 
	public void underLength(){
		try{
			new Hotel("XPTO12", "Lisbon");
			Assert.fail();
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@Test
	public void overLength(){
		try{
			new Hotel("XPTO1234", "Lisbon");
			Assert.fail();
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}

	
	
	@Test
	public void codeNull() {
		try{
			new Hotel(null, "Lisbon");
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@Test
	public void nameNull() {
		try{
			new Hotel("XPTO123", null);
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@Test 
	public void spacesCode(){
		try{
			new Hotel("       ","London");
			Assert.fail();
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@Test
	public void spacesName(){
		try{
			new Hotel("XPT0123","      ");
			Assert.fail();
		}
		catch(HotelException he){
			Assert.assertEquals(0,Hotel.hotels.size());
		}
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}


}
