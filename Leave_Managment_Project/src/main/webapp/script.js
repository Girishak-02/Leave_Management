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
})
document.getElementById('my-leaves-link').addEventListener('click', function () {
    const empId = Number(document.getElementById('empId').innerHTML);
    const getURl = "http://localhost:8080/Leave_Managment_Project/leaves?empId=" + empId;
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
    loadTeamLeaves();
});
document.getElementById('apply-leave-form').addEventListener('submit', function (e) {
    e.preventDefault();
    const formData = new FormData(this);

    const fromDate = formData.get('fromDate');
    const toDate = formData.get('toDate');
    const today = new Date().toISOString().split('T')[0];
    if (new Date(fromDate) < new Date() || new Date(toDate) < new Date(fromDate)) {
        //alert('Invalid date selection.');

        return;
    }

    const leaveData = {
        leaveType: formData.get('leaveType'),
        fromDate: fromDate,
        toDate: toDate,
        reason: formData.get('reason'),
        status: 'Pending',
        currentDate: new Date().toISOString().split('T')[0]
    };

    fetch('http://localhost:8080/Leave_Managment_Project/leaves', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(leaveData)
    })
        .then(response => {
            if (response.ok) {
                console.log('Leave submitted successfully.');
            } else {
                console.error('Failed to submit leave.');
            }
        });

    $('#applyLeaveModal').modal('hide');
});

document.body.onload = function () {
    fetch('http://localhost:8080/Leave_Managment_Project/userprofile')
        .then(response => response.json())
        .then(profile => {
            document.getElementById('profileName').textContent = profile.empName;
            document.getElementById('profileEmail').textContent = profile.empEmail;
            document.getElementById('profilePhone').textContent = profile.PhoneNumber;
            document.getElementById('dateOdBirth').textContent = profile.dob;
            document.getElementById('employeeId').textContent = profile.empId;
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
function loadTeamLeaves() {
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
                    updateLeaveStatus(leave.leaveId, 'APPROVED');
                });
                const rejectButton = document.createElement('button');
                rejectButton.className = 'btn btn-danger reject-leave';
                rejectButton.innerText = 'Reject';
                rejectButton.addEventListener('click', function () {
                    updateLeaveStatus(leave.leaveId, 'REJECTED');
                });
                const actionCell = row.insertCell(5);
                actionCell.appendChild(acceptButton);
                actionCell.appendChild(rejectButton);
            });
        })
        .catch(error => console.log('Error fetching leaves:', error));
}
function fetchTeamLeaves() {
    fetch('/manager/leaves', {
        method: 'GET',
        credentials: 'include'
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
function displayLeaveRequests(leaves) {
    const leaveTableBody = document.getElementById('leaveTableBody');
    leaveTableBody.innerHTML = '';

    leaves.forEach(leave => {
        const row = leaveTableBody.insertRow();

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
//function updateLeaveStatus(leaveId, newStatus) {
//
//    const updateLeaveStatusUrl = `http://localhost:8080/Leave_Managment_Project/manager/leaves?leaveId=${leaveId}&status=${newStatus}`;
//    fetch(updateLeaveStatusUrl, {
//        method: 'POST'
//
//    }).then(response => {
//        if (response.ok) {
//            return response.json();
//        } else {
//            throw new Error('Failed to update leave status.');
//        }
//    }).then(data => {
//        if (data.success) {
//            console.log(`Leave ${newStatus.toLowerCase()} successfully.`);
//        } else {
//            console.error(`Failed to ${newStatus.toLowerCase()} leave.`);
//        }
//    }).catch(error => {
//        console.log('Error:', error);
//    });
//
//}
function updateLeaveStatus(leaveId, newStatus) {
    console.log(leaveId)
    console.log(newStatus)
    const updateLeaveStatusUrl = `http://localhost:8080/Leave_Managment_Project/manager/leaves?leaveId=${leaveId}&&status=${newStatus}`; // URL without query parameters

    // Create the JSON payload
    const payload = {
        leaveId: leaveId,
        status: newStatus
    };

    fetch(updateLeaveStatusUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // Specify that the payload is JSON
        },
        // Convert the payload to JSON
    })
//    .then(response => {
//        if (response.ok) {
//            return response.json();
//        } else {
//            throw new Error('Failed to update leave status.');
//        }
//    })
//    .then(data => {
//        if (data.success) {
//            console.log(`Leave ${newStatus.toLowerCase()} successfully.`);
//        } else {
//            console.error(`Failed to ${newStatus.toLowerCase()} leave.`);
//        }
//    })
    .catch(error => {
        console.log('Error:', error);
    });
}
