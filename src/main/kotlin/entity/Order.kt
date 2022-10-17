package entity

import java.util.*
import javax.persistence.*

enum class Status {
    PENDING_APPROVAL,
    IN_PROGRESS,
    CANCELLED,
    COMPLETE
}

@Entity
@Table(name = "orderassistances")
class Order(
    @ManyToOne(cascade = [CascadeType.ALL])
    val assistance: Assistance,
    val street: String,
    val betweenStreets: String,
    val city: String,
    val province: String,
    val phoneNumber: String,
    val costPerKm: Double,
    val fixedCost: Double,
    @Enumerated(EnumType.STRING)
    var status: Status,
    @ManyToOne(cascade = [CascadeType.ALL])
    val user: User
) : AbstractJpaPersistable<UUID>()
