<script setup>

// import { RouterLink } from 'vue-router'

</script>

<template>

    <div class="spinner-overlay" ref="spinner" style="display: none;">
        <div class="spinner"></div>
    </div>
    <div class="container">

        <div class="form-wrap">
            <h1 class="title">PANEL SRM</h1>

            <form class="form" @submit.prevent="login">
                <div>
                    <label for="user" class="label">Usuario</label>
                    <input type="text" id="user" name="user" placeholder="usuario" class="input-text">
                </div>

                <div>
                    <label for="password" class="label">Contraseña</label>
                    <input type="password" id="password" name="password" placeholder="password" class="input-text">
                </div>

                <span ref="wrongData" class="wrong-data hidden-wrong-data">Datos
                    incorrectos</span>

                <button type="submit" class="btn-submit">Login</button>
                <!-- <RouterLink class="btn" to="/panel">Regresar</RouterLink> -->
            </form>
        </div>

    </div>

</template>

<script>
export default {
    data() {
        return {
            url: 'http://10.21.11.156:8080'
        };
    },
    mounted() {
        sessionStorage.clear();
        this.$refs.wrongData.classList.add('hidden-wrong-data');
    },
    methods: {
        async login() {
            try {
                const userValue = document.getElementById('user').value;
                const passwordValue = document.getElementById('password').value;

                const queryString = new URLSearchParams();
                queryString.append('username', userValue);
                queryString.append('password', passwordValue);

                const response = await fetch(`${this.url}/auth?${queryString.toString()}`);
                const authToken = await response.text();

                if (response.ok) {
                    // Successful login
                    this.$refs.spinner.style.display = 'flex';
                    setTimeout(() => {
                        sessionStorage.setItem('srm-admin-user', userValue);
                        sessionStorage.setItem('srm-admin-token', authToken);
                        this.$router.push('/panel');
                    }, 1000);

                } else {
                    // Failed login
                    document.querySelector('.wrong-data').classList.remove('hidden-wrong-data');
                }
            } catch (error) {
                console.error('Error al realizar la solicitud:', error);
            }
        }
    }
}
</script>

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
    min-height: 220px;
    max-height: 280px;
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

.wrong-data {
    color: rgb(207, 13, 13);
    font-size: 15px;
}

.hidden-wrong-data {
    /* display: none; */
    color: transparent;
    user-select: none;
}

.btn-submit {
    width: 100%;
    padding: 10px;
    border: 0;
    border-radius: 5px;
    background-color: #18181b;
    color: white;
    border: 5px solid rgb(255, 255, 255);
}

.btn-submit:hover {
    border: 5px solid rgb(199, 199, 199);
    cursor: pointer;
}
</style>