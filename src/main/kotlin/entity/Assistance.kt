package entity

import java.util.*
import javax.persistence.*

enum class Kind {
    SMALL,
    MEDIUM,
    LARGE,
    BATTERY,
    START_UP
}

@Entity
@Table(name = "assistance")
class Assistance(
    @Enumerated(EnumType.STRING)
    var kind: Kind,
    var costPerKm: Double,
    var fixedCost: Double,
    @OneToOne(cascade = [CascadeType.ALL])
    val user: User
) : AbstractJpaPersistable<UUID>()
