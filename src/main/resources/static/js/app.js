console.log("JS LOADED ✅");

// ===========================
// Add Passenger
// ===========================
let passengerCount = 1;

function addPassenger() {
    passengerCount++;
    const container = document.getElementById("passenger-container");

    if (!container) return;

    const div = document.createElement("div");
    div.className = "passenger-block";

    div.innerHTML = `
        <div class="passenger-header">Passenger ${passengerCount}</div>
        <div class="form-row">
            <div class="form-group">
                <label>Name</label>
                <input type="text" class="form-control" name="passengerName" required />
            </div>
            <div class="form-group">
                <label>Age</label>
                <input type="number" class="form-control" name="age" min="1" required />
            </div>
            <div class="form-group">
                <label>Gender</label>
                <select class="form-control" name="gender" required>
                    <option value="">Select</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                </select>
            </div>
        </div>
    `;

    container.appendChild(div);
}


// ===========================
// Redirect to Seat Selection
// ===========================
function goToSeatSelection(scheduleId) {

    const ticketInput = document.getElementById("ticketCount_" + scheduleId);

    if (!ticketInput) {
        alert("Ticket input not found!");
        return;
    }

    const ticketCount = ticketInput.value;

    if (!ticketCount || ticketCount <= 0) {
        alert("Please enter valid ticket count.");
        return;
    }

    window.location.href =
        "/user/bus/seats/" + scheduleId + "?ticketCount=" + ticketCount;
}


// ===========================
// SLIDER (FINAL WORKING)
// ===========================
document.addEventListener("DOMContentLoaded", function () {

    let index = 0;
    const slides = document.querySelectorAll(".slide");

    if (slides.length > 0) {
        setInterval(() => {

            slides[index].classList.remove("active");
            slides[index].classList.add("exit");

            index = (index + 1) % slides.length;

            slides[index].classList.remove("exit");
            slides[index].classList.add("active");

        }, 3000);
    }

});


// ===========================
// PROFILE DROPDOWN (Enhanced)
// ===========================
document.addEventListener("DOMContentLoaded", function () {

    const btn = document.getElementById("profileBtn");
    const menu = document.getElementById("dropdownMenu");

    if (!btn || !menu) return;

    // Toggle dropdown with animation
    btn.addEventListener("click", function (e) {
        e.preventDefault();
        e.stopPropagation();
        menu.classList.toggle("show");
    });

    // Close when clicking outside
    window.addEventListener("click", function () {
        menu.classList.remove("show");
    });

    // Prevent menu clicks from closing
    menu.addEventListener("click", function (e) {
        e.stopPropagation();
    });
});


// ===========================
// SCROLL-AWARE NAVBAR
// ===========================
document.addEventListener("DOMContentLoaded", function () {
    const navbar = document.querySelector('.navbar');
    if (!navbar) return;

    let lastScroll = 0;
    window.addEventListener('scroll', function () {
        const currentScroll = window.scrollY;
        if (currentScroll > 20) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
        lastScroll = currentScroll;
    }, { passive: true });
});


// ===========================
// HAMBURGER MENU
// ===========================
document.addEventListener("DOMContentLoaded", function () {
    const hamburger = document.querySelector('.hamburger');
    const navLinks = document.querySelector('.navbar-links');

    if (!hamburger || !navLinks) return;

    hamburger.addEventListener('click', function (e) {
        e.stopPropagation();
        hamburger.classList.toggle('active');
        navLinks.classList.toggle('mobile-open');
    });

    // Close mobile menu on link click
    navLinks.querySelectorAll('a').forEach(function (link) {
        link.addEventListener('click', function () {
            hamburger.classList.remove('active');
            navLinks.classList.remove('mobile-open');
        });
    });

    // Close on outside click
    document.addEventListener('click', function (e) {
        if (!hamburger.contains(e.target) && !navLinks.contains(e.target)) {
            hamburger.classList.remove('active');
            navLinks.classList.remove('mobile-open');
        }
    });
});


// ===========================
// FADE-IN ON SCROLL (Observer)
// ===========================
document.addEventListener("DOMContentLoaded", function () {
    const observer = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
            if (entry.isIntersecting) {
                entry.target.classList.add('animate-fade-up');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });

    document.querySelectorAll('.card, .dash-card, .schedule-item, .passenger-block').forEach(function (el) {
        el.style.opacity = '0';
        observer.observe(el);
    });
});


// ===========================
// AUTO-HIDE ALERTS
// ===========================
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('.alert').forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity .4s ease, transform .4s ease';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-8px)';
            setTimeout(function () {
                alert.style.display = 'none';
            }, 400);
        }, 5000);
    });
});