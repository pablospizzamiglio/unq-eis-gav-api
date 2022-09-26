package dao

import entity.Order

class HibernateOrderDAO : HibernateDAO<Order>(Order::class.java) {

}
