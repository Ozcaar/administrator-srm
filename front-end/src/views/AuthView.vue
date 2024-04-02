<script setup>

// import { RouterLink } from 'vue-router'

</script>

<template>

    <div class="container">

        <div class="form-wrap">
            <h1 class="title">PANEL SRM</h1>

            <form class="form" action="" method="POST">
                <div>
                    <label for="user" class="label">Usuario</label> <span class="invalid-data invalid-user">*</span>
                    <input type="text" id="user" name="user" placeholder="usuario" class="input-text">
                </div>

                <div>
                    <label for="password" class="label">Contraseña</label> <span
                        class="invalid-data invalid-password">*</span>
                    <input type="password" id="password" name="password" placeholder="password" class="input-text">
                </div>

                <span class="invalid-data">Datos incorrectos</span>

                <button type="submit" class="btn-submit">Login</button>
                <!-- <RouterLink class="btn" to="/panel">Regresar</RouterLink> -->
            </form>
        </div>

    </div>

</template>

<style scoped>
.container {
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #18181b;
    font-family: system-ui;
    background-image: url(../../public/imgs/dot-grid.png);
}

.form-wrap {
    width: 300px;
    margin: 0 auto;
    border: 1px solid #e5e7eb;
    border-radius: 5px;
    padding: 50px 30px;
    background-color: white;
    box-shadow: #83838b 0px 0px 10px -5px;
}

.title {
    text-align: center;
    margin-bottom: 20px;
}

.form {
    display: flex;
    height: 220px;
    flex-direction: column;
    justify-content: space-between;
}

.label {
    font-weight: 500;
}

.input-text {
    width: 100%;
    border: 1px solid #e5e7eb;
    border-radius: 5px;
    padding: 10px;
    margin-top: 10px;
}

.invalid-data {
    color: rgb(207, 13, 13);
    font-size: 15px;
}

.btn-submit {
    width: 100%;
    padding: 10px;
    border: 0;
    border-radius: 5px;
    background-color: #18181b;
    color: white;
}
</style>