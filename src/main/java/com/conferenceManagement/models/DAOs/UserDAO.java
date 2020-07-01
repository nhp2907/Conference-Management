package com.conferenceManagement.models.DAOs;

import com.conferenceManagement.models.Admin;
import com.conferenceManagement.models.Conference;
import com.conferenceManagement.models.User;
import com.conferenceManagement.models.hibernate.HibernateUtils;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static void save(User user) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Long id = (Long) session.save(user);
            user.setId(id);
            tx.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
    }


    public void update(User user) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
    }

    public void delete(User o) {

    }

    public static List<User> getAll() {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (!tx.isActive())
                tx.begin();

            var result = (List<User>) session.createQuery(
                    "from User", User.class)
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

    public static User getByID(Object o) {
        return null;
    }

    public static User getUserByUsername(String username) {
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (!tx.isActive())
                tx.begin();
//            var query = session.createSQLQuery("select * from Users where username = ?")
//                    .addEntity(User.class)
//                    .setParameter(1, username);
            var query = session.createQuery("from User as u where u.userName = ?1", User.class)
                    .setParameter(1, username);
            var user = (User) query.uniqueResult();
            if (user != null) {
                var query1 = session.createQuery("from Admin as ad where ad.id = ?1", Admin.class)
                        .setParameter(1, user.getId());
                var admin = query1.uniqueResult();
//                admin.setUserInfo(user);
                if (admin != null) {
                    tx.commit();
                    return admin;
                }
            }

            tx.commit();
            return user;

        } catch (Exception ex) {
            ex.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return null;
    }


}
