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

        const farmerId =
            localStorage.getItem(
                "milkMateFarmerId"
            );


        if (
            !token ||
            role !== "FARMER"
        ) {

            window.location.href =
                "/farmer-login.html";

            return;
        }


        if (!farmerId) {

            showMessage(
                "Farmer profile is not linked yet."
            );

            return;
        }


        const readAllBtn =
            document.getElementById(
                "readAllBtn"
            );


        if (readAllBtn) {

            readAllBtn.addEventListener(
                "click",
                markAllAsRead
            );
        }


        loadNotifications();
    }
);


// ==========================================
// LOAD
// ==========================================

async function loadNotifications() {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );

    const farmerId =
        localStorage.getItem(
            "milkMateFarmerId"
        );


    if (
        !token ||
        !farmerId
    ) {
        logout();
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
                "You are not authorized to view notifications."
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
            "Unable to load notifications."
        );
    }
}


// ==========================================
// DISPLAY
// ==========================================

function displayNotifications(
    notifications
) {

    const list =
        document.getElementById(
            "notificationList"
        );


    if (!list) {
        return;
    }


    list.innerHTML =
        "";


    if (
        !notifications ||
        notifications.length === 0
    ) {

        list.innerHTML = `
            <div class="empty">
                No notifications available.
            </div>
        `;

        return;
    }


    notifications.forEach(
        function (notification) {

            const item =
                document.createElement(
                    "div"
                );


            const read =
                Boolean(
                    notification.readStatus
                );


            item.className =
                read
                    ? "notification"
                    : "notification unread";


            item.innerHTML = `

                <div class="notification-title">

                    ${escapeHtml(
                        notification.title ||
                        "Notification"
                    )}

                </div>


                <div class="notification-message">

                    ${escapeHtml(
                        notification.message ||
                        ""
                    )}

                </div>


                <div class="notification-date">

                    ${formatDate(
                        notification.createdAt
                    )}

                </div>


                <div class="notification-type">

                    ${escapeHtml(
                        notification.type ||
                        "GENERAL"
                    )}

                </div>


                ${
                    !read
                    ?
                    `
                    <button
                        type="button"
                        style="
                            margin-top:12px;
                            background:#176b3a;
                            color:white;
                            border:none;
                            padding:8px 14px;
                            border-radius:5px;
                            cursor:pointer;
                        "
                        onclick="markAsRead(${notification.id})">

                        Mark as Read

                    </button>
                    `
                    :
                    ""
                }

            `;


            list.appendChild(
                item
            );
        }
    );
}


// ==========================================
// MARK ONE READ
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
            "Notification marked as read."
        );


        await loadNotifications();


    } catch (error) {

        console.error(
            "Mark Read Error:",
            error
        );


        showMessage(
            "Unable to update notification."
        );
    }
}


// ==========================================
// MARK ALL READ
// ==========================================

async function markAllAsRead() {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );

    const farmerId =
        localStorage.getItem(
            "milkMateFarmerId"
        );


    if (
        !token ||
        !farmerId
    ) {

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
                "Unable to mark all notifications as read."
            );
        }


        showMessage(
            "All notifications marked as read."
        );


        await loadNotifications();


    } catch (error) {

        console.error(
            "Mark All Read Error:",
            error
        );


        showMessage(
            "Unable to update notifications."
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
            dateStyle: "medium",
            timeStyle: "short"
        }
    );
}


// ==========================================
// MESSAGE
// ==========================================

function showMessage(
    message
) {

    const element =
        document.getElementById(
            "message"
        );


    if (element) {

        element.textContent =
            message;
    }
}


// ==========================================
// SECURITY
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