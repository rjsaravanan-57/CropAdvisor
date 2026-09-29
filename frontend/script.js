// ==========================================
// CROPADVISOR FRONTEND
// ==========================================

const API_URL = "http://localhost:8080/api";


// ==========================================
// 1. RAISE QUERY
// ==========================================

const queryForm = document.getElementById("queryForm");

if (queryForm) {

    queryForm.addEventListener("submit", async function (event) {

        event.preventDefault();


        const farmerId =
            document.getElementById("farmerId").value;

        const crop =
            document.getElementById("crop").value;

        const symptoms =
            document.getElementById("symptoms").value;

        const photoReference =
            document.getElementById("photoReference").value;


        const ticketData = {

            crop: crop,

            symptoms: symptoms,

            photoReference: photoReference,

            farmer: {
                id: Number(farmerId)
            }

        };


        try {

            const response = await fetch(
                API_URL + "/tickets",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(ticketData)
                }
            );


            if (!response.ok) {

                throw new Error(
                    "Failed to create ticket"
                );

            }


            const ticket =
                await response.json();


            alert(
                "Query submitted successfully!\n\n" +
                "Ticket ID: " + ticket.id +
                "\nStatus: " + ticket.status
            );


            queryForm.reset();


        } catch (error) {

            console.error(error);

            alert(
                "Unable to submit the query.\n\n" +
                "Make sure the Spring Boot server is running."
            );

        }

    });

}


// ==========================================
// 2. FARMER - VIEW MY TICKETS
// ==========================================

async function loadFarmerTickets() {

    const farmerId =
        document.getElementById("searchFarmerId").value;


    if (!farmerId) {

        alert("Please enter Farmer ID.");

        return;
    }


    const container =
        document.getElementById("farmerTickets");


    container.innerHTML =
        "<p class='message'>Loading tickets...</p>";


    try {

        const response = await fetch(
            API_URL + "/tickets/farmer/" + farmerId
        );


        if (!response.ok) {

            throw new Error(
                "Unable to get tickets"
            );

        }


        const tickets =
            await response.json();


        if (tickets.length === 0) {

            container.innerHTML =
                "<p class='message'>" +
                "No tickets found." +
                "</p>";

            return;
        }


        container.innerHTML = "";


        for (let i = 0; i < tickets.length; i++) {

            const ticket = tickets[i];

            container.appendChild(
                createFarmerTicketCard(ticket)
            );

        }


    } catch (error) {

        console.error(error);

        container.innerHTML =
            "<p class='message'>" +
            "Unable to load tickets. " +
            "Make sure the Spring Boot server is running." +
            "</p>";

    }

}


// ==========================================
// CREATE FARMER TICKET CARD
// ==========================================

function createFarmerTicketCard(ticket) {

    const card =
        document.createElement("div");

    card.className = "ticket-card";


    const officerName =
        ticket.officer
            ? ticket.officer.name
            : "Not assigned";


    const recommendation =
        ticket.recommendation
            ? ticket.recommendation
            : "No recommendation yet";


    const createdDate =
        ticket.createdAt
            ? new Date(ticket.createdAt).toLocaleString()
            : "Not available";


    card.innerHTML = `

        <h3>
            Ticket #${ticket.id}
        </h3>


        <div class="ticket-row">
            <strong>Crop:</strong>
            ${ticket.crop}
        </div>


        <div class="ticket-row">
            <strong>Symptoms:</strong>
            ${ticket.symptoms}
        </div>


        <div class="ticket-row">
            <strong>Photo:</strong>
            ${ticket.photoReference || "Not provided"}
        </div>


        <div class="ticket-row">
            <strong>Officer:</strong>
            ${officerName}
        </div>


        <div class="ticket-row">
            <strong>Status:</strong>
            <span class="status">
                ${ticket.status}
            </span>
        </div>


        <div class="ticket-row">
            <strong>Recommendation:</strong>
            ${recommendation}
        </div>


        <div class="ticket-row">
            <strong>Created:</strong>
            ${createdDate}
        </div>

    `;


    // Farmer can reopen a closed ticket

    if (ticket.status === "CLOSED") {

        const actions =
            document.createElement("div");

        actions.className =
            "ticket-actions";


        const reopenButton =
            document.createElement("button");

        reopenButton.className =
            "secondary-button";

        reopenButton.innerText =
            "Reopen Ticket";


        reopenButton.onclick =
            function () {

                reopenTicket(
                    ticket.id,
                    ticket.farmer.id
                );

            };


        actions.appendChild(
            reopenButton
        );


        card.appendChild(actions);

    }


    return card;

}


// ==========================================
// 3. FARMER - REOPEN TICKET
// ==========================================

async function reopenTicket(ticketId, farmerId) {

    try {

        const response = await fetch(
            API_URL +
            "/tickets/" +
            ticketId +
            "/reopen?farmerId=" +
            farmerId,
            {
                method: "PUT"
            }
        );


        if (!response.ok) {

            throw new Error(
                "Unable to reopen ticket"
            );

        }


        alert(
            "Ticket reopened successfully."
        );


        loadFarmerTickets();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to reopen the ticket."
        );

    }

}


// ==========================================
// 4. OFFICER - VIEW ASSIGNED TICKETS
// ==========================================

async function loadOfficerTickets() {

    const officerId =
        document.getElementById("officerId").value;


    if (!officerId) {

        alert("Please enter Officer ID.");

        return;
    }


    const container =
        document.getElementById("officerTickets");


    container.innerHTML =
        "<p class='message'>Loading tickets...</p>";


    try {

        const response = await fetch(
            API_URL +
            "/tickets/officer/" +
            officerId
        );


        if (!response.ok) {

            throw new Error(
                "Unable to get officer tickets"
            );

        }


        const tickets =
            await response.json();


        if (tickets.length === 0) {

            container.innerHTML =
                "<p class='message'>" +
                "No tickets assigned to this officer." +
                "</p>";

            return;
        }


        container.innerHTML = "";


        for (let i = 0; i < tickets.length; i++) {

            const ticket = tickets[i];

            container.appendChild(
                createOfficerTicketCard(
                    ticket,
                    officerId
                )
            );

        }


    } catch (error) {

        console.error(error);

        container.innerHTML =
            "<p class='message'>" +
            "Unable to load officer tickets." +
            "</p>";

    }

}


// ==========================================
// CREATE OFFICER TICKET CARD
// ==========================================

function createOfficerTicketCard(ticket, officerId) {

    const card =
        document.createElement("div");

    card.className =
        "ticket-card";


    const recommendation =
        ticket.recommendation
            ? ticket.recommendation
            : "No recommendation yet";


    card.innerHTML = `

        <h3>
            Ticket #${ticket.id}
        </h3>


        <div class="ticket-row">
            <strong>Crop:</strong>
            ${ticket.crop}
        </div>


        <div class="ticket-row">
            <strong>Symptoms:</strong>
            ${ticket.symptoms}
        </div>


        <div class="ticket-row">
            <strong>Photo:</strong>
            ${ticket.photoReference || "Not provided"}
        </div>


        <div class="ticket-row">
            <strong>Farmer:</strong>
            ${ticket.farmer.name}
        </div>


        <div class="ticket-row">
            <strong>Status:</strong>
            <span class="status">
                ${ticket.status}
            </span>
        </div>


        <div class="ticket-row">
            <strong>Current Recommendation:</strong>
            ${recommendation}
        </div>

    `;


    // Don't show actions for already closed ticket

    if (ticket.status !== "CLOSED") {

        const actions =
            document.createElement("div");

        actions.className =
            "ticket-actions";


        // Recommendation input

        const recommendationInput =
            document.createElement("input");

        recommendationInput.type =
            "text";

        recommendationInput.placeholder =
            "Enter recommendation";


        // Recommendation button

        const recommendationButton =
            document.createElement("button");

        recommendationButton.className =
            "button";

        recommendationButton.innerText =
            "Add Recommendation";


        recommendationButton.onclick =
            function () {

                addRecommendation(
                    ticket.id,
                    officerId,
                    recommendationInput.value
                );

            };


        // Close button

        const closeButton =
            document.createElement("button");

        closeButton.className =
            "secondary-button";

        closeButton.innerText =
            "Close Ticket";


        closeButton.onclick =
            function () {

                closeTicket(
                    ticket.id,
                    officerId
                );

            };


        actions.appendChild(
            recommendationInput
        );

        actions.appendChild(
            recommendationButton
        );

        actions.appendChild(
            closeButton
        );


        card.appendChild(actions);

    }


    return card;

}


// ==========================================
// 5. OFFICER - ADD RECOMMENDATION
// ==========================================

async function addRecommendation(
    ticketId,
    officerId,
    recommendation
) {

    if (!recommendation.trim()) {

        alert(
            "Please enter a recommendation."
        );

        return;
    }


    try {

        const response = await fetch(

            API_URL +
            "/tickets/" +
            ticketId +
            "/recommendation?officerId=" +
            officerId,

            {
                method: "PUT",

                headers: {
                    "Content-Type": "text/plain"
                },

                body: recommendation
            }

        );


        if (!response.ok) {

            throw new Error(
                "Unable to add recommendation"
            );

        }


        alert(
            "Recommendation added successfully."
        );


        loadOfficerTickets();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to add recommendation."
        );

    }

}


// ==========================================
// 6. OFFICER - CLOSE TICKET
// ==========================================

async function closeTicket(
    ticketId,
    officerId
) {

    const confirmClose =
        confirm(
            "Are you sure you want to close this ticket?"
        );


    if (!confirmClose) {

        return;

    }


    try {

        const response = await fetch(

            API_URL +
            "/tickets/" +
            ticketId +
            "/close?officerId=" +
            officerId,

            {
                method: "PUT"
            }

        );


        if (!response.ok) {

            throw new Error(
                "Unable to close ticket"
            );

        }


        alert(
            "Ticket closed successfully."
        );


        loadOfficerTickets();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to close the ticket."
        );

    }

}