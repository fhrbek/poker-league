package cz.fhsoft.poker.league.server.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtil {

	public static Field getDeclaredField(Class<?> clazz, String name) throws NoSuchFieldException {
		while(clazz != null) {
			try {
				return clazz.getDeclaredField(name);
			} catch(NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		
		throw new NoSuchFieldException(name);
	}

	public static Field[] getDeclaredFields(Class<?> clazz) {
		List<Field> collector = new ArrayList<Field>();

		return getDeclaredFields(clazz, collector);
	}

	private static Field[] getDeclaredFields(Class<?> clazz, List<Field> collector) {
		for(Field field : clazz.getDeclaredFields())
			collector.add(field);

		Class<?> superClass = clazz.getSuperclass();
		if(superClass == null)
			return collector.toArray(new Field[collector.size()]);

		return getDeclaredFields(superClass, collector);
	}
}
