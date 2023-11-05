package id.s.hendisantika.springbootvirtualthread.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-virtual-thread
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/5/23
 * Time: 07:11
 * To change this template use File | Settings | File Templates.
 */
@Component
@Slf4j
public class AppEventListener {
    @EventListener
    @Async
    public void onGreetingEvent(String message) {
        log.info(Thread.currentThread() + " :: Received: {}", message);
    }
}
