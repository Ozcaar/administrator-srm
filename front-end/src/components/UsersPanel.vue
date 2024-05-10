<script setup>
// import { RouterLink } from 'vue-router'
import PanelView from '../views/PanelView.vue'

import Swal from 'sweetalert2'
</script>

<template>
    <PanelView>
        <div class="container">
            <div class="header">
                <h2>Usuarios</h2>
            </div>
            <div class="content">
                <div class="content-inputs">
                    <input id="input-search" class="input-search" type="text" placeholder="🔍 Búsqueda por nombre"
                        @input="searchByInput()" />

                    <button class="btn" @click="openModal(newData())">🧑‍💼 Agregar usuario</button>
                    <button class="btn btn-toggle-password" @click="togglePassword()">🔓 Mostrar contraseña</button>
                    <button class="btn btn-toggle-inactive" @click="toggleInactive()">🙂 Mostrar inactivos</button>
                    <!-- <button class="btn"><strong>❔</strong></button> -->
                </div>

                <div class="table-container" @click="handleClick">
                    <div class="spinner-table" ref="spinnerTable" style="display: flex;">
                        <div class="spinner"></div>
                    </div>
                    <table id="table">
                        <thead>
                            <tr class="table-header">
                                <th @click="ordenarTabla(0)" class="text-center">Usuario</th>
                                <th @click="ordenarTabla(1)" class="cell-md">Nombre</th>
                                <th @click="ordenarTabla(2)" class="cell-md">Contraseña</th>
                                <th @click="ordenarTabla(3)" class="text-center cell-sm">Activo</th>
                                <th @click="ordenarTabla(4)" class="text-center cell-md">PC</th>
                                <th @click="ordenarTabla(5)" class="cell-lg">Comentarios</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <tr v-for="user in users" :key="user.id" :id="`${user.id}`" class="user-row"
                                :class="{ 'inactive user-hidden': !user.active }" @click="openModal(user)">
                                <td class="text-center cell-md">{{ user.user }}</td>
                                <td class="cell-md">{{ user.name }}</td>
                                <td class="password password-hidden cell-md">{{ user.password }}</td>
                                <td class="text-center cell-sm">{{
                            user.active ? 'Si' : 'No' }}</td>
                                <td class="text-center cell-md">{{ findComputer(user.id_computer)['name'] }}</td>
                                <td class="cell-lg">{{ user.comment }}</td>
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
            url: 'http://' + window.location.hostname + ':8090',
            urlWithParams: '',
            urlWithParamsComputers: '',
            params: {
                username: sessionStorage.getItem('srm-admin-user'),
                token: sessionStorage.getItem('srm-admin-token')
            },
            tempIdComputer: 0,
            tempUser: {},
            users: [],
            auxUsers: [],
            computers: [],
            passwordHidden: true,
            inactiveHidden: true,
            originalPasswords: {},
            modalData: {
                id: '',
                user: '',
                name: '',
                password: '',
                active: false,
                id_computer: '',
                comment: ''
            },
            computerData: {
                id: '',
                name: '',
                active: false,
                service_tag: '',
                ip: '',
                adquisition_date: '',
                comment: ''
            }
        }
    },
    mounted() {
        //this.url = window.location.hostname + ':8090';
        this.urlWithParams = new URL(this.url + '/users');
        this.urlWithParams.search = new URLSearchParams(this.params).toString();

        this.urlWithParamsComputers = new URL(this.url + '/computers');
        this.urlWithParamsComputers.search = new URLSearchParams(this.params).toString();
        this.fetchUsers();
    },
    methods: {

        async fetchUsers() {
            try {
                const response = await fetch(this.urlWithParams);
                const data = await response.json();
                this.users = data;
                this.auxUsers = data;
                document.getElementById("input-search").value = '';
                this.$refs.spinnerTable.style.display = 'none';
            } catch (error) {
                console.error('Error al obtener usuarios:', error)
            }

            try {
                const response = await fetch(this.urlWithParamsComputers);
                const data = await response.json();
                this.computers = data;
            } catch (error) {
                console.error('Error al obtener computadoras:', error);
            }

        },
        async updateUser() {
            try {
                const usersResponse = await fetch(this.urlWithParams, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                        // KEYS DEL CORS
                    },
                    body: JSON.stringify(this.modalData)
                });

                if (!usersResponse.ok) {
                    throw new Error('Network users response was not ok');
                }

                await this.fetchUsers()
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
        findUser(user_name) {
            const user_name_list = this.users.filter((u) => u.user === user_name);
            return user_name_list;
        },
        findComputer(id) {
            const computer = this.computers.find((c) => c.id === id);
            return computer != null ? computer : '';
        },
        findComputerByName(name) {
            const computer = this.computers.find((c) => c.name === name);
            return computer != null ? computer : null;
        },
        findUserComputerId(id_computer) {
            const user = this.users.find((u) => u.id_computer === id_computer);
            return user != undefined ? user : null;
        },
        async updateComputer() {
            const computersResponse = await fetch(this.urlWithParamsComputers, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                    // KEYS DEL CORS
                },
                body: JSON.stringify(this.computerData)
            });

            if (!computersResponse.ok) {
                throw new Error('Network computers response was not ok');
            }
            const jsonResponse = await computersResponse.json();
            return jsonResponse;
        },
        modalHTML() {
            return `
                    <form class="form-container">
                        <label style="display: none;">ID</label>
                        <input style="display: none;" disabled value ="${this.modalData.id}" id="swal-input0" class="input input-text input-id" placeholder="[no modificable]" autocomplete="off">

                        <label>Usuario</label>
                        <input value ="${this.modalData.user.trim()}" id="swal-input1" class="input input-text" placeholder="Usuario / usrm" autocomplete="off">
                        
                        <label>Nombre</label>
                        <input value ="${this.modalData.name}" id="swal-input2" class="input input-text" placeholder="Nombre de usuario" autocomplete="off">
                        
                        <div class="">
                            <label>Contraseña</label>
                            <div class="password-field">
                                <input value ="${this.modalData.password.trim()}" type="${this.modalData.password.trim() === 'N/A' || this.modalData.password.trim() === 'S/D' ? 'text' : 'password'}" id="swal-input3" class="input input-text" placeholder="Contraseña" autocomplete="off">
                                <input ${this.modalData.password.trim() === 'N/A' || this.modalData.password.trim() === 'S/D' ? 'checked' : ''} type="checkbox" id="show-hide-checkbox">
                                <span>Mostrar contraseña</span>
                            </div>
                        </div>

                        <label>PC</label>
                        <input value ="${this.findComputer(this.modalData.id_computer)['name'] ? this.findComputer(this.modalData.id_computer)['name'] : ''}" id="swal-input5" class="input input-text" placeholder="Etiqueta de activo fijo" autocomplete="off">
                        
                        <label>Comentarios</label>
                        <textarea maxlength="300" id="swal-input6" class="input input-textarea" placeholder="Comentarios adicionales">${this.modalData.comment}</textarea>
                    
                        <div>
                            <input ${this.modalData.active ? 'checked' : ''} id="swal-input4" type="checkbox" class="active-checkbox">
                            <span>Usuario activo</span>
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

                        .password-container{
                        }
                        
                        .password-field {
                            margin-bottom: 20px
                        }
                        
                        .show-hide-checkbox {
                            /*background-color: transparent;*/
                            padding: 30px;
                            margin-bottom: 25px;
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
        openModal(user) {
            this.modalData = {
                id: user.id,
                user: user.user,
                name: user.name,
                password: user.password,
                active: user.active,
                id_computer: user.id_computer,
                comment: user.comment
            }

            // ABRIR MODAL DE MODIFICACIÓN DE USUARIO
            this.tempUser = {}
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
                    if (
                        document.getElementById('swal-input5').value.trim() != null
                    ) {
                        // VERIFICAR SI LA PC DADA YA EXISTE
                        let computer = this.findComputerByName(document.getElementById('swal-input5').value);
                        if (computer == null) {
                            this.computerData = {
                                id: null,
                                name: document.getElementById('swal-input5').value,
                                active: true,
                                service_tag: null,
                                ip: null,
                                adquisition_date: '',
                                comment: ''
                            }

                            computer = await this.updateComputer();
                        };
                        this.computerData = computer;
                    }

                    this.tempIdComputer = this.modalData.id_computer;
                    this.tempUser = this.modalData.user;

                    // OBTENER LOS DATOS DE LOS INPUTS DEL MODAL Y GUARDARLOS
                    this.modalData = {
                        id: document.getElementById('swal-input0').value, // id
                        user:
                            document.getElementById('swal-input1').value.trim() == ''
                                ? null
                                : document.getElementById('swal-input1').value, // user
                        name: document.getElementById('swal-input2').value, // name
                        password:
                            document.getElementById('swal-input3').value.trim() == ''
                                ? null
                                : document.getElementById('swal-input3').value, // password
                        active: document.getElementById('swal-input4').checked, // active
                        id_computer: this.computerData['id'], // id_computer
                        comment: document.getElementById('swal-input6').value // comment
                    }
                    console.log(this.modalData);
                }
            }).then((result) => {
                // CONSTANTES NECESARIAS PARA LA VALIDACIÓN
                const sameUser = this.tempUser === this.modalData.user;
                const userExists = this.findUser(this.modalData.user).length > 0 ? true : false;
                const computerIdNotFound = document.getElementById('swal-input5').value.trim() == '' ? true : (this.findUserComputerId(this.computerData.id) === null);
                const sameIdOrNoComputerId =
                    this.tempIdComputer === this.modalData.id_computer || this.modalData.id_computer === null;
                const fieldsRequired =
                    this.modalData.user === null ||
                    this.modalData.name === null ||
                    this.modalData.password === null;

                //VALIDACIÓN DE DATOS INGRESADOS
                if (result.isConfirmed) {
                    try {
                        if (fieldsRequired) {
                            Swal.fire({
                                title: 'Error!',
                                text: 'Los campos "Usuario", "Nombre" y "Contraseña" son requeridos',
                                icon: 'warning'
                            })
                            return;
                        }

                        if (computerIdNotFound || sameIdOrNoComputerId) {
                            if (!sameUser && userExists && this.tempUser === '') {
                                Swal.fire({
                                    title: 'Error!',
                                    text: 'Ya existe otro registro con ese usuario',
                                    icon: 'warning'
                                })
                                return;
                            }
                            this.updateUser()
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Error!',
                                text: 'Ya existe otro usuario registrado con esa PC'
                            })
                            return;
                        }
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
                user: '',
                name: '',
                password: '',
                active: false,
                id_computer: '',
                comment: ''
            }
        },
        togglePassword() {
            const toggleButton = document.querySelector('.btn-toggle-password')

            document.querySelectorAll('.password').forEach((cell) => {
                if (this.passwordHidden) {
                    cell.classList.remove('password-hidden');
                    toggleButton.textContent = '🔒 Ocultar contraseñas';
                } else {
                    cell.classList.add('password-hidden');
                    toggleButton.textContent = '🔓 Mostrar contraseñas';
                }
            })
            this.passwordHidden = !this.passwordHidden
        },
        toggleInactive() {
            const toggleButton = document.querySelector('.btn-toggle-inactive')

            document.querySelectorAll('.user-row').forEach((row) => {
                if (row.classList.contains('inactive')) {
                    if (this.inactiveHidden) {
                        row.classList.remove('user-hidden');
                        toggleButton.textContent = '😌 Ocultar inactivos';
                    } else {
                        row.classList.add('user-hidden');
                        toggleButton.textContent = '🙂 Mostrar inactivos';
                    }
                }
            })
            this.inactiveHidden = !this.inactiveHidden
        },
        searchByInput() {
            const inputSearch = this.removeAccents(document.getElementById("input-search").value.toLowerCase());

            if (inputSearch !== '') {
                this.users = this.auxUsers.filter(u => {
                    const nameWithoutAccents = this.removeAccents(u.name.toLowerCase());
                    return nameWithoutAccents.includes(inputSearch);
                });
            } else {
                this.users = this.auxUsers;
            }
        },
        removeAccents(str) {
            return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
        },
        ordenarTabla(n) {
            var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
            table = document.getElementById("table");
            switching = true;

            // Establece la dirección de orden inicial a ascendente
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

<style scoped></style>
