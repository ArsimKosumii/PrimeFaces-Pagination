package model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity()
@Table(name = "employee")

public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	@NotEmpty
	private String name;

	@Column(nullable = false)
	@NotEmpty
	private String lastname;

	@Column(nullable = false)
	private int age;

	@Column(nullable = false)
	@NotEmpty
	private String dateofbirth;

	@Column(nullable = false)
	@NotEmpty
	private String dateofemployment;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDateofbirth() {
		return dateofbirth;
	}

	public void setDateofbirth(String dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	public String getDateofemployment() {
		return dateofemployment;
	}

	public void setDateofemployment(String dateofemployment) {
		this.dateofemployment = dateofemployment;
	}


	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Employee) {
			return ((Employee) obj).getId() == id;
		}
		return false;
	}

}
