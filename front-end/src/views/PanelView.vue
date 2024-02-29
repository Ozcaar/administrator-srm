<script setup>
import { RouterLink } from 'vue-router'

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

    <div class="general-container">
        <aside class="sidebar-container">
            <div class="title-container">
                <h2 class="header">Admin Panel</h2>
            </div>

            <div class="nav-container">
                <nav class="nav">
                    <RouterLink class="btn" to="/panel/users">Usuarios</RouterLink>
                    <RouterLink class="btn" to="/panel/computers">Computadoras</RouterLink>
                    <RouterLink class="btn" to="/panel/passwords">Contraseñas SRM</RouterLink>
                    <!-- <RouterLink class="btn" to="/panel/mails">Correos</RouterLink> -->
                    <!-- <RouterLink class="btn" to="/panel/antivirus">Antivirus</RouterLink> -->
                    <!-- <RouterLink class="btn" to="/panel/office">Office</RouterLink> -->
                    <!-- <RouterLink class="btn" to="/panel/dameware">Dameware</RouterLink> -->
                    <!-- <RouterLink class="btn" to="/panel/extensions">Extensiones</RouterLink> -->
                </nav>
            </div>
        </aside>

        <slot></slot>

        <input name="input-menu" id="input-menu" class="input-menu" type="checkbox">
        <label class="btn-menu" for="input-menu">
            <span class="menu-span-1"></span>
            <span class="menu-span-2"></span>
            <span class="menu-span-3"></span>
        </label>
        <aside class="sidebar-container-mobile">
            <!-- <div class="title-container">
                <h2 class="header">Admin Panel</h2>
            </div> -->

            <nav class="nav-mobile">
                <RouterLink class="btn" to="/panel/users" @click="toggleCheckBox()">Usuarios</RouterLink>
                <RouterLink class="btn" to="/panel/computers" @click="toggleCheckBox()">Computadoras</RouterLink>
                <RouterLink class="btn" to="/panel/passwords" @click="toggleCheckBox()">Contraseñas SRM</RouterLink>
                <!-- <RouterLink class="btn" to="/panel/mails" @click="toggleCheckBox()">Correos</RouterLink> -->
                <!-- <RouterLink class="btn" to="/panel/antivirus" @click="toggleCheckBox()">Antivirus</RouterLink> -->
                <!-- <RouterLink class="btn" to="/panel/office" @click="toggleCheckBox()">Office</RouterLink> -->
                <!-- <RouterLink class="btn" to="/panel/dameware" @click="toggleCheckBox()">Dameware</RouterLink> -->
                <!-- <RouterLink class="btn" to="/panel/extensions" @click="toggleCheckBox()">Extensiones</RouterLink> -->
            </nav>
        </aside>


    </div>
</template>

<script>
export default {
    data() {
        return {
        }
    },
    mounted() {
    },
    methods: {
        async toggleCheckBox() {
            document.getElementById("input-menu").checked = false;
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
    background-color: white;

    /* border-bottom: var(--light-gray) solid 1px; */
    border-bottom: 1px solid #e4e4e7;
}

.header {
    font-family: system-ui, sans-serif;
    font-weight: bold;
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

.btn:hover {
    background-color: var(--btn-selected);
    cursor: pointer;
    color: var(--dark);
}

.input-menu,
.btn-menu,
.sidebar-container-mobile {
    display: none;
}

/* RESPONSIVE */

@media screen and (max-width: 768px) {

    .general-container {
        position: relative;
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
    .input-menu:checked~.btn-menu span:nth-child(1) {
        rotate: 45deg;
        transform: translate(4px, 3px);
    }

    .input-menu:checked~.btn-menu span:nth-child(2) {
        rotate: -45deg;
        transform: translate(4px, -3px);
        width: 24px;
    }

    .input-menu:checked~.btn-menu span:nth-child(3) {
        opacity: 0;
    }

    .btn-menu {
        display: flex;
        align-items: flex-end;
        flex-direction: column;
        position: absolute;
        gap: 8px;
        z-index: 2;
        cursor: pointer;
        margin-top: 20px;
        right: 20px;
        z-index: 3;

        transition: 500ms all;
    }

    .input-menu:checked~.btn-menu {
        transform: scale(120%) translate(0, 4px);
    }

    .btn-menu span {
        background-color: black;
        height: 2px;
        width: 24px;
        transition: 500ms all;
        transform-origin: center;
    }

    .btn-menu span:nth-child(2) {
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
</style>