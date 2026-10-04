document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("milkMateToken");
    const role = localStorage.getItem("milkMateRole");
    const farmerId = localStorage.getItem("milkMateFarmerId");

    if (!token || role !== "FARMER" || !farmerId) {
        window.location.href = "/farmer-login.html";
        return;
    }

    const dateInput =
        document.getElementById("collectionDate");

    if (dateInput) {
        dateInput.value =
            new Date().toISOString().split("T")[0];

        loadMilkRate(dateInput.value);

        dateInput.addEventListener(
            "change",
            function () {
                loadMilkRate(this.value);
            }
        );
    }

    const quantityInput =
        document.getElementById("quantity");

    const fatInput =
        document.getElementById("fat");

    const rateInput =
        document.getElementById("rate");

    if (quantityInput) {
        quantityInput.addEventListener(
            "input",
            calculateAmount
        );
    }

    if (fatInput) {
        fatInput.addEventListener(
            "input",
            calculateAmount
        );
    }

    if (rateInput) {
        rateInput.addEventListener(
            "input",
            calculateAmount
        );
    }

    const form =
        document.getElementById("milkRecordForm");

    if (form) {
        form.addEventListener(
            "submit",
            submitMilkRecord
        );
    }
});


async function loadMilkRate(date) {

    try {

        const response = await fetch(
            "/api/milk-rates/applicable?date=" +
            encodeURIComponent(date)
        );

        if (!response.ok) {
            throw new Error(
                "Milk rate not available."
            );
        }

        const rate =
            await response.json();

        const rateInput =
            document.getElementById("rate");

        if (rateInput) {
            rateInput.value =
                rate.ratePerLiter || "";
        }

        calculateAmount();

    } catch (error) {

        console.error(
            "Milk rate error:",
            error
        );

        showMessage(
            "Milk rate is not available for this date."
        );
    }
}


function calculateAmount() {

    const quantity =
        Number(
            document.getElementById("quantity")?.value || 0
        );

    const rate =
        Number(
            document.getElementById("rate")?.value || 0
        );

    const amount =
        quantity * rate;

    const amountElement =
        document.getElementById("estimatedAmount");

    if (amountElement) {

        amountElement.textContent =
            "₹" +
            amount.toLocaleString(
                "en-IN",
                {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2
                }
            );
    }
}


async function submitMilkRecord(event) {

    event.preventDefault();

    const token =
        localStorage.getItem("milkMateToken");

    const farmerId =
        localStorage.getItem("milkMateFarmerId");

    const collectionDate =
        document.getElementById(
            "collectionDate"
        )?.value;

    const session =
        document.getElementById(
            "session"
        )?.value;

    const quantity =
        Number(
            document.getElementById(
                "quantity"
            )?.value || 0
        );

    const fat =
        Number(
            document.getElementById(
                "fat"
            )?.value || 0
        );

    const snfElement =
        document.getElementById("snf");

    const snf =
        snfElement
            ? Number(snfElement.value || 0)
            : null;


    if (!collectionDate ||
        !session ||
        quantity <= 0 ||
        fat <= 0) {

        showMessage(
            "Please enter all required milk details."
        );

        return;
    }


    const requestBody = {

        collectionDate:
            collectionDate,

        session:
            session,

        quantity:
            quantity,

        fat:
            fat,

        snf:
            snf
    };


    try {

        const response = await fetch(
            "/api/farmer/milk-collections/" +
            farmerId,
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json",

                    "Authorization":
                        "Bearer " + token
                },

                body:
                    JSON.stringify(
                        requestBody
                    )
            }
        );


        if (response.status === 401) {

            logout();

            return;
        }


        const responseText =
            await response.text();


        if (!response.ok) {

            console.error(
                "Milk record error:",
                responseText
            );

            showMessage(
                "Unable to save milk record."
            );

            return;
        }


        showMessage(
            "Milk collection record saved successfully."
        );


        const form =
            document.getElementById(
                "milkRecordForm"
            );

        if (form) {
            form.reset();
        }


        if (collectionDate) {

            const dateInput =
                document.getElementById(
                    "collectionDate"
                );

            if (dateInput) {
                dateInput.value =
                    collectionDate;
            }
        }


        calculateAmount();


        setTimeout(
            function () {
                window.location.href =
                    "/my-milk-collection.html";
            },
            1000
        );


    } catch (error) {

        console.error(
            "Submit milk record error:",
            error
        );

        showMessage(
            "Server error. Please try again."
        );
    }
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

    window.location.href =
        "/farmer-login.html";
}