package dao

import entity.User

class HibernateUserDAO : HibernateDAO<User>(User::class.java)