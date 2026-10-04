document.addEventListener("DOMContentLoaded", function () {

    const form =
        document.getElementById("registerForm");

    if (!form) {
        return;
    }

    form.addEventListener(
        "submit",
        registerFarmer
    );
});


async function registerFarmer(event) {

    event.preventDefault();

    const fullName =
        document.getElementById("fullName")
            ?.value.trim();

    const mobile =
        document.getElementById("mobile")
            ?.value.trim();

    const email =
        document.getElementById("email")
            ?.value.trim();

    const password =
        document.getElementById("password")
            ?.value;

    const confirmPassword =
        document.getElementById("confirmPassword")
            ?.value;

    const message =
        document.getElementById("message");

    const button =
        document.getElementById("registerButton");


    // =========================
    // VALIDATION
    // =========================

    if (!fullName || fullName.length < 2) {

        showMessage(
            "Please enter your full name.",
            "error"
        );

        return;
    }


    if (!/^[0-9]{10}$/.test(mobile)) {

        showMessage(
            "Please enter a valid 10 digit mobile number.",
            "error"
        );

        return;
    }


    if (!email) {

        showMessage(
            "Please enter your email address.",
            "error"
        );

        return;
    }


    if (password.length < 8) {

        showMessage(
            "Password must be at least 8 characters.",
            "error"
        );

        return;
    }


    if (password !== confirmPassword) {

        showMessage(
            "Passwords do not match.",
            "error"
        );

        return;
    }


    // =========================
    // LOADING
    // =========================

    if (button) {

        button.disabled = true;

        button.innerText =
            "Creating Account...";
    }


    showMessage(
        "Creating your account...",
        "info"
    );


    try {

        const response =
            await fetch(
                "/api/auth/register",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({

                            fullName:
                                fullName,

                            mobile:
                                mobile,

                            email:
                                email,

                            password:
                                password

                        })
                }
            );


        const responseText =
            await response.text();

        let data = {};

        try {

            data =
                responseText
                    ? JSON.parse(responseText)
                    : {};

        } catch (error) {

            data = {};
        }


        if (!response.ok) {

            if (response.status === 409) {

                throw new Error(
                    "Mobile number or email already registered."
                );
            }

            throw new Error(
                data.message ||
                "Registration failed."
            );
        }


        if (!data.token) {

            throw new Error(
                "Token not received from server."
            );
        }


        if (!data.farmerId) {

            throw new Error(
                "Farmer ID not received from server."
            );
        }


        // =========================
        // SAVE MILKMATE LOGIN DATA
        // =========================

        localStorage.setItem(
            "milkMateToken",
            data.token
        );

        localStorage.setItem(
            "milkMateRole",
            data.role || "FARMER"
        );

        localStorage.setItem(
            "milkMateUserId",
            data.id
        );

        localStorage.setItem(
            "milkMateFarmerId",
            data.farmerId
        );

        localStorage.setItem(
            "milkMateFullName",
            data.fullName || fullName
        );

        localStorage.setItem(
            "milkMateMobile",
            data.mobile || mobile
        );

        localStorage.setItem(
            "milkMateEmail",
            data.email || email
        );


        // Save complete user object
        localStorage.setItem(
            "milkMateUser",
            JSON.stringify({
                id:
                    data.id,

                farmerId:
                    data.farmerId,

                fullName:
                    data.fullName || fullName,

                mobile:
                    data.mobile || mobile,

                email:
                    data.email || email,

                role:
                    data.role || "FARMER"
            })
        );


        showMessage(
            "Account created successfully! Opening dashboard...",
            "success"
        );


        setTimeout(
            function () {

                window.location.href =
                    "/farmer-dashboard.html";

            },
            800
        );


    } catch (error) {

        console.error(
            "Registration Error:",
            error
        );


        showMessage(
            error.message ||
            "Registration failed.",
            "error"
        );


        if (button) {

            button.disabled = false;

            button.innerText =
                "Create Account";
        }
    }
}


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

    element.innerText =
        message;


    if (type === "success") {

        element.style.color =
            "#2e7d32";

    } else if (type === "error") {

        element.style.color =
            "#d32f2f";

    } else {

        element.style.color =
            "#667085";
    }
}