package transaction

interface Transaction {
    fun begin()
    fun commit()
    fun rollback()
}