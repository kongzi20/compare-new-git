package com.iris.egrant.core.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.CharacterType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

/**
 * 封装Hibernate原生API的DAO泛型基类.
 * 
 * 可在Service层直接使用,也可以扩展泛型DAO子类使用. 参考Spring2.5自带的Petlinc例子,取消了HibernateTemplate,直接使用Hibernate原生API.
 * 
 * @param <T>
 *            DAO操作的对象类型
 * @param <PK>
 *            主键类型
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class SimpleHibernateDao<T, PK extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;

	/**
	 * 用于Dao层子类使用的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends SimpleHibernateDao<User, Long>
	 */
	public SimpleHibernateDao() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 用于用于省略Dao层, 在Service层直接使用通用SimpleHibernateDao的构造函数. 在构造函数中定义对象类型Class. eg. SimpleHibernateDao<User, Long> userDao
	 * = new SimpleHibernateDao<User, Long>(sessionFactory, User.class);
	 * 
	 * @param sessionFactory
	 * @param entityClass
	 */
	public SimpleHibernateDao(@Qualifier("sessionFactory") final SessionFactory sessionFactory,
			final Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}

	/**
	 * 取得sessionFactory.
	 * 
	 * @return SessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 采用@Autowired按类型注入SessionFactory,当有多个SesionFactory的时候Override本函数.
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(@Qualifier("sessionFactory") final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 取得当前Session.
	 * 
	 * @return Session
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 保存新增或修改的对象.
	 * 
	 * @param entity
	 * 
	 */
	public void save(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().saveOrUpdate(entity);
		logger.debug("save or update entity: {}", entity);
	}

	/**
	 * 保存新增的对象.
	 * 
	 * @param entity
	 */
	public void create(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().save(entity);
		logger.debug("create entity: {}", entity);
	}

	/**
	 * 删除对象.
	 * 
	 * @param entity
	 *            对象必须是session中的对象或含id属性的transient对象.
	 */
	public void delete(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().delete(entity);
		logger.debug("delete entity: {}", entity);
	}

	/**
	 * 按id删除对象.
	 */
	public void delete(final PK id) {
		Assert.notNull(id, "id不能为空");
		delete(get(id));
		logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
	}

	/**
	 * 按id获取对象.
	 */
	public T get(final PK id) {
		Assert.notNull(id, "id不能为空");
		return (T) getSession().get(entityClass, id);
	}

	/**
	 * 获取全部对象.
	 */
	public List<T> getAll() {
		return find();
	}

	/**
	 * 按属性查找对象列表,匹配方式为相等.
	 */
	public List<T> findBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return find(criterion);
	}

	/**
	 * 按属性查找唯一对象,匹配方式为相等.
	 */
	public T findUniqueBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(criterion).uniqueResult();
	}

	/**
	 * 按id列表获取对象.
	 */
	public List<T> findByIds(List<PK> ids) {
		return find(Restrictions.in(getIdName(), ids));
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <X> List<X> find(final String hql, final Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <X> List<X> find(final String hql, final Map<String, Object> values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <X> X findUnique(final String hql, final Object... values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <K> K findUniqueTest(final String hql, final Object... values) {
		return null;
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <X> X findUnique(final String hql, final Map<String, Object> values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 */
	public int batchExecute(final String hql, final Object... values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Map<String, Object> values) {
		return createQuery(hql, values).executeUpdate();
	}
	
	/**
	 * 仿spring JdbcTemplate 的 update 方法.
	 * 
	 * @param sql
	 * @param objects
	 * @return
	 */
	public int update(String sql, Object[] objects) {
		Session session = this.getSession();

		SQLQuery sqlQuery = session.createSQLQuery(sql);
		if (objects != null) {
			sqlQuery.setParameters(objects, this.findTypes(objects));
		}
		int i = sqlQuery.executeUpdate();
		return i;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	public SQLQuery createSqlQuery(String sql, List<Object> paramList) {
		SQLQuery query = getSession().createSQLQuery(sql);
		Iterator<Object> iterator = paramList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			query.setParameter(i++, iterator.next());
		}
		return query;
	}
	
	public List queryForList(String sql, Object[] objects) {
		Session session = this.getSession();

		SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		if (objects != null) {
			sqlQuery.setParameters(objects, this.findTypes(objects));
		}

		return sqlQuery.setFetchSize(2000).list();
	}
	
	/**
	 * 仿spring JdbcTemplate 的 queryForList 方法.
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryForList(String sql) {
		return this.queryForList(sql, null);
	}

	/**
	 * 仿spring JdbcTemplate 的 queryForInt 方法.
	 * 
	 * @param sql
	 * @return
	 */
	public int queryForInt(String sql) {
		return this.queryForInt(sql, null);
	}
	
	/**
	 * 仿spring JdbcTemplate 的 queryForInt 方法.
	 * 
	 * @param sql
	 * @param objects
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int queryForInt(String sql, Object[] objects) {
		Session session = this.getSession();

		SQLQuery sqlQuery = session.createSQLQuery(sql);

		if (objects != null) {
			sqlQuery.setParameters(objects, this.findTypes(objects));
		}
		Object obj = sqlQuery.uniqueResult();
		String rsValue = "";
		if (obj instanceof Map) {
			Map result = (Map) obj;
			for (Iterator iterator = result.keySet().iterator(); iterator.hasNext();) {
				Object key = iterator.next();
				rsValue = ObjectUtils.toString(result.get(key));
			}
		} else {
			rsValue = ObjectUtils.toString(obj);
		}
		return NumberUtils.toInt(rsValue);
	}
	
	/**
	 * 仿spring JdbcTemplate 的 queryForLong 方法.
	 * 
	 * @param sql
	 * @param objects
	 * @return
	 */
	public long queryForLong(String sql) {

		return this.queryForLong(sql, null);
	}
	
	/**
	 * 仿spring JdbcTemplate 的 queryForLong 方法.
	 * 
	 * @param sql
	 * @param objects
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public long queryForLong(String sql, Object[] objects) {
		Session session = this.getSession();

		SQLQuery sqlQuery = session.createSQLQuery(sql);

		if (objects != null) {
			sqlQuery.setParameters(objects, this.findTypes(objects));
		}
		Object obj = sqlQuery.uniqueResult();
		String rsValue = "";
		if (obj instanceof Map) {
			Map result = (Map) obj;
			for (Iterator iterator = result.keySet().iterator(); iterator.hasNext();) {
				Object key = iterator.next();
				rsValue = ObjectUtils.toString(result.get(key));
			}
		} else {
			rsValue = ObjectUtils.toString(obj);
		}

		return NumberUtils.toLong(rsValue);
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public Query createQuery(final String queryString, final Map<String, Object> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 按Criteria查询对象列表.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public List<T> find(final Criterion... criterions) {
		return createCriteria(criterions).list();
	}

	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public T findUnique(final Criterion... criterions) {
		return (T) createCriteria(criterions).uniqueResult();
	}

	/**
	 * 根据Criterion条件创建Criteria.
	 * 
	 * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 初始化对象. 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化. 只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性. 如需初始化关联属性,可实现新的函数,执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合. Hibernate.initialize
	 * (user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initEntity(T entity) {
		Hibernate.initialize(entity);
	}

	/**
	 * @see #initEntity(Object)
	 */
	public void initEntity(List<T> entityList) {
		for (T entity : entityList) {
			Hibernate.initialize(entity);
		}
	}

	/**
	 * 为Query添加distinct transformer.
	 */
	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	/**
	 * 为Criteria添加distinct transformer.
	 */
	public Criteria distinct(Criteria criteria) {
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	/**
	 * 通过Set将不唯一的对象列表唯一化. 主要用于HQL/Criteria预加载关联集合形成重复记录,又不方便使用distinct查询语句时.
	 */
	@SuppressWarnings("rawtypes")
	public <X> List<X> distinct(List list) {
		Set<X> set = new LinkedHashSet<X>(list);
		return new ArrayList<X>(set);
	}

	/**
	 * 取得对象的主键名.
	 */
	public String getIdName() {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}
	
	// 拼类型，所有类型都当字符串处理
		protected Type[] findTypes(Object[] objects) {
			List<Type> list = new ArrayList<Type>();
			for (Object object : objects) {
				if (object instanceof Integer) {
					list.add(new IntegerType());
				} else if (object instanceof Long) {
					list.add(new LongType());
				} else if (object instanceof BigDecimal) {
					list.add(new BigDecimalType());
				} else if (object instanceof Character) {
					list.add(new CharacterType());
				} else if (object instanceof Double) {
					list.add(new DoubleType());
				} else if (object instanceof Date) {
					list.add(new DateType());
				} else {
					list.add(new StringType());
				}
			}
			return list.toArray(new Type[] {});
		}
}