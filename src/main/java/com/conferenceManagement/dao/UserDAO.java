package com.conferenceManagement.dao;

import com.conferenceManagement.model.User;
import com.conferenceManagement.dao.hibernate.HibernateUtils;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    public UserDAO(){}

    @Override
    public void save(User user) {
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

    @Override
    public void read(Long id) {

    }

    @Override
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

    @Override
    public void delete(User o) {

    }

    @Override
    public List<User> getAll() {
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

    public User getByID(Object o) {
        return null;
    }

    public User getUserByUsername(String username) {
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
//            if (user != null) {
//                var query1 = session.createQuery("from Admin as ad where ad.id = ?1", Admin.class)
//                        .setParameter(1, user.getId());
//                var admin = query1.uniqueResult();
////                admin.setUserInfo(user);
//                if (admin != null) {
//                    tx.commit();
//                    return admin;
//                }
//            }

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
