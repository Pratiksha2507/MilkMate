// =====================================================
// MILKMATE - MY MILK COLLECTION
// =====================================================

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
            role !== "FARMER" ||
            !farmerId
        ) {

            window.location.href =
                "/farmer-login.html";

            return;
        }

        loadMilkCollections(
            token,
            farmerId
        );
    }
);


// =====================================================
// LOAD FARMER RECORDS
// =====================================================

async function loadMilkCollections(
    token,
    farmerId
) {

    try {

        const response =
            await fetch(
                "/api/farmer/milk-collections/" +
                farmerId +
                "/records",
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token,

                        "Content-Type":
                            "application/json"
                    }
                }
            );

        if (response.status === 401) {

            logout();
            return;
        }

        if (!response.ok) {

            throw new Error(
                "Unable to load milk records."
            );
        }

        const records =
            await response.json();

        displayRecords(records);

    }
    catch (error) {

        console.error(
            "Milk Collection Error:",
            error
        );

        showMessage(
            "Unable to load milk collection records."
        );
    }
}


// =====================================================
// DISPLAY RECORDS
// =====================================================

function displayRecords(records) {

    const tableBody =
        document.getElementById(
            "milkTableBody"
        );

    if (!tableBody) {
        return;
    }

    tableBody.innerHTML = "";

    let totalMilk = 0;
    let totalAmount = 0;

    if (
        !records ||
        records.length === 0
    ) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="7"
                    style="text-align:center;">
                    No milk collection records found.
                </td>
            </tr>
        `;

        updateSummary(
            0,
            0
        );

        return;
    }

    records.forEach(
        function (record) {

            const quantity =
                Number(
                    record.quantity || 0
                );

            const amount =
                Number(
                    record.totalAmount || 0
                );

            totalMilk += quantity;
            totalAmount += amount;

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
                    ${formatNumber(
                        quantity
                    )}
                </td>

                <td>
                    ${record.fat ?? "-"}
                </td>

                <td>
                    ${record.snf ?? "-"}
                </td>

                <td>
                    ₹${formatNumber(
                        record.rate
                    )}
                </td>

                <td>
                    ₹${formatNumber(
                        amount
                    )}
                </td>
            `;

            tableBody.appendChild(row);
        }
    );

    updateSummary(
        totalMilk,
        totalAmount
    );
}


// =====================================================
// SUMMARY
// =====================================================

function updateSummary(
    totalMilk,
    totalAmount
) {

    const milk =
        document.getElementById(
            "totalMilk"
        );

    const amount =
        document.getElementById(
            "totalAmount"
        );

    if (milk) {

        milk.textContent =
            formatNumber(totalMilk) +
            " L";
    }

    if (amount) {

        amount.textContent =
            "₹" +
            formatNumber(totalAmount);
    }
}


// =====================================================
// FORMAT
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
// MESSAGE
// =====================================================

function showMessage(message) {

    const element =
        document.getElementById(
            "message"
        );

    if (element) {

        element.textContent =
            message;

        element.style.color =
            "#d92d20";
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

    window.location.href =
        "/farmer-login.html";
}