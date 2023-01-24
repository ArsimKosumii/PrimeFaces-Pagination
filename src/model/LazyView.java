package model;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
public class LazyView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private LazyEmployeeDataModel lazyModel;

	public LazyDataModel<Employee> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyEmployeeDataModel lazyModel) {
		this.lazyModel = lazyModel;
	}

}