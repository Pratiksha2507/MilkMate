document.addEventListener("DOMContentLoaded", function () {

    const form =
        document.getElementById("milkCollectionForm");

    const farmerId =
        document.getElementById("farmerId");

    const collectionDate =
        document.getElementById("collectionDate");

    const session =
        document.getElementById("session");

    const quantity =
        document.getElementById("quantity");

    const fat =
        document.getElementById("fat");

    const snf =
        document.getElementById("snf");

    const rate =
        document.getElementById("rate");

    const totalAmount =
        document.getElementById("totalAmount");

    const message =
        document.getElementById("message");

    const saveBtn =
        document.getElementById("saveBtn");

    const filterFarmer =
        document.getElementById("filterFarmer");

    const filterFromDate =
        document.getElementById("filterFromDate");

    const filterToDate =
        document.getElementById("filterToDate");

    const searchRecords =
        document.getElementById("searchRecords");

    const recordsTableBody =
        document.getElementById("recordsTableBody");

    const recordCount =
        document.getElementById("recordCount");

    const refreshBtn =
        document.getElementById("refreshBtn");


    let allRecords = [];
    let editId = null;


    function getToken() {

        return localStorage.getItem(
            "milkMateToken"
        );
    }


    function checkLogin() {

        const token =
            getToken();

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

            return false;
        }

        return true;
    }


    function showMessage(
        text,
        type
    ) {

        if (!message) {
            return;
        }

        message.textContent = text;
        message.className =
            type || "";


        setTimeout(
            function () {

                message.textContent = "";
                message.className = "";

            },
            5000
        );
    }


    function getTodayDate() {

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

        const day =
            String(
                today.getDate()
            ).padStart(
                2,
                "0"
            );

        return `${year}-${month}-${day}`;
    }


    if (collectionDate) {

        collectionDate.value =
            getTodayDate();

    }


    loadFarmers();
    loadMilkRate();
    loadRecords();


    if (collectionDate) {

        collectionDate.addEventListener(
            "change",
            loadMilkRate
        );
    }


    if (quantity) {

        quantity.addEventListener(
            "input",
            calculateTotal
        );
    }


    if (form) {

        form.addEventListener(
            "submit",
            saveMilkCollection
        );
    }


    if (searchRecords) {

        searchRecords.addEventListener(
            "input",
            applyFilters
        );
    }


    if (filterFarmer) {

        filterFarmer.addEventListener(
            "change",
            applyFilters
        );
    }


    if (filterFromDate) {

        filterFromDate.addEventListener(
            "change",
            applyFilters
        );
    }


    if (filterToDate) {

        filterToDate.addEventListener(
            "change",
            applyFilters
        );
    }


    if (refreshBtn) {

        refreshBtn.addEventListener(
            "click",
            function () {

                resetFilters();
                loadRecords();

            }
        );
    }


    async function loadFarmers() {

        if (!checkLogin()) {
            return;
        }

        try {

            const response =
                await fetch(
                    "/api/admin/farmers",
                    {
                        method: "GET",

                        headers: {
                            "Authorization":
                                "Bearer " +
                                getToken()
                        }
                    }
                );


            if (
                response.status === 401
            ) {

                logout();
                return;
            }


            if (
                !response.ok
            ) {

                throw new Error(
                    "Unable to load farmers."
                );
            }


            const farmers =
                await response.json();


            if (farmerId) {

                farmerId.innerHTML =
                    '<option value="">Select Farmer</option>';
            }


            if (filterFarmer) {

                filterFarmer.innerHTML =
                    '<option value="">All Farmers</option>';
            }


            farmers.forEach(
                function (farmer) {

                    const farmerText =
                        (
                            farmer.farmerCode ||
                            "-"
                        ) +
                        " - " +
                        (
                            farmer.fullName ||
                            "Farmer"
                        );


                    if (farmerId) {

                        const option =
                            document.createElement(
                                "option"
                            );

                        option.value =
                            farmer.id;

                        option.textContent =
                            farmerText;

                        farmerId.appendChild(
                            option
                        );
                    }


                    if (filterFarmer) {

                        const filterOption =
                            document.createElement(
                                "option"
                            );

                        filterOption.value =
                            farmer.id;

                        filterOption.textContent =
                            farmerText;

                        filterFarmer.appendChild(
                            filterOption
                        );
                    }

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


    async function loadMilkRate() {

        if (!collectionDate) {
            return;
        }

        if (
            !collectionDate.value
        ) {
            return;
        }

        if (!checkLogin()) {
            return;
        }


        try {

            const response =
                await fetch(
                    "/api/milk-rates/applicable?date=" +
                    encodeURIComponent(
                        collectionDate.value
                    ),
                    {
                        method: "GET",

                        headers: {
                            "Authorization":
                                "Bearer " +
                                getToken()
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

                throw new Error(
                    data.message ||
                    "Milk rate unavailable."
                );
            }


            const rateValue =
                Number(
                    data.ratePerLiter || 0
                );


            if (rate) {

                rate.textContent =
                    "₹" +
                    rateValue.toFixed(2) +
                    " / L";
            }


            calculateTotal();


        } catch (error) {

            console.error(
                "Milk Rate Error:",
                error
            );


            if (rate) {
                rate.textContent =
                    "Rate unavailable";
            }


            if (totalAmount) {
                totalAmount.textContent =
                    "₹0.00";
            }
        }
    }


    function getRateValue() {

        if (!rate) {
            return 0;
        }

        const text =
            rate.textContent || "";

        const numericText =
            text.replace(
                /[^0-9.]/g,
                ""
            );

        return Number(
            numericText || 0
        );
    }


    function calculateTotal() {

        if (
            !quantity ||
            !totalAmount
        ) {
            return;
        }


        const quantityValue =
            Number(
                quantity.value || 0
            );


        const rateValue =
            getRateValue();


        const total =
            quantityValue *
            rateValue;


        totalAmount.textContent =
            "₹" +
            total.toLocaleString(
                "en-IN",
                {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2
                }
            );
    }


    async function saveMilkCollection(
        event
    ) {

        event.preventDefault();


        if (!checkLogin()) {
            return;
        }


        if (
            !farmerId ||
            !farmerId.value
        ) {

            showMessage(
                "Please select a farmer.",
                "error"
            );

            return;
        }


        const requestData = {

            collectionDate:
                collectionDate.value,

            session:
                session.value,

            quantity:
                Number(
                    quantity.value || 0
                ),

            fat:
                fat.value !== ""
                    ? Number(fat.value)
                    : null,

            snf:
                snf.value !== ""
                    ? Number(snf.value)
                    : null
        };


        if (
            !requestData.collectionDate ||
            !requestData.session ||
            requestData.quantity <= 0
        ) {

            showMessage(
                "Please enter all required milk details.",
                "error"
            );

            return;
        }


        try {

            saveBtn.disabled = true;


            saveBtn.textContent =
                editId
                    ? "Updating..."
                    : "Saving...";


            let url;
            let method;


            if (editId) {

                url =
                    "/api/admin/milk-collections/" +
                    editId;

                method =
                    "PUT";

            } else {

                url =
                    "/api/admin/milk-collections/farmer/" +
                    farmerId.value;

                method =
                    "POST";
            }


            const response =
                await fetch(
                    url,
                    {
                        method:
                            method,

                        headers: {

                            "Content-Type":
                                "application/json",

                            "Authorization":
                                "Bearer " +
                                getToken()
                        },

                        body:
                            JSON.stringify(
                                requestData
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

                throw new Error(
                    data.message ||
                    data.error ||
                    text ||
                    "Milk collection operation failed."
                );
            }


            showMessage(
                editId
                    ? "Milk collection updated successfully."
                    : "Milk collection saved successfully.",
                "success"
            );


            resetForm();

            await loadRecords();


        } catch (error) {

            console.error(
                "Save Milk Collection Error:",
                error
            );

            showMessage(
                error.message ||
                "Unable to save milk collection.",
                "error"
            );


        } finally {

            saveBtn.disabled = false;

            saveBtn.textContent =
                editId
                    ? "Update Milk Collection"
                    : "Save Milk Collection";
        }
    }


    async function loadRecords() {

        if (!checkLogin()) {
            return;
        }


        try {

            const response =
                await fetch(
                    "/api/admin/milk-collections",
                    {
                        method: "GET",

                        headers: {
                            "Authorization":
                                "Bearer " +
                                getToken()
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
                    "Unable to load milk records."
                );
            }


            allRecords =
                await response.json();


            displayRecords(
                allRecords
            );


        } catch (error) {

            console.error(
                "Load Records Error:",
                error
            );


            if (recordsTableBody) {

                recordsTableBody.innerHTML =
                    `
                    <tr>
                        <td colspan="9"
                            class="no-records">
                            Unable to load records.
                        </td>
                    </tr>
                    `;
            }
        }
    }


    function displayRecords(
        records
    ) {

        if (!recordsTableBody) {
            return;
        }


        recordsTableBody.innerHTML =
            "";


        if (
            !records ||
            records.length === 0
        ) {

            recordsTableBody.innerHTML =
                `
                <tr>
                    <td colspan="9"
                        class="no-records">
                        No milk collection records found.
                    </td>
                </tr>
                `;


            if (recordCount) {

                recordCount.textContent =
                    "Total Records: 0";
            }

            return;
        }


        records.forEach(
            function (record) {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML =
                    `
                    <td>
                        ${escapeHtml(
                            record.id
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            record.farmer?.farmerCode ||
                            record.farmerCode ||
                            "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            record.collectionDate ||
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
                        ₹${formatNumber(
                            record.rate
                        )}
                    </td>

                    <td>
                        ₹${formatNumber(
                            record.totalAmount
                        )}
                    </td>

                    <td>

                        <button
                            type="button"
                            class="action-btn edit-btn"
                            onclick="editRecord(${record.id})">
                            Edit
                        </button>

                        <button
                            type="button"
                            class="action-btn delete-btn"
                            onclick="deleteRecord(${record.id})">
                            Delete
                        </button>

                    </td>
                    `;


                recordsTableBody.appendChild(
                    row
                );
            }
        );


        if (recordCount) {

            recordCount.textContent =
                "Total Records: " +
                records.length;
        }
    }


    function applyFilters() {

        const farmer =
            filterFarmer?.value || "";

        const fromDate =
            filterFromDate?.value || "";

        const toDate =
            filterToDate?.value || "";

        const search =
            (
                searchRecords?.value || ""
            )
            .trim()
            .toLowerCase();


        const filtered =
            allRecords.filter(
                function (record) {

                    const recordFarmer =
                        String(
                            record.farmer?.id ||
                            record.farmerId ||
                            ""
                        );


                    const recordCode =
                        String(
                            record.farmer?.farmerCode ||
                            record.farmerCode ||
                            ""
                        )
                        .toLowerCase();


                    const recordName =
                        String(
                            record.farmer?.fullName ||
                            record.farmerName ||
                            ""
                        )
                        .toLowerCase();


                    const recordDate =
                        String(
                            record.collectionDate ||
                            ""
                        );


                    if (
                        farmer &&
                        recordFarmer !== farmer
                    ) {
                        return false;
                    }


                    if (
                        fromDate &&
                        recordDate < fromDate
                    ) {
                        return false;
                    }


                    if (
                        toDate &&
                        recordDate > toDate
                    ) {
                        return false;
                    }


                    if (search) {

                        const text =
                            (
                                recordCode +
                                " " +
                                recordName +
                                " " +
                                recordDate +
                                " " +
                                (
                                    record.session || ""
                                )
                            )
                            .toLowerCase();


                        if (
                            !text.includes(
                                search
                            )
                        ) {
                            return false;
                        }
                    }


                    return true;
                }
            );


        displayRecords(
            filtered
        );
    }


    function resetFilters() {

        if (filterFarmer) {
            filterFarmer.value = "";
        }

        if (filterFromDate) {
            filterFromDate.value = "";
        }

        if (filterToDate) {
            filterToDate.value = "";
        }

        if (searchRecords) {
            searchRecords.value = "";
        }

        displayRecords(
            allRecords
        );
    }


    window.editRecord =
        async function (id) {

            try {

                const response =
                    await fetch(
                        "/api/admin/milk-collections/" +
                        id,
                        {
                            method: "GET",

                            headers: {
                                "Authorization":
                                    "Bearer " +
                                    getToken()
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
                        "Unable to load record."
                    );
                }


                const record =
                    await response.json();


                editId =
                    record.id;


                if (farmerId) {

                    farmerId.value =
                        record.farmer?.id ||
                        record.farmerId ||
                        "";
                }


                if (collectionDate) {

                    collectionDate.value =
                        record.collectionDate ||
                        getTodayDate();
                }


                if (session) {

                    session.value =
                        record.session ||
                        "";
                }


                if (quantity) {

                    quantity.value =
                        record.quantity ||
                        "";
                }


                if (fat) {

                    fat.value =
                        record.fat ??
                        "";
                }


                if (snf) {

                    snf.value =
                        record.snf ??
                        "";
                }


                if (saveBtn) {

                    saveBtn.textContent =
                        "Update Milk Collection";
                }


                loadMilkRate();


            } catch (error) {

                console.error(
                    "Edit Record Error:",
                    error
                );

                showMessage(
                    "Unable to edit record.",
                    "error"
                );
            }
        };


    window.deleteRecord =
        async function (id) {

            const confirmed =
                confirm(
                    "Are you sure you want to delete this milk collection record?"
                );


            if (!confirmed) {
                return;
            }


            try {

                const response =
                    await fetch(
                        "/api/admin/milk-collections/" +
                        id,
                        {
                            method: "DELETE",

                            headers: {
                                "Authorization":
                                    "Bearer " +
                                    getToken()
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

                    const text =
                        await response.text();

                    throw new Error(
                        text ||
                        "Unable to delete record."
                    );
                }


                showMessage(
                    "Milk collection deleted successfully.",
                    "success"
                );


                if (
                    editId === id
                ) {

                    resetForm();
                }


                await loadRecords();


            } catch (error) {

                console.error(
                    "Delete Record Error:",
                    error
                );

                showMessage(
                    "Unable to delete milk collection.",
                    "error"
                );
            }
        };


    function resetForm() {

        if (form) {
            form.reset();
        }


        editId = null;


        if (collectionDate) {

            collectionDate.value =
                getTodayDate();
        }


        if (rate) {

            rate.textContent =
                "Loading...";
        }


        if (totalAmount) {

            totalAmount.textContent =
                "₹0.00";
        }


        if (saveBtn) {

            saveBtn.textContent =
                "Save Milk Collection";
        }


        loadMilkRate();
    }


    function formatNumber(value) {

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

});