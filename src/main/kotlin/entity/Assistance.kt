package entity

import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "assistance")
class Assistance(
    val kind: String,
    val detail: String,
    val costPerKm: Double,
    @OneToOne(cascade = [CascadeType.ALL])
    val assistant: User
) : AbstractJpaPersistable<UUID>()
