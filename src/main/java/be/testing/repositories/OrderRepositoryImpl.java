package be.testing.repositories;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import be.testing.entities.Order;
import be.testing.entities.Product;

/**
 * @author Koen Serneels
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void saveOrder(Order order) {
		entityManager.persist(order);
	}

	@Override
	public Collection<Order> findOrders(OrderSearchCriteria orderSearchCriteria) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteria = criteriaBuilder.createQuery(Order.class);

		Root<Order> orderRoot = criteria.from(Order.class);
		Join<Order, Product> productRoot = orderRoot.join("products");

		if (orderSearchCriteria.getOrderDate() != null) {
			Date startDate = DateUtils.truncate(orderSearchCriteria.getOrderDate(), Calendar.DAY_OF_MONTH);
			Date endDate = DateUtils.addDays(startDate, 1);

			Predicate conjunction = criteriaBuilder.conjunction();
			predicates.add(conjunction);
			conjunction.getExpressions().add(
					criteriaBuilder.greaterThanOrEqualTo(orderRoot.<Date> get("date"), startDate));
			conjunction.getExpressions().add(criteriaBuilder.lessThan(orderRoot.<Date> get("date"), endDate));
		}

		if (StringUtils.isNotBlank(orderSearchCriteria.getProductName())) {
			predicates.add(criteriaBuilder.equal(productRoot.get("name"), orderSearchCriteria.getProductName()));
		}
		if (StringUtils.isNotBlank(orderSearchCriteria.getProductDescription())) {
			predicates.add(criteriaBuilder.equal(productRoot.get("description"),
					orderSearchCriteria.getProductDescription()));
		}

		if (predicates.isEmpty()) {
			return new ArrayList<Order>();
		}

		criteria.distinct(true);
		criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		return entityManager.createQuery(criteria).getResultList();
	}
}