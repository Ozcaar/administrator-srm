<script setup>
import { RouterLink } from 'vue-router'
import Swal from 'sweetalert2'
</script>

<template>
    <!-- <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
      </nav>
    </div>
  </header> -->
    <div class="spinner-overlay" ref="spinner" style="display: none;">
        <div class="spinner"></div>
    </div>

    <div class="general-container">
        <aside class="sidebar-container">
            <div class="title-container">
                <div class="header-logo"></div>
                <h2 class="header-title">Admin Panel</h2>
            </div>

            <div class="nav-container">
                <nav class="nav">
                    <div class="nav-top">
                        <RouterLink class="nav-btn-pc" to="/panel/users">Usuarios</RouterLink>
                        <RouterLink class="nav-btn-pc" to="/panel/computers">Computadoras</RouterLink>
                        <RouterLink class="nav-btn-pc" to="/panel/passwords">Contraseñas SRM</RouterLink>
                        <RouterLink class="nav-btn-pc" to="/panel/mails">Correos</RouterLink>
                        <RouterLink class="nav-btn-pc" to="/panel/admins">Administradores</RouterLink>
                        <!-- <RouterLink class="nav-btn-pc" to="/panel/antivirus">Antivirus</RouterLink> -->
                        <!-- <RouterLink class="nav-btn-pc" to="/panel/office">Office</RouterLink> -->
                        <!-- <RouterLink class="nav-btn-pc" to="/panel/dameware">Dameware</RouterLink> -->
                        <!-- <RouterLink class="nav-btn-pc" to="/panel/extensions">Extensiones</RouterLink> -->
                    </div>
                    <hr style="height: 1px; padding: 0; background-color: #dddddd;">
                    <div class="nav-bottom">
                        <a class="nav-btn-pc backup-btn" @click="saveData()">Guardar sin descargar</a>
                        <a class="nav-btn-pc backup-btn" @click="downloadData()">Guardar y descargar datos</a>
                        <a class="nav-btn-pc" @click="closeSession()">Cerrar sesion</a>
                    </div>
                </nav>
            </div>
        </aside>

        <slot></slot>

        <input name="input-menu" id="input-menu" class="input-menu" type="checkbox" ref="menuCheckbox">
        <label class="nav-btn-menu" for="input-menu">
            <span class="menu-span-1"></span>
            <span class="menu-span-2"></span>
            <span class="menu-span-3"></span>
        </label>
        <aside class="sidebar-container-mobile">
            <!-- <div class="title-container">
                <h2 class="header">Admin Panel</h2>
            </div> -->

            <nav class="nav-mobile">
                <RouterLink class="nav-btn" to="/panel/users">Usuarios</RouterLink>
                <RouterLink class="nav-btn" to="/panel/computers">Computadoras</RouterLink>
                <RouterLink class="nav-btn" to="/panel/passwords">Contraseñas SRM</RouterLink>
                <RouterLink class="nav-btn" to="/panel/mails">Correos</RouterLink>
                <RouterLink class="nav-btn" to="/panel/admins">Administradores</RouterLink>
                <!-- <RouterLink class="nav-btn" to="/panel/antivirus">Antivirus</RouterLink> -->
                <!-- <RouterLink class="nav-btn" to="/panel/office">Office</RouterLink> -->
                <!-- <RouterLink class="nav-btn" to="/panel/dameware">Dameware</RouterLink> -->
                <!-- <RouterLink class="nav-btn" to="/panel/extensions">Extensiones</RouterLink> -->

                <hr style="height: 1px; padding: 0; background-color: #dddddd;">

                <a class="nav-btn" @click="saveData()">Guardar sin descargar</a>
                <a class="nav-btn" @click="downloadData()">Guardar y descargar datos</a>
                <a class="nav-btn" @click="closeSession()">Cerrar sesion</a>
            </nav>
        </aside>


    </div>
</template>

<script>
export default {
    data() {
        return {
            url: 'http://' + window.location.hostname + ':8090',
            urlWithParamsBackup: '',
            urlWithParamsDownloadBackup: '',
            currentAut: '',
            currentToken: '',
            params: {
                username: sessionStorage.getItem('srm-admin-user'),
                token: sessionStorage.getItem('srm-admin-token')
            }
        }
    },
    mounted() {
        // this.url = window.location.hostname + ':8090';
        this.urlWithParamsBackup = new URL(this.url + '/admin/backup');
        this.urlWithParamsBackup.search = new URLSearchParams(this.params).toString();

        this.urlWithParamsDownloadBackup = new URL(this.url + '/admin/backup/downloadBackup');
        this.urlWithParamsDownloadBackup.search = new URLSearchParams(this.params).toString();

        this.currentAuthURL = new URL(this.url + '/auth/current');
        this.currentAuthURL.search = new URLSearchParams(this.params).toString();

        this.authenticate();
    },
    methods: {
        async authenticate() {
            const validLogin = this.params.username && this.params.token;

            try {
                const response = await fetch(this.currentAuthURL);
                if (!response.ok) {
                    throw new Error('Error verifying identity: ' + response.statusText);
                }
                this.currentToken = await response.text();
            } catch (error) {
                this.$router.push('/auth');
                throw new Error('Error verifying identity: ' + error);
            }

            if (!validLogin || await this.currentToken != this.params.token) {
                Swal.fire({
                    showCancelButton: false,
                    title: 'Es necesario volver a iniciar sesion',
                    confirmButtonText: 'Aceptar',
                }).then(() => {
                    this.$router.push('/auth');
                })
            }
        },
        async saveData() {
            try {
                // Generate backup
                await fetch(this.urlWithParamsBackup);
                Swal.fire({
                    title: 'Reespaldo realzado con éxito!',
                    //text: 'Reespaldo realzado con éxito!',
                    icon: 'success'
                })
            } catch (error) {
                console.error('Error al realizar el reespaldo:', error);
            }
        },
        async downloadData() {
            await this.saveData();

            try {
                // Download backup
                const response = await fetch(this.urlWithParamsDownloadBackup);
                if (!response.ok) {
                    throw new Error('Error downloading data: ' + response.statusText);
                }

                // Create a blob object from the response
                const blob = await response.blob();

                // Create a downloadable link (optional)
                const link = document.createElement('a');
                link.href = URL.createObjectURL(blob);
                link.download = 'backup_' + this.getCurrentDate() + '.csv';
                link.click();

                // Alternatively, trigger a custom event to handle download elsewhere
                const downloadEvent = new CustomEvent('download', { detail: blob });
                document.dispatchEvent(downloadEvent);
            } catch (error) {
                console.error('Error downloading data:', error);
            }
        },
        closeSession() {
            this.$refs.spinner.style.display = 'flex';
            this.$refs.menuCheckbox.checked = false;

            setTimeout(() => {
                sessionStorage.removeItem('srm-admin-user');
                sessionStorage.removeItem('srm-admin-token');
                this.$router.push('/auth');
            }, 1000);
        },
        getCurrentDate() {
            var currentDate = new Date();
            var year = currentDate.getFullYear();
            var month = (currentDate.getMonth() + 1).toString().padStart(2, '0'); // Agrega cero al principio si es necesario
            var day = currentDate.getDate().toString().padStart(2, '0'); // Agrega cero al principio si es necesario
            var formattedDate = year + '-' + month + '-' + day;
            return formattedDate;
        }
    }
}
</script>

<style>
.general-container {
    display: flex;
    width: 100%;
    height: 100vh;
    position: relative;
}

.sidebar-container {
    width: 100%;
    max-width: 350px;
    height: 100vh;
    display: flex;
    flex-direction: column;
    border-right: var(--light-gray) solid 1px;
    background-color: #f5f5f5;

    border-right: 1px solid #e4e4e7;

}

.title-container {
    height: 60px;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: var(--white);

    /* border-bottom: var(--light-gray) solid 1px; */
    border-bottom: 1px solid #e4e4e7;
    background-image: url("../../public/imgs/dot-grid.png");
}

.header-title {
    font-family: system-ui, sans-serif;
    font-weight: bold;
}

.header-logo {
    height: 40px;
    width: 40px;

    background-image: url(../../public/favicon.ico);
    background-repeat: no-repeat;
    background-position: center;
}

.nav {
    display: flex;
    flex-direction: column;
    flex: 1;
    align-items: center;
    background-color: var(--white-dark);
}

.nav * {
    display: flex;
    align-items: center;
    height: 30px;
    width: 250px;
    background-color: transparent;
    color: #6b7280;
    font-weight: bolder;
    text-align: left;
    padding: 20px;
    border: 0;
    border-radius: 10px;
    transition: all 100ms ease-in-out;
}

.nav-btn-pc {
    font-family: Arial, Helvetica, sans-serif;
    font-weight: normal;
    text-decoration: none;
    user-select: none;
}

.nav-btn-pc:hover {
    background-color: #dddddd;
}


.nav-btn {}

.nav-btn:hover {
    background-color: var(--btn-selected);
    cursor: pointer;
    color: var(--dark);
}

.input-menu,
.nav-btn-menu,
.sidebar-container-mobile {
    display: none;
}

.nav-top {
    height: auto;
    display: flex;
    flex-direction: column;
}

.nav-bottom {
    height: auto;
    display: flex;
    flex-direction: column;
    justify-self: end;
}

/* .backup-btn {
    -ms-flex-align: end;
    bottom: 0;
} */

/* ---------------- */

/* Estilos generales */

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
    /* Se reemplazó por la propiedad border-bottom */
}

.password-hidden {
    -webkit-text-security: disc !important;
}

.user-hidden,
.computer-hidden {
    display: none;
}

/* Estilos de contenido */
.content {
    padding: 30px;
    height: calc(100% - 60px);
    display: flex;
    flex-direction: column;
    background-image: url(../../public/imgs/dot-grid.png);
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

/* Estilos de botones */
.btn:hover {
    cursor: pointer;
    color: #111827;
    background-color: #f3f4f6;
}

/* Estilos de tablas */
.table-header th {
    font-weight: bold;
    background-color: #f5f5f5;
}

.table-container {
    position: relative;
    overflow: scroll;
    flex-grow: 1;
    background-color: white;
    outline: 1px solid #ddd;
    border-radius: 10px;
}

table {
    font-family: Arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
    user-select: none;
    border-radius: 10px;
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
    background-color: #f0f0f0;
}

.inactive {
    background-color: #ddbebe79;
}

.inactive:hover {
    background-color: #ddbebec2;
}

.cell-sm {
    min-width: 1 00px;
}

.cell-md {
    min-width: 200px;
}

.cell-lg {
    min-width: 300px;
}

/* Estilos responsivos */
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

/* ---------------- */


/* RESPONSIVE */

@media screen and (max-width: 768px) {

    .general-container {
        position: relative;
        min-height: 100vh;
        height: auto;
    }

    .sidebar-container {
        display: none;
    }

    .sidebar-container-mobile {
        position: absolute;
        background-color: #fafafa;
        height: 100%;
        right: 0;
        top: 0;
        overflow: hidden;
        /* filter: drop-shadow(-5px 0px 5px #ddd); */
        /* border: 1px solid #ddd; */
    }

    .table-container {
        flex-grow: initial;
        min-height: 300px;
    }

    .nav-mobile {
        margin-top: 60px;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .nav-mobile * {
        display: flex;
        flex-direction: column;
        padding: 0 40px;

        text-decoration: none;
        color: black;
        font-family: sans-serif;
        /* border: 1px solid black; */
        height: 45px;

        justify-content: center;

        background-color: #ececec;
        margin: 5px 0;
        width: 90%;
        border-radius: 5px;
    }

    /* HAMBURGUER MENU */
    /* Estilos para el menú */
    .sidebar-container-mobile {
        width: 0;
        display: initial;
        transition: 500ms all;
        z-index: 200;
    }

    .menu {
        position: relative;
    }

    .input-menu:checked~.sidebar-container-mobile {
        height: 100%;
        width: 70%;
    }

    /* Estilos para el input del menú */
    .input-menu {
        appearance: none;
        padding: 0;
        margin: 0;
        outline: none;
        pointer-events: none;
        display: none;
    }

    /* Estilos para el botón del menú */
    .input-menu:checked~.nav-btn-menu span:nth-child(1) {
        rotate: 45deg;
        transform: translate(4px, 3px);
    }

    .input-menu:checked~.nav-btn-menu span:nth-child(2) {
        rotate: -45deg;
        transform: translate(4px, -3px);
        width: 24px;
    }

    .input-menu:checked~.nav-btn-menu span:nth-child(3) {
        opacity: 0;
    }

    .nav-btn-menu {
        display: flex;
        align-items: flex-end;
        flex-direction: column;
        position: absolute;
        gap: 8px;
        z-index: 2;
        cursor: pointer;
        margin-top: 20px;
        right: 20px;
        z-index: 300;

        transition: 500ms all;
    }

    .input-menu:checked~.nav-btn-menu {
        transform: scale(120%) translate(0, 4px);
    }

    .nav-btn-menu span {
        background-color: black;
        height: 2px;
        width: 24px;
        transition: 500ms all;
        transform-origin: center;
    }

    .nav-btn-menu span:nth-child(2) {
        height: 2px;
        width: 18px;
    }

    /* Animación de apertura de la barra lateral */
    @keyframes openSidebar {
        0% {
            width: 0;
        }

        20% {
            width: 0;
        }

        80% {
            width: 360px;
        }

        100% {
            width: 360px;
        }
    }

    /* END HAMBURGUER MENU */
}

/* END RESPONSIVE */


/* Spinner styles */
.spinner-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(255, 255, 255, 0.7);
    z-index: 9999;
    justify-content: center;
    align-items: center;
}

.spinner-table {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgb(255, 255, 255);
    z-index: 99;
    /* display: flex; */
    justify-content: center;
    align-items: center;
}

.spinner {
    border: 4px solid #f3f3f3;
    border-top: 4px solid #3498db;
    border-radius: 50%;
    width: 50px;
    height: 50px;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}
</style>