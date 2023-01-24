package model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import dao.Dao;

@Named
@SessionScoped
public class LazyEmployeeDataModel extends LazyDataModel<Employee> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Dao dao;
	
	@Override
	public List<Employee> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
		List<Employee> employees = dao.getAllEmployees(first, pageSize, sortBy, filterBy);
		long rowCount = dao.nrOfFiltered(filterBy);
		setRowCount((int)rowCount);
		return employees;
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// TODO Auto-generated method stub
		return 0;
	}

}
