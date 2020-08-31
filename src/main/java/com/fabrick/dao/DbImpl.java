package com.fabrick.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fabio.sgroi
 */
@Repository
@Slf4j
public class DbImpl {

    @Autowired
    SessionFactory sessionFactory;

    @Transactional
    public int saveOrUpdateObject(Object obj) {
        int ret = -1;
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            session.saveOrUpdate(obj);
            tx = session.beginTransaction();
            session.flush();
            tx.commit();
            ret = 1;
        } catch (Exception e) {
            log.error("Error [saveOrUpdateObject]: " + e.getMessage());
            log.error("Error [saveOrUpdateObject]: ", e);
            if (tx != null) {
                tx.rollback();
            }
        }
        return ret;
    }

    @Transactional
    public int deteleObject(Object obj) {
        int ret = -1;
        try (Session session = sessionFactory.openSession()) {
            session.delete(obj);
            ret = 1;
        } catch (Exception e) {
            log.error("Error [deteleObject]: " + e.getMessage());
            log.error("Error [deteleObject]: ", e);
        }
        return ret;
    }

}
