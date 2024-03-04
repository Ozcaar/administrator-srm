<script setup>

import { RouterLink } from 'vue-router'

</script>

<template>
    <div class="container">

        <div class="header">
            <h1 class="title">404</h1>
            <p class="subtitle">Oops! No encontramos la página que buscabas.</p>
        </div>
        <RouterLink class="btn" to="/panel">Regresar</RouterLink>

    </div>
</template>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
    text-align: center;
}

.title {
    font-size: 80px;
    font-family: Verdana;
    font-weight: bold;
    color: rgb(10, 10, 10);
    margin-bottom: 10px;
}

.subtitle {
    font-size: 25px;
    font-family: Verdana;
    color: rgb(104, 104, 104);
    font-weight: lighter;
    margin-bottom: 20px;
}

.btn {
    font-size: 15px;
    font-family: Verdana;
    border-radius: 10px;
    padding: 15px 40px;
    margin: 10px;
    color: rgb(104, 104, 104);
    border: #e4e4e7 1px solid;
    text-decoration: none;
}
</style>