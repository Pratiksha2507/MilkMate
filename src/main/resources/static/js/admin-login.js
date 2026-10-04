function loginAdmin() {

    const mobile =
        document.getElementById(
            "mobile"
        ).value.trim();

    const password =
        document.getElementById(
            "password"
        ).value;

    const message =
        document.getElementById(
            "message"
        );

    const loginButton =
        document.querySelector(
            "button"
        );


    // ==========================================
    // VALIDATION
    // ==========================================

    if (!mobile) {

        message.innerText =
            "Please enter mobile number.";

        return;
    }


    if (!password) {

        message.innerText =
            "Please enter password.";

        return;
    }


    message.innerText =
        "Logging in...";


    if (loginButton) {

        loginButton.disabled =
            true;
    }


    // ==========================================
    // DEMO ADMIN LOGIN
    // ANY MOBILE + ANY PASSWORD
    // ==========================================

    fetch(
        "/api/auth/demo-admin-login",
        {
            method: "POST",

            headers: {

                "Content-Type":
                    "application/json"
            },

            body: JSON.stringify({

                mobile:
                    mobile,

                password:
                    password
            })
        }
    )

    .then(
        async function (response) {

            const text =
                await response.text();

            let data = {};

            try {

                data =
                    text
                        ? JSON.parse(text)
                        : {};

            } catch (error) {

                throw new Error(
                    "Invalid server response."
                );
            }


            console.log(
                "Admin Demo Login Response:",
                data
            );


            if (!response.ok) {

                throw new Error(
                    data.message ||
                    "Demo login failed."
                );
            }


            return data;
        }
    )


    .then(
        function (data) {

            // ======================================
            // SAVE TOKEN
            // ======================================

            localStorage.setItem(
                "milkMateToken",
                data.token
            );


            // Always ADMIN
            localStorage.setItem(
                "milkMateRole",
                "ADMIN"
            );


            localStorage.setItem(
                "milkMateUserId",
                data.userId || ""
            );


            localStorage.setItem(
                "milkMateFullName",
                data.fullName ||
                "MilkMate Demo User"
            );


            localStorage.setItem(
                "milkMateMobile",
                data.mobile ||
                mobile
            );


            localStorage.setItem(
                "milkMateUser",
                JSON.stringify({

                    userId:
                        data.userId || null,

                    fullName:
                        data.fullName ||
                        "MilkMate Demo User",

                    mobile:
                        data.mobile ||
                        mobile,

                    role:
                        "ADMIN"
                })
            );


            message.innerText =
                "Login successful. Redirecting...";


            // ======================================
            // DASHBOARD
            // ======================================

            setTimeout(
                function () {

                    window.location.href =
                        "/admin-dashboard.html";

                },
                500
            );
        }
    )


    .catch(
        function (error) {

            console.error(
                "Admin Login Error:",
                error
            );


            message.innerText =
                error.message ||
                "Login failed.";


            if (loginButton) {

                loginButton.disabled =
                    false;
            }
        }
    );
}