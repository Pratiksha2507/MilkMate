// =====================================================
// MILKMATE - FARMER DASHBOARD
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("milkMateToken");
    const role = localStorage.getItem("milkMateRole");

    // Farmer login check
    if (!token || role !== "FARMER") {
        window.location.href = "/farmer-login.html";
        return;
    }

    loadDashboard();
    loadRecentMilk();

    const logoutBtn = document.getElementById("logoutBtn");

    if (logoutBtn) {
        logoutBtn.addEventListener("click", logout);
    }
});


// =====================================================
// LOAD FARMER DASHBOARD
// =====================================================

async function loadDashboard() {

    const token = localStorage.getItem("milkMateToken");

    try {

        const response = await fetch(
            "/api/farmer/dashboard/my",
            {
                method: "GET",

                headers: {
                    "Authorization": "Bearer " + token,
                    "Content-Type": "application/json"
                }
            }
        );

        // Login expired / unauthorized
        if (response.status === 401 || response.status === 403) {

            console.error("Farmer is not authorized.");

            logout();
            return;
        }

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.message || "Unable to load farmer dashboard."
            );
        }


        // Farmer name
        const farmerName =
            document.getElementById("farmerName");

        if (farmerName) {

            farmerName.textContent =
                data.farmerName || "Farmer";
        }


        // Dashboard cards
        setText(
            "todayMilk",
            formatNumber(data.todayMilk) + " L"
        );

        setText(
            "todayAmount",
            "₹" + formatNumber(data.todayAmount)
        );

        setText(
            "monthMilk",
            formatNumber(data.monthlyMilk) + " L"
        );

        setText(
            "monthAmount",
            "₹" + formatNumber(data.monthlyAmount)
        );

        setText(
            "totalPaid",
            "₹" + formatNumber(data.totalPaid)
        );

        setText(
            "pendingAmount",
            "₹" + formatNumber(data.pendingAmount)
        );


        // Monthly summary
        setText(
            "summaryMonthlyMilk",
            formatNumber(data.monthlyMilk) + " L"
        );

        setText(
            "summaryMonthlyAmount",
            "₹" + formatNumber(data.monthlyAmount)
        );

        setText(
            "summaryTotalPaid",
            "₹" + formatNumber(data.totalPaid)
        );

        setText(
            "summaryPending",
            "₹" + formatNumber(data.pendingAmount)
        );


    } catch (error) {

        console.error(
            "Farmer Dashboard Error:",
            error
        );

        showMessage(
            "Unable to load dashboard. Please login again."
        );
    }
}


// =====================================================
// LOAD RECENT MILK COLLECTION
// =====================================================

async function loadRecentMilk() {

    const token =
        localStorage.getItem("milkMateToken");

    try {

        const response = await fetch(
            "/api/farmer/milk-collections/my",
            {
                method: "GET",

                headers: {
                    "Authorization": "Bearer " + token,
                    "Content-Type": "application/json"
                }
            }
        );


        // Unauthorized
        if (
            response.status === 401 ||
            response.status === 403
        ) {

            logout();
            return;
        }


        const records = await response.json();


        if (!response.ok) {

            throw new Error(
                records.message ||
                "Unable to load milk records."
            );
        }


        const tableBody =
            document.getElementById(
                "recentMilkTableBody"
            );


        if (!tableBody) {
            return;
        }


        tableBody.innerHTML = "";


        // No records
        if (
            !records ||
            records.length === 0
        ) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="6">
                        No milk collection records yet.
                    </td>
                </tr>
            `;

            return;
        }


        // Show latest 5 records
        records
            .slice(0, 5)
            .forEach(function (record) {

                const row =
                    document.createElement("tr");


                row.innerHTML = `
                    <td>
                        ${record.collectionDate || "-"}
                    </td>

                    <td>
                        ${record.session || "-"}
                    </td>

                    <td>
                        ${formatNumber(record.quantity)} L
                    </td>

                    <td>
                        ${record.fat ?? "-"}
                    </td>

                    <td>
                        ₹${formatNumber(record.rate)}
                    </td>

                    <td>
                        ₹${formatNumber(record.totalAmount)}
                    </td>
                `;


                tableBody.appendChild(row);
            });


    } catch (error) {

        console.error(
            "Recent Milk Error:",
            error
        );
    }
}


// =====================================================
// SET TEXT
// =====================================================

function setText(id, value) {

    const element =
        document.getElementById(id);

    if (element) {

        element.textContent = value;
    }
}


// =====================================================
// FORMAT NUMBER
// =====================================================

function formatNumber(value) {

    const number =
        Number(value || 0);

    return number.toLocaleString(
        "en-IN",
        {
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }
    );
}


// =====================================================
// SHOW MESSAGE
// =====================================================

function showMessage(message) {

    const element =
        document.getElementById(
            "dashboardMessage"
        );

    if (element) {

        element.textContent = message;
    }
}


// =====================================================
// LOGOUT
// =====================================================

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
        "milkMateUser"
    );


    window.location.href =
        "/farmer-login.html";
}