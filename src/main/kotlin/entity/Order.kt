package entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

public enum class Status {
    PENDING_APPROVAL,
    IN_PROGRESS,
    CANCELLED,
    COMPLETE
}

@Entity
@Table(name = "orderassistances")
class Order(
    val assistanceId: UUID,
    val street: String,
    val betweenStreets: String,
    val city: String,
    val province: String,
    val phoneNumber: Int,
    val costPerKm: Double,
    val fixedCost: Double,
    @Enumerated(EnumType.STRING)
    var status: Status
) : AbstractJpaPersistable<UUID>()
