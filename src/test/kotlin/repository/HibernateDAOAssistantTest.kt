package repository

import dao.HibernateAssistanceDAO
import dao.HibernateDataDAO
import entity.Assistance
import entity.Kind
import entity.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import services.DataServiceImpl
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

        val assistanceOne = Assistance(Kind.LARGE, 250.0, 500.0, userOne)
        val assistanceTwo = Assistance(Kind.MEDIUM, 325.0, 425.0, userTwo)

        val assOne = runTrx {
            assistanceDAO.save(assistanceOne)
        }
        val assTwo = runTrx {
            assistanceDAO.save(assistanceTwo)
        }

        Assertions.assertThat(userOne).isEqualTo(assOne.user)
        Assertions.assertThat(userTwo).isEqualTo(assTwo.user)
    }

    @Test
    fun `wizard filtering by kind no data`() {
        val assistanceDAO = HibernateAssistanceDAO()

        val assistances = runTrx {
            assistanceDAO.findAllByKind("SMALL")
        }

        Assertions.assertThat(assistances).isEmpty()
    }

    @Test
    fun `wizard filtering by kind whiy data`() {
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

        val assistanceOne = Assistance(Kind.LARGE, 250.0, 500.0, userOne)
        val assistanceTwo = Assistance(Kind.SMALL, 325.0, 425.0, userTwo)

        runTrx {
            assistanceDAO.save(assistanceOne)
        }
        val assTwo = runTrx {
            assistanceDAO.save(assistanceTwo)
        }

        val result = runTrx {
            assistanceDAO.findAllByKind("SMALL")
        }

        Assertions.assertThat(result).contains(assTwo)
    }

    @AfterEach
    fun cleanup() {
        DataServiceImpl(HibernateDataDAO()).clear()
    }
}
