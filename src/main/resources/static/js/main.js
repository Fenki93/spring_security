
const baseEndPoint = "http://localhost:8080/note";
const noteList = document.querySelector(".list");
const errorBox = document.querySelector(".error-box");
const errorList = document.querySelector(".error-list");
const form = document.querySelector(".form");
const formEdit = document.querySelector(".form-edit");
const formId = document.querySelector("[data-id]");
const formTitle = document.querySelector("[data-title]");
const formContent = document.querySelector("[data-content]");
const formUserId = document.querySelector("[data-user-id]");
const modal = document.querySelector("#editModal");
const spanCloseModal = document.querySelector(".close-span");
const btmCloseModal = document.querySelector(".cancel-btn");

async function editNote(id) {
    clearErrorBox();

    const url = `${baseEndPoint}/edit?id=${id}`;
    const params = {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json; charset=UTF-8'
        })
    }
    let response = await fetch(url, params);

    if (response.ok) {
        const note = await response.json()
        formId.value = note.id;
        formTitle.value = note.title;
        formContent.value = note.content;
        formUserId.value = note.userId;
        modal.style.display = "block";
    } else {
        errorBox.style.display = "block";
        buildErrorList(await response.json());
    }
}


const onFormEditSubmit = (e) => {
    e.preventDefault();

    const note = {
        id: formEdit.elements.id.value.trim(),
        title: formEdit.elements.title.value.trim(),
        content: formEdit.elements.content.value.trim(),
        userId: formEdit.elements.userId.value.trim(),
    }

    if (note.id && note.title && note.content) {
        e.currentTarget.reset();
        updateNote(note);
    }
}

formEdit.addEventListener("submit", onFormEditSubmit);

async function updateNote({id, title, content, userId}) {
    clearErrorBox();
    const url = `${baseEndPoint}/edit`;
    const params = {
        method: 'PUT',
        body: JSON.stringify({
            id: id,
            title: title,
            content: content,
            userId: userId,
        }),
        headers: new Headers({
            'Content-Type': 'application/json; charset=UTF-8'
        })
    }
    let response = await fetch(url, params);

    if (response.ok) {
        modal.style.display = "none";
        location.href=`${baseEndPoint}`;
    } else {
        errorBox.style.display = "block";
        buildErrorList(await response.json());
    }
}


const onFormSubmit = (e) => {
    e.preventDefault();

    const title = form.elements.title.value.trim();
    const content = form.elements.content.value.trim();

    if (title && content) {
        e.currentTarget.reset();
        addNote(title, content);
    }
}

form.addEventListener("submit", onFormSubmit);

async function addNote(title, content) {
    clearErrorBox();
    const url = `${baseEndPoint}/create`;
    const params = {
        method: 'POST',
        body: JSON.stringify({
            title: title,
            content: content,
        }),
        headers: new Headers({
            'Content-Type': 'application/json; charset=UTF-8'
        })
    }
        let response = await fetch(url, params);

        if (response.ok) {
            buildNotesList([await response.json()]);
        } else {
            errorBox.style.display = "block";
            buildErrorList(await response.json());
        }
}

async function deleteNote(id) {
    clearErrorBox();
    const url = `${baseEndPoint}/delete`;
    const params = {
        method: 'DELETE',
        body: JSON.stringify({id: id}),
        headers: new Headers({
            'Content-Type': 'application/json; charset=UTF-8'
        })
    }
    let response = await fetch(url, params);

    if (response.ok) {
        location.href=`${baseEndPoint}`
    } else {
        buildErrorList(await response.json());
    }
}

const buildNotesList = (notes) => {
    noteList.insertAdjacentHTML("beforeend", notes.map(fillNotesList).join(""));
}

const fillNotesList = ({id, title, content, userId}) => {
    return `
    <li class="list-item">
        <p class="item-id">${id}</p>
        <p class="item-id">UserID: ${userId}</p>
        <p class="item-title">${title}</p>
        <p class="item-content">${content}</p>
        <button class="btn" type="button" onclick="editNote('${id}')">Edit</button>
        <button class="btn" type="button" onclick="deleteNote('${id}')">Delete</button>
    </li>`;
};

async function getNotes() {
    const url = `${baseEndPoint}/list`;
    const params = {
        method: "GET",
        headers: {"Content-Type": "application/json; charset=UTF-8",},
    }
    let response = await fetch(url, params);

    if (response.ok) {
        buildNotesList(await response.json());
    } else {
        buildErrorList(await response.json());
    }
}

const buildErrorList = (errResp) => {
    if (!errResp) return;
    errorBox.style.display = "block";
    // entity validations errors
    if (typeof errResp?.errors === 'object' && !Array.isArray(errResp.errors)) {
        Object.keys(errResp.errors).forEach(key => {
            errorList.insertAdjacentHTML("beforeend", errResp.errors[key].map(error => {
                return `<li><p>${key}</p>${error}</li>`;
            }).join(""));
        });
        // user manual errors
    } else if (Array.isArray(errResp.errors)) {
        errorList.insertAdjacentHTML("beforeend", errResp.errors.map(error => {
            return `<li>${error}</li>`;
        }).join(""));
        // other errors
    } else if (errResp.hasOwnProperty("error") && errResp.hasOwnProperty("status")) {
        errorList.insertAdjacentHTML("beforeend",
            `<li><p>Error</p>${errResp.error}</li>
                 <li><p>Message</p>${errResp.message}</li>
                 <li><p>Status</p>${errResp.status}</li>`
        );
    }
}

const clearErrorBox = () => {
    errorBox.style.display = "none";
    errorList.innerHTML = "";
}

spanCloseModal.onclick = function() {
    modal.style.display = "none";
}
btmCloseModal.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}


getNotes().then();