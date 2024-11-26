package org.example.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean isHealthy = checkCustomHealthLogic();

        if (isHealthy) {
            return Health.up().withDetail("healthIndicator", "Everything is fine").build();
        } else {
            return Health.down().withDetail("healthIndicator", "Something is wrong").build();
        }
    }

    private boolean checkCustomHealthLogic() {
        // Add check for NBP
        return true;
    }
}

