<script setup>
import PanelView from '../views/PanelView.vue'

import Swal from 'sweetalert2'
</script>

<template>
    <PanelView>
        <div class="container">
            <div class="header">
                <h2>Administradores</h2>
            </div>
            <div class="content">
                <div class="content-inputs">
                    <input id="input-search" class="input-search" type="text" placeholder="🔍 Búsqueda por usuario"
                        @input="searchByInput()" />

                    <button class="btn" @click="openModal(newData())">👨‍✈️ Agregar usuario administrador</button>
                    <!-- <button class="btn btn-toggle" @click="togglePassword()">🔓 Mostrar contraseñas</button> -->
                    <!-- <button class="btn"><strong>❔</strong></button> -->
                </div>

                <div class="table-container" @click="handleClick">
                    <div class="spinner-table" ref="spinnerTable" style="display: flex;">
                        <div class="spinner"></div>
                    </div>
                    <table id="table">
                        <thead>
                            <tr class="table-header">
                                <th @click="ordenarTabla(0)" class="cell-md text-center">Usuario</th>
                                <th @click="ordenarTabla(1)" class="cell-md text-center">Ultimo inicio de sesión</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <!-- <tr v-for="admin in admins" :key="admin.id" :id="`${admin.id}`" @click="openModal(admin)"> -->
                            <tr v-for="admin in admins" :key="admin.id" :id="`${admin.id}`" class="row-md">
                                <td class="cell-md text-center">{{ admin.username }}</td>
                                <td class="cell-md text-center">{{ admin.last_login }}</td>
                                <td class="borderless">
                                    <div class="row-buttons-container">
                                        <button class="btn-admin btn-chpw" @click="changePassword(admin)"> Cambiar
                                            contraseña</button>
                                        <button class="btn-admin btn-delete" @click="openModal(admin)">Eliminar</button>
                                    </div>
                                </td>
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
            tempAdmin: {},
            // users: [],
            auxAdmins: [],
            admins: [],
            //passwordHidden: true,
            //originalPasswords: {},
            modalData: {
                id: '',
                username: '',
                last_login: '',
            },
        }
    },
    mounted() {
        this.urlWithParams = new URL(this.url + '/admins');
        this.urlWithParams.search = new URLSearchParams(this.params).toString();
        this.fetchAdmins();
    },
    methods: {
        async fetchAdmins() {
            try {
                const response = await fetch(this.urlWithParams);
                const data = await response.json();
                this.admins = data;
                this.auxAdmins = data;
                document.getElementById("input-search").value = '';
                this.$refs.spinnerTable.style.display = 'none';
            } catch (error) {
                console.error('Error al obtener la lista de administradores:', error)
            }
        },
        async updateAdmin() {
            try {
                const adminsResponse = await fetch(this.urlWithParams, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                        // KEYS DEL CORS
                    },
                    body: JSON.stringify(this.modalData)
                });

                if (!adminsResponse.ok) {
                    throw new Error('Network admins response was not ok');
                }

                await this.fetchAdmins()
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
        async deleteAdmin() {
            try {
                const deleteURL = new URL(this.url + '/admins' + '/' + this.modalData.id);
                deleteURL.search = new URLSearchParams(this.params).toString();
                const adminsResponse = await fetch(deleteURL, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                        // KEYS DEL CORS
                    },
                    body: JSON.stringify(this.modalData)
                });

                if (!adminsResponse.ok) {
                    throw new Error('Network computers response was not ok');
                }

                await this.fetchAdmins()
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
        modalChangePassword() {

        },
        findAdmin(admin_user) {
            const admin_user_list = this.admins.filter((a) => a.user === admin_user);
            return admin_user_list;
        },
        findAdminByName(admin) {
            const computer = this.admins.find((a) => a.name === admin);
            return computer != null ? computer : null;
        },
        findAdminId(id_service) {
            const service = this.mails.find((u) => u.id_computer === id_service);
            return service != undefined ? service : null;
        },
        modalHTML() {
            return `
                    <form class="form-container">
                        <label style="display: none;">ID</label>
                        <input style="display: none;" disabled value ="${this.modalData.id}" id="swal-input0" class="input input-text input-id" placeholder="[no modificable]" autocomplete="off">

                        <label>Nombre de usuario</label>
                        <input value ="${this.modalData.username ? this.modalData.username : ''}" id="swal-input1" class="input input-text" placeholder="Nombre del servicio" autocomplete="off">

                        <label>Último inicio de sesión</label>
                        <input value ="${this.modalData.last_login ? this.modalData.last_login : ''}" id="swal-input2" class="input input-text" placeholder="Usuario de Acceso" autocomplete="off" type="date">
                    </form>   

                    <style>
                    .form-container {
                            margin: 0 auto;
                            display: flex;
                            flex-direction: column;
                            align-items: start;
                            text-align: left;
                            padding: 30px 30px 0 30px;
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
        },
        openModal(admin) {
            this.modalData = {
                id: admin.id,
                username: admin.username,
                last_login: admin.last_login
            }

            // OPEN UPDATE MAIL MODAL
            this.tempMail = {}
            Swal.fire({
                title: '',
                showCancelButton: true,
                showDenyButton: this.modalData.id != '' ? true : false,
                confirmButtonText: 'Modificar',
                cancelButtonText: 'Cancelar',
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
                                text: 'La contraseña es un campo requerido (mayor a 5 caractéres)',
                                icon: 'warning'
                            })
                            return;
                        }
                        this.updateAdmin()
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
            // this.computerData = {
            //     id: null,
            //     service: null,
            //     user: null,
            //     mail: null,
            //     password: null,
            //     recovery_mail: null,
            // }

            return {
                id: '',
                service: '',
                user: '',
                mail: '',
                password: '',
                recovery_mail: '',
            }
        },
        // togglePassword() {
        //     const toggleButton = document.querySelector('.btn-toggle')

        //     document.querySelectorAll('.password').forEach((cell) => {
        //         if (this.passwordHidden) {
        //             cell.classList.remove('password-hidden');
        //             toggleButton.textContent = '🔒 Ocultar contraseñas';
        //         } else {
        //             cell.classList.add('password-hidden');
        //             toggleButton.textContent = '🔓 Mostrar contraseñas';
        //         }
        //     })
        //     this.passwordHidden = !this.passwordHidden
        // },
        searchByInput() {
            const inputSearch = this.removeAccents(document.getElementById("input-search").value.toLowerCase());

            if (inputSearch !== '') {
                this.admins = this.auxAdmins.filter(a => {
                    const nameWithoutAccents = this.removeAccents(a.username.toLowerCase());
                    return nameWithoutAccents.includes(inputSearch);
                });
            } else {
                this.admins = this.auxAdmins;
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
        // validLink(url) {
        //     const regex = /^(ftp|http|https|ws|wss|ftps|file|dns):\/\/[\w-]+(\.[\w-]+)+([\w\-.,@?^=%&:/~+#]*[\w\-@?^=%&/~+#])?$/;

        //     if (!regex.test(url)) {
        //         url = "https://" + url;
        //         if (!regex.test(url)) url = "/404";
        //     }

        //     return url;
        // }
    }
}
</script>

<style scoped>
.table-container>table {
    /* width: auto; */
    min-width: 700px;
    /* max-width: 500px; */
}

.borderless {
    border: 0;
    padding: 0;
}

.row-buttons-container {
    display: flex;
    justify-content: space-around;
    width: 350px;
}

.row-md {
    height: 50px;
}

.row-md:hover {
    background-color: initial;
    cursor: initial;
}

.btn-admin {
    height: 40px;
    border-radius: 5px;
    border: 0;
    padding: 10px;
    color: white;
    font: Arial;
    font-size: 16px;
    border: 3px solid transparent;

    cursor: pointer;
}

.btn-chpw {
    background-color: #7066e0;
}

.btn-chpw:hover {
    background-color: #655cc9;
}

.btn-chpw:focus {
    outline: 3px solid #b7b2ef;
}

.btn-delete {
    background-color: #dc3741;
}

.btn-delete:hover {
    background-color: #c6313a;
}

.btn-delete:focus {
    outline: 3px solid #ed9ba0;
}
</style>
