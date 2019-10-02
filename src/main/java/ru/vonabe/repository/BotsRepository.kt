package ru.vonabe.repository

import ru.vonabe.entitys.EntityBot
import utils.HibernateSessionFactoryUtil

class BotsRepository {

    private constructor()

    companion object {
        val instance = BotsRepository()
    }

    fun findById(id: Int): EntityBot {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(EntityBot::class.java, id)
    }

    fun isExists(login: String): Boolean {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val size = session.createQuery("From EntityBot where login = $login").resultList.size
        session.close()
        return size > 0
    }

    fun save(entity: EntityBot) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        session.save(entity)
        transaction.commit()
        session.close()
    }

    fun update(user: EntityBot) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        session.update(user)
        transaction.commit()
        session.close()
    }

    fun delete(user: EntityBot) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        session.delete(user)
        transaction.commit()
        session.close()
    }

    fun findAll(): List<EntityBot> {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from EntityBot").list() as List<EntityBot>
    }

    fun findByLocation(id_location: Int) : List<EntityBot> {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from EntityBot where map=:id_location", EntityBot::class.java).setParameter("id_location", id_location).list() as List<EntityBot>
    }

}
