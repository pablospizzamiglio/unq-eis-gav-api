package services

import api.dtos.OrderCreateRequestDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import entity.Assistance
import entity.Kind
import entity.OrderStatus
import entity.User
import org.junit.jupiter.api.*
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceTest {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var entityManager: EntityManager
    private lateinit var assistanceDAO: HibernateAssistanceDAO
    private lateinit var userDAO: HibernateUserDAO
    private lateinit var orderDAO: HibernateOrderDAO
    private lateinit var orderService: OrderServiceImpl
    private lateinit var user: User
    private lateinit var assistance: Assistance


    @BeforeAll
    fun setUpBeforeAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("GAV")
    }

    @BeforeEach
    fun setUp() {
        entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()

        assistanceDAO = HibernateAssistanceDAO(entityManager)
        userDAO = HibernateUserDAO(entityManager)
        orderDAO = HibernateOrderDAO(entityManager)
        orderService = OrderServiceImpl(orderDAO, assistanceDAO, userDAO)

        val newUser = User(
            "Test",
            "McTest",
            "ASSISTANCE",
            "email@email.com",
            "55556666"
        )
        val newAssistance = Assistance(Kind.LARGE, 250.0, 500.0, newUser)

        user = userDAO.save(newUser)
        assistance = assistanceDAO.save(newAssistance)
    }

    @AfterEach
    fun tearDown() {
        entityManager.transaction.rollback()
        entityManager.close()
    }

    @Test
    fun `create order`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 and 2",
            "Springfield",
            "Springfield",
            "1122223333",
            user.id
        )

        val order = orderService.createOrder(orderCreateRequest)

        assertEquals(orderCreateRequest.assistanceId, order.assistance.id)
        assertEquals(orderCreateRequest.street, order.street)
        assertEquals(orderCreateRequest.betweenStreets, order.betweenStreets)
        assertEquals(orderCreateRequest.city, order.city)
        assertEquals(orderCreateRequest.province, order.province)
        assertEquals(orderCreateRequest.userId, order.user.id)
        assertEquals(OrderStatus.PENDING_APPROVAL, order.status)
    }

    @Test
    fun `street with special character rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen |23",
            "1 and 2",
            "Springfield",
            "Springfield",
            "1122223333",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `between streets with special character rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "| y 2",
            "Springfield",
            "Springfield",
            "1122223333",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `city with special character rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 y 2",
            "\$pringfield",
            "Springfield",
            "1122223333",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `city with numbers rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 y 2",
            "Spr1ngfield",
            "Springfield",
            "1122223333",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `province with special character rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 y 2",
            "Springfield",
            "\$pringfield",
            "1122223333",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `province with numbers rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 y 2",
            "Springfield",
            "Spr1ngfield",
            "1122223333",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `phone with non numeric characters rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 y 2",
            "Springfield",
            "Spr1ngfield",
            "the number",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `9 digit long phone number is rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 y 2",
            "Springfield",
            "Spr1ngfield",
            "112222333",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `11 digit long phone number is rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 y 2",
            "Springfield",
            "Spr1ngfield",
            "11222233334",
            user.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }
}
