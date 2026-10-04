let farmerId = null;


// ==========================================
// PAGE LOAD
// ==========================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        const token =
            localStorage.getItem(
                "milkMateToken"
            );

        const role =
            localStorage.getItem(
                "milkMateRole"
            );


        if (
            !token ||
            role !== "FARMER"
        ) {

            window.location.href =
                "/farmer-login.html";

            return;
        }


        farmerId =
            localStorage.getItem(
                "milkMateFarmerId"
            );


        if (!farmerId) {

            showMessage(
                "Farmer profile not found.",
                "error"
            );

            return;
        }


        loadNotifications();
    }
);


// ==========================================
// LOAD NOTIFICATIONS
// ==========================================

async function loadNotifications() {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );


    if (!token || !farmerId) {
        return;
    }


    try {

        const response =
            await fetch(
                `/api/notifications/farmer/${farmerId}`,
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (
            response.status === 401
        ) {

            logout();
            return;
        }


        if (
            response.status === 403
        ) {

            showMessage(
                "You are not authorized to view notifications.",
                "error"
            );

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Failed to load notifications."
            );
        }


        const notifications =
            await response.json();


        displayNotifications(
            notifications
        );


    } catch (error) {

        console.error(
            "Notification Error:",
            error
        );


        showMessage(
            "Unable to load notifications.",
            "error"
        );
    }
}


// ==========================================
// DISPLAY
// ==========================================

function displayNotifications(
    notifications
) {

    const container =
        document.getElementById(
            "notificationList"
        );


    if (!container) {
        return;
    }


    container.innerHTML = "";


    if (
        !notifications ||
        notifications.length === 0
    ) {

        container.innerHTML = `
            <div class="empty">
                No notifications available.
            </div>
        `;

        return;
    }


    notifications.forEach(
        function (notification) {

            const card =
                document.createElement(
                    "div"
                );


            const isRead =
                Boolean(
                    notification.readStatus
                );


            card.className =
                isRead
                    ? "notification-card read"
                    : "notification-card unread";


            card.innerHTML = `

                <div class="notification-header">

                    <div class="notification-title">

                        ${escapeHtml(
                            notification.title ||
                            "Notification"
                        )}

                    </div>

                </div>


                <div class="notification-message">

                    ${escapeHtml(
                        notification.message ||
                        ""
                    )}

                </div>


                <div class="notification-footer">

                    <span class="notification-type">

                        ${escapeHtml(
                            notification.type ||
                            "GENERAL"
                        )}

                    </span>

                    <span class="notification-date">

                        ${formatDate(
                            notification.createdAt
                        )}

                    </span>

                </div>


                ${
                    !isRead
                    ?
                    `
                    <div style="margin-top:12px;">

                        <button
                            type="button"
                            class="btn-read"
                            onclick="markAsRead(${notification.id})">

                            Mark as Read

                        </button>

                    </div>
                    `
                    :
                    ""
                }

            `;


            container.appendChild(
                card
            );
        }
    );
}


// ==========================================
// MARK ONE AS READ
// ==========================================

async function markAsRead(
    notificationId
) {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );


    if (!token) {

        logout();
        return;
    }


    try {

        const response =
            await fetch(
                `/api/notifications/${notificationId}/read`,
                {
                    method: "PUT",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (
            response.status === 401
        ) {

            logout();
            return;
        }


        if (!response.ok) {

            throw new Error(
                "Unable to mark notification as read."
            );
        }


        showMessage(
            "Notification marked as read.",
            "success"
        );


        await loadNotifications();


    } catch (error) {

        console.error(
            "Mark Read Error:",
            error
        );


        showMessage(
            "Unable to update notification.",
            "error"
        );
    }
}


// ==========================================
// MARK ALL AS READ
// ==========================================

async function markAllAsRead() {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );


    if (!token || !farmerId) {

        logout();
        return;
    }


    try {

        const response =
            await fetch(
                `/api/notifications/farmer/${farmerId}/read-all`,
                {
                    method: "PUT",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (
            response.status === 401
        ) {

            logout();
            return;
        }


        if (!response.ok) {

            throw new Error(
                "Unable to mark notifications as read."
            );
        }


        showMessage(
            "All notifications marked as read.",
            "success"
        );


        await loadNotifications();


    } catch (error) {

        console.error(
            "Mark All Read Error:",
            error
        );


        showMessage(
            "Unable to update notifications.",
            "error"
        );
    }
}


// ==========================================
// DATE FORMAT
// ==========================================

function formatDate(
    value
) {

    if (!value) {
        return "-";
    }


    const date =
        new Date(value);


    if (
        Number.isNaN(
            date.getTime()
        )
    ) {

        return String(value);
    }


    return date.toLocaleString(
        "en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        }
    );
}


// ==========================================
// MESSAGE
// ==========================================

function showMessage(
    message,
    type
) {

    const element =
        document.getElementById(
            "message"
        );


    if (!element) {
        return;
    }


    element.textContent =
        message;


    element.className =
        "message " +
        (
            type || ""
        );
}


// ==========================================
// HTML SECURITY
// ==========================================

function escapeHtml(
    value
) {

    return String(
        value ?? ""
    )
    .replace(
        /&/g,
        "&amp;"
    )
    .replace(
        /</g,
        "&lt;"
    )
    .replace(
        />/g,
        "&gt;"
    )
    .replace(
        /"/g,
        "&quot;"
    )
    .replace(
        /'/g,
        "&#039;"
    );
}


// ==========================================
// LOGOUT
// ==========================================

function logout() {

    localStorage.removeItem(
        "milkMateToken"
    );

    localStorage.removeItem(
        "milkMateRole"
    );

    localStorage.removeItem(
        "milkMateUser"
    );

    localStorage.removeItem(
        "milkMateUserId"
    );

    localStorage.removeItem(
        "milkMateFarmerId"
    );

    localStorage.removeItem(
        "milkMateFullName"
    );

    localStorage.removeItem(
        "milkMateMobile"
    );

    localStorage.removeItem(
        "milkMateEmail"
    );

    window.location.href =
        "/farmer-login.html";
}