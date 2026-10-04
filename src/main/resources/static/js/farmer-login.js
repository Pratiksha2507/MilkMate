// =====================================================
// MILKMATE - FARMER LOGIN
// =====================================================

async function loginFarmer() {

    const mobile =
        document.getElementById("mobile").value.trim();

    const password =
        document.getElementById("password").value;

    const message =
        document.getElementById("message");

    const button =
        document.getElementById("loginButton");

    if (!/^[6-9]\d{9}$/.test(mobile)) {

        message.innerText =
            "Please enter a valid 10-digit mobile number.";

        message.style.color = "#d92d20";
        return;
    }

    if (!password) {

        message.innerText =
            "Please enter your password.";

        message.style.color = "#d92d20";
        return;
    }

    if (button) {
        button.disabled = true;
        button.innerText = "Signing in...";
    }

    message.innerText = "Checking login...";
    message.style.color = "#176b3a";

    try {

        const response = await fetch(
            "/api/auth/login",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    mobile: mobile,
                    password: password
                })
            }
        );

        const text = await response.text();

        let data = {};

        try {
            data = text ? JSON.parse(text) : {};
        } catch (error) {
            throw new Error(
                "Invalid server response."
            );
        }

        if (!response.ok) {

            throw new Error(
                data.message ||
                "Invalid mobile number or password."
            );
        }

        if (data.role !== "FARMER") {

            throw new Error(
                "This login is only for farmers."
            );
        }

        if (!data.token) {

            throw new Error(
                "Login token not received."
            );
        }

        if (!data.farmerId) {

            throw new Error(
                "Farmer profile is not linked."
            );
        }

        localStorage.setItem(
            "milkMateToken",
            data.token
        );

        localStorage.setItem(
            "milkMateRole",
            data.role
        );

        localStorage.setItem(
            "milkMateUserId",
            data.userId || ""
        );

        localStorage.setItem(
            "milkMateFarmerId",
            data.farmerId
        );

        localStorage.setItem(
            "milkMateFullName",
            data.fullName || "Farmer"
        );

        localStorage.setItem(
            "milkMateMobile",
            data.mobile || mobile
        );

        localStorage.setItem(
            "milkMateUser",
            JSON.stringify({
                userId: data.userId || null,
                farmerId: data.farmerId,
                fullName: data.fullName || "Farmer",
                mobile: data.mobile || mobile,
                role: "FARMER"
            })
        );

        message.innerText =
            "Login successful. Opening your dashboard...";

        message.style.color = "#176b3a";

        setTimeout(function () {

            window.location.href =
                "/farmer-dashboard.html";

        }, 500);

    } catch (error) {

        console.error(
            "Farmer Login Error:",
            error
        );

        message.innerText =
            error.message ||
            "Login failed.";

        message.style.color = "#d92d20";

        if (button) {

            button.disabled = false;
            button.innerText = "Login";
        }
    }
}