package entity

import java.util.*
import javax.persistence.*

enum class OrderStatus {
    PENDING_APPROVAL,
    IN_PROGRESS,
    CANCELLED,
    COMPLETED
}

@Entity
@Table(name = "assistance_order")
class Order(
    @ManyToOne(cascade = [CascadeType.ALL])
    val assistance: Assistance,
    var street: String,
    var betweenStreets: String,
    var city: String,
    var province: String,
    var phoneNumber: String,
    var costPerKm: Double,
    var fixedCost: Double,
    var kmTraveled: Int?,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus,
    @ManyToOne(cascade = [CascadeType.ALL])
    val user: User
) : AbstractJpaPersistable<UUID>()
