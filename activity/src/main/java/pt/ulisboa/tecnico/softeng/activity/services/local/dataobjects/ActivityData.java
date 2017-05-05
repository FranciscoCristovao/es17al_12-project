package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;

public class ActivityData {
	private String code;
	private String name;
	private int maxAge;
	private int minAge;
	
	private int capacity;
	
	public ActivityData(){}
	
	public ActivityData(Activity a){
		this.code = a.getCode();
		this.name = a.getName();
		this.maxAge = a.getMaxAge();
		this.minAge = a.getMinAge();
		this.capacity = a.getCapacity();
		
	}
	
	public String getCode(){return this.code;}
	public String getName(){return this.name;}
	public int getMaxAge(){return this.maxAge;}
	public int getMinAge(){return this.minAge;}
	public int getCapacity(){return this.capacity;}
	
	public void setCode(String code){ this.code = code;}
	public void setName(String name){this.name = name;}
	public void setMaxAge(int i){this.maxAge = i;}
	public void setMinAge(int i){this.minAge = i;}
	public void setCapacity(int i){this.capacity = i;}
	
}
