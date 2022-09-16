package transaction

object TransactionRunner {
    private var transactions: MutableList<Transaction> = mutableListOf()

    fun <T> runTrx(f: () -> T): T {
        transactions.forEach { it.begin() }
        try {
            val result = f()
            transactions.forEach { it.commit() }
            return result
        } catch (e: RuntimeException) {
            transactions.forEach { it.rollback() }
            throw e
        } finally {
            transactions.clear()
        }
    }

    fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
    }
}