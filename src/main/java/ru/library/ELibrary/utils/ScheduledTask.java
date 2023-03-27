package ru.library.ELibrary.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.OrderStatus;
import ru.library.ELibrary.services.OrderService;

@Component
public class ScheduledTask {
    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(cron = "0 0 0 * * *")
    @Async
    @Transactional
    public void removeOverdueOrders() {
        Session session = entityManager.unwrap(Session.class);

        System.out.println("scheduled task");
        Query query = session.createQuery("delete from Order where date < current_date and orderStatus = :status");
        query.setParameter("status", OrderStatus.ORDERED);
        query.executeUpdate();
    }
}
