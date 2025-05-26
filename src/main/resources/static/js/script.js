document.addEventListener("DOMContentLoaded", function () {
    const loginBtn = document.getElementById("loginBtn");
    const signupBtn = document.getElementById("signupBtn");
    const mypageBtn = document.getElementById("mypageBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    function updateAuthUI() {
        const isLoggedIn = window.auth?.isLoggedIn;

        if (isLoggedIn) {
            loginBtn.style.display = "none";
            signupBtn.style.display = "none";
            mypageBtn.style.display = "inline-block";
            logoutBtn.style.display = "inline-block";
        } else {
            loginBtn.style.display = "inline-block";
            signupBtn.style.display = "inline-block";
            mypageBtn.style.display = "none";
            logoutBtn.style.display = "none";
        }
    }

    loginBtn.addEventListener("click", function () {
        window.location.href = "./login.jsp";
    });

    signupBtn.addEventListener("click", function () {
        window.location.href = "./signup.jsp";
    });

    mypageBtn.addEventListener("click", function () {
        window.location.href = "./mypage.jsp";
    });

    logoutBtn.addEventListener("click", function () {
        window.location.href = contextPath + "/user?action=user_logout";
    });

    updateAuthUI();

    // 부드러운 스크롤 기능
    document.querySelectorAll('.smooth-scroll').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = this.getAttribute("href").substring(1);
            const targetElement = document.getElementById(targetId);

            if (targetElement) {
                window.scrollTo({
                    top: targetElement.offsetTop - 50,  // 헤더 높이만큼 조정
                    behavior: "smooth"
                });
            }
        });
    });
});
