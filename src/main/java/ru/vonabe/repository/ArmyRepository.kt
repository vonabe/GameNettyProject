package ru.vonabe.repository

import ru.vonabe.entitys.EntityArmy
import utils.HibernateSessionFactoryUtil

class ArmyRepository {

    private constructor()

    companion object {
        val instance = ArmyRepository()
    }

    fun findById(id: Int): EntityArmy {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From EntityArmy where id=:id", EntityArmy::class.java).setParameter("id", id).singleResult
    }

    fun save(entity: EntityArmy): EntityArmy {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        val save = session.save(entity) as EntityArmy
        transaction.commit()
        session.close()
        return save
    }

    fun update(user: EntityArmy) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.update(user)
        tx1.commit()
        session.close()
    }

    fun delete(user: EntityArmy) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.delete(user)
        tx1.commit()
        session.close()
    }

    fun findAll(): List<EntityArmy> {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From EntityArmy", EntityArmy::class.java).list() as List<EntityArmy>
    }


}
