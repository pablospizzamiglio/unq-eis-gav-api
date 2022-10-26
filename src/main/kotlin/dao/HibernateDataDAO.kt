package dao

import javax.persistence.EntityManager
import javax.transaction.Transactional

open class HibernateDataDAO(private val entityManager: EntityManager) : DataDAO {

    @Transactional
    override fun clear() {
        val tableNames = entityManager.createNativeQuery("SHOW TABLES").resultList
        entityManager.transaction.begin()
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate()
        tableNames.forEach { result ->
            var table = ""
            when (result) {
                is String -> table = result
                is Array<*> -> table = result[0].toString()
            }
            entityManager.createNativeQuery("TRUNCATE TABLE $table").executeUpdate()
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate()
        entityManager.transaction.commit()
    }
}
