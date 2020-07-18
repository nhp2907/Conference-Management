package com.conferenceManagement.dao;

import com.conferenceManagement.model.ConferenceAttendance;
import com.conferenceManagement.dao.hibernate.HibernateUtils;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ConferenceAttendanceDAO {
    public static List<ConferenceAttendance> getUsersByConferenceID(Long id) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var result = session.createQuery("from ConferenceAttendance as cfa where cfa.conference.id = ?1")
                    .setParameter(1, id)
                    .list();
            tx.commit();
            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return new ArrayList<>();
    }

    public static void save(ConferenceAttendance c) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(c);
            tx.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
    }

    public static ConferenceAttendance get(long conferenceID, long userID) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var result = session.createQuery("from ConferenceAttendance as c " +
                    "where c.userID  = ?1 and c.conferenceID = ?2", ConferenceAttendance.class)
                    .setParameter(1, userID)
                    .setParameter(2, conferenceID)
                    .uniqueResult();
            tx.commit();
            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return null;
    }

    public static void update(ConferenceAttendance c) {
        Transaction tx = null;
        var session = HibernateUtils.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            if (!tx.isActive())
                tx.begin();

            session.update(c);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }
}
