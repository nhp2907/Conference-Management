package com.conferenceManagement.models.DAOs;

import com.conferenceManagement.models.User;
import com.conferenceManagement.models.hibernate.HibernateUtils;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ConferenceAttendenceDAO {
    public static List<User> getUsersByConferenceID(Long id){
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var result = session.createQuery("select u from User as u join ConferenceAttendence as c on u.id = c.userID " +
                    "where c.conferenceID = ?1", User.class)
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

    public static void save(long id, long id1) {
        //do it later
        System.out.println("saved to database");
    }
}
