package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import model.Country;
import model.Employee;

@Stateless
public class Dao implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	protected EntityManager em;

	public <T> T persist(T object) {
		em.persist(object);
		em.flush();

		return object;
	}

	public <T> void remove(T object) {
		object = em.merge(object);
		em.remove(object);
	}

	public <T> T update(T object) {
		object = em.merge(object);

		return object;
	}

	public <T> T refresh(T object) {
		em.refresh(object);
		return object;
	}

	public <T> T findById(Class<T> clazz, int id) {
		TypedQuery<T> tq = (TypedQuery<T>) em.createQuery("select c from " + clazz.getName() + " c where c.id = " + id,
				clazz);

		if (tq == null || tq.getResultList().isEmpty())
			return null;

		return tq.getResultList().get(0);
	}

	public <T> T findByName(Class<T> clazz, String name) {
		TypedQuery<T> tq = (TypedQuery<T>) em
				.createQuery("select c from " + clazz.getName() + " c where c.country_name = '" + name + "'", clazz);

		if (tq == null || tq.getResultList().isEmpty())
			return null;

		return tq.getResultList().get(0);
	}

	public List<Employee> getAllEmployees(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
		Root<Employee> root = criteria.from(Employee.class);
		criteria.select(root);

		if (filterBy != null) {
			filter(cb, criteria, root, filterBy);
		}

		if (sortBy != null) {
			List<Order> orderByList = new ArrayList<Order>();

			for (Map.Entry<String, SortMeta> kvp : sortBy.entrySet()) {
				SortMeta sortMeta = kvp.getValue();

				if (sortMeta.getOrder().toString().equals("ASCENDING")) {
					orderByList.add(cb.asc(root.get(sortMeta.getField())));
				} else {
					orderByList.add(cb.desc(root.get(sortMeta.getField())));
				}
			}

			criteria.orderBy(orderByList);
		}

		TypedQuery<Employee> typed = em.createQuery(criteria);
		typed.setFirstResult(first);
		typed.setMaxResults(pageSize);

		return typed.getResultList();
	}

	public int nrOfFiltered(Map<String, FilterMeta> filterBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Employee> root = criteria.from(Employee.class);
		criteria.select(cb.count(root));

		if (filterBy != null) {
			filter(cb, criteria, root, filterBy);
		}

		return em.createQuery(criteria).getSingleResult().intValue();
	}

	public CriteriaQuery<?> filter(CriteriaBuilder cb, CriteriaQuery<?> criteria, Root<Employee> root,
			Map<String, FilterMeta> filterBy) {
		List<Predicate> filterPredicateList = new ArrayList<>();
		List<Predicate> countryPredicateList = new ArrayList<>();
		List<Predicate> agePredicateList = new ArrayList<>();
		Object[] countriesAndAges = null;

		for (Map.Entry<String, FilterMeta> kvp : filterBy.entrySet()) {
			FilterMeta filterMeta = kvp.getValue();
			Predicate result = null;

			if (filterMeta.getFilterValue() instanceof String) {
				result = cb.like(root.get(filterMeta.getField()), "%" + filterMeta.getFilterValue() + "%");
				filterPredicateList.add(result);
			} else {
				countriesAndAges = (Object[]) filterMeta.getFilterValue();
				if (countriesAndAges != null) {
					for (Object o : countriesAndAges) {
						result = cb.equal(root.get(filterMeta.getField()), o);
						if (o instanceof Country) {
							countryPredicateList.add(result);
						} else {
							agePredicateList.add(result);
						}
					}
				}
			}

			Predicate filterPredicates = cb.and(filterPredicateList.toArray(new Predicate[] {}));
			Predicate countryPredicates = cb.or(countryPredicateList.toArray(new Predicate[] {}));
			Predicate agePredicates = cb.or(agePredicateList.toArray(new Predicate[] {}));
			Predicate countryAndAge = cb.and(countryPredicates, agePredicates);

			if (countriesAndAges == null) {
				criteria.where(filterPredicates);
			} else if (filterPredicateList.size() != 0 && agePredicateList.size() != 0
					&& countryPredicateList.size() != 0) {
				Predicate filtersAndCountryAndAge = cb.and(filterPredicates, countryAndAge);
				criteria.where(filtersAndCountryAndAge);
			} else if (agePredicateList.size() != 0 && countryPredicateList.size() != 0) {
				criteria.where(countryAndAge);
			} else if (countryPredicateList.size() != 0) {
				Predicate filtersAndCountry = cb.and(filterPredicates, countryPredicates);
				criteria.where(filtersAndCountry);
			} else {
				Predicate filtersAndAge = cb.and(filterPredicates, agePredicates);
				criteria.where(filtersAndAge);
			}
		}
		return criteria;
	}

	public int numberOfCountries() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Country> root = criteria.from(Country.class);
		criteria.select(cb.count(root));

		TypedQuery<Long> tq = em.createQuery(criteria);
		return tq.getSingleResult().intValue();
	}

	// Delete Criteria
	public int deleteRows() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<Employee> criteria = cb.createCriteriaDelete(Employee.class);
		Root<Employee> root = criteria.from(Employee.class);
		criteria.where(cb.gt(root.get("id"), 151));

		int rowsDeleted = em.createQuery(criteria).executeUpdate();
		System.out.println("Rows Deleted: " + rowsDeleted);
		return rowsDeleted;
	}

	public int minAge() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> criteria = cb.createQuery(Integer.class);
		Root<Employee> root = criteria.from(Employee.class);
		criteria.select(cb.min(root.<Integer>get("age")));

		TypedQuery<Integer> tq = em.createQuery(criteria);
		return tq.getSingleResult();
	}

	public int maxAge() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> criteria = cb.createQuery(Integer.class);
		Root<Employee> root = criteria.from(Employee.class);
		criteria.select(cb.max(root.<Integer>get("age")));

		TypedQuery<Integer> tq = em.createQuery(criteria);
		return tq.getSingleResult();
	}

	public List<Country> getCountries() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Country> criteria = cb.createQuery(Country.class);
		Root<Country> root = criteria.from(Country.class);
		criteria.select(root);

		TypedQuery<Country> tq = em.createQuery(criteria);
		return tq.getResultList();
	}
}
