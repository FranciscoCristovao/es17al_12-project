package pt.ulisboa.tecnico.softeng.broker.domain;

public class Reference extends Reference_Base{
	public Reference(String reference){
		setReference(reference);
	}
	public void delete(){
		setBulkRoomBooking(null);
		deleteDomainObject();
	}
}
