document.addEventListener("DOMContentLoaded", function () {

    const token =
        localStorage.getItem("milkMateToken");

    const role =
        localStorage.getItem("milkMateRole");


    // ==========================================
    // ADMIN LOGIN CHECK
    // ==========================================

    if (!token) {

        window.location.href =
            "/admin-login.html";

        return;
    }


    if (role !== "ADMIN") {

        window.location.href =
            "/admin-login.html";

        return;
    }


    // ==========================================
    // ADMIN NAME
    // ==========================================

    const adminName =
        document.getElementById("adminName");

    const fullName =
        localStorage.getItem(
            "milkMateFullName"
        );


    if (adminName) {

        adminName.textContent =
            fullName || "Admin";
    }


    // ==========================================
    // LOAD FARMERS
    // ==========================================

    loadFarmers();


    // ==========================================
    // ADD FORM
    // ==========================================

    const farmerForm =
        document.getElementById(
            "farmerForm"
        );


    if (farmerForm) {

        farmerForm.addEventListener(
            "submit",
            addFarmer
        );
    }


    // ==========================================
    // EDIT FORM
    // ==========================================

    const editForm =
        document.getElementById(
            "editFarmerForm"
        );


    if (editForm) {

        editForm.addEventListener(
            "submit",
            updateFarmer
        );
    }
});


// ==========================================
// LOAD FARMERS
// ==========================================

async function loadFarmers() {

    const tableBody =
        document.getElementById(
            "farmerTableBody"
        );


    if (!tableBody) {
        return;
    }


    const token =
        localStorage.getItem(
            "milkMateToken"
        );


    if (!token) {

        window.location.href =
            "/admin-login.html";

        return;
    }


    tableBody.innerHTML = `
        <tr>
            <td colspan="7">
                Loading farmers...
            </td>
        </tr>
    `;


    try {

        const controller =
            new AbortController();

        const timeout =
            setTimeout(
                function () {
                    controller.abort();
                },
                10000
            );


        const response =
            await fetch(
                "/api/admin/farmers",
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"
                    },

                    signal:
                        controller.signal
                }
            );


        clearTimeout(timeout);


        console.log(
            "Farmer API Status:",
            response.status
        );


        // ======================================
        // UNAUTHORIZED
        // ======================================

        if (response.status === 401) {

            logout();
            return;
        }


        // ======================================
        // FORBIDDEN
        // ======================================

        if (response.status === 403) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="7">
                        Access denied. Admin permission required.
                    </td>
                </tr>
            `;

            return;
        }


        // ======================================
        // OTHER ERROR
        // ======================================

        if (!response.ok) {

            const errorText =
                await response.text();

            console.error(
                "Farmer API Error:",
                errorText
            );


            tableBody.innerHTML = `
                <tr>
                    <td colspan="7">
                        Unable to load farmers.
                        (HTTP ${response.status})
                    </td>
                </tr>
            `;

            return;
        }


        // ======================================
        // READ JSON
        // ======================================

        const farmers =
            await response.json();


        console.log(
            "Farmers:",
            farmers
        );


        tableBody.innerHTML =
            "";


        // ======================================
        // EMPTY
        // ======================================

        if (
            !Array.isArray(farmers) ||
            farmers.length === 0
        ) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="7">
                        No farmers found.
                    </td>
                </tr>
            `;

            return;
        }


        // ======================================
        // DISPLAY FARMERS
        // ======================================

        farmers.forEach(
            function (farmer) {

                const row =
                    document.createElement(
                        "tr"
                    );


                const status =
                    farmer.active === false
                        ? "Inactive"
                        : "Active";


                const statusClass =
                    farmer.active === false
                        ? "status-inactive"
                        : "status-active";


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                            farmer.id
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            farmer.farmerCode ||
                            "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            farmer.fullName ||
                            "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            farmer.mobile ||
                            "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            farmer.village ||
                            "-"
                        )}
                    </td>

                    <td>

                        <span class="${statusClass}">
                            ${status}
                        </span>

                    </td>

                    <td>

                        <button
                            type="button"
                            class="action-btn edit-btn"
                            onclick="editFarmer(${farmer.id})">

                            ✏️ Edit

                        </button>


                        <button
                            type="button"
                            class="action-btn delete-btn"
                            onclick="deleteFarmer(${farmer.id})">

                            🗑️ Delete

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
            "Load Farmers Error:",
            error
        );


        if (
            error.name === "AbortError"
        ) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="7">
                        Server took too long to respond.
                    </td>
                </tr>
            `;

        } else {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="7">
                        Unable to connect to server.
                    </td>
                </tr>
            `;
        }
    }
}


// ==========================================
// ADD FARMER
// ==========================================

async function addFarmer(event) {

    event.preventDefault();


    const token =
        localStorage.getItem(
            "milkMateToken"
        );


    const message =
        document.getElementById(
            "farmerMessage"
        );


    const farmerCode =
        document.getElementById(
            "farmerCode"
        )?.value
        .trim()
        .toUpperCase();


    const fullName =
        document.getElementById(
            "fullName"
        )?.value
        .trim();


    const mobile =
        document.getElementById(
            "mobile"
        )?.value
        .trim();


    const village =
        document.getElementById(
            "village"
        )?.value
        .trim();


    const address =
        document.getElementById(
            "address"
        )?.value
        .trim();


    if (
        !farmerCode ||
        !fullName ||
        !mobile
    ) {

        showMessage(
            "Please fill all required fields.",
            "error"
        );

        return;
    }


    if (
        !/^[6-9][0-9]{9}$/.test(
            mobile
        )
    ) {

        showMessage(
            "Enter a valid 10-digit mobile number.",
            "error"
        );

        return;
    }


    const requestBody = {

        farmerCode:
            farmerCode,

        fullName:
            fullName,

        mobile:
            mobile,

        village:
            village || null,

        address:
            address || null,

        active:
            true
    };


    try {

        showMessage(
            "Adding farmer...",
            "info"
        );


        const response =
            await fetch(
                "/api/admin/farmers",
                {
                    method: "POST",

                    headers: {

                        "Content-Type":
                            "application/json",

                        "Authorization":
                            "Bearer " +
                            token
                    },

                    body:
                        JSON.stringify(
                            requestBody
                        )
                }
            );


        if (
            response.status === 401
        ) {

            logout();
            return;
        }


        const text =
            await response.text();


        let data = {};


        try {

            data =
                text
                    ? JSON.parse(text)
                    : {};

        } catch (error) {

            data = {};
        }


        if (!response.ok) {

            showMessage(
                data.message ||
                "Failed to add farmer.",
                "error"
            );

            return;
        }


        showMessage(
            "Farmer added successfully!",
            "success"
        );


        const form =
            document.getElementById(
                "farmerForm"
            );


        if (form) {
            form.reset();
        }


        await loadFarmers();

    } catch (error) {

        console.error(
            "Add Farmer Error:",
            error
        );


        showMessage(
            "Server connection failed.",
            "error"
        );
    }
}


// ==========================================
// EDIT FARMER
// ==========================================

async function editFarmer(id) {

    const token =
        localStorage.getItem(
            "milkMateToken"
        );


    try {

        const response =
            await fetch(
                `/api/admin/farmers/${id}`,
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
                "Unable to load farmer."
            );
        }


        const farmer =
            await response.json();


        setValue(
            "editFarmerId",
            farmer.id
        );

        setValue(
            "editFarmerCode",
            farmer.farmerCode
        );

        setValue(
            "editFullName",
            farmer.fullName
        );

        setValue(
            "editMobile",
            farmer.mobile
        );

        setValue(
            "editVillage",
            farmer.village
        );

        setValue(
            "editAddress",
            farmer.address
        );

        setValue(
            "editActive",
            String(
                farmer.active !== false
            )
        );


        const section =
            document.getElementById(
                "editFarmerSection"
            );


        if (section) {

            section.style.display =
                "block";


            section.scrollIntoView({
                behavior: "smooth"
            });
        }


    } catch (error) {

        console.error(
            "Edit Farmer Error:",
            error
        );


        alert(
            "Unable to load farmer."
        );
    }
}


// ==========================================
// UPDATE FARMER
// ==========================================

async function updateFarmer(event) {

    event.preventDefault();


    const token =
        localStorage.getItem(
            "milkMateToken"
        );


    const id =
        document.getElementById(
            "editFarmerId"
        )?.value;


    if (!id) {

        return;
    }


    const farmerData = {

        farmerCode:
            document.getElementById(
                "editFarmerCode"
            )?.value
            .trim()
            .toUpperCase(),

        fullName:
            document.getElementById(
                "editFullName"
            )?.value
            .trim(),

        mobile:
            document.getElementById(
                "editMobile"
            )?.value
            .trim(),

        village:
            document.getElementById(
                "editVillage"
            )?.value
            .trim(),

        address:
            document.getElementById(
                "editAddress"
            )?.value
            .trim(),

        active:
            document.getElementById(
                "editActive"
            )?.value === "true"
    };


    try {

        const response =
            await fetch(
                `/api/admin/farmers/${id}`,
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
                            farmerData
                        )
                }
            );


        if (
            response.status === 401
        ) {

            logout();
            return;
        }


        const text =
            await response.text();


        let data = {};


        try {

            data =
                text
                    ? JSON.parse(text)
                    : {};

        } catch (error) {

            data = {};
        }


        if (!response.ok) {

            alert(
                data.message ||
                "Failed to update farmer."
            );

            return;
        }


        alert(
            "Farmer updated successfully!"
        );


        cancelEdit();

        await loadFarmers();


    } catch (error) {

        console.error(
            "Update Farmer Error:",
            error
        );


        alert(
            "Server connection failed."
        );
    }
}


// ==========================================
// DELETE / DEACTIVATE
// ==========================================

async function deleteFarmer(id) {

    const confirmed =
        confirm(
            "Do you want to deactivate this farmer?"
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
                `/api/admin/farmers/${id}`,
                {
                    method: "DELETE",

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


        const text =
            await response.text();


        let data = {};


        try {

            data =
                text
                    ? JSON.parse(text)
                    : {};

        } catch (error) {

            data = {};
        }


        if (!response.ok) {

            alert(
                data.message ||
                "Unable to deactivate farmer."
            );

            return;
        }


        alert(
            "Farmer deactivated successfully!"
        );


        loadFarmers();


    } catch (error) {

        console.error(
            "Delete Farmer Error:",
            error
        );


        alert(
            "Server connection failed."
        );
    }
}


// ==========================================
// CANCEL EDIT
// ==========================================

function cancelEdit() {

    const section =
        document.getElementById(
            "editFarmerSection"
        );


    if (section) {

        section.style.display =
            "none";
    }
}


// ==========================================
// SET VALUE
// ==========================================

function setValue(
    id,
    value
) {

    const element =
        document.getElementById(
            id
        );


    if (element) {

        element.value =
            value ?? "";
    }
}


// ==========================================
// MESSAGE
// ==========================================

function showMessage(
    text,
    type
) {

    const element =
        document.getElementById(
            "farmerMessage"
        );


    if (!element) {
        return;
    }


    element.textContent =
        text;


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