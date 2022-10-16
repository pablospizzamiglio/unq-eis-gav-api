package entity

import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "user")
class User(
    val firstName: String,
    val lastName: String,
    val type: String,
    val emailAddress: String,
    val telephoneNumber: String,
    var debts:Double = 0.0
) : AbstractJpaPersistable<UUID>()
