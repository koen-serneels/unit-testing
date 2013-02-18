package be.testing.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.util.ReflectionUtils;

/**
 * @author Koen Serneels
 */
public class TestDataBuilderManager {

	private static ThreadLocal<State> state = new ThreadLocal<State>() {
		@Override
		protected State initialValue() {
			return new State();
		}
	};

	private static class State {
		EntityManager entityManager;
		Map<Object, Object> instancesByKey = new HashMap<>();
	}

	public static void init(EntityManager entityManager) {
		State threadState = state.get();
		threadState.entityManager = entityManager;
		threadState.instancesByKey.clear();
	}

	public static void init() {
		init(null);
	}

	public static EntityManager entityManager() {
		return state.get().entityManager;
	}

	@SuppressWarnings("unchecked")
	public static <T> T lookup(Object key) {
		State threadState = state.get();
		if (threadState.instancesByKey.containsKey(key)) {
			T t = (T) threadState.instancesByKey.get(key);
			if (entityManager() != null) {
				// make sure we have the value from the session
				try {
					return (T) entityManager().find(t.getClass(),
							ReflectionUtils.getField(t.getClass().getField("id"), t));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				return t;
			}
		} else {
			return null;
		}
	}

	public static <T> T register(Object key, T value) {
		State threadState = state.get();
		if (key != null) {
			threadState.instancesByKey.put(key, value);
		}
		return value;
	}

	public static void clear() {
		State threadState = state.get();
		threadState.instancesByKey.clear();
	}

	public static <T> List<T> save(List<T> values) {
		List<T> result = new ArrayList<>();
		for (T value : values) {
			result.add(save(value));
		}
		return result;
	}

	public static <T> T save(T value) {
		if (entityManager() != null) {
			entityManager().persist(value);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T save(Object key, T value) {
		State threadState = state.get();
		if (threadState.instancesByKey.containsKey(key)) {
			return (T) threadState.instancesByKey.get(key);
		} else {
			return register(key, save(value));
		}
	}
}