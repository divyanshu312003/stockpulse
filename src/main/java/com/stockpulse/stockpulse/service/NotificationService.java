package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.model.user;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Slf4j
public class NotificationService {
    @Value("${twilio.phone.number}")
    private String fromPhone;
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendAlert(user user, String stockSymbol, double score) {
        // For now: Simulate SMS
        String message1 = String.format(
                "📈 Stock Alert: %s is showing a strong bullish sentiment (score: %.2f).", stockSymbol, score);

        log.info("📤 Sending SMS to {}: {}", user.getPhone(), message1);
        try {
            Message message = Message.creator(
                    new PhoneNumber(user.getPhone()),   // TO
                    new PhoneNumber(fromPhone),         // FROM
                    message1
            ).create();

            log.info("📤 SMS sent to {} with SID: {}", user.getPhone(), message.getSid());
        } catch (Exception e) {
            log.error("❌ Failed to send SMS to {}: {}", user.getPhone(), e.getMessage());
        }
    }
}
