package ru.vonabe.repository

import ru.vonabe.entitys.EntityGuild
import utils.HibernateSessionFactoryUtil

class GuildRepository {

    companion object {
        val instance = GuildRepository()
    }

    fun findById(id: Int): EntityGuild {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(EntityGuild::class.java, id)
    }

    fun save(entity: EntityGuild) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        session.save(entity)
        transaction.commit()
        session.close()
    }

    fun update(user: EntityGuild) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.update(user)
        tx1.commit()
        session.close()
    }

    fun delete(user: EntityGuild) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.delete(user)
        tx1.commit()
        session.close()
    }

    fun findAll(): List<EntityGuild> {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From EntityGuild").list() as List<EntityGuild>
    }

}
