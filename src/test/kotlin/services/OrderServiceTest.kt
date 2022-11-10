package services

import api.dtos.OrderCreateRequestDTO
import api.dtos.OrderUpdateRequestDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import entity.*
import org.junit.jupiter.api.*
import java.util.*
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
        val newAssistance = Assistance(AssistanceKind.LARGE, newUserForAssistance, 250.0, 500.0, 150.0)

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
        assertTrue(order.kmTraveled == null)
    }

    @Test
    fun `street with special character throws an exception`() {
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
    fun `between streets with special character throws an exception`() {
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
    fun `city with special character throws an exception`() {
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
    fun `city with numbers throws an exception`() {
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
    fun `province with special character throws an exception`() {
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
    fun `province with numbers throws an exception`() {
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
    fun `phone with non numeric characters throws an exception`() {
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
    fun `9 digit long phone number throws an exception`() {
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
    fun `11 digit long phone number throws an exception`() {
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
    fun `updating a CANCELLED order to CANCELLED throws an exception`() {
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
            OrderStatus.CANCELLED.toString(),
            null,
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest)
        assertThrows<RuntimeException> { orderService.updateOrderStatus(orderUpdateRequest) }
    }

    @Test
    fun `updating a CANCELLED order to IN_PROGRESS throws an exception`() {
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
            OrderStatus.CANCELLED.toString(),
            null,
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest)

        val newOrderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.IN_PROGRESS.toString(),
            null,
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(newOrderUpdateRequest) }
    }

    @Test
    fun `updating a IN_PROGRESS order to IN_PROGRESS throws an exception`() {
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
            OrderStatus.IN_PROGRESS.toString(),
            null,
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest)

        val newOrderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.IN_PROGRESS.toString(),
            null,
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(newOrderUpdateRequest) }
    }

    @Test
    fun `updating a IN_PROGRESS order to COMPLETED throws an exception`() {
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
        val newOrderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.IN_PROGRESS.toString(),
            null,
            "0303456"
        )

        orderService.updateOrderStatus(newOrderUpdateRequest)

        val orderUpdateRequest2 = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.COMPLETED.toString(),
            5.0,
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest2)

        val newOrderUpdateRequest3 = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.IN_PROGRESS.toString(),
            null,
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(newOrderUpdateRequest3) }
    }

    @Test
    fun `updating a CANCELLED order to COMPLETED throws an exception`() {
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
        val newOrderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.IN_PROGRESS.toString(),
            null,
            "0303456"
        )

        orderService.updateOrderStatus(newOrderUpdateRequest)

        val orderUpdateRequest2 = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.COMPLETED.toString(),
            5.0,
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest2)

        val newOrderUpdateRequest3 = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.CANCELLED.toString(),
            5.0,
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(newOrderUpdateRequest3) }
    }

    @Test
    fun `updating a COMPLETED order to PENDING_APPROVAL throws an exception`() {
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
            OrderStatus.COMPLETED.toString(),
            5.0,
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(orderUpdateRequest) }
    }

    @Test
    fun `updating a COMPLETED order to CANCELLED throws an exception`() {
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
            OrderStatus.CANCELLED.toString(),
            null,
            "0303456"
        )

        orderService.updateOrderStatus(orderUpdateRequest)
        val orderUpdateRequest2 = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.COMPLETED.toString(),
            5.0,
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(orderUpdateRequest2) }
    }

    @Test
    fun `updating an order with negative kilometers throws an exception`() {
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
        val newOrderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.IN_PROGRESS.toString(),
            null,
            "0303456"
        )

        orderService.updateOrderStatus(newOrderUpdateRequest)

        val orderUpdateRequest2 = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.COMPLETED.toString(),
            -5.0,
            "0303456"
        )

        assertThrows<RuntimeException> { orderService.updateOrderStatus(orderUpdateRequest2) }
    }
    @Test
    fun `creating an order with nonexistent user throws an exception`() {
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 and 2",
            "Springfield",
            "Springfield",
            "1122223333",
            UUID.randomUUID()
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }
    }

    @Test
    fun `creating an order with user of type assistance throws an exception`() {
        val newUserForAssistance = User(
            "Pablo",
            "Escobar",
            UserType.ASSISTANCE,
            "elpatron@gmail.com",
            "1166607770"
        )
        val userTest = userDAO.save(newUserForAssistance)
        val orderCreateRequest = OrderCreateRequestDTO(
            assistance.id,
            "Evergreen 123",
            "1 and 2",
            "Springfield",
            "Springfield",
            "1122223333",
            userTest.id
        )

        assertThrows<RuntimeException> { orderService.createOrder(orderCreateRequest) }
    }

    @Test
    fun `cancelling an order`() {
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

        assertEquals(OrderStatus.PENDING_APPROVAL, order.status)

        val orderUpdateRequest = OrderUpdateRequestDTO(
            order.id,
            OrderStatus.CANCELLED.toString(),
            null,
            "0303456",
        )

        val updatedOrder = orderService.updateOrderStatus(orderUpdateRequest)

        assertEquals(OrderStatus.CANCELLED, updatedOrder.status)

        val updatedUser = userDAO.find(updatedOrder.user.id!!)

        assertEquals(assistance.cancellationCost, updatedUser.debts)
    }
}
