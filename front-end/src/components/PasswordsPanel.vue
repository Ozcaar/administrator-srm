<script setup>
import PanelView from '../views/PanelView.vue'

import Swal from 'sweetalert2'
</script>

<template>
    <PanelView>
        <div class="container">
            <div class="header">
                <h2>Contraseñas SRM</h2>
            </div>
            <div class="content">
                <div class="content-inputs">
                    <input id="input-search" class="input-search" type="text" placeholder="🔍 Búsqueda por servicio"
                        @input="searchByInput()" />

                    <button class="btn" @click="openModal(newData())">🔐 Agregar servicio</button>
                    <button class="btn btn-toggle" @click="togglePassword()">🙂 Mostrar contraseña</button>
                    <button class="btn"><strong>❔</strong></button>
                </div>

                <div class="table-container" @click="handleClick">
                    <table id="table">
                        <thead>
                            <tr class="table-header">
                                <th @click="ordenarTabla(0)">Servicio</th>
                                <th @click="ordenarTabla(1)">Usuario</th>
                                <th @click="ordenarTabla(2)">Contraseña</th>
                                <th @click="ordenarTabla(3)">Liga de Acceso</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <tr v-for="service in services" :key="service.id" :id="`${service.id}`" class="user-row"
                                @click="openModal(service)">
                                <td>{{ service.service }}</td>
                                <td>{{ service.user }}</td>
                                <td class="password password-hidden">{{ service.password }}</td>
                                <td><a :href="`${validLink(service.access_link)}`" target="_blank">{{ service.access_link
                                }}</a></td>
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
            tempService: {},
            // users: [],
            auxServices: [],
            services: [],
            passwordHidden: true,
            originalPasswords: {},
            modalData: {
                id: '',
                service: '',
                user: '',
                password: '',
                access_link: '',
            },
        }
    },
    mounted() {
        this.fetchServices()
    },
    methods: {
        async fetchServices() {
            try {
                const response = await fetch(this.url + '/services');
                const data = await response.json();
                this.services = data;
                this.auxServices = data;
                document.getElementById("input-search").value = '';
            } catch (error) {
                console.error('Error al obtener las cuentas:', error)
            }
        },
        async updateService() {
            try {
                const usersResponse = await fetch(this.url + '/services', {
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

                await this.fetchServices()
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
        findService(service_user) {
            const service_user_list = this.services.filter((s) => s.user === service_user);
            return service_user_list;
        },
        findServiceByName(user) {
            const computer = this.services.find((c) => c.name === user);
            return computer != null ? computer : null;
        },
        findServiceId(id_service) {
            const service = this.services.find((u) => u.id_computer === id_service);
            return service != undefined ? service : null;
        },
        modalHTML() {
            return `
                    <form class="form-container">
                        <label style="display: none;">ID</label>
                        <input style="display: none;" disabled value ="${this.modalData.id}" id="swal-input0" class="input input-text input-id" placeholder="[no modificable]" autocomplete="off">

                        <label>Servicio</label>
                        <input value ="${this.modalData.service ? this.modalData.service : ''}" id="swal-input1" class="input input-text" placeholder="Nombre del servicio" autocomplete="off">
                        
                        <label>Usuario</label>
                        <input value ="${this.modalData.user ? this.modalData.user : ''}" id="swal-input2" class="input input-text" placeholder="Usuario de Acceso" autocomplete="off">

                        <div class="">
                            <label>Contraseña</label>
                            <div class="password-field">
                                <input value ="${this.modalData.password}" type="${this.modalData.password === 'N/A' || this.modalData.password === 'S/D' ? 'text' : 'password'}" id="swal-input3" class="input input-text" placeholder="Contraseña" autocomplete="off">
                                <input ${this.modalData.password === 'N/A' || this.modalData.password === 'S/D' ? 'checked' : ''} type="checkbox" id="show-hide-checkbox">
                                <span>Mostrar contraseña</span>
                            </div>
                        </div>

                        <label>Liga de Acceso</label>
                        <input value ="${this.modalData.access_link ? this.modalData.access_link : ''}" id="swal-input4" class="input input-text" placeholder="Liga de Acceso" autocomplete="off">
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
        openModal(service) {
            this.modalData = {
                id: service.id,
                service: service.service,
                user: service.user,
                password: service.password,
                access_link: service.access_link,
            }

            // OPEN UPDATE COMPUTER MODAL
            this.tempService = {}
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
                    // this.tempService = this.modalData.name;

                    // GET INPUT VALUES FROM MODAL AND SAVE IT
                    this.modalData = {
                        id: document.getElementById('swal-input0').value, // id
                        service:
                            document.getElementById('swal-input1').value == ''
                                ? null
                                : document.getElementById('swal-input1').value === '' ? null : document.getElementById('swal-input1').value, // service
                        user: document.getElementById('swal-input2').value, // user
                        password: document.getElementById('swal-input3').value === '' ? null : document.getElementById('swal-input3').value, // password
                        access_link: document.getElementById('swal-input4').value === '' ? null : document.getElementById('swal-input4').value, // access link
                    }
                }
            }).then((result) => {
                // VALIDATION CONSTANTS
                // const sameComputer = this.tempService === this.modalData.name;
                // const computerExists = this.findService(this.modalData.name).length > 0 ? true : false;
                const fieldsRequired =
                    this.modalData.service === null;

                // DATA VALIDATION
                if (result.isConfirmed) {
                    try {
                        if (fieldsRequired) {
                            Swal.fire({
                                title: 'Error!',
                                text: 'El campo "Servicio" es requerido',
                                icon: 'warning'
                            })
                            return;
                        }

                        // if (!sameComputer && computerExists) {
                        //     Swal.fire({
                        //         title: 'Error!',
                        //         text: 'Ya existe otro registro con ese nombre',
                        //         icon: 'warning'
                        //     })
                        //     return;
                        // }
                        this.updateService()
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
                service: null,
                user: null,
                password: null,
                access_link: null,
            }

            return {
                id: '',
                service: '',
                user: '',
                password: '',
                access_link: false,
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
                this.services = this.auxServices.filter(s => s.service.includes(inputSearch))
            } else {
                this.services = this.auxServices;
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
        },
        validLink(url) {
            const regex = /^(ftp|http|https|ws|wss|ftps|file|dns):\/\/[\w-]+(\.[\w-]+)+([\w\-.,@?^=%&:/~+#]*[\w\-@?^=%&/~+#])?$/;

            if (!regex.test(url)) {
                url = "https://" + url;
                if (!regex.test(url)) url = "/404";
            }

            return url;
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

