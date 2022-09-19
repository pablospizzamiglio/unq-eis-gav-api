package repository

import dao.HibernateAssistanceDAO
import entity.Assistance
import entity.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import transaction.TransactionRunner.runTrx

class HibernateDAOAssistantTest {
    @Test
    fun `basic assistance checks`() {
        val assistanceDAO = HibernateAssistanceDAO()
        val userOne = User(
            "Test",
            "McTest",

            "ASSISTANCE",
            "email@email.com",
            "55556666"
        )
        val userTwo = User(
            "German",
            "McRonalds",
            "ASSISTANCE",
            "german@email.com",
            "0303456664"
        )

        val assistanceOne = Assistance("Auto", "Security included", 40.0, userOne)
        val assistanceTwo = Assistance("Vans", "Top 10 in assistance's", 0.0, userTwo)

        val assOne = runTrx {
            assistanceDAO.save(assistanceOne)
        }
        val assTwo = runTrx {
            assistanceDAO.save(assistanceTwo)
        }

        Assertions.assertThat(userOne).isEqualTo(assOne.assistant)
        Assertions.assertThat(userTwo).isEqualTo(assTwo.assistant)
    }
}
