package entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "user")
class User(
    var firstName: String,
    var lastName: String,
    var type: String,
    var emailAddress: String,
    var telephoneNumber: String,
    var debts: Double = 0.0
) : AbstractJpaPersistable<UUID>()
