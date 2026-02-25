// ===========================
// Add Passenger (Booking Form)
// ===========================
let passengerCount = 1;

function addPassenger() {
    passengerCount++;
    const container = document.getElementById("passenger-container");

    const div = document.createElement("div");
    div.className = "passenger-block";
    div.innerHTML = `
        <div class="passenger-header">Passenger ${passengerCount}</div>
        <div class="form-row">
            <div class="form-group">
                <label>Name</label>
                <input type="text" class="form-control" name="passengerName"
                       placeholder="Full Name" required />
            </div>
            <div class="form-group">
                <label>Age</label>
                <input type="number" class="form-control" name="age"
                       placeholder="Age" min="1" required />
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
