package com.test.example.core.dao.hibernate;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Id;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;


/**
 * 
 * <pre>
 * 自定义序列生成器：
 * 有些数据可能需要指定ID，有些需要同步序列自动生成ID，因此做法是如果已经指定了ID，则不生成新的ID.
 * </pre>
 * 
 * @author cg
 */
public class AssignedSequenceGenerator extends SequenceGenerator implements PersistentIdentifierGenerator, Configurable {

	@Override
	public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {

		// 如果是方法注解，查找方法注解
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			for (Annotation annotation : annotations) {
				// 查找ID
				if (annotation instanceof Id) {
					Serializable id = (Serializable) ReflectionUtils.invokeMethod(obj, method.getName(), null, null);
					// 不为空，直接返回
					if (id != null) {
						return id;
					} else {
						// 使用序列生成
						return super.generate(session, obj);
					}
				}
			}
		}

		// 如果是属性主键，查找属性注解
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field filed : fields) {
			Annotation[] annotations = filed.getAnnotations();
			for (Annotation annotation : annotations) {
				// 查找ID
				if (annotation instanceof Id) {
					Serializable id = (Serializable) ReflectionUtils.getFieldValue(obj, filed.getName());
					// 不为空，直接返回
					if (id != null) {
						return id;
					} else {
						// 使用序列生成
						return super.generate(session, obj);
					}
				}
			}
		}
		// 父类方法获取.
		return super.generate(session, obj);
	}

}
