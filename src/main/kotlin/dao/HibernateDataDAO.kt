package dao

import transaction.HibernateTransaction

class HibernateDataDAO : DataDAO {
    override fun clear() {
        val session = HibernateTransaction.currentSession
        val nombreDeTablas = session.createNativeQuery("SHOW TABLES").resultList
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate()
        nombreDeTablas.forEach { result ->
            var tabla = ""
            when (result) {
                is String -> tabla = result
                is Array<*> -> tabla = result[0].toString()
            }
            session.createNativeQuery("TRUNCATE TABLE $tabla").executeUpdate()
        }
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate()
    }
}
