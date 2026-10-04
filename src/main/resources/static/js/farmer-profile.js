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


        // =====================================
        // GET SAVED FARMER DATA
        // =====================================

        const fullName =
            localStorage.getItem(
                "milkMateFullName"
            ) || "Farmer";


        const mobile =
            localStorage.getItem(
                "milkMateMobile"
            ) || "-";


        const email =
            localStorage.getItem(
                "milkMateEmail"
            ) || "-";


        const farmerId =
            localStorage.getItem(
                "milkMateFarmerId"
            ) || "-";


        const userId =
            localStorage.getItem(
                "milkMateUserId"
            ) || "-";


        const savedRole =
            localStorage.getItem(
                "milkMateRole"
            ) || "FARMER";


        // =====================================
        // DISPLAY
        // =====================================

        setText(
            "profileName",
            fullName
        );

        setText(
            "fullName",
            fullName
        );

        setText(
            "mobile",
            mobile
        );

        setText(
            "email",
            email
        );

        setText(
            "farmerId",
            farmerId
        );

        setText(
            "userId",
            userId
        );

        setText(
            "role",
            savedRole
        );


        // =====================================
        // LOGOUT
        // =====================================

        const logoutBtn =
            document.getElementById(
                "logoutBtn"
            );


        if (logoutBtn) {

            logoutBtn.addEventListener(
                "click",
                logout
            );
        }
    }
);


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
            value;
    }
}


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