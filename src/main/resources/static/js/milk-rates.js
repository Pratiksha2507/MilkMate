document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("milkMateToken");
    const role = localStorage.getItem("milkMateRole");
    const userData = localStorage.getItem("milkMateUser");

    if (!token || role !== "ADMIN") {
        window.location.href = "/admin-login.html";
        return;
    }

    loadAdminName(userData);

    const effectiveFrom =
        document.getElementById("effectiveFrom");

    if (effectiveFrom) {
        effectiveFrom.value = getTodayDate();
    }

    const rateForm =
        document.getElementById("rateForm");

    if (rateForm) {
        rateForm.addEventListener(
            "submit",
            addRate
        );
    }

    loadRates();
});


function loadAdminName(userData) {

    const adminName =
        document.getElementById("adminName");

    if (!adminName) {
        return;
    }

    adminName.textContent = "Administrator";

    if (!userData) {
        return;
    }

    try {

        const user = JSON.parse(userData);

        if (user.fullName) {
            adminName.textContent =
                user.fullName;
        }

    } catch (error) {

        console.error(
            "Admin user data error:",
            error
        );
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


async function loadRates() {

    const token =
        localStorage.getItem("milkMateToken");

    const tableBody =
        document.getElementById(
            "rateTableBody"
        );

    if (!tableBody) {
        return;
    }

    tableBody.innerHTML = `
        <tr>
            <td colspan="6">
                Loading milk rates...
            </td>
        </tr>
    `;

    try {

        const response =
            await fetch(
                "/api/admin/milk-rates",
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

        if (response.status === 403) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="6">
                        Admin access required.
                    </td>
                </tr>
            `;

            return;
        }

        if (!response.ok) {
            throw new Error(
                "Failed to load milk rates."
            );
        }

        const rates =
            await response.json();

        tableBody.innerHTML = "";

        if (!rates || rates.length === 0) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="6">
                        No milk rates found.
                    </td>
                </tr>
            `;

            return;
        }

        rates.forEach(function (rate) {

            const row =
                document.createElement("tr");

            row.innerHTML = `
                <td>
                    ${escapeHtml(rate.id)}
                </td>

                <td>
                    ₹${formatAmount(
                        rate.ratePerLiter
                    )}
                </td>

                <td>
                    ${escapeHtml(
                        rate.effectiveFrom || "-"
                    )}
                </td>

                <td>
                    ${escapeHtml(
                        rate.effectiveTo || "-"
                    )}
                </td>

                <td>
                    ${
                        Boolean(rate.active)
                        ? `<span class="status-active">Active</span>`
                        : `<span class="status-inactive">Inactive</span>`
                    }
                </td>

                <td>

                    <button
                        type="button"
                        class="action-btn edit-btn"
                        onclick="editRate(${rate.id})">
                        ✏️ Edit
                    </button>

                    <button
                        type="button"
                        class="action-btn delete-btn"
                        onclick="deleteRate(${rate.id})">
                        🗑️ Delete
                    </button>

                </td>
            `;

            tableBody.appendChild(row);
        });

    } catch (error) {

        console.error(
            "Load Rates Error:",
            error
        );

        tableBody.innerHTML = `
            <tr>
                <td colspan="6">
                    Unable to load milk rates.
                </td>
            </tr>
        `;
    }
}


async function addRate(event) {

    event.preventDefault();

    const token =
        localStorage.getItem("milkMateToken");

    const message =
        document.getElementById(
            "rateMessage"
        );

    const rateInput =
        document.getElementById(
            "ratePerLiter"
        );

    const fromInput =
        document.getElementById(
            "effectiveFrom"
        );

    const toInput =
        document.getElementById(
            "effectiveTo"
        );

    const activeInput =
        document.getElementById(
            "active"
        );

    if (
        !message ||
        !rateInput ||
        !fromInput ||
        !toInput ||
        !activeInput
    ) {
        return;
    }

    const ratePerLiter =
        Number(rateInput.value);

    const effectiveFrom =
        fromInput.value;

    const effectiveTo =
        toInput.value || null;

    const active =
        activeInput.value === "true";

    if (
        !effectiveFrom ||
        !Number.isFinite(ratePerLiter) ||
        ratePerLiter <= 0
    ) {

        message.style.color = "#d92d20";
        message.textContent =
            "Please enter a valid rate and effective date.";

        return;
    }

    if (
        effectiveTo &&
        effectiveTo < effectiveFrom
    ) {

        message.style.color = "#d92d20";
        message.textContent =
            "Effective To cannot be before Effective From.";

        return;
    }

    try {

        message.style.color = "#667085";
        message.textContent =
            "Adding milk rate...";

        const response =
            await fetch(
                "/api/admin/milk-rates",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json",

                        "Authorization":
                            "Bearer " + token
                    },

                    body: JSON.stringify({
                        ratePerLiter:
                            ratePerLiter,

                        effectiveFrom:
                            effectiveFrom,

                        effectiveTo:
                            effectiveTo,

                        active:
                            active
                    })
                }
            );

        if (response.status === 401) {
            logout();
            return;
        }

        const responseText =
            await response.text();

        let data = {};

        try {
            data =
                responseText
                ? JSON.parse(responseText)
                : {};
        } catch (error) {
            data = {};
        }

        if (!response.ok) {

            message.style.color = "#d92d20";

            message.textContent =
                data.message ||
                "Failed to add milk rate.";

            return;
        }

        message.style.color = "#16803c";

        message.textContent =
            "Milk rate added successfully.";

        const form =
            document.getElementById(
                "rateForm"
            );

        if (form) {
            form.reset();
        }

        fromInput.value =
            getTodayDate();

        loadRates();

    } catch (error) {

        console.error(
            "Add Rate Error:",
            error
        );

        message.style.color = "#d92d20";

        message.textContent =
            "Server connection failed.";
    }
}


async function editRate(id) {

    const token =
        localStorage.getItem("milkMateToken");

    try {

        const response =
            await fetch(
                `/api/admin/milk-rates/${id}`,
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

            alert(
                "Unable to load milk rate."
            );

            return;
        }

        const rate =
            await response.json();

        const newRate =
            prompt(
                "Enter new rate per liter:",
                rate.ratePerLiter
            );

        if (
            newRate === null ||
            newRate.trim() === ""
        ) {
            return;
        }

        const numericRate =
            Number(newRate);

        if (
            !Number.isFinite(numericRate) ||
            numericRate <= 0
        ) {

            alert(
                "Please enter a valid rate."
            );

            return;
        }

        const updatedRate = {

            ratePerLiter:
                numericRate,

            effectiveFrom:
                rate.effectiveFrom,

            effectiveTo:
                rate.effectiveTo,

            active:
                rate.active
        };

        const updateResponse =
            await fetch(
                `/api/admin/milk-rates/${id}`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json",

                        "Authorization":
                            "Bearer " + token
                    },

                    body:
                        JSON.stringify(
                            updatedRate
                        )
                }
            );

        if (
            updateResponse.status === 401
        ) {

            logout();
            return;
        }

        const responseText =
            await updateResponse.text();

        let data = {};

        try {
            data =
                responseText
                ? JSON.parse(responseText)
                : {};
        } catch (error) {
            data = {};
        }

        if (!updateResponse.ok) {

            alert(
                data.message ||
                "Failed to update milk rate."
            );

            return;
        }

        alert(
            "Milk rate updated successfully."
        );

        loadRates();

    } catch (error) {

        console.error(
            "Edit Rate Error:",
            error
        );

        alert(
            "Server connection failed."
        );
    }
}


async function deleteRate(id) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this milk rate?"
        );

    if (!confirmed) {
        return;
    }

    const token =
        localStorage.getItem("milkMateToken");

    try {

        const response =
            await fetch(
                `/api/admin/milk-rates/${id}`,
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

            const responseText =
                await response.text();

            let data = {};

            try {
                data =
                    responseText
                    ? JSON.parse(responseText)
                    : {};
            } catch (error) {
                data = {};
            }

            alert(
                data.message ||
                "Failed to delete milk rate."
            );

            return;
        }

        alert(
            "Milk rate deleted successfully."
        );

        loadRates();

    } catch (error) {

        console.error(
            "Delete Rate Error:",
            error
        );

        alert(
            "Server connection failed."
        );
    }
}


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


function logout() {

    localStorage.removeItem(
        "milkMateToken"
    );

    localStorage.removeItem(
        "milkMateRole"
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

    localStorage.removeItem(
        "milkMateUser"
    );

    window.location.href =
        "/admin-login.html";
}