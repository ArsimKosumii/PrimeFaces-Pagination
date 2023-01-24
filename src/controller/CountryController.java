package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dao.Dao;
import model.Country;
import model.Employee;

@Named
@SessionScoped
public class CountryController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Dao dao;
	
	private Country country;
	private List<Country> listCountries = new ArrayList<Country>();
	private Country selectedCountry;
	
	private List<Country> selectedCountries;
	
	@PostConstruct
	public void init() {
		listCountries = dao.getCountries();
	}
	
//	public void createTables() {
//		dao.findById(Country.class, 0);
//		dao.findById(Employee.class, 0);
//	}

	public void insertCountries() {
		String[] countries = {"Kosova", "Shqiperia", "Gjermania", "Anglia", "Amerika", "Kina", "Gjeorgjia"};
		for(int i = 0; i < countries.length; i++) {
			country = new Country();
			country.setCountry_name(countries[i]);
			dao.persist(country);
		}
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Country> getListCountries() {
		return listCountries;
	}

	public void setListCountries(List<Country> listCountries) {
		this.listCountries = listCountries;
	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public List<Country> getSelectedCountries() {
		return selectedCountries;
	}

	public void setSelectedCountries(List<Country> selectedCountries) {
		this.selectedCountries = selectedCountries;
	}
	
	public void printSelectedCountries() {
		if(selectedCountries == null) {
			System.out.println("No countries selected.");
		} else {
			for (Country c : selectedCountries) {
				System.out.println(c.getCountry_name());
			}
		}
	}
	
}
