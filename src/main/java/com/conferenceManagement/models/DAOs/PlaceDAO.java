package com.conferenceManagement.models.DAOs;

import com.conferenceManagement.models.Conference;
import com.conferenceManagement.models.Place;
import com.conferenceManagement.models.hibernate.HibernateUtils;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PlaceDAO {
    public static List<Place> getAll(){
        var session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            var result = (List<Place>) session.createSQLQuery(
                    "select * from Places")
                    .addEntity(Place.class)
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
}
