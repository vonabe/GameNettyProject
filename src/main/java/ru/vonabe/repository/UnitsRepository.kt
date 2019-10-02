package ru.vonabe.repository

import ru.vonabe.entitys.EntityUnits
import utils.HibernateSessionFactoryUtil

class UnitsRepository {

    constructor()

    companion object {
        val instance = UnitsRepository()
    }

    fun findById(id: Int): EntityUnits {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From EntityUnits where id=:id", EntityUnits::class.java).setParameter("id", id).singleResult
    }

    fun save(entity: EntityUnits) : EntityUnits {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        val save = session.save(entity) as EntityUnits
        transaction.commit()
        session.close()
        return save
    }

    fun update(user: EntityUnits) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.update(user)
        tx1.commit()
        session.close()
    }

    fun delete(user: EntityUnits) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.delete(user)
        tx1.commit()
        session.close()
    }

    fun findAll(): List<EntityUnits> {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From EntityUnits", EntityUnits::class.java).list() as List<EntityUnits>
    }

}
