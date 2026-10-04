document.addEventListener("DOMContentLoaded", function () {

    const token =
        localStorage.getItem("milkMateToken");

    const role =
        localStorage.getItem("milkMateRole");

    if (!token || role !== "ADMIN") {
        window.location.href =
            "/admin-login.html";
        return;
    }

    setDefaultDates();

    loadDashboard();
    loadFarmerAnalytics();
    loadDateAnalytics();
    loadRecentCollections();


    // ==========================================
    // DATE ANALYTICS BUTTON
    // ==========================================

    const generateButton =
        document.getElementById(
            "generateDateAnalyticsBtn"
        );

    if (generateButton) {

        generateButton.addEventListener(
            "click",
            loadDateAnalytics
        );
    }


    // ==========================================
    // LOGOUT
    // ==========================================

    const logoutButton =
        document.getElementById(
            "logoutBtn"
        );

    if (logoutButton) {

        logoutButton.addEventListener(
            "click",
            logout
        );
    }
});


// ==========================================
// COMMON API REQUEST
// ==========================================

async function apiRequest(
    url,
    options = {}
) {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );

    const headers = {
        "Content-Type":
            "application/json",

        "Authorization":
            "Bearer " + token,

        ...(options.headers || {})
    };

    const response =
        await fetch(
            url,
            {
                ...options,
                headers: headers
            }
        );

    if (response.status === 401) {

        logout();
        return null;
    }

    if (response.status === 403) {

        throw new Error(
            "Access denied"
        );
    }

    if (!response.ok) {

        throw new Error(
            "API Error: " +
            response.status
        );
    }

    return await response.json();
}


// ==========================================
// ADMIN DASHBOARD
// ==========================================

async function loadDashboard() {

    try {

        const data =
            await apiRequest(
                "/api/admin/dashboard"
            );

        if (!data) {
            return;
        }


        setText(
            "totalFarmers",
            data.totalFarmers ?? 0
        );


        setText(
            "activeFarmers",
            data.activeFarmers ?? 0
        );


        setText(
            "todayMilk",
            formatNumber(
                data.todayMilk
            )
        );


        setText(
            "todayCollectionAmount",
            formatAmount(
                data.todayCollection
            )
        );


        setText(
            "morningMilk",
            formatNumber(
                data.morningMilk
            )
        );


        setText(
            "eveningMilk",
            formatNumber(
                data.eveningMilk
            )
        );


        setText(
            "totalPaid",
            formatAmount(
                data.totalPaid
            )
        );


        setText(
            "pendingAmount",
            formatAmount(
                data.pendingAmount
            )
        );


        setText(
            "analyticsTotalMilk",
            formatNumber(
                data.todayMilk
            )
        );


        setText(
            "analyticsTotalAmount",
            formatAmount(
                data.todayCollection
            )
        );


        setText(
            "analyticsMorningMilk",
            formatNumber(
                data.morningMilk
            )
        );


        setText(
            "analyticsEveningMilk",
            formatNumber(
                data.eveningMilk
            )
        );

    } catch (error) {

        console.error(
            "Dashboard Error:",
            error
        );
    }
}


// ==========================================
// FARMER ANALYTICS
// ==========================================

async function loadFarmerAnalytics() {

    try {

        const data =
            await apiRequest(
                "/api/admin/analytics/farmers"
            );

        if (!data) {
            return;
        }


        const tableBody =
            document.getElementById(
                "farmerAnalyticsBody"
            );

        if (!tableBody) {
            return;
        }


        tableBody.innerHTML = "";


        if (
            !data ||
            data.length === 0
        ) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="7">
                        No farmer analytics available.
                    </td>
                </tr>
            `;

            return;
        }


        data.forEach(
            function (farmer) {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                            farmer.farmerCode
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            farmer.farmerName
                        )}
                    </td>

                    <td>
                        ${formatNumber(
                            farmer.totalMilk
                        )} L
                    </td>

                    <td>
                        ${formatNumber(
                            farmer.morningMilk
                        )} L
                    </td>

                    <td>
                        ${formatNumber(
                            farmer.eveningMilk
                        )} L
                    </td>

                    <td>
                        ₹${formatAmount(
                            farmer.totalAmount
                        )}
                    </td>

                    <td>
                        ${farmer.totalCollections ?? 0}
                    </td>
                `;


                tableBody.appendChild(
                    row
                );
            }
        );

    } catch (error) {

        console.error(
            "Farmer Analytics Error:",
            error
        );
    }
}


// ==========================================
// DATE-WISE ANALYTICS
// ==========================================

async function loadDateAnalytics() {

    const startDate =
        document.getElementById(
            "analyticsStartDate"
        )?.value;

    const endDate =
        document.getElementById(
            "analyticsEndDate"
        )?.value;


    if (
        !startDate ||
        !endDate
    ) {
        return;
    }


    if (startDate > endDate) {

        showDateAnalyticsMessage(
            "Start date cannot be after end date."
        );

        return;
    }


    try {

        const data =
            await apiRequest(
                `/api/admin/analytics/date-range?startDate=${encodeURIComponent(
                    startDate
                )}&endDate=${encodeURIComponent(
                    endDate
                )}`
            );


        if (!data) {
            return;
        }


        setText(
            "dateTotalMilk",
            formatNumber(
                data.totalMilk
            )
        );


        setText(
            "dateTotalAmount",
            formatAmount(
                data.totalAmount
            )
        );


        setText(
            "dateMorningMilk",
            formatNumber(
                data.morningMilk
            )
        );


        setText(
            "dateEveningMilk",
            formatNumber(
                data.eveningMilk
            )
        );


        setText(
            "dateTotalCollections",
            data.totalCollections ?? 0
        );


        setText(
            "analyticsTotalMilk",
            formatNumber(
                data.totalMilk
            )
        );


        setText(
            "analyticsTotalAmount",
            formatAmount(
                data.totalAmount
            )
        );


        setText(
            "analyticsMorningMilk",
            formatNumber(
                data.morningMilk
            )
        );


        setText(
            "analyticsEveningMilk",
            formatNumber(
                data.eveningMilk
            )
        );


        showDateAnalyticsMessage(
            ""
        );


    } catch (error) {

        console.error(
            "Date Analytics Error:",
            error
        );

        showDateAnalyticsMessage(
            "Unable to load date analytics."
        );
    }
}


// ==========================================
// RECENT COLLECTIONS
// ==========================================

async function loadRecentCollections() {

    const tableBody =
        document.getElementById(
            "recentCollectionsBody"
        );

    if (!tableBody) {
        return;
    }


    try {

        const data =
            await apiRequest(
                "/api/admin/milk-collections"
            );


        if (!data) {
            return;
        }


        tableBody.innerHTML = "";


        if (
            !data ||
            data.length === 0
        ) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="8">
                        No recent milk collections.
                    </td>
                </tr>
            `;

            return;
        }


        const recentRecords =
            data
                .slice()
                .sort(
                    function (a, b) {

                        return String(
                            b.collectionDate || ""
                        ).localeCompare(
                            String(
                                a.collectionDate || ""
                            )
                        );
                    }
                )
                .slice(
                    0,
                    10
                );


        recentRecords.forEach(
            function (record) {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                            record.collectionDate
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            record.farmerCode ||
                            "-"
                        )}
                        -
                        ${escapeHtml(
                            record.farmerName ||
                            "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            record.session ||
                            "-"
                        )}
                    </td>

                    <td>
                        ${formatNumber(
                            record.quantity
                        )} L
                    </td>

                    <td>
                        ${formatNumber(
                            record.fat
                        )}
                    </td>

                    <td>
                        ${formatNumber(
                            record.snf
                        )}
                    </td>

                    <td>
                        ₹${formatAmount(
                            record.rate
                        )}
                    </td>

                    <td>
                        ₹${formatAmount(
                            record.totalAmount
                        )}
                    </td>

                `;


                tableBody.appendChild(
                    row
                );
            }
        );

    } catch (error) {

        console.error(
            "Recent Collections Error:",
            error
        );


        tableBody.innerHTML = `
            <tr>
                <td colspan="8">
                    Unable to load recent collections.
                </td>
            </tr>
        `;
    }
}


// ==========================================
// DEFAULT DATES
// ==========================================

function setDefaultDates() {

    const startInput =
        document.getElementById(
            "analyticsStartDate"
        );

    const endInput =
        document.getElementById(
            "analyticsEndDate"
        );


    if (
        !startInput ||
        !endInput
    ) {
        return;
    }


    const today =
        new Date();


    const year =
        today.getFullYear();


    const month =
        String(
            today.getMonth() + 1
        ).padStart(
            2,
            "0"
        );


    const firstDay =
        `${year}-${month}-01`;


    const todayDate =
        `${year}-${month}-${String(
            today.getDate()
        ).padStart(
            2,
            "0"
        )}`;


    startInput.value =
        firstDay;

    endInput.value =
        todayDate;
}


// ==========================================
// DATE MESSAGE
// ==========================================

function showDateAnalyticsMessage(
    message
) {

    const element =
        document.getElementById(
            "dateAnalyticsMessage"
        );

    if (element) {

        element.textContent =
            message;
    }
}


// ==========================================
// SET TEXT
// ==========================================

function setText(
    id,
    value
) {

    const element =
        document.getElementById(
            id
        );

    if (element) {

        element.textContent =
            value ?? 0;
    }
}


// ==========================================
// NUMBER FORMAT
// ==========================================

function formatNumber(
    value
) {

    return Number(
        value || 0
    ).toLocaleString(
        "en-IN",
        {
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }
    );
}


// ==========================================
// CURRENCY FORMAT
// ==========================================

function formatAmount(
    value
) {

    return Number(
        value || 0
    ).toLocaleString(
        "en-IN",
        {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }
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
        "/admin-login.html";
}