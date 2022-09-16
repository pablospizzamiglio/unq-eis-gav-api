package dao

import entity.Assistance

class HibernateAssistanceDAO : HibernateDAO<Assistance>(Assistance::class.java)
