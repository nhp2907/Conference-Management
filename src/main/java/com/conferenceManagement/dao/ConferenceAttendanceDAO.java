package com.conferenceManagement.dao;

import com.conferenceManagement.model.Conference;
import com.conferenceManagement.model.ConferenceAttendance;
import com.conferenceManagement.dao.hibernate.HibernateUtils;
import com.conferenceManagement.model.User;
import javafx.collections.ObservableList;
import net.bytebuddy.asm.Advice;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;

import javax.persistence.TemporalType;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ConferenceAttendanceDAO {
    public static boolean isExists(Conference c, User u) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var result = session.createQuery("from ConferenceAttendance as cfa where cfa.conference = ?1 " +
                    "and cfa.user = ?2", ConferenceAttendance.class)
                    .setParameter(1, c)
                    .setParameter(2, u)
                    .uniqueResult();

            return result != null;
        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return false;
    }

    public static ConferenceAttendance getConferenceById(Conference c, User u) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ConferenceAttendance result;
            result = session.createQuery("from ConferenceAttendance as cfa where cfa.conference = ?1 and cfa.user = ?2", ConferenceAttendance.class)
                    .setParameter(1, c)
                    .setParameter(2, u)
                    .uniqueResult();

            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    public static List<ConferenceAttendance> getConferencesByConferenceId(Long id) {
        Transaction tx = null;
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            var result = session.createQuery("from ConferenceAttendance as cfa where cfa.conference.id = ?1")
                    .setParameter(1, id)
                    .list();
            tx.commit();
            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }

        return new ArrayList<>();
    }

    public static void save(ConferenceAttendance c) {

        Transaction tx = null;
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(c);
            tx.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
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

    public static void deleteAttendanceRegistrationByUser(User user) {
        Transaction tx = null;
        var session = HibernateUtils.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            if (!tx.isActive())
                tx.begin();

            var currentDate = LocalDateTime.now();
            var sql = "delete ca from conferenceattendance ca join conferences c " +
                    "on ca.conferenceID = c.id " +
                    "where ca.userid = ? and c.startDateTime > ?";
            var q = session.createSQLQuery(sql)
                    .setParameter(1, user.getId())
                    .setParameter(2, currentDate);
            q.executeUpdate();


//            var query = session.createQuery("delete from ConferenceAttendance as c where c.user = ?1" +
//                    " and c.conference.startDateTime > ?2")
//                    .setParameter(1, user)
//                    .setParameter(2, currentDate);
//            query.executeUpdate();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }

    //conferenceAttendance'is id has 2 columns
    public static List<ConferenceAttendance> getConferencesById(Conference conference, User user) {
        Transaction tx = null;
        var session = HibernateUtils.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            if (!tx.isActive())
                tx.begin();

            var result = session.createQuery("from ConferenceAttendance where conference = ?1" +
                    "and user = ?2", ConferenceAttendance.class)
                    .setParameter(1, conference)
                    .setParameter(2, user)
                    .list();

            tx.commit();

            return result;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<ConferenceAttendance> getConferencesByConference(Conference conference) {
        Transaction tx = null;
        var session = HibernateUtils.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            if (!tx.isActive())
                tx.begin();

            var result = session.createQuery("from ConferenceAttendance where conference = ?1", ConferenceAttendance.class)
                    .setParameter(1, conference)
                    .list();

            tx.commit();

            return result;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static void deleteAttendanceRegistrationById(Conference conference, User user) {
        Transaction tx = null;
        var session = HibernateUtils.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            if (!tx.isActive())
                tx.begin();

            var query = session.createQuery("delete from ConferenceAttendance where conference = ?1 " +
                    "and user = ?2")
                    .setParameter(1, conference)
                    .setParameter(2, user);
            query.executeUpdate();
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }

    }

    public static List<Conference> getConferenceByUserId(User user) {

        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("select conference from ConferenceAttendance as ca where ca.user = ?1 ", Conference.class)
                    .setParameter(1, user)
                    .list();
        } catch (Exception ex) {
            return null;
        }
    }

    public static void cancelRegistration(ConferenceAttendance ca) {
        Transaction tx = null;
        try (var session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
            var query = session.createQuery("delete from ConferenceAttendance as ca where ca.conference = ?1 " +
                    "and ca.user = ?2")
                    .setParameter(1, ca.getConference())
                    .setParameter(2, ca.getUser());
            query.executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        }

    }
}
