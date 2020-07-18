package com.conferenceManagement.dao.hibernate;


import com.conferenceManagement.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static SessionFactory sessionFactory;
    private static HibernateUtils instance;

    static {
        config();
    }


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static void config() {
        var connectionString = "jdbc:mysql://localhost/ConferenceManagement";
        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.url", connectionString)
                .setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver")
                .setProperty("hibernate.connection.username", "root")
                .setProperty("hibernate.connection.password", "1234")
                .setProperty("hibernate.show_sql", "false")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        configuration.addAnnotatedClass(Conference.class);
        configuration.addAnnotatedClass(Place.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Admin.class);
        configuration.addAnnotatedClass(ConferenceAttendance.class);

        try {
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addConference(Conference conference) {
        var session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Long id = (Long) session.save(conference);
            conference.setId(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
