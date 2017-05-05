package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;


import pt.ulisboa.tecnico.softeng.bank.domain.Bank;


public class BankData {
	public static enum CopyDepth {
		SHALLOW, CLIENTS
	};
	
	private String name;
	private String code;
	//private List<ClientData> clients = new ArrayList<>();
	
	public BankData() {
	}
	
	public BankData(Bank bank, CopyDepth depth) {
		this.name = bank.getName();
		this.code = bank.getCode();

		switch (depth) {
		case CLIENTS:
			//TODO
			break;
		case SHALLOW:
			break;
		default:
			break;
		}

	}
	
	public String getName(){
		return this.name;
		}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code=code;
	}
	/*
	public List<ClientData> getClients(){
		return this.clients;
	}
	
	public void setClients(List<ClientData> clients){
		this.clients=clients;
	}
	*/


	
}
