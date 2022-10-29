package dao

import entity.*
import entity.Order
import org.junit.jupiter.api.*
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateDAOOrderTest {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var entityManager: EntityManager
    private lateinit var assistanceDAO: HibernateAssistanceDAO
    private lateinit var orderDAO: HibernateOrderDAO
    private lateinit var userDAO: HibernateUserDAO

    @BeforeAll
    fun setUpBeforeAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("GAV")
    }

    @BeforeEach
    fun setUp() {
        entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()

        assistanceDAO = HibernateAssistanceDAO(entityManager)
        orderDAO = HibernateOrderDAO(entityManager)
        userDAO = HibernateUserDAO(entityManager)
    }

    @AfterEach
    fun tearDown() {
        entityManager.transaction.rollback()
        entityManager.close()
    }

    @Test
    fun `basic order update`() {
        val newUser = User(
            "Test",
            "McTest",
            UserType.ASSISTANCE,
            "email@email.com",
            "55556666"
        )

        val persistedUser = userDAO.save(newUser)

        val newAssistance = Assistance(Kind.LARGE, 250.0, 500.0, persistedUser)

        val persistedAssistance = assistanceDAO.save(newAssistance)

        val order = Order(
            persistedAssistance,
            "q",
            "q",
            "q",
            "q",
            "1111111111",
            persistedAssistance.costPerKm,
            persistedAssistance.fixedCost,
            OrderStatus.PENDING_APPROVAL,
            persistedUser
        )

        val persistedOrder = orderDAO.save(order)

        persistedOrder.status = OrderStatus.COMPLETE

        orderDAO.update(persistedOrder)

        val updatedOrder = orderDAO.find(persistedOrder.id!!)

        assertEquals(OrderStatus.COMPLETE, updatedOrder.status)
    }
}
