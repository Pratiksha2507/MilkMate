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


        loadMonthlyRecords();
    }
);


// ==========================================
// LOAD RECORDS
// ==========================================

async function loadMonthlyRecords() {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );


    try {

        const response =
            await fetch(
                "/api/farmer/milk-collections/my",
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


        if (!response.ok) {

            throw new Error(
                "Unable to load records."
            );
        }


        const records =
            await response.json();


        displayRecords(records);


    } catch (error) {

        console.error(
            "Monthly Records Error:",
            error
        );


        document.getElementById(
            "message"
        ).textContent =
            "Unable to load monthly records.";
    }
}


// ==========================================
// DISPLAY
// ==========================================

function displayRecords(records) {

    const body =
        document.getElementById(
            "recordsBody"
        );


    if (!body) {
        return;
    }


    body.innerHTML =
        "";


    let totalMilk = 0;
    let totalAmount = 0;


    if (
        !records ||
        records.length === 0
    ) {

        body.innerHTML = `
            <tr>
                <td colspan="7">
                    No records found.
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


            totalMilk +=
                quantity;

            totalAmount +=
                amount;


            const row =
                document.createElement(
                    "tr"
                );


            row.innerHTML = `

                <td>
                    ${escapeHtml(
                        record.collectionDate || "-"
                    )}
                </td>

                <td>
                    ${escapeHtml(
                        record.session || "-"
                    )}
                </td>

                <td>
                    ${formatNumber(
                        quantity
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


            body.appendChild(
                row
            );
        }
    );


    updateSummary(
        totalMilk,
        totalAmount
    );
}


// ==========================================
// SUMMARY
// ==========================================

function updateSummary(
    totalMilk,
    totalAmount
) {

    document.getElementById(
        "totalMilk"
    ).textContent =
        formatNumber(totalMilk) +
        " L";


    document.getElementById(
        "totalAmount"
    ).textContent =
        "₹" +
        formatAmount(totalAmount);


    const paid =
        Number(
            localStorage.getItem(
                "milkMateTotalPaid"
            ) || 0
        );


    const pending =
        Math.max(
            totalAmount - paid,
            0
        );


    document.getElementById(
        "totalPaid"
    ).textContent =
        "₹" +
        formatAmount(paid);


    document.getElementById(
        "pendingAmount"
    ).textContent =
        "₹" +
        formatAmount(pending);
}


// ==========================================
// FORMAT
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


function escapeHtml(
    value
) {

    return String(
        value ?? ""
    )
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
        "/farmer-login.html";
}