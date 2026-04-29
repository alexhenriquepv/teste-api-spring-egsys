const API_URL = ''; // Mesma origem

// Elementos da UI
const loginView = document.getElementById('login-view');
const dashboardView = document.getElementById('dashboard-view');
const loginForm = document.getElementById('login-form');
const taskForm = document.getElementById('task-form');
const tasksContainer = document.getElementById('tasks-container');
const categorySelect = document.getElementById('task-category');
const loginError = document.getElementById('login-error');
const loader = document.getElementById('loader');
const btnLogout = document.getElementById('btn-logout');

let jwtToken = localStorage.getItem('jwt') || null;

document.addEventListener('DOMContentLoaded', () => {
    if (jwtToken) {
        showDashboard();
    } else {
        showLogin();
    }
});

// Exibição de Telas
function showLogin() {
    loginView.classList.remove('d-none');
    dashboardView.classList.add('d-none');
}

function showDashboard() {
    loginView.classList.add('d-none');
    dashboardView.classList.remove('d-none');
    fetchCategories();
    fetchTasks();
}

function showLoader() {
    loader.classList.remove('d-none');
    loader.classList.add('d-flex');
}

function hideLoader() {
    loader.classList.add('d-none');
    loader.classList.remove('d-flex');
}

// Login
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    loginError.classList.add('d-none');

    const login = document.getElementById('username').value;
    const senha = document.getElementById('password').value;

    showLoader();
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ login, senha })
        });

        const data = await response.json();

        if (response.ok) {
            jwtToken = data.token;
            localStorage.setItem('jwt', jwtToken);
            showDashboard();
        } else {
            loginError.textContent = data.erro || 'Falha na autenticação';
            loginError.classList.remove('d-none');
        }
    } catch (err) {
        loginError.textContent = 'Erro ao conectar ao servidor.';
        loginError.classList.remove('d-none');
    } finally {
        hideLoader();
    }
});

// Logout
btnLogout.addEventListener('click', () => {
    jwtToken = null;
    localStorage.removeItem('jwt');
    showLogin();
});

async function fetchTasks() {
    showLoader();
    try {
        const response = await fetch(`${API_URL}/tarefas`, {
            headers: { 'Authorization': `Bearer ${jwtToken}` }
        });

        if (response.status === 401 || response.status === 403) {
            btnLogout.click(); // Força logout se token inválido
            return;
        }

        const tarefas = await response.json();
        renderTasks(tarefas);
    } catch (err) {
        console.error('Erro ao buscar tarefas:', err);
    } finally {
        hideLoader();
    }
}

// Fetch Categories
async function fetchCategories() {
    try {
        const response = await fetch(`${API_URL}/categorias`, {
            headers: { 'Authorization': `Bearer ${jwtToken}` }
        });

        if (response.ok) {
            const categorias = await response.json();
            categorySelect.innerHTML = '<option value="" disabled selected>Selecione a categoria...</option>';
            categorias.forEach(c => {
                categorySelect.insertAdjacentHTML('beforeend', `<option value="${c.id}">${c.descricao}</option>`);
            });
        }
    } catch (err) {
        console.error('Erro ao buscar categorias:', err);
    }
}

// Criar Tarefa
taskForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const titulo = document.getElementById('task-title').value;
    const descricao = document.getElementById('task-desc').value;
    const categoriaId = document.getElementById('task-category').value;

    const novaTarefa = {
        titulo: titulo,
        descricao: descricao || null,
        categoria: { id: parseInt(categoriaId) }
    };

    showLoader();
    try {
        const response = await fetch(`${API_URL}/tarefas`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            body: JSON.stringify(novaTarefa)
        });

        if (response.ok) {
            taskForm.reset();
            fetchTasks();
        } else {
            const errorData = await response.json();
            const msgErro = errorData.titulo || errorData.categoria || errorData.erro || 'Erro desconhecido';
            alert('Aviso: ' + msgErro);
        }
    } catch (err) {
        console.error('Erro ao criar tarefa:', err);
    } finally {
        hideLoader();
    }
});

async function deleteTask(id) {
    if (!confirm('Tem certeza que deseja apagar esta tarefa?')) return;

    showLoader();
    try {
        const response = await fetch(`${API_URL}/tarefas/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${jwtToken}` }
        });

        if (response.ok) {
            fetchTasks();
        } else {
            alert('Erro ao apagar tarefa.');
        }
    } catch (err) {
        console.error('Erro ao apagar tarefa:', err);
    } finally {
        hideLoader();
    }
}

function renderTasks(tarefas) {
    tasksContainer.innerHTML = '';

    if (tarefas.length === 0) {
        tasksContainer.innerHTML = `
            <div class="text-center py-5 text-muted">
                <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="currentColor" class="bi bi-inboxes mb-3 opacity-50" viewBox="0 0 16 16">
                  <path d="M4.98 1a.5.5 0 0 0-.39.188L1.54 5H6a.5.5 0 0 1 .5.5 1.5 1.5 0 0 0 3 0A.5.5 0 0 1 10 5h4.46l-3.05-3.812A.5.5 0 0 0 11.02 1H4.98zm9.954 5H10.45a2.5 2.5 0 0 1-4.9 0H1.066l.32 2.562A.5.5 0 0 0 1.884 9h12.234a.5.5 0 0 0 .496-.438L14.933 6zM3.809.563A1.5 1.5 0 0 1 4.981 0h6.038a1.5 1.5 0 0 1 1.172.563l3.7 4.625a.5.5 0 0 1 .105.374l-.39 3.124A1.5 1.5 0 0 1 14.117 10H1.883A1.5 1.5 0 0 1 .394 8.686l-.39-3.124a.5.5 0 0 1 .106-.374l3.7-4.625z"/>
                </svg>
                <h5 class="fw-semibold">Nenhuma tarefa encontrada.</h5>
                <p>Crie uma nova tarefa ao lado!</p>
            </div>
        `;
        return;
    }

    tarefas.forEach(t => {
        const dateObj = new Date(t.dataHora);
        const dataFormatada = dateObj.toLocaleDateString('pt-BR') + ' ' + dateObj.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });

        const html = `
            <div class="card mb-3 shadow-sm">
                <div class="card-body d-flex justify-content-between align-items-center">
                    <div class="pe-3">
                        <h5 class="card-title fw-bold text-primary mb-1">${t.titulo}</h5>
                        ${t.descricao ? `<p class="card-text text-secondary mb-2">${t.descricao}</p>` : ''}
                        <div class="d-flex gap-2 align-items-center mt-2">
                            <span class="badge text-bg-primary">${t.categoria.descricao}</span>
                            <small class="text-muted">🕒 ${dataFormatada}</small>
                        </div>
                    </div>
                    <button class="btn btn-outline-danger btn-sm p-2" onclick="deleteTask(${t.id})" title="Apagar Tarefa">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                          <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                          <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                        </svg>
                    </button>
                </div>
            </div>
        `;
        tasksContainer.insertAdjacentHTML('beforeend', html);
    });
}
