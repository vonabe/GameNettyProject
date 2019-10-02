package ru.vonabe.repository

import ru.vonabe.entitys.EntityLord
import utils.HibernateSessionFactoryUtil

class LordRepository {

    private constructor()

    companion object {
        val instance = LordRepository()
    }

    fun findByLogin(login: String): EntityLord? {
        val openSession = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        return openSession.createQuery("FROM EntityLord WHERE login= :login", EntityLord::class.java).setParameter("login", login).singleResult
    }

    fun findByLoginAndPassword(login: String, password: String): EntityLord? {
        val openSession = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val createQuery = openSession.createQuery("FROM EntityLord WHERE login= :login and password= :password", EntityLord::class.java)
        createQuery.setParameter("login", login)
        createQuery.setParameter("password", password)
        return createQuery.singleResult
    }

    fun findById(id: Int): EntityLord {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(EntityLord::class.java, id)
    }

    fun isExistsLogin(login: String): Boolean {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val size = session.createQuery("FROM EntityLord WHERE login=:login").setParameter("login", login).resultList.size
        session.close()
        return size > 0
    }

    fun isExistsEmail(email: String): Boolean {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val size = session.createQuery("FROM EntityLord WHERE email=:email").setParameter("email", email).resultList.size
        session.close()
        return size > 0
    }

    fun save(entity: EntityLord) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val transaction = session.beginTransaction()
        session.save(entity)
        transaction.commit()
        session.close()
    }

    fun update(user: EntityLord) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.update(user)
        tx1.commit()
        session.close()
    }

    fun delete(user: EntityLord) {
        val session = HibernateSessionFactoryUtil.getSessionFactory().openSession()
        val tx1 = session.beginTransaction()
        session.delete(user)
        tx1.commit()
        session.close()
    }

    fun findAll(): List<EntityLord> {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From EntityLord").list() as List<EntityLord>
    }

}
