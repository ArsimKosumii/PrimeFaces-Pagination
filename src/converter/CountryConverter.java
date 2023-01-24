package converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import dao.Dao;
import model.Country;

@Named(value = "countryConverter")
@RequestScoped
public class CountryConverter implements Converter<Country> {
	
	@Inject 
	private Dao dao;

	@Override
	public Country getAsObject(FacesContext arg0, UIComponent arg1, String cId) {
		try {	
			return dao.findById(Country.class, Integer.valueOf(cId));
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Country country) {
		if(country != null) {
			return String.valueOf(country.getId());
		}
		return null;
	}
}