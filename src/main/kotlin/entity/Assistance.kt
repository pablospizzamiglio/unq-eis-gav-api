package entity

import java.util.*
import javax.persistence.*

enum class AssistanceKind {
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
    var kind: AssistanceKind,
    @OneToOne(cascade = [CascadeType.ALL])
    var user: User,
    var costPerKm: Double,
    var fixedCost: Double,
    var cancellationCost: Double,
) : AbstractJpaPersistable<UUID>()
