document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("milkMateToken");
    const role = localStorage.getItem("milkMateRole");

    if (!token || role !== "ADMIN") {
        window.location.href = "/admin-login.html";
        return;
    }

    loadAdminName();
    setDefaultDate();
    loadFarmers();
    loadPayments();

    const form = document.getElementById("paymentForm");

    if (form) {
        form.addEventListener("submit", savePayment);
    }
});


// ==========================================
// ADMIN NAME
// ==========================================

function loadAdminName() {

    const element = document.getElementById("adminName");

    if (!element) {
        return;
    }

    const userData =
        localStorage.getItem("milkMateUser");

    if (!userData) {
        element.textContent = "Admin";
        return;
    }

    try {

        const user = JSON.parse(userData);

        element.textContent =
            user.fullName || "Admin";

    } catch (error) {

        element.textContent = "Admin";
    }
}


// ==========================================
// DEFAULT DATE
// ==========================================

function setDefaultDate() {

    const dateInput =
        document.getElementById("paymentDate");

    if (dateInput) {
        dateInput.value = getTodayDate();
    }
}


function getTodayDate() {

    const today = new Date();

    const year =
        today.getFullYear();

    const month =
        String(
            today.getMonth() + 1
        ).padStart(2, "0");

    const day =
        String(
            today.getDate()
        ).padStart(2, "0");

    return `${year}-${month}-${day}`;
}


// ==========================================
// LOAD FARMERS
// ==========================================

async function loadFarmers() {

    const select =
        document.getElementById("farmerId");

    if (!select) {
        return;
    }

    const token =
        localStorage.getItem("milkMateToken");

    try {

        const response =
            await fetch(
                "/api/admin/farmers",
                {
                    method: "GET",
                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );

        if (response.status === 401) {
            logout();
            return;
        }

        if (!response.ok) {
            throw new Error("Unable to load farmers.");
        }

        const farmers =
            await response.json();

        select.innerHTML =
            `<option value="">Select Farmer</option>`;

        farmers.forEach(function (farmer) {

            if (
                farmer.active === false
            ) {
                return;
            }

            const option =
                document.createElement("option");

            option.value =
                farmer.id;

            option.textContent =
                `${farmer.farmerCode || "-"} - ${farmer.fullName || "Farmer"}`;

            select.appendChild(option);
        });

    } catch (error) {

        console.error(
            "Load Farmers Error:",
            error
        );

        select.innerHTML =
            `<option value="">Unable to load farmers</option>`;
    }
}


// ==========================================
// SAVE PAYMENT
// ==========================================

async function savePayment(event) {

    event.preventDefault();

    const token =
        localStorage.getItem("milkMateToken");

    const message =
        document.getElementById(
            "paymentMessage"
        );

    const farmerId =
        document.getElementById(
            "farmerId"
        )?.value;

    const paymentDate =
        document.getElementById(
            "paymentDate"
        )?.value;

    const amount =
        document.getElementById(
            "amount"
        )?.value;

    const paymentMode =
        document.getElementById(
            "paymentMethod"
        )?.value;

    if (
        !farmerId ||
        !paymentDate ||
        !amount ||
        !paymentMode
    ) {

        showMessage(
            "Please fill all required fields.",
            "error"
        );

        return;
    }

    const numericAmount =
        Number(amount);

    if (
        !Number.isFinite(numericAmount) ||
        numericAmount <= 0
    ) {

        showMessage(
            "Payment amount must be greater than zero.",
            "error"
        );

        return;
    }

    const validModes = [
        "CASH",
        "BANK_TRANSFER",
        "UPI"
    ];

    if (!validModes.includes(paymentMode)) {

        showMessage(
            "Invalid payment mode.",
            "error"
        );

        return;
    }

    try {

        showMessage(
            "Saving payment...",
            "info"
        );

        const response =
            await fetch(
                `/api/admin/payments/farmer/${farmerId}`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json",

                        "Authorization":
                            "Bearer " + token
                    },

                    body:
                        JSON.stringify({

                            paymentDate:
                                paymentDate,

                            amount:
                                numericAmount,

                            paymentMode:
                                paymentMode
                        })
                }
            );

        if (response.status === 401) {
            logout();
            return;
        }

        const text =
            await response.text();

        let data = {};

        try {
            data =
                text ? JSON.parse(text) : {};
        } catch (error) {
            data = {};
        }

        if (!response.ok) {

            showMessage(
                data.message ||
                "Failed to save payment.",
                "error"
            );

            return;
        }

        showMessage(
            "Payment saved successfully!",
            "success"
        );

        const form =
            document.getElementById(
                "paymentForm"
            );

        if (form) {
            form.reset();
        }

        setDefaultDate();

        await loadPayments();

    } catch (error) {

        console.error(
            "Save Payment Error:",
            error
        );

        showMessage(
            "Server connection failed.",
            "error"
        );
    }
}


// ==========================================
// LOAD PAYMENTS
// ==========================================

async function loadPayments() {

    const tableBody =
        document.getElementById(
            "paymentTableBody"
        );

    if (!tableBody) {
        return;
    }

    const token =
        localStorage.getItem(
            "milkMateToken"
        );

    tableBody.innerHTML = `
        <tr>
            <td colspan="6">
                Loading payments...
            </td>
        </tr>
    `;

    try {

        const response =
            await fetch(
                "/api/admin/payments",
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );

        if (response.status === 401) {
            logout();
            return;
        }

        if (!response.ok) {
            throw new Error(
                "Unable to load payments."
            );
        }

        const payments =
            await response.json();

        tableBody.innerHTML = "";

        if (
            !payments ||
            payments.length === 0
        ) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="6">
                        No payment records found.
                    </td>
                </tr>
            `;

            return;
        }

        payments
            .slice()
            .sort(
                function (a, b) {

                    return String(
                        b.paymentDate || ""
                    ).localeCompare(
                        String(
                            a.paymentDate || ""
                        )
                    );
                }
            )
            .forEach(
                function (payment) {

                    const row =
                        document.createElement(
                            "tr"
                        );

                    const farmerName =
                        payment.farmer?.fullName ||
                        payment.farmerName ||
                        payment.farmer?.farmerCode ||
                        "-";

                    row.innerHTML = `
                        <td>
                            ${escapeHtml(
                                payment.id ?? "-"
                            )}
                        </td>

                        <td>
                            ${escapeHtml(
                                farmerName
                            )}
                        </td>

                        <td>
                            ${escapeHtml(
                                payment.paymentDate || "-"
                            )}
                        </td>

                        <td>
                            ₹${formatAmount(
                                payment.amount
                            )}
                        </td>

                        <td>
                            ${escapeHtml(
                                getPaymentModeText(
                                    payment.paymentMode
                                )
                            )}
                        </td>

                        <td>

                            <button
                                type="button"
                                class="action-btn delete-btn"
                                onclick="deletePayment(${payment.id})">
                                Delete
                            </button>

                        </td>
                    `;

                    tableBody.appendChild(
                        row
                    );
                }
            );

    } catch (error) {

        console.error(
            "Load Payments Error:",
            error
        );

        tableBody.innerHTML = `
            <tr>
                <td colspan="6">
                    Unable to load payments.
                </td>
            </tr>
        `;
    }
}


// ==========================================
// PAYMENT MODE
// ==========================================

function getPaymentModeText(mode) {

    if (mode === "CASH") {
        return "Cash";
    }

    if (mode === "BANK_TRANSFER") {
        return "Bank Transfer";
    }

    if (mode === "UPI") {
        return "UPI";
    }

    return mode || "-";
}


// ==========================================
// DELETE PAYMENT
// ==========================================

async function deletePayment(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this payment?"
        );

    if (!confirmed) {
        return;
    }

    const token =
        localStorage.getItem(
            "milkMateToken"
        );

    try {

        const response =
            await fetch(
                `/api/admin/payments/${id}`,
                {
                    method: "DELETE",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );

        if (response.status === 401) {
            logout();
            return;
        }

        if (!response.ok) {

            const text =
                await response.text();

            let data = {};

            try {
                data =
                    text ? JSON.parse(text) : {};
            } catch (error) {
                data = {};
            }

            alert(
                data.message ||
                "Unable to delete payment."
            );

            return;
        }

        alert(
            "Payment deleted successfully."
        );

        loadPayments();

    } catch (error) {

        console.error(
            "Delete Payment Error:",
            error
        );

        alert(
            "Server connection failed."
        );
    }
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
            "paymentMessage"
        );

    if (!element) {
        return;
    }

    element.textContent =
        message;

    if (type === "success") {

        element.style.color =
            "#16803c";

    } else if (type === "error") {

        element.style.color =
            "#d92d20";

    } else {

        element.style.color =
            "#667085";
    }
}


// ==========================================
// FORMAT
// ==========================================

function formatAmount(value) {

    return Number(value || 0)
        .toLocaleString(
            "en-IN",
            {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            }
        );
}


function escapeHtml(value) {

    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
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
        "/admin-login.html";
}