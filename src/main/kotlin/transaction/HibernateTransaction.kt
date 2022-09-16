package transaction

import org.hibernate.Session

object HibernateTransaction : Transaction {
    private var sessionThreadLocal: ThreadLocal<Session> = ThreadLocal()
    private var transaction: ThreadLocal<org.hibernate.Transaction> = ThreadLocal()

    val currentSession: Session
        get() {
            if (sessionThreadLocal.get() == null) {
                TransactionRunner.addTransaction(HibernateTransaction)
                begin()
            }
            return sessionThreadLocal.get()
        }

    override fun begin() {
        val session = HibernateSessionFactoryProvider.instance.createSession()
        sessionThreadLocal.set(session)
        transaction.set(session.beginTransaction())
    }

    override fun commit() {
        transaction.get().commit()
        close()
    }

    override fun rollback() {
        transaction.get().rollback()
        close()
    }

    private fun close() {
        sessionThreadLocal.get().close()
        sessionThreadLocal.set(null)
        transaction.set(null)
    }
}
