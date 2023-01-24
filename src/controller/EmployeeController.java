package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import dao.Dao;
import model.Country;
import model.Employee;

@Named
@SessionScoped
public class EmployeeController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Dao dao;

	private Employee emp = new Employee();
	private Employee selectedEmployee;
	private List<Country> countries;
	private String country_name;

	public List<Integer> getListAges() {
		return listAges;
	}

	public void setListAges(List<Integer> listAges) {
		this.listAges = listAges;
	}

	private Country selectedCountry;
	private List<Integer> listAges = new ArrayList<>();

	@PostConstruct
	public void init() {
		countries = dao.getCountries();
		
		for(int i = dao.minAge(); i <= dao.maxAge(); i++) {
			listAges.add(i);
		}
	}

	public Employee getEmp() {
		return emp;
	}

	public void setEmp(Employee emp) {
		this.emp = emp;
	}

	public int deleteRows() {
		return dao.deleteRows();
	}

	public void insertHundredRows() {
		Random random = new Random();
		for (int i = 0; i < 150; i++) {
			Employee emp = new Employee();
			emp.setName("Emp_" + i);
			emp.setLastname("Loyee_" + (i + 20));
			if (i <= 75)
				emp.setAge(i + 20);
			else
				emp.setAge(i / 2);
			emp.setDateofbirth("10/10/1990");
			emp.setDateofemployment("10/10/2021");
			emp.setCountry(countries.get(random.nextInt(countries.size() - 1)));
			dao.persist(emp);
		}
	}

	public void insertRow() {
		emp.setCountry(selectedCountry);
		dao.persist(emp);
		emp = new Employee();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Added"));
		PrimeFaces.current().executeScript("PF('add').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:employeeTable");

	}

	public void updateRow() {
		emp = selectedEmployee;
		dao.update(emp);
		emp = new Employee();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Updated"));
		PrimeFaces.current().executeScript("PF('edit').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:employeeTable");

		emp = new Employee();
	}

	public void deleteEmployee(Employee emp) {
		dao.remove(emp);
	}

	public Employee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}
}
