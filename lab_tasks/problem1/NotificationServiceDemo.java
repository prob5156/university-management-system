package lab_tasks.problem1;

interface NotificationChannel {
    void send(String recipient, String message);
}

class EmailNotificationChannel implements NotificationChannel {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[EMAIL] Sent email to " + recipient + ": " + message);
    }
}

class SmsNotificationChannel implements NotificationChannel {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[SMS] Sent SMS to " + recipient + ": " + message);
    }
}

class PushNotificationChannel implements NotificationChannel {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[PUSH] Sent push notification to " + recipient + ": " + message);
    }
}

class NotificationChannelFactory {
    public static NotificationChannel createChannel(String channelType) {
        if (channelType == null) {
            throw new IllegalArgumentException("Channel type cannot be null");
        }
        switch (channelType.trim().toUpperCase()) {
            case "EMAIL":
                return new EmailNotificationChannel();
            case "SMS":
                return new SmsNotificationChannel();
            case "PUSH":
            case "PUSH_NOTIFICATION":
                return new PushNotificationChannel();
            default:
                throw new IllegalArgumentException("Unknown channel type: " + channelType);
        }
    }
}

class NotificationManager {
    private static volatile NotificationManager instance;

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        if (instance == null) {
            synchronized (NotificationManager.class) {
                if (instance == null) {
                    instance = new NotificationManager();
                }
            }
        }
        return instance;
    }

    public void sendAlert(String channelType, String recipient, String message) {
        NotificationChannel channel = NotificationChannelFactory.createChannel(channelType);
        channel.send(recipient, message);
    }
}

public class NotificationServiceDemo {
    public static void main(String[] args) {
        NotificationManager manager1 = NotificationManager.getInstance();
        NotificationManager manager2 = NotificationManager.getInstance();

        System.out.println("Are both instances same? " + (manager1 == manager2));

        manager1.sendAlert("EMAIL", "user@example.com", "Order #1001 confirmed!");
        manager1.sendAlert("SMS", "+8801700000000", "OTP is 482910");
        manager1.sendAlert("PUSH", "device_xyz", "Flash sale started!");
    }
}
