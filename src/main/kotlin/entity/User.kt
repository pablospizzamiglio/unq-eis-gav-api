package entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table


enum class UserType {
    ASSISTANCE,
    CLIENT
}

@Entity
@Table(name = "user")
class User(
    var firstName: String,
    var lastName: String,
    @Enumerated(EnumType.STRING)
    var type: UserType,
    var emailAddress: String,
    var telephoneNumber: String,
    var debts: Double = 0.0
) : AbstractJpaPersistable<UUID>()
