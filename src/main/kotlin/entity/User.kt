package entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "user")
class User(
    val firstName: String,
    val lastName: String,
    val type: String,
    val emailAddress: String,
    val telephoneNumber: String,
    var debts: Double = 0.0
) : AbstractJpaPersistable<UUID>()
