let allAccounts = [];


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
            role !== "ADMIN"
        ) {

            window.location.href =
                "/admin-login.html";

            return;
        }

        loadAdminName();

        loadFarmers();

        loadAccounts();

        setDefaultMonthAndYear();
    }
);


function loadAdminName() {

    const adminName =
        document.getElementById(
            "adminName"
        );

    if (!adminName) {
        return;
    }

    adminName.textContent =
        "Administrator";

    const userData =
        localStorage.getItem(
            "milkMateUser"
        );

    if (!userData) {
        return;
    }

    try {

        const user =
            JSON.parse(userData);

        if (user.fullName) {
            adminName.textContent =
                user.fullName;
        }

    } catch (error) {

        console.error(
            "Admin data error:",
            error
        );
    }
}


function getAdminToken() {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );

    if (!token) {

        showMessage(
            "Admin login required.",
            "error"
        );

        return null;
    }

    return token;
}


function setDefaultMonthAndYear() {

    const monthSelect =
        document.getElementById(
            "month"
        );

    const yearInput =
        document.getElementById(
            "year"
        );

    const today =
        new Date();

    if (
        monthSelect &&
        !monthSelect.value
    ) {

        monthSelect.value =
            String(
                today.getMonth() + 1
            );
    }

    if (
        yearInput &&
        !yearInput.value
    ) {

        yearInput.value =
            today.getFullYear();
    }
}


async function loadFarmers() {

    const farmerSelect =
        document.getElementById(
            "farmerId"
        );

    if (!farmerSelect) {
        return;
    }

    const token =
        getAdminToken();

    if (!token) {
        return;
    }

    farmerSelect.innerHTML =
        '<option value="">Loading farmers...</option>';

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

        if (response.status === 403) {

            farmerSelect.innerHTML =
                '<option value="">Admin access required</option>';

            return;
        }

        if (!response.ok) {

            farmerSelect.innerHTML =
                '<option value="">Unable to load farmers</option>';

            return;
        }

        const farmers =
            await response.json();

        farmerSelect.innerHTML =
            '<option value="">Select Farmer</option>';

        if (
            !farmers ||
            farmers.length === 0
        ) {

            farmerSelect.innerHTML =
                '<option value="">No farmers found</option>';

            return;
        }

        farmers.forEach(
            function (farmer) {

                const option =
                    document.createElement(
                        "option"
                    );

                option.value =
                    farmer.id;

                option.textContent =
                    (
                        farmer.farmerCode ||
                        "-"
                    ) +
                    " - " +
                    (
                        farmer.fullName ||
                        farmer.name ||
                        "Farmer"
                    );

                farmerSelect.appendChild(
                    option
                );
            }
        );

    } catch (error) {

        console.error(
            "Load Farmers Error:",
            error
        );

        farmerSelect.innerHTML =
            '<option value="">Error loading farmers</option>';
    }
}


async function loadAccounts() {

    const tableBody =
        document.getElementById(
            "accountTableBody"
        );

    if (!tableBody) {
        return;
    }

    const token =
        getAdminToken();

    if (!token) {

        tableBody.innerHTML =
            '<tr><td colspan="9">Admin login required.</td></tr>';

        return;
    }

    tableBody.innerHTML =
        '<tr><td colspan="9">Loading monthly accounts...</td></tr>';

    try {

        const response =
            await fetch(
                "/api/admin/monthly-accounts",
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

            tableBody.innerHTML =
                '<tr><td colspan="9">Admin access required.</td></tr>';

            return;
        }

        if (!response.ok) {

            throw new Error(
                "Failed to load monthly accounts."
            );
        }

        allAccounts =
            await response.json();

        displayAccounts(
            allAccounts
        );

    } catch (error) {

        console.error(
            "Load Accounts Error:",
            error
        );

        tableBody.innerHTML =
            '<tr><td colspan="9">Unable to load monthly accounts.</td></tr>';
    }
}


function displayAccounts(accounts) {

    const tableBody =
        document.getElementById(
            "accountTableBody"
        );

    if (!tableBody) {
        return;
    }

    tableBody.innerHTML = "";

    if (
        !accounts ||
        accounts.length === 0
    ) {

        tableBody.innerHTML =
            '<tr><td colspan="9">No monthly accounts found.</td></tr>';

        updateSummary([]);

        return;
    }

    accounts
        .slice()
        .sort(
            function (a, b) {

                const yearDiff =
                    Number(
                        b.accountYear || 0
                    ) -
                    Number(
                        a.accountYear || 0
                    );

                if (yearDiff !== 0) {
                    return yearDiff;
                }

                return (
                    Number(
                        b.accountMonth || 0
                    ) -
                    Number(
                        a.accountMonth || 0
                    )
                );
            }
        )
        .forEach(
            function (account) {

                const row =
                    document.createElement(
                        "tr"
                    );

                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                            account.farmerCode || "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            account.farmerName || "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            getMonthName(
                                account.accountMonth
                            )
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            account.accountYear || "-"
                        )}
                    </td>

                    <td>
                        ${formatNumber(
                            account.totalMilk
                        )} L
                    </td>

                    <td>
                        ₹${formatAmount(
                            account.totalAmount
                        )}
                    </td>

                    <td>
                        ₹${formatAmount(
                            account.paidAmount
                        )}
                    </td>

                    <td>
                        ₹${formatAmount(
                            account.balanceAmount
                        )}
                    </td>

                    <td>

                        <span class="status ${getStatusClass(
                            account.status
                        )}">
                            ${escapeHtml(
                                account.status || "-"
                            )}
                        </span>

                    </td>
                `;

                tableBody.appendChild(
                    row
                );
            }
        );

    updateSummary(
        accounts
    );
}


function updateSummary(accounts) {

    let totalMilk = 0;
    let totalAmount = 0;
    let paidAmount = 0;
    let balanceAmount = 0;

    accounts.forEach(
        function (account) {

            totalMilk +=
                Number(
                    account.totalMilk || 0
                );

            totalAmount +=
                Number(
                    account.totalAmount || 0
                );

            paidAmount +=
                Number(
                    account.paidAmount || 0
                );

            balanceAmount +=
                Number(
                    account.balanceAmount || 0
                );
        }
    );

    setText(
        "totalMilk",
        formatNumber(totalMilk) + " L"
    );

    setText(
        "totalAmount",
        "₹" + formatAmount(totalAmount)
    );

    setText(
        "paidAmount",
        "₹" + formatAmount(paidAmount)
    );

    setText(
        "balanceAmount",
        "₹" + formatAmount(balanceAmount)
    );
}


async function generateAccount() {

    const farmerSelect =
        document.getElementById(
            "farmerId"
        );

    const month =
        document.getElementById(
            "month"
        );

    const year =
        document.getElementById(
            "year"
        );

    if (
        !farmerSelect ||
        !month ||
        !year
    ) {
        return;
    }

    const farmerId =
        farmerSelect.value;

    const selectedMonth =
        month.value;

    const selectedYear =
        year.value;

    if (!farmerId) {

        showMessage(
            "Please select a farmer.",
            "error"
        );

        return;
    }

    if (
        !selectedMonth ||
        Number(selectedMonth) < 1 ||
        Number(selectedMonth) > 12
    ) {

        showMessage(
            "Please select a valid month.",
            "error"
        );

        return;
    }

    if (
        !selectedYear ||
        Number(selectedYear) < 2000
    ) {

        showMessage(
            "Please enter a valid year.",
            "error"
        );

        return;
    }

    const token =
        getAdminToken();

    if (!token) {
        return;
    }

    try {

        showMessage(
            "Generating monthly account...",
            "success"
        );

        const url =
            "/api/admin/monthly-accounts/farmer/" +
            farmerId +
            "/generate?month=" +
            encodeURIComponent(
                selectedMonth
            ) +
            "&year=" +
            encodeURIComponent(
                selectedYear
            );

        const response =
            await fetch(
                url,
                {
                    method: "POST",

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

            showMessage(
                data.message ||
                "Unable to generate monthly account.",
                "error"
            );

            return;
        }

        showMessage(
            "Monthly account generated successfully.",
            "success"
        );

        await loadAccounts();

    } catch (error) {

        console.error(
            "Generate Account Error:",
            error
        );

        showMessage(
            "Server error while generating account.",
            "error"
        );
    }
}


function setText(id, value) {

    const element =
        document.getElementById(id);

    if (element) {
        element.textContent = value;
    }
}


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
            type || "success"
        );
}


function getMonthName(month) {

    const months = [
        "",
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    ];

    return (
        months[
            Number(month)
        ] || "-"
    );
}


function getStatusClass(status) {

    if (!status) {
        return "";
    }

    return String(status)
        .toLowerCase();
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