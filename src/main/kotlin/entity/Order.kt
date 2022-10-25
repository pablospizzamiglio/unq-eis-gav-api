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
    var street: String,
    var betweenStreets: String,
    var city: String,
    var province: String,
    var phoneNumber: String,
    var costPerKm: Double,
    var fixedCost: Double,
    @Enumerated(EnumType.STRING)
    var status: Status,
    @ManyToOne(cascade = [CascadeType.ALL])
    val user: User
) : AbstractJpaPersistable<UUID>()
