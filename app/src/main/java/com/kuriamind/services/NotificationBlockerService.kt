package com.kuriamind.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

/**
 * Stub NotificationListenerService required for BIND_NOTIFICATION_LISTENER_SERVICE.
 * Full implementation will follow in a future phase.
 */
class NotificationBlockerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}
