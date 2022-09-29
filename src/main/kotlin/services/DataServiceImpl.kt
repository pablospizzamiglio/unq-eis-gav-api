package services

import dao.DataDAO
import transaction.TransactionRunner.runTrx

class DataServiceImpl(private val dataDAO: DataDAO) : DataService {
    override fun clear() {
        return runTrx {
            dataDAO.clear()
        }
    }
}