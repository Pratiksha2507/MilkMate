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

        setDefaultDates();

        loadFarmers();
    }
);


// ==========================================
// TOKEN
// ==========================================

function getToken() {

    return localStorage.getItem(
        "milkMateToken"
    );
}


// ==========================================
// DEFAULT DATES
// ==========================================

function setDefaultDates() {

    const today =
        new Date();

    const endDate =
        formatDate(today);

    const firstDay =
        new Date(
            today.getFullYear(),
            today.getMonth(),
            1
        );

    const startDate =
        formatDate(firstDay);

    const startInput =
        document.getElementById(
            "startDate"
        );

    const endInput =
        document.getElementById(
            "endDate"
        );

    if (startInput) {
        startInput.value =
            startDate;
    }

    if (endInput) {
        endInput.value =
            endDate;
    }
}


function formatDate(date) {

    const year =
        date.getFullYear();

    const month =
        String(
            date.getMonth() + 1
        ).padStart(2, "0");

    const day =
        String(
            date.getDate()
        ).padStart(2, "0");

    return `${year}-${month}-${day}`;
}


// ==========================================
// LOAD FARMERS
// ==========================================

async function loadFarmers() {

    const select =
        document.getElementById(
            "farmerId"
        );

    if (!select) {
        return;
    }

    const token =
        getToken();

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

            showMessage(
                "Admin access required.",
                "error"
            );

            return;
        }

        if (!response.ok) {

            throw new Error(
                "Unable to load farmers."
            );
        }

        const farmers =
            await response.json();

        select.innerHTML =
            `
            <option value="">
                Select Farmer
            </option>
            `;

        farmers.forEach(
            function (farmer) {

                const option =
                    document.createElement(
                        "option"
                    );

                option.value =
                    farmer.id;

                option.textContent =
                    `${farmer.farmerCode || "-"} - ${farmer.fullName || "Farmer"}`;

                select.appendChild(
                    option
                );
            }
        );

    } catch (error) {

        console.error(
            "Load Farmers Error:",
            error
        );

        showMessage(
            "Unable to load farmers.",
            "error"
        );
    }
}


// ==========================================
// GENERATE FARMER REPORT
// ==========================================

async function generateReport() {

    const farmerId =
        document.getElementById(
            "farmerId"
        )?.value;

    const startDate =
        document.getElementById(
            "startDate"
        )?.value;

    const endDate =
        document.getElementById(
            "endDate"
        )?.value;

    if (!farmerId) {

        showMessage(
            "Please select a farmer.",
            "error"
        );

        return;
    }

    if (
        !startDate ||
        !endDate
    ) {

        showMessage(
            "Please select start date and end date.",
            "error"
        );

        return;
    }

    if (startDate > endDate) {

        showMessage(
            "Start date cannot be after end date.",
            "error"
        );

        return;
    }

    const token =
        getToken();

    try {

        showMessage(
            "Generating report...",
            "info"
        );

        // IMPORTANT:
        // ReportController uses /farmer-wise

        const url =
            "/api/admin/reports/farmer-wise" +
            "?farmerId=" +
            encodeURIComponent(
                farmerId
            ) +
            "&startDate=" +
            encodeURIComponent(
                startDate
            ) +
            "&endDate=" +
            encodeURIComponent(
                endDate
            );

        const response =
            await fetch(
                url,
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

            showMessage(
                "Admin access required.",
                "error"
            );

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

            console.error(
                "Report API Error:",
                text
            );

            showMessage(
                data.message ||
                "Unable to generate report.",
                "error"
            );

            return;
        }

        displayReport(data);

        showMessage(
            "Report generated successfully.",
            "success"
        );

    } catch (error) {

        console.error(
            "Generate Report Error:",
            error
        );

        showMessage(
            "Server connection failed.",
            "error"
        );
    }
}


// ==========================================
// DISPLAY REPORT
// ==========================================

function displayReport(report) {

    const result =
        document.getElementById(
            "reportResult"
        );

    if (result) {
        result.classList.remove("hidden");
        result.style.display = "";
    }

    const farmerInfo =
        document.getElementById(
            "farmerInfo"
        );

    if (farmerInfo) {

        farmerInfo.innerHTML =
            `
            <strong>Farmer Code:</strong>
            ${escapeHtml(
                report.farmerCode || "-"
            )}

            <br>

            <strong>Farmer Name:</strong>
            ${escapeHtml(
                report.farmerName || "-"
            )}

            <br>

            <strong>Period:</strong>
            ${escapeHtml(
                report.startDate || "-"
            )}
            to
            ${escapeHtml(
                report.endDate || "-"
            )}
            `;
    }

    setText(
        "totalMilk",
        formatNumber(
            report.totalMilk
        ) + " L"
    );

    setText(
        "totalAmount",
        "₹" +
        formatAmount(
            report.totalAmount
        )
    );

    setText(
        "morningMilk",
        formatNumber(
            report.morningMilk
        ) + " L"
    );

    setText(
        "eveningMilk",
        formatNumber(
            report.eveningMilk
        ) + " L"
    );

    setText(
        "totalCollections",
        String(
            report.totalCollections || 0
        )
    );
}


// ==========================================
// SET TEXT
// ==========================================

function setText(
    id,
    value
) {

    const element =
        document.getElementById(id);

    if (element) {
        element.textContent =
            value;
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
            "message"
        );

    if (!element) {
        return;
    }

    element.textContent =
        message;

    if (type === "error") {

        element.style.color =
            "#d92d20";

    } else if (type === "success") {

        element.style.color =
            "#16803c";

    } else {

        element.style.color =
            "#667085";
    }
}


// ==========================================
// FORMAT
// ==========================================

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
        "/admin-login.html";
}