package com.conferenceManagement.models.DAOs;

import com.conferenceManagement.models.ConferenceAttendence;
import com.conferenceManagement.models.User;
import com.conferenceManagement.models.hibernate.HibernateUtils;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ConferenceAttendenceDAO {
    public static List<ConferenceAttendence> getUsersByConferenceID(Long id){
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var result = session.createQuery("from ConferenceAttendence as cfa where cfa.conference.id = ?1")
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

    public static void save(ConferenceAttendence c) {
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

    public static ConferenceAttendence get(long conferenceID, long userID) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var result = session.createQuery("from ConferenceAttendence as c " +
                    "where c.userID  = ?1 and c.conferenceID = ?2", ConferenceAttendence.class)
                    .setParameter(1, userID)
                    .setParameter(2,conferenceID)
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
}
