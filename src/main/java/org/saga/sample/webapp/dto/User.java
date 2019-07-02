package org.saga.sample.webapp.dto;


public class User {
	private String firstname;
	private int age;
	
	public User(String firstname, int age) {
		super();
		this.firstname = firstname;
		this.age = age;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
    public String toString() {
        return "User [firstName=" + firstname + ", age=" + age + "]";
    }
}
