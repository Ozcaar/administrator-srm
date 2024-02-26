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
                    <!-- <RouterLink class="btn" to="/panel/correos">Correos</RouterLink> -->
                    <!-- <RouterLink class="btn" to="/antivirus">Antivirus</RouterLink> -->
                    <!-- <RouterLink class="btn" to="/office">Office</RouterLink> -->
                    <!-- <RouterLink class="btn" to="/dameware">Dameware</RouterLink> -->
                    <!-- <RouterLink class="btn" to="/extensiones">Extensiones</RouterLink> -->
                </nav>
            </div>
        </aside>

        <slot></slot>
    </div>
</template>

<style>
.general-container {
    display: flex;
    width: 100%;
    height: 100vh;
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

/* RESPONSIVE */
@media screen and (max-width: 768px) {
  .sidebar-container {
    display: none;
  }
}
/* END RESPONSIVE */
</style>