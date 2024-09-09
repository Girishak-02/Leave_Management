document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function () {
            navLinks.forEach(nav => nav.classList.remove('active'));
            this.classList.add('active');
            showSection(this.id);
        });
//    });

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
});

document.getElementById('my-leaves-link').addEventListener('click', function () {
    const empId = Number(document.getElementById('empId').innerHTML);
    const getURL = `http://localhost:8080/Leave_Managment_Project/leaves?empId=${empId}`;
    fetch(getURL, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => response.json())
    .then(data => {
        const table = document.getElementById('my-leaves-table');
        table.innerHTML = '';
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
onload();
function onload(){
    const empId = Number(document.getElementById('empId').innerHTML);
    const getURL = `http://localhost:8080/Leave_Managment_Project/leaves?empId=${empId}`;
    fetch(getURL, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => response.json())
    .then(data => {document.getElementById('sickUsed').textContent = data.leaveCount['SICK'];
        document.getElementById('ptoUsed').textContent = data.leaveCount['PTO'];
        document.getElementById('casualUsed').textContent = data.leaveCount['OTHER'];
        document.getElementById('sickRemaining').textContent = 10- data.leaveCount['SICK'];
        document.getElementById('ptoRemaining').textContent = 10-data.leaveCount['PTO'];
        document.getElementById('casualRemaining').textContent = 10-data.leaveCount['OTHER'];

    })
}

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
        return;
    }

    const leaveData = {
        empId: Number(document.getElementById('empId').innerHTML),
        managerId: Number(document.getElementById('managerId').innerHTML),
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
            document.getElementById('managerId').textContent = profile.managerId;
        });
};

document.getElementById('profileButton').addEventListener('click', function () {
    $('#profileModal').modal('show');
});

document.getElementById('logoutButton').addEventListener('click', function () {
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
    const getURL = `http://localhost:8080/Leave_Managment_Project/manager/leaves?managerId=${empId}`;
    fetch(getURL, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => response.json())
    .then(data => {
        const table = document.getElementById('team-leaves-table');
        table.innerHTML = '';

        if (data.leaves.length === 0) {
            console.log('No leaves to display');
            const row = table.insertRow();
            row.insertCell(0).innerText = 'No leaves to display';
            table.appendChild(row);
            return;
        }

        data.leaves.forEach(leave => {
            const row = table.insertRow();
            row.insertCell(0).innerText = leave.empId;
            row.insertCell(1).innerText = leave.leaveType;
            row.insertCell(2).innerText = leave.fromDate;
            row.insertCell(3).innerText = leave.toDate;
            row.insertCell(4).innerText = leave.reason;
            row.insertCell(5).innerText = leave.status;

            const acceptButton = document.createElement('button');
            acceptButton.className = 'btn btn-success m-2 accept-leave';
            acceptButton.innerText = 'Accept';
            console.log(leave.leaveId);
            acceptButton.addEventListener('click', function () {
                updateLeaveStatus(leave.leaveId, 'APPROVED', acceptButton, rejectButton);
            });

            const rejectButton = document.createElement('button');
            rejectButton.className = 'btn btn-danger reject-leave';
            rejectButton.innerText = 'Reject';
            rejectButton.addEventListener('click', function () {
                updateLeaveStatus(leave.leaveId, 'REJECTED', acceptButton, rejectButton);
            });

            const actionCell = row.insertCell(5);
            actionCell.appendChild(acceptButton);
            actionCell.appendChild(rejectButton);
        });
    })
    .catch(error => console.log('Error fetching leaves:', error));
}

function updateLeaveStatus(leaveId, newStatus, acceptButton, rejectButton) {
    const updateLeaveStatusUrl = `http://localhost:8080/Leave_Managment_Project/manager/leaves`;
    const payload = {
        leaveId: leaveId,
        status: newStatus
    };

    fetch(updateLeaveStatusUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            console.log(`Leave ${newStatus.toLowerCase()} successfully`);
            acceptButton.disabled = true;
            rejectButton.disabled = true;
        } else {
            console.error('Failed to update leave status');
        }
    })
    .catch(error => console.error('Error:', error));
}
function filterLeaves(tableId, filterId) {
        const filterValue = document.getElementById(filterId).value.toLowerCase();
        const rows = document.querySelectorAll(`#${tableId} tbody tr`);

        rows.forEach(row => {
            const statusCell = row.cells[4];
            const status = statusCell ? statusCell.textContent.toLowerCase() : '';

            if (filterValue === 'all' || status.includes(filterValue)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    document.getElementById('myLeavesFilter').addEventListener('change', function() {
        filterLeaves('my-leaves-section', 'myLeavesFilter');
    });

    document.getElementById('teamLeavesFilter').addEventListener('change', function() {
        filterLeaves('my-team-leaves-section', 'teamLeavesFilter');
    });

    filterLeaves('my-leaves-section', 'myLeavesFilter');
    filterLeaves('my-team-leaves-section', 'teamLeavesFilter');


});
