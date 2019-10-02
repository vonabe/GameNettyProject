package ru.vonabe.repository

import ru.vonabe.entitys.EntityAdmin
import utils.HibernateSessionFactoryUtil

class AdminRepository {

    companion object {
        val instance = AdminRepository()
    }

    private fun findById(id: Int): EntityAdmin {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(EntityAdmin::class.java, id)
    }

    fun save(entity: EntityAdmin) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        session.save(entity)
        transaction.commit()
        session.close()
    }

    private fun update(user: EntityAdmin) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.update(user)
        tx1.commit()
        session.close()
    }

    private fun delete(user: EntityAdmin) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.delete(user)
        tx1.commit()
        session.close()
    }

    private fun findAll(): List<EntityAdmin> {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Admin").list() as List<EntityAdmin>
    }

}
