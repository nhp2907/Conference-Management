package com.conferenceManagement.models.DAOs;

import com.conferenceManagement.models.Conference;
import com.conferenceManagement.models.hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ConferenceDAO {

//    static private ConferenceDAO instance;
//
//    private ConferenceDAO() {
//
//    }
//
//    public static ConferenceDAO getInstance() {
//        if (instance == null)
//            instance = new ConferenceDAO();
//        return instance;
//    }

    public static void save(Conference conference) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Long id = (Long) session.save(conference);
            conference.setId(id);
            tx.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
    }

    public static void saveOrUpdate(Conference conference){
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
          session.saveOrUpdate(conference);
            tx.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
    }

    public static void update(Conference conference) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(conference);
            tx.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
    }

    public static void remove(Conference conference) {

    }

    public static List<Conference> getAll() {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var result = (List<Conference>) session.createSQLQuery(
                    "select * from Conferences")
                    .addEntity(Conference.class)
                    .list();

            return result;

        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            tx.commit();
            session.close();
        }

        return new ArrayList<>();
    }


    public Conference getByID(Object o) {
        return null;
    }

}
