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
                <div class="movile-nav">
                    <a href="#" id="menu_on">
                        <span></span>
                        <span></span>
                        <span></span>
                    </a>
                    <!-- <nav class="menu">
                        <ul>
                        <li>
                                <RouterLink class="btn" to="/panel/users">Usuarios</RouterLink>
                            </li>
                            <li>
                                <RouterLink class="btn" to="/panel/computers">Computadoras</RouterLink>
                            </li>
                            <li><a href="#">Servicios</a></li>
                            <li><a href="#">Contacto</a></li>
                        </ul>
                    </nav> -->
                </div>
            </div>
            <div class="content">
                <div class="content-inputs">
                    <input class="input-find" type="text" placeholder="🔍 Búsqueda por usuario (aún no funciona)" />

                    <button class="btn" @click="openModal(newData())">🧑‍💼 Agregar usuario</button>
                    <button class="btn btn-toggle" @click="togglePassword()">🙂 Mostrar contraseña</button>
                    <button class="btn"><strong>❔</strong></button>
                </div>

                <div class="table-container" @click="handleClick">
                    <table>
                        <thead>
                            <tr class="table-header">
                                <th>Usuario</th>
                                <th>Nombre</th>
                                <th>Contraseña</th>
                                <th>Activo</th>
                                <th>PC</th>
                                <th>Comentarios</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <tr v-for="user in users" :key="user.id" :id="`${user.id}`" class="user-row"
                                @click="openModal(user)">
                                <td>{{ user.user }}</td>
                                <td>{{ user.name }}</td>
                                <td class="password password-hidden">{{ user.password }}</td>
                                <td>{{ user.active }}</td>
                                <td>{{ findComputer(user.id_computer)['name'] }}</td>
                                <td>{{ user.comment }}</td>
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
            tempIdComputer: 0,
            tempUser: {},
            users: [],
            computers: [],
            passwordHidden: true,
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
        this.fetchUsers()
    },
    methods: {
        async fetchUsers() {
            try {
                const response = await fetch('http://10.21.11.156:8080/users')
                const data = await response.json()
                this.users = data
            } catch (error) {
                console.error('Error al obtener usuarios:', error)
            }

            try {
                const response = await fetch('http://10.21.11.156:8080/computers')
                const data = await response.json()
                this.computers = data
            } catch (error) {
                console.error('Error al obtener computadoras:', error)
            }
        },
        async updateUser() {
            try {
                console.log(this.modalData)
                const usersResponse = await fetch('http://10.21.11.156:8080/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                        // KEYS DEL CORS
                    },
                    body: JSON.stringify(this.modalData)
                })

                if (!usersResponse.ok) {
                    console.log(this.modalData)
                    throw new Error('Network users response was not ok')
                }

                await this.fetchUsers()
                Swal.fire({
                    title: 'Hecho!',
                    icon: 'success'
                })
            } catch (error) {
                console.error('There was a problem with the fetch operation:', error)
                Swal.fire({
                    title: 'Error!',
                    text: '(2) Hay un error en la captura de los datos: ' + error,
                    icon: 'warning'
                })
            }
        },
        findUser(user_name) {
            const user_name_list = this.users.filter((u) => u.user === user_name)
            return user_name_list
        },
        findComputer(id) {
            const computer = this.computers.find((c) => c.id === id)
            return computer != null ? computer : ''
        },
        findComputerByName(name) {
            const computer = this.computers.find((c) => c.name === name)
            return computer != null ? computer : null
        },
        findUserComputerId(id_computer) {
            const user = this.users.find((u) => u.id_computer === id_computer)
            return user != undefined ? user : null
        },
        async updateComputer() {
            const computersResponse = await fetch('http://10.21.11.156:8080/computers', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                    // KEYS DEL CORS
                },
                body: JSON.stringify(this.computerData)
            })

            if (!computersResponse.ok) {
                throw new Error('Network computers response was not ok')
            }
            const jsonResponse = await computersResponse.json()
            return jsonResponse
        },
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
                    const checkbox = document.getElementById('show-hide-checkbox')
                    if (checkbox) {
                        checkbox.addEventListener('click', () => {
                            const passwordInput = document.getElementById('swal-input3')
                            if (passwordInput) {
                                passwordInput.type = checkbox.checked ? 'text' : 'password'
                            }
                        })
                    }
                },
                html: `
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
                         `,
                preConfirm: async () => {
                    if (
                        document.getElementById('swal-input5').value.trim() != '' &&
                        document.getElementById('swal-input5').value.trim() != null
                    ) {
                        let computer = this.findComputerByName(document.getElementById('swal-input5').value)

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

                            computer = await this.updateComputer()
                        }
                        this.computerData = computer
                    }

                    console.log(this.modalData)

                    this.tempIdComputer = this.modalData.id_computer
                    this.tempUser = this.modalData.user

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
                    console.log(this.modalData)
                }
            }).then((result) => {
                const sameUser = this.tempUser === this.modalData.user
                const userExists = this.findUser(this.modalData.user).length > 0 ? true : false
                const computerIdNotFound = this.findUserComputerId(this.computerData.id) === null
                const sameIdOrNoComputerId =
                    this.tempIdComputer === this.modalData.id_computer || this.modalData.id_computer === null
                const fieldsRequired =
                    this.modalData.user === null ||
                    this.modalData.name === null ||
                    this.modalData.password === null

                if (result.isConfirmed) {
                    try {
                        if (fieldsRequired) {
                            Swal.fire({
                                title: 'Error!',
                                text: 'Los campos "Usuario", "Nombre" y "Contraseña" son requeridos',
                                icon: 'warning'
                            })
                            return
                        }

                        if (computerIdNotFound || sameIdOrNoComputerId) {
                            console.log(sameUser)
                            console.log(userExists)
                            console.log(!sameUser && userExists)
                            console.log(!sameUser && userExists && this.tempUser === '')
                            if (!sameUser && userExists && this.tempUser === '') {
                                Swal.fire({
                                    title: 'Error!',
                                    text: 'Ya existe otro registro con ese usuario',
                                    icon: 'warning'
                                })
                                return
                            }
                            this.updateUser()
                        } else {
                            console.log(this.modalData)
                            Swal.fire({
                                icon: 'error',
                                title: 'Error!',
                                text: 'Ya existe otro usuario registrado con esa PC'
                            })
                            return
                        }
                    } catch (error) {
                        Swal.fire({
                            title: 'Error!',
                            text: '(1) Hay un error en la captura de los datos: ' + error,
                            icon: 'warning'
                        })
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
            const toggleButton = document.querySelector('.btn-toggle')

            document.querySelectorAll('.password').forEach((cell) => {
                if (this.passwordHidden) {
                    cell.classList.remove('password-hidden')
                    toggleButton.textContent = '😌 Ocultar contraseñas'
                } else {
                    cell.classList.add('password-hidden')
                    toggleButton.textContent = '🙂 Mostrar contraseñas'
                }
            })
            this.passwordHidden = !this.passwordHidden
        }
    }
}
</script>

<style scoped>
/* HAMBURGUER MENU */
.menu-icon {
    position: fixed;
    top: 20px;
    right: 20px;
    width: 40px;
    height: 4px;
    background-color: #333;
    cursor: pointer;
    z-index: 9999;
    transition: transform 0.5s;
}

.menu-icon::before,
.menu-icon::after {
    content: '';
    position: absolute;
    width: 100%;
    height: 4px;
    background-color: #333;
    transition: transform 0.5s;
}

.menu-icon::before {
    top: -12px;
}

.menu-icon::after {
    top: 12px;
}

#menu-toggle {
    display: none;
}

.menu {
    position: fixed;
    top: 0;
    /* left: 0; */
    width: 100%;
    height: 100%;
    background-color: #fff;
    z-index: 9998;
    transform: translateY(-100%);
    transition: transform 0.5s;
}

/* .menu ul {
    list-style: none;
    padding: 0;
    margin: 50% 0 0 0;
    text-align: center;
}

.menu ul li {
    margin: 20px 0;
}

.menu ul li a {
    text-decoration: none;
    color: #333;
    font-size: 20px;
} */

/* 
#menu-toggle:checked+.menu {
    transform: translateY(0);
}

#menu-toggle:checked+.menu-icon {
    transform: rotate(90deg);
}

#menu-toggle:checked+.menu-icon::before {
    transform: translateY(12px) rotate(45deg);
}

#menu-toggle:checked+.menu-icon::after {
    transform: translateY(-12px) rotate(-45deg);
} */

/* END HAMBURGUER MENU */

.container {
    width: 100%;
    overflow: hidden;
}

.header {
    height: 60px;
    width: 100%;
    display: flex;
    align-items: center;
    /* justify-content: space-between; */
    background-color: #f5f5f5;
    font-family: system-ui, sans-serif;
    font-weight: bold;
    padding: 0 0 0 20px;
    /* border-bottom: var(--light-gray) solid 1px; */

    border-bottom: 1px solid #e4e4e7;
}

.password-hidden {
    -webkit-text-security: disc !important;
}

.content {
    padding: 30px;
    height: calc(100% - 60px);
    display: flex;
    flex-direction: column;
}

.content-inputs {
    display: flex;
}

.input-find,
.btn {
    border-radius: 10px;
    /* border: var(--light-gray) 1px solid; */
    padding: 10px;
    margin-bottom: 20px;
    font-size: 15px;
    /* color: var(--gray); */
    color: #6b7280;
    border: #e4e4e7 1px solid;
    height: 50px;
    margin-right: 5px;

    min-width: 50px;
}

.btn:hover {
    cursor: pointer;
    color: var(--dark);
    background-color: var(--btn-selected);
}

.btn {}

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

/* RESPONSIVE */

@media screen and (max-width: 768px) {
    .input-find {
        width: 100%;
    }

    .btn {
        border-radius: 10px;
        /* border: var(--light-gray) 1px solid; */
        padding: 10px;
        margin-bottom: 20px;
        font-size: 13px;
        /* color: var(--gray); */
        color: #6b7280;
        border: #e4e4e7 1px solid;
        height: 50px;
        margin-right: 5px;

        min-width: 50px;
    }

    .content {
        padding: 15px;
        /* height: calc(100% - 60px);
        display: flex;
        flex-direction: column; */
    }

    .table-container {
        border: 1px solid #ddd;
    }

    .content-inputs {
        display: block;
    }
}

/* END RESPONSIVE */
</style>
