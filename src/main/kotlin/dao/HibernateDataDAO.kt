package dao

import transaction.HibernateTransaction

class HibernateDataDAO : DataDAO {
    override fun clear() {
        val session = HibernateTransaction.currentSession
        val tableNames = session.createNativeQuery("SHOW TABLES").resultList
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate()
        tableNames.forEach { result ->
            var table = ""
            when (result) {
                is String -> table = result
                is Array<*> -> table = result[0].toString()
            }
            session.createNativeQuery("TRUNCATE TABLE $table").executeUpdate()
        }
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate()
    }
}
