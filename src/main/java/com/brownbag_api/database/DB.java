package com.brownbag_api.database;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.brownbag_api.model.Log;
import com.brownbag_api.model.User;
import com.brownbag_api.util.UtilDate;

public class DB {
	private static DB dbInstance;
	private static SessionFactory factory;
	public static Session s;

	public DB() {

	}

	public static DB getInstance() {
		if (dbInstance == null) {
			dbInstance = new DB();
		}
		return dbInstance;
	}

	public static boolean connect() {
		try {
			Configuration config = new Configuration();
			config.addAnnotatedClass(User.class);
			config.addAnnotatedClass(Log.class);

			factory = config.configure().buildSessionFactory();

		} catch (Throwable ex) {
//			DB.log("DB.connect(): Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		return true;
	}

	public static <Entity> Long saveEntityCommit(Entity entity) {
		return saveEntity(entity, true);
	}

	public static <Entity> Long saveEntity(Entity entity) {
		return saveEntity(entity, false);
	}

	public static <Entity> Long saveEntity(Entity entity, boolean commit) {
		Long id;
		try {
			if (commit) {
				restartSession();
				beginTrx();
			}
//			DB.logTrx("DB.saveEntity: Save entity: " + entity.getClass().getSimpleName());
			id = (Long) DB.s.save(entity);

			if (commit) {
				DB.commitTrx();
			}

		} catch (Throwable ex) {
//			DB.logTrx("DB.saveEntity(): Failed to create sessionFactory object for entity: " + entity.getClass().getSimpleName());
			throw new ExceptionInInitializerError(ex);
		}
		return id;
	}

	public static void clearTable(Class<?> tableClass) {
		DB.s.close();
		DB.openSession();
		DB.s.beginTransaction();
		DB.s.createQuery("delete from " + tableClass.getSimpleName()).executeUpdate();
		DB.s.getTransaction().commit();
	}

	public static void createEvt(String evtName, String doStmt, Date timeStamp) {

		String timeStampStr = UtilDate.nowAsSQLTimeStamp();

		if (timeStamp != null) {
			timeStampStr = UtilDate.dateAsSQLTimeStamp(timeStamp);
		}

		String qryStr = "CREATE EVENT " + evtName + " ON SCHEDULE AT '" + timeStampStr + "' DO " + doStmt;

//		DB.log("DB.createEvt(): " + qryStr);

		DB.s.beginTransaction();
		@SuppressWarnings("rawtypes")
		Query query = DB.s.createNativeQuery(qryStr);
		query.executeUpdate();
		DB.s.getTransaction().commit();
	}

	public static void createEvtColVal(String evtName, String tableName, String colName, String val, String whereStmt,
			Date timeStamp) {
		String updateStmt = "UPDATE " + tableName + " SET " + colName + "  = '" + val + "' WHERE" + whereStmt;
		createEvt(evtName, updateStmt, timeStamp);
	}

	public static <Entity> boolean updateEntityCommit(Entity entity) {
		try {
			DB.s.close();
			DB.openSession();
			DB.s.beginTransaction();
			DB.s.update(entity);
			DB.s.getTransaction().commit();
		} catch (Throwable ex) {
			DB.log("DB.updateEntityCommit(): Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		return true;
	}

	public static <Entity> boolean updateEntity(Entity entity) {
		try {
			DB.s.update(entity);
		} catch (Throwable ex) {
//			DB.logTrx("DB.updateEntity(): Failed to create sessionFactory object.");
			throw new ExceptionInInitializerError(ex);
		}
		return true;
	}

	public static <Entity> boolean deleteObj(Entity entity) {
		try {
			DB.s.close();
			DB.openSession();
			DB.s.beginTransaction();
			DB.s.delete(entity);
			DB.s.getTransaction().commit();

		} catch (Throwable ex) {
//			DB.log("DB.deleteObj(): Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		return true;
	}

	public static <Entity> Entity getById(Class<?> entityClass, Long id) {
		return (Entity) DB.s.get(entityClass, id);
	}

	public static <Entity> Entity getByIntlId(Class<?> entityClass, String intlId) {
		@SuppressWarnings("rawtypes")
		Query query = DB.s.createQuery("from " + entityClass.getSimpleName() + " where intlId = :intlId");
		query.setParameter("intlId", intlId);
		if (query.list().isEmpty()) {
//			DB.log("DB.getByIntlId(): " + entityClass.getSimpleName() + " with INTL_ID: '" + intlId + "' does not yet exist in the Database.");
			return null;
		} else {
			return (Entity) query.list().get(0);
		}
	}

	public static void openSession() {
		s = factory.openSession();
	}

	public static void closeSession() {
		s.close();
	}

	public static void restartSession() {
		if (s != null) {
			closeSession();
		}
		openSession();
	}

	public static void beginTrx() {
		s.beginTransaction();
	}

	public static void commitTrx() {
		DB.s.getTransaction().commit();
	}

	public static void logTrx(String msg) {
		Log log = new Log(msg);
		DB.s.save(log);
	}

	public static void log(String msg) {

		restartSession();
		beginTrx();
		logTrx(msg);
		commitTrx();

	}
}
