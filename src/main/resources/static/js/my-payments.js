document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("milkMateToken");
    const role = localStorage.getItem("milkMateRole");

    if (!token || role !== "FARMER") {
        window.location.href = "/farmer-login.html";
        return;
    }

    loadPayments();
});


async function loadPayments() {

    const token =
        localStorage.getItem("milkMateToken");

    try {

        const response = await fetch(
            "/api/farmer/payments",
            {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (response.status === 401) {
            logout();
            return;
        }

        if (!response.ok) {
            throw new Error(
                "Failed to load payment records."
            );
        }

        const payments =
            await response.json();

        displayPayments(payments);

    } catch (error) {

        console.error(
            "Payment loading error:",
            error
        );

        showMessage(
            "Unable to load payment records."
        );
    }
}


function displayPayments(payments) {

    const tableBody =
        document.getElementById(
            "paymentTableBody"
        );

    if (!tableBody) {
        return;
    }

    tableBody.innerHTML = "";

    let totalPaid = 0;


    if (!payments || payments.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="5">
                    No payment records found.
                </td>
            </tr>
        `;

        updateTotal(0);

        return;
    }


    payments.forEach(function (payment) {

        const amount =
            Number(payment.amount || 0);

        totalPaid += amount;


        const row =
            document.createElement("tr");


        row.innerHTML = `
            <td>
                ${escapeHtml(
                    payment.paymentDate || "-"
                )}
            </td>

            <td>
                ₹${formatNumber(amount)}
            </td>

            <td>
                ${escapeHtml(
                    payment.paymentMode || "-"
                )}
            </td>

            <td>
                ${escapeHtml(
                    payment.referenceNumber || "-"
                )}
            </td>

            <td>
                ${escapeHtml(
                    payment.notes || "-"
                )}
            </td>
        `;


        tableBody.appendChild(row);
    });


    updateTotal(totalPaid);
}


function updateTotal(amount) {

    const totalElement =
        document.getElementById(
            "totalPaid"
        );

    if (totalElement) {

        totalElement.textContent =
            "₹" +
            formatNumber(amount);
    }
}


function formatNumber(value) {

    return Number(value || 0)
        .toLocaleString(
            "en-IN",
            {
                minimumFractionDigits: 0,
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


function showMessage(message) {

    const element =
        document.getElementById("message");

    if (element) {

        element.textContent =
            message;
    }
}


function logout() {

    localStorage.removeItem("milkMateToken");
    localStorage.removeItem("milkMateRole");
    localStorage.removeItem("milkMateUserId");
    localStorage.removeItem("milkMateFarmerId");
    localStorage.removeItem("milkMateFullName");
    localStorage.removeItem("milkMateMobile");
    localStorage.removeItem("milkMateEmail");
    localStorage.removeItem("milkMateUser");

    window.location.href =
        "/farmer-login.html";
}