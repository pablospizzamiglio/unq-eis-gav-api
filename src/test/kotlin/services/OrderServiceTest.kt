package services

import api.dtos.OrderCreateRequestDTO
import api.dtos.OrderUpdateRequestDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import entity.*
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
    private lateinit var userAssistance: User
    private lateinit var userClient: User
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

        val newUserForAssistance = User(
            "Pablo",
            "Escobar",
            UserType.ASSISTANCE,
            "elpatron@gmail.com",
            "1166607770"
        )
        val newUserForClient = User(
            "Lisa",
            "Simpsons",
            UserType.CLIENT,
            "lisa.simpson@email.com",
            "1122223333"
        )
        val newAssistance = Assistance(Kind.LARGE, 250.0, 500.0, newUserForAssistance)

        userAssistance = userDAO.save(newUserForAssistance)
        userClient = userDAO.save(newUserForClient)
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
            userClient.id
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
            userClient.id
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
            userClient.id
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
            userClient.id
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
            userClient.id
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
            userClient.id
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
            userClient.id
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
            userClient.id
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
            userClient.id
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
            userClient.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }

        val orders = orderDAO.findAll()

        assertTrue { orders.isEmpty() }
    }

    @Test
    fun `cancel an order that has already been canceled is rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 and 2",
            "Springfield",
            "Springfield",
            "1122223333",
            userClient.id
        )

        val order = orderService.createOrder(orderCreateRequest)
        val orderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            "CANCELLED",
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest)
        assertThrows<RuntimeException> { orderService.updateOrderStatus(orderUpdateRequest) }
    }

    @Test
    fun `update a canceled order to IN_PROGRESS is rejected`() {
       val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 and 2",
            "Springfield",
            "Springfield",
            "1122223333",
           userClient.id
        )

        val order = orderService.createOrder(orderCreateRequest)
        val orderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            "CANCELLED",
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest)

        val newOrderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            "IN_PROGRESS",
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(newOrderUpdateRequest) }
    }

    @Test
    fun `update a IN_PROGRESS order to IN_PROGRESS is rejected`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 and 2",
            "Springfield",
            "Springfield",
            "1122223333",
            userClient.id
        )

        val order = orderService.createOrder(orderCreateRequest)
        val orderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            "IN_PROGRESS",
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest)

        val newOrderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            "IN_PROGRESS",
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(newOrderUpdateRequest) }
    }
}
