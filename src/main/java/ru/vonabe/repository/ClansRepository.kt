package ru.vonabe.repository

import ru.vonabe.entitys.EntityClans
import utils.HibernateSessionFactoryUtil

class ClansRepository {

    companion object {
        val instance = ClansRepository()
    }

    fun findById(id: Int): EntityClans {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(EntityClans::class.java, id)
    }

    fun save(entity: EntityClans) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        session.save(entity)
        transaction.commit()
        session.close()
    }

    fun update(user: EntityClans) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.update(user)
        tx1.commit()
        session.close()
    }

    fun delete(user: EntityClans) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.delete(user)
        tx1.commit()
        session.close()
    }

    fun findAll(): List<EntityClans> {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From EntityClans").list() as List<EntityClans>
    }

}
