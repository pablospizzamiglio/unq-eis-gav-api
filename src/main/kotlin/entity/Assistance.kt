package entity

import java.util.UUID
import javax.persistence.*

public enum class Kind{
    SMALL,
    MEDIUM,
    LARGE,
    BATTERY,
    START_UP
}
@Entity
@Table(name = "assistance")
class Assistance(
    @Enumerated (EnumType.STRING)
    val kind: Kind,
    val costPerKm: Double,
    val fixedCost: Double,
    @OneToOne(cascade = [CascadeType.ALL])
    val user: User
) : AbstractJpaPersistable<UUID>()
