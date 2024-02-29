<script setup>
import PanelView from '../views/PanelView.vue'

import Swal from 'sweetalert2'
</script>

<template>
    <PanelView>
        <div class="container">
            <div class="header">
                <h2>Computadoras</h2>
            </div>
            <div class="content">
                <div class="content-inputs">
                    <input id="input-search" class="input-search" type="text" placeholder="🔍 Búsqueda por nombre"
                        @input="searchByInput()" />

                    <button class="btn" @click="openModal(newData())">🖥️ Agregar computadora</button>
                    <button class="btn"><strong>❔</strong></button>
                </div>

                <div class="table-container" @click="handleClick">
                    <table id="table">
                        <thead>
                            <tr class="table-header">
                                <th @click="ordenarTabla(0)" class="text-center">Nombre</th>
                                <th @click="ordenarTabla(1)" class="text-center">Activo</th>
                                <th @click="ordenarTabla(2)" class="text-center">Service Tag</th>
                                <th @click="ordenarTabla(3)" class="text-center">IP</th>
                                <th @click="ordenarTabla(4)" class="text-center">Fecha de Adquisición</th>
                                <th @click="ordenarTabla(5)">Comentarios</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <tr v-for="computer in computers" :key="computer.id" :id="`${computer.id}`" class="user-row"
                                @click="openModal(computer)">
                                <td class="text-center">{{ computer.name }}</td>
                                <td class="text-center">{{ computer.active ? 'Si' : 'No' }}</td>
                                <td class="text-center">{{ computer.service_tag }}</td>
                                <td class="text-center">{{ computer.ip }}</td>
                                <td class="text-center">{{ computer.adquisition_date }}</td>
                                <td>{{ computer.comment }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </PanelView>
</template>

<script>
export default {
    data() {
        return {
            url: 'http://10.21.11.156:8080',
            // url: 'http://localhost:8080',
            // url: 'http://192.168.1.15:8080',
            tempComputer: {},
            // users: [],
            auxComputers: [],
            computers: [],
            passwordHidden: true,
            originalPasswords: {},
            modalData: {
                id: '',
                name: '',
                active: false,
                service_tag: '',
                ip: '',
                adquisition_date: '',
                comment: ''
            },
        }
    },
    mounted() {
        this.fetchComputers()
    },
    methods: {
        async fetchComputers() {
            try {
                const response = await fetch(this.url + '/computers');
                const data = await response.json();
                this.computers = data;
                this.auxComputers = data;
                document.getElementById("input-search").value = '';
            } catch (error) {
                console.error('Error al obtener usuarios:', error)
            }
        },
        async updateComputer() {
            try {
                const usersResponse = await fetch(this.url + '/computers', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                        // KEYS DEL CORS
                    },
                    body: JSON.stringify(this.modalData)
                });

                if (!usersResponse.ok) {
                    throw new Error('Network computers response was not ok');
                }

                await this.fetchComputers()
                Swal.fire({
                    title: 'Hecho!',
                    icon: 'success'
                });
            } catch (error) {
                console.error('There was a problem with the fetch operation:', error)
                Swal.fire({
                    title: 'Error!',
                    text: '(2) Hay un error en la captura de los datos: ' + error,
                    icon: 'warning'
                });
            }
        },
        findComputer(computer_name) {
            const computer_name_list = this.computers.filter((c) => c.name === computer_name);
            return computer_name_list;
        },
        findComputerByName(name) {
            const computer = this.computers.find((c) => c.name === name);
            return computer != null ? computer : null;
        },
        findUserComputerId(id_computer) {
            const user = this.users.find((u) => u.id_computer === id_computer);
            return user != undefined ? user : null;
        },
        modalHTML() {
            return `
                    <form class="form-container">
                        <label style="display: none;">ID</label>
                        <input style="display: none;" disabled value ="${this.modalData.id}" id="swal-input0" class="input input-text input-id" placeholder="[no modificable]" autocomplete="off">

                        <label>Nombre</label>
                        <input value ="${this.modalData.name ? this.modalData.name.trim() : ''}" id="swal-input1" class="input input-text" placeholder="Nombre" autocomplete="off">
                        
                        <label>Serice Tag</label>
                        <input value ="${this.modalData.service_tag ? this.modalData.service_tag.trim() : ''}" id="swal-input3" class="input input-text" placeholder="Servive Tag" autocomplete="off">

                        <label>IP</label>
                        <input value ="${this.modalData.ip ? this.modalData.ip.trim() : ''}" id="swal-input4" class="input input-text" placeholder="Dirección IP" autocomplete="off">
                        
                        <label>Fecha de Adquisición</label>
                        <input value ="${this.modalData.adquisition_date}" id="swal-input5" type="date" class="input input-text" placeholder="Fecha de Adquisición" autocomplete="off">
                        
                        <label>Comentarios</label>
                        <textarea maxlength="300" id="swal-input6" class="input input-textarea" placeholder="Comentarios adicionales">${this.modalData.comment}</textarea>
                    
                        <div>
                            <input ${this.modalData.active ? 'checked' : ''} id="swal-input2" type="checkbox" class="active-checkbox">
                            <span>Computadora activa    </span>
                        </div>
                    </form>   
                    
                    <style>
                    .form-container {
                            margin: 0 auto;
                            display: flex;
                            flex-direction: column;
                            align-items: start;
                            text-align: left;
                            padding: 30px;
                            font-family: sans-serif;
                            width: 400px;
                            user-select: none;
                        }

                        .input-text, .input-textarea {
                            all: initial;
                        }

                        .input {
                            background-color: #f0f0f0;
                            border-radius: 10px;
                            padding: 10px;
                            margin: 5px 0 25px 0;
                            font-size: 15px;
                            font-family: sans-serif;
                            width: 90%;
                        }

                        .input-id {
                            color: gray;
                        }
                        
                        input[type="checkbox"] ~ span{
                            margin-left: 10px;
                        }

                        .input-textarea {
                            width: 90%;
                            height: 50px;
                            padding: 10px;
                            font-size: 16px;
                            font-family: sans-serif;
                            border: 1px solid #ccc;
                            border-radius: 5px;
                            resize: vertical;
                            min-height: 50px;
                            max-height: 200px;
                        }

                        .input-textarea:focus {
                            border-color: #bdbdbd;
                        }

                        @media screen and (max-width: 768px) {
                        .form-container {
                            width: 100%;
                        }
                    </style>
                    `
        }
        ,
        openModal(computer) {
            this.modalData = {
                id: computer.id,
                name: computer.name,
                active: computer.active,
                service_tag: computer.service_tag,
                ip: computer.ip,
                adquisition_date: computer.adquisition_date,
                comment: computer.comment
            }

            // OPEN UPDATE COMPUTER MODAL
            this.tempComputer = {}
            Swal.fire({
                showCancelButton: true,
                confirmButtonText: 'Modificar',
                cancelButtonText: 'Cancelar',
                didRender: () => {
                    // TOGGLE HIDE PASSWORD IN THE MODAL
                    const checkbox = document.getElementById('show-hide-checkbox');
                    if (checkbox) {
                        checkbox.addEventListener('click', () => {
                            const passwordInput = document.getElementById('swal-input3');
                            if (passwordInput) {
                                passwordInput.type = checkbox.checked ? 'text' : 'password';
                            }
                        });
                    }
                },
                html: this.modalHTML(),
                preConfirm: async () => {
                    this.tempComputer = this.modalData.name;

                    // GET INPUT VALUES FROM MODAL AND SAVE IT
                    this.modalData = {
                        id: document.getElementById('swal-input0').value, // id
                        name:
                            document.getElementById('swal-input1').value.trim() == ''
                                ? null
                                : document.getElementById('swal-input1').value === '' ? null : document.getElementById('swal-input1').value.trim(), // name
                        active: document.getElementById('swal-input2').checked, // active
                        service_tag: document.getElementById('swal-input3').value === '' ? null : document.getElementById('swal-input3').value.trim(), // service_tag
                        ip: document.getElementById('swal-input4').value === '' ? null : document.getElementById('swal-input4').value.trim(), // ip
                        adquisition_date: document.getElementById('swal-input5').value, // adquisition_date
                        comment: document.getElementById('swal-input6').value // comment
                    }
                }
            }).then((result) => {
                // VALIDATION CONSTANTS
                const sameComputer = this.tempComputer === this.modalData.name;
                const computerExists = this.findComputer(this.modalData.name).length > 0 ? true : false;
                const fieldsRequired =
                    this.modalData.name === null;

                // DATA VALIDATION
                if (result.isConfirmed) {
                    try {
                        if (fieldsRequired) {
                            Swal.fire({
                                title: 'Error!',
                                text: 'El campo "Nombre" es requerido',
                                icon: 'warning'
                            })
                            return;
                        }

                        if (!sameComputer && computerExists) {
                            Swal.fire({
                                title: 'Error!',
                                text: 'Ya existe otro registro con ese nombre',
                                icon: 'warning'
                            })
                            return;
                        }
                        this.updateComputer()
                    } catch (error) {
                        Swal.fire({
                            title: 'Error!',
                            text: '(1) Hay un error en la captura de los datos: ' + error,
                            icon: 'warning'
                        });
                    }
                }
            })
        },
        newData() {
            this.computerData = {
                id: null,
                name: null,
                active: null,
                service_tag: null,
                ip: null,
                adquisition_date: null,
                comment: ''
            }

            return {
                id: '',
                name: '',
                active: '',
                service_tag: '',
                ip: false,
                adquisition_date: '',
                comment: ''
            }
        },
        togglePassword() {
            const toggleButton = document.querySelector('.btn-toggle')

            document.querySelectorAll('.password').forEach((cell) => {
                if (this.passwordHidden) {
                    cell.classList.remove('password-hidden');
                    toggleButton.textContent = '😌 Ocultar contraseñas';
                } else {
                    cell.classList.add('password-hidden');
                    toggleButton.textContent = '🙂 Mostrar contraseñas';
                }
            })
            this.passwordHidden = !this.passwordHidden
        },
        searchByInput() {
            const inputSearch = document.getElementById("input-search").value;

            if (inputSearch !== '') {
                this.computers = this.auxComputers.filter(c => c.name.includes(inputSearch))
            } else {
                this.computers = this.auxComputers;
            }
        },
        ordenarTabla(n) {
            var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
            table = document.getElementById("table");
            switching = true;

            // SET THE STARTING ORDER DIRECTION TO ASCENDING
            dir = "asc";
            while (switching) {
                switching = false;
                rows = table.rows;
                for (i = 1; i < (rows.length - 1); i++) {
                    shouldSwitch = false;
                    x = rows[i].getElementsByTagName("td")[n];
                    y = rows[i + 1].getElementsByTagName("td")[n];
                    if (dir == "asc") {
                        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                            shouldSwitch = true;
                            break;
                        }
                    } else if (dir == "desc") {
                        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                            shouldSwitch = true;
                            break;
                        }
                    }
                }
                if (shouldSwitch) {
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                    switchcount++;
                } else {
                    if (switchcount == 0 && dir == "asc") {
                        dir = "desc";
                        switching = true;
                    }
                }
            }
        }
    }
}
</script>

<style scoped>
/* GENERAL STYLES */
.text-center {
    text-align: center;
}

.container {
    width: 100%;
    overflow: hidden;
}

.header {
    height: 60px;
    width: 100%;
    display: flex;
    align-items: center;
    background-color: #f5f5f5;
    font-family: system-ui, sans-serif;
    font-weight: bolder;
    padding: 0 0 0 20px;
    border-bottom: 1px solid #e4e4e7;
}

.password-hidden {
    -webkit-text-security: disc !important;
}

/* CONTENT STYLES */
.content {
    padding: 30px;
    height: calc(100% - 60px);
    display: flex;
    flex-direction: column;
}

.content-inputs {
    display: flex;
}

.input-search,
.btn {
    border-radius: 10px;
    padding: 10px;
    margin-bottom: 20px;
    font-size: 15px;
    color: #6b7280;
    border: #e4e4e7 1px solid;
    height: 50px;
    margin-right: 5px;
    min-width: 50px;
}

/* BUTTON STYLES */
.btn:hover {
    cursor: pointer;
    color: var(--dark);
    background-color: var(--btn-selected);
}

/* TABLE STYLES */
.table-header th {
    font-weight: bold;
}

.table-container {
    overflow: scroll;
    flex-grow: 1;
}

table {
    font-family: Arial, sans-serif;
    border-collapse: collapse;
    border-radius: 10px;
    width: 100%;
    user-select: none;
}

th,
td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
}

th {
    cursor: pointer;
}

tbody>tr:hover {
    cursor: pointer;
    background-color: var(--btn-selected);
}

/* RESPONSIVE STYLES */
@media screen and (max-width: 768px) {
    .input-search {
        width: 100%;
    }

    .btn {
        border-radius: 10px;
        padding: 10px;
        margin-bottom: 20px;
        font-size: 13px;
        color: #6b7280;
        border: #e4e4e7 1px solid;
        height: 50px;
        margin-right: 5px;
        min-width: 50px;
    }

    .content {
        padding: 15px;
    }

    .table-container {
        border: 1px solid #ddd;
    }

    .content-inputs {
        display: block;
    }
}
</style>

