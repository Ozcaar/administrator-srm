<script setup>
import PanelView from '../views/PanelView.vue'

import Swal from 'sweetalert2'
</script>

<template>
    <PanelView>
        <div class="container">
            <div class="header">
                <h2>Correos</h2>
            </div>
            <div class="content">
                <div class="content-inputs">
                    <input id="input-search" class="input-search" type="text" placeholder="🔍 Búsqueda por correo"
                        @input="searchByInput()" />

                    <button class="btn" @click="openModal(newData())">📧 Agregar correo</button>
                    <button class="btn btn-toggle" @click="togglePassword()">🔓 Mostrar contraseñas</button>
                    <!-- <button class="btn"><strong>❔</strong></button> -->
                </div>

                <div class="table-container" @click="handleClick">
                    <div class="spinner-table" ref="spinnerTable" style="display: flex;">
                        <div class="spinner"></div>
                    </div>
                    <table id="table">
                        <thead>
                            <tr class="table-header">
                                <th @click="ordenarTabla(0)">Servicio</th>
                                <th @click="ordenarTabla(1)">Usuario</th>
                                <th @click="ordenarTabla(2)">Correo</th>
                                <th @click="ordenarTabla(3)">Contraseña</th>
                                <th @click="ordenarTabla(4)">Correo de Recuperación</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <tr v-for="mail in mails" :key="mail.id" :id="`${mail.id}`" @click="openModal(mail)">
                                <td>{{ mail.service }}</td>
                                <td>{{ mail.user }}</td>
                                <td>{{ mail.mail }}</td>
                                <td class="password password-hidden">{{ mail.password }}</td>
                                <td>{{ mail.recovery_mail }}</td>
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
            urlWithParams: '',
            params: {
                username: sessionStorage.getItem('srm-admin-user'),
                token: sessionStorage.getItem('srm-admin-token')
            },
            tempMail: {},
            // users: [],
            auxMails: [],
            mails: [],
            passwordHidden: true,
            originalPasswords: {},
            modalData: {
                id: '',
                service: '',
                user: '',
                mail: '',
                password: '',
                recovery_mail: '',
            },
        }
    },
    mounted() {
        this.urlWithParams = new URL(this.url + '/mails');
        this.urlWithParams.search = new URLSearchParams(this.params).toString();
        this.fetchMails();
    },
    methods: {
        async fetchMails() {
            try {
                const response = await fetch(this.urlWithParams);
                const data = await response.json();
                this.mails = data;
                this.auxMails = data;
                document.getElementById("input-search").value = '';
                this.$refs.spinnerTable.style.display = 'none';
            } catch (error) {
                console.error('Error al obtener las cuentas:', error)
            }
        },
        async updateMail() {
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
                    throw new Error('Network mails response was not ok');
                }

                await this.fetchMails()
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
        async deleteMail() {
            try {
                const deleteURL = new URL(this.url + '/mails' + '/' + this.modalData.id);
                deleteURL.search = new URLSearchParams(this.params).toString();
                const usersResponse = await fetch(deleteURL, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                        // KEYS DEL CORS
                    },
                    body: JSON.stringify(this.modalData)
                });

                if (!usersResponse.ok) {
                    throw new Error('Network mails response was not ok');
                }

                await this.fetchMails()
                Swal.fire({
                    title: 'Eliminado!',
                    icon: 'error'
                });
            } catch (error) {
                console.error('There was a problem with the fetch operation:', error)
                Swal.fire({
                    title: 'Error!',
                    text: '(3) Hay un error en la captura de los datos: ' + error,
                    icon: 'warning'
                });
            }
        },
        findMail(mail_user_list) {
            const mail_user_list_list = this.mails.filter((m) => m.user === mail_user_list);
            return mail_user_list_list;
        },
        findMailByName(user) {
            const computer = this.mails.find((c) => c.name === user);
            return computer != null ? computer : null;
        },
        findMailId(id_service) {
            const service = this.mails.find((u) => u.id_computer === id_service);
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

                        <label>Correo</label>
                        <input value ="${this.modalData.mail ? this.modalData.mail : ''}" id="swal-input3" class="input input-text" placeholder="Usuario de Acceso" autocomplete="off">

                        <div class="">
                            <label>Contraseña</label>
                            <div class="password-field">
                                <input value ="${this.modalData.password}" type="${this.modalData.password === 'N/A' || this.modalData.password === 'S/D' ? 'text' : 'password'}" id="swal-input4" class="input input-text" placeholder="Contraseña" autocomplete="off">
                                <input ${this.modalData.password === 'N/A' || this.modalData.password === 'S/D' ? 'checked' : ''} type="checkbox" id="show-hide-checkbox">
                                <span>Mostrar contraseña</span>
                            </div>
                        </div>

                        <label>Correo de recuperación</label>
                        <input value ="${this.modalData.recovery_mail ? this.modalData.recovery_mail : ''}" id="swal-input5" class="input input-text" placeholder="Liga de Acceso" autocomplete="off">
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
        openModal(mail) {
            this.modalData = {
                id: mail.id,
                service: mail.service,
                user: mail.user,
                mail: mail.mail,
                password: mail.password,
                recovery_mail: mail.recovery_mail,
            }

            // OPEN UPDATE MAIL MODAL
            this.tempMail = {}
            Swal.fire({
                showCancelButton: true,
                showDenyButton: this.modalData.id != '' ? true : false,
                confirmButtonText: 'Modificar',
                denyButtonText: 'Eliminar',
                cancelButtonText: 'Cancelar',
                didRender: () => {
                    // TOGGLE HIDE PASSWORD IN THE MODAL
                    const checkbox = document.getElementById('show-hide-checkbox');
                    if (checkbox) {
                        checkbox.addEventListener('click', () => {
                            const passwordInput = document.getElementById('swal-input4');
                            if (passwordInput) {
                                passwordInput.type = checkbox.checked ? 'text' : 'password';
                            }
                        });
                    }
                },
                html: this.modalHTML(),
                preConfirm: async () => {
                    // this.tempMail = this.modalData.name;

                    // GET INPUT VALUES FROM MODAL AND SAVE IT
                    this.modalData = {
                        id: document.getElementById('swal-input0').value, // id
                        service:
                            document.getElementById('swal-input1').value == ''
                                ? null
                                : document.getElementById('swal-input1').value === '' ? null : document.getElementById('swal-input1').value, // service
                        user: document.getElementById('swal-input2').value, // user
                        mail: document.getElementById('swal-input3').value, // mail
                        password: document.getElementById('swal-input4').value === '' ? null : document.getElementById('swal-input4').value, // password
                        recovery_mail: document.getElementById('swal-input5').value === '' ? null : document.getElementById('swal-input5').value, // recovery mail
                    }
                }
            }).then((result) => {
                // VALIDATION CONSTANTS
                const fieldsRequired =
                    this.modalData.service === null ||
                    this.modalData.user === null ||
                    this.modalData.mail === null ||
                    this.modalData.password === null;

                // DATA VALIDATION
                if (result.isConfirmed) {
                    try {
                        if (fieldsRequired) {
                            Swal.fire({
                                title: 'Error!',
                                text: 'Los campos "Servicio", "Usuario", "Correo" y "Contraseña", son requeridos',
                                icon: 'warning'
                            })
                            return;
                        }
                        this.updateMail()
                    } catch (error) {
                        Swal.fire({
                            title: 'Error!',
                            text: '(1) Hay un error en la captura de los datos: ' + error,
                            icon: 'warning'
                        });
                    }
                } else if (result.isDenied) {
                    Swal.fire({
                        showConfirmButton: true,
                        showDenyButton: true,
                        confirmButtonText: 'Confirmar',
                        denyButtonText: 'Cancelar',
                        text: '¿Desea eliminar el correo ' + this.modalData.mail + ' ?',
                    }).then((res) => {
                        if (res.isConfirmed) {
                            this.deleteMail();
                        } else if (res.isDenied) {
                            Swal.fire("Cancelado", "", "info")
                        }
                    });
                };
            })
        },
        newData() {
            this.computerData = {
                id: null,
                service: null,
                user: null,
                mail: null,
                password: null,
                recovery_mail: null,
            }

            return {
                id: '',
                service: '',
                user: '',
                mail: '',
                password: '',
                recovery_mail: '',
            }
        },
        togglePassword() {
            const toggleButton = document.querySelector('.btn-toggle')

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
        searchByInput() {
            const inputSearch = this.removeAccents(document.getElementById("input-search").value.toLowerCase());

            if (inputSearch !== '') {
                this.mails = this.auxMails.filter(m => {
                    const nameWithoutAccents = this.removeAccents(m.user.toLowerCase());
                    return nameWithoutAccents.includes(inputSearch);
                });
            } else {
                this.mails = this.auxMails;
            }
        },
        removeAccents(str) {
            return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
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

<style scoped></style>
