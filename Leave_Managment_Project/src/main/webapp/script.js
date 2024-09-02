  document.addEventListener('DOMContentLoaded', function () {
        const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', function () {
                navLinks.forEach(nav => nav.classList.remove('active'));
                this.classList.add('active');
                showSection(this.id);
            });
        });
        function showSection(sectionId) {
            const sections = {
                'dashboard-link': 'dashboard-section',
                'my-leaves-link': 'my-leaves-section',
                'my-team-leaves-link': 'my-team-leaves-section'
            };
            Object.values(sections).forEach(section => {
                document.getElementById(section).classList.add('d-none');
            });
            document.getElementById(sections[sectionId]).classList.remove('d-none');
        }
document.getElementById('my-leaves-link').addEventListener('click', function () {
const empId=Number(document.getElementById('empId').innerHTML);
const getURl="http://localhost:8080/Leave_Managment_Project/leaves?empId="+ empId;
    fetch(getURl, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => response.json())
        .then(data => {
            const table = document.getElementById('my-leaves-table');
            table.innerHTML = ''; // Clear existing rows
            data.leaves.forEach(leave => {
                const row = table.insertRow();
                row.insertCell(0).innerText = leave.leaveType;
                row.insertCell(1).innerText = leave.fromDate;
                row.insertCell(2).innerText = leave.toDate;
                row.insertCell(3).innerText = leave.reason;
                row.insertCell(4).innerText = leave.status;
            });
        })
        .catch(error => console.error('Error fetching leaves:', error));
});
document.getElementById('my-team-leaves-link').addEventListener('click', function () {
    const empId = Number(document.getElementById('empId').innerHTML);
    const getURl = "http://localhost:8080/Leave_Managment_Project/manager/leaves?managerId=" + empId;
    fetch(getURl, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => response.json())
        .then(data => {
            const table = document.getElementById('team-leaves-table');
            table.innerHTML = '';
            data.leaves.forEach(leave => {
                const row = table.insertRow();
                row.insertCell(0).innerText = leave.leaveType;
                row.insertCell(1).innerText = leave.fromDate;
                row.insertCell(2).innerText = leave.toDate;
                row.insertCell(3).innerText = leave.reason;
                row.insertCell(4).innerText = leave.status;
                const acceptButton = document.createElement('button');
                acceptButton.className = 'btn btn-success m-2 accept-leave';
                acceptButton.innerText = 'Accept';
                acceptButton.addEventListener('click', function () {
                    updateLeaveStatus(leave.leaveId, 'APPROVED', row);
                });
                const rejectButton = document.createElement('button');
                rejectButton.className = 'btn btn-danger reject-leave';
                rejectButton.innerText = 'Reject';
                rejectButton.addEventListener('click', function () {
                    updateLeaveStatus(leave.leaveId, 'REJECTED', row);
                });
                const actionCell = row.insertCell(5);
                actionCell.appendChild(acceptButton);
                actionCell.appendChild(rejectButton);
            });
        })
        .catch(error => console.error('Error fetching leaves:', error));
});

function updateLeaveStatus(leaveId, status, row) {
    fetch(`http://localhost:8080/Leave_Managment_Project/leaves?leaveId=${leaveId}&status=${status}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => {
        if (response.ok) {
            console.log(`Leave ${status.toLowerCase()} successfully.`);
            row.cells[4].innerText = status; // Update the status cell in the row
        } else {
            console.error(`Failed to ${status.toLowerCase()} leave.`);
        }
    }).catch(error => console.error(`Error updating leave status:`, error));
}

function updateLeaveStatus(leaveId, status) {
    fetch(`http://localhost:8080/Leave_Managment_Project/leaves?leaveId=${leaveId}&status=${status}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => {
        if (response.ok) {
            console.log(`Leave ${status.toLowerCase()} successfully.`);

        } else {
            console.error(`Failed to ${status.toLowerCase()} leave.`);
        }
    }).catch(error => console.error(`Error updating leave status:`, error));
}
        document.getElementById('apply-leave-form').addEventListener('submit', function (e) {
            e.preventDefault();
            const leaveType = document.getElementById('leaveType').value;
            const fromDate = document.getElementById('fromDate').value;
            const toDate = document.getElementById('toDate').value;
            const reason = document.getElementById('reason').value;
            if (new Date(fromDate) < new Date() || new Date(toDate) < new Date(fromDate)) {
                alert('Invalid date selection.');
                return;
            }

            const leaveData = {
                leaveType,
                fromDate,
                toDate,
                reason,
                status: 'Pending'
            };

            fetch('http://localhost:8080/Leave_Managment_Project/leaves', {
                method: 'POST',
                headers: {
                       'Accept': 'application/json',
                       'Content-Type': 'application/json'
                            },
                body: JSON.stringify(leaveData)
            }).then(response => {
                if (response.ok) {
                    console.log('Leave submitted successfully.');
                    // Additional logic to handle the response
                } else {
                    console.error('Failed to submit leave.');
                }
            });

            // Close modal
            $('#applyLeaveModal').modal('hide');
        });

        // Profile Button Click
        document.body.onload = function() {
            fetch('http://localhost:8080/Leave_Managment_Project/userprofile')
                .then(response => response.json())
                .then(profile => {
                    document.getElementById('profileName').textContent = profile.empName;
                    document.getElementById('profileEmail').textContent = profile.empEmail;
                    document.getElementById('profilePhone').textContent = profile.PhoneNumber;
                    document.getElementById('empId').textContent = profile.empId;
                });
        };

        document.getElementById('profileButton').addEventListener('click', function () {
            $('#profileModal').modal('show');
        });

        // Logout Button Click
        document.getElementById('logoutButton').addEventListener('click', function () {
            // Perform logout
            fetch('http://localhost:8080/Leave_Managment_Project/logout')
                .then(() => {
                    window.location.href = 'LoginPage.html';
                });
        });
function checkIfManager() {
    fetch('http://localhost:8080/Leave_Managment_Project/manager/leaves')
        .then(response => response.json())
        .then(data => {
            if (data.isManager) {
                loadTeamLeaves();
            }
        })
        .catch(error => console.error('Error checking manager status:', error));
}

// Fetch team leaves if user is a manager
function loadTeamLeaves() {
    fetch('http://localhost:8080/Leave_Managment_Project/managerservlet')
        .then(response => response.json())
        .then(teamLeaves => {
            const table = document.getElementById('team-leaves-table');
            table.innerHTML = ''; // Clear previous content
            teamLeaves.forEach(leave => {
                const row = table.insertRow();
                row.insertCell(0).innerText = leave.employeeName;
                row.insertCell(1).innerText = leave.leaveType;
                row.insertCell(2).innerText = leave.fromDate;
                row.insertCell(3).innerText = leave.toDate;
                row.insertCell(4).innerText = leave.status;
                const actionCell = row.insertCell(5);
                if (leave.status === 'Pending') {
                    actionCell.innerHTML = `
                        <button class="btn btn-success accept-leave">Accept</button>
                        <button class="btn btn-danger reject-leave">Reject</button>
                    `;
                } else {
                    actionCell.innerText = 'No Action';
                }
            });
        })
        .catch(error => console.error('Error loading team leaves:', error));
}
// Function to fetch team leave requests
function fetchTeamLeaves() {
    fetch('/manager/leaves', {
        method: 'GET',
        credentials: 'include' // Ensures cookies (including session cookies) are sent with the request
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        displayLeaveRequests(data);
    })
    .catch(error => console.error('Error fetching team leaves:', error));
}

// Function to display leave requests in a table
function displayLeaveRequests(leaves) {
    const leaveTableBody = document.getElementById('leaveTableBody');
    leaveTableBody.innerHTML = ''; // Clear any existing rows

    leaves.forEach(leave => {
        const row = leaveTableBody.insertRow();

        // Insert cells and set their text content
        const leaveIdCell = row.insertCell(0);
        leaveIdCell.textContent = leave.leaveId;

        const empIdCell = row.insertCell(1);
        empIdCell.textContent = leave.empId;

        const leaveTypeCell = row.insertCell(2);
        leaveTypeCell.textContent = leave.leaveType;

        const fromDateCell = row.insertCell(3);
        fromDateCell.textContent = leave.fromDate;

        const toDateCell = row.insertCell(4);
        toDateCell.textContent = leave.toDate;

        const reasonCell = row.insertCell(5);
        reasonCell.textContent = leave.reason;

        const statusCell = row.insertCell(6);
        statusCell.textContent = leave.status;
    });
}
function decreaseUsedLeave(leaveType, amount) {
    const usedElement = document.getElementById(`${leaveType}Used`);
    const remainingElement = document.getElementById(`${leaveType}Remaining`);

    let usedValue = parseInt(usedElement.innerText);
    let remainingValue = parseInt(remainingElement.innerText);

    if (usedValue >= amount) {
        usedValue -= amount;
        remainingValue += amount;

        usedElement.innerText = usedValue;
        remainingElement.innerText = remainingValue;
    } else {
        console.error(`Cannot decrease ${amount} from ${leaveType} used leaves. Not enough used leaves.`);
    }
}
        function updateLeaveStatus(leaveId, status) {
            fetch(`/LeaveServlet?action=${status.toLowerCase()}&leaveId=${leaveId}`, {
                method: 'PUT'
            }).then(response => {
                if (response.ok) {
                    console.log(`Leave ${status.toLowerCase()} successfully.`);
                    // Additional logic to update UI
                } else {
                    console.error(`Failed to ${status.toLowerCase()} leave.`);
                }
            });
        }
    });