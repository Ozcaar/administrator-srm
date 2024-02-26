<script setup>
import PanelView from '../views/PanelView.vue'
</script>

<script>
export default {
    data() {
        return {
            pcs: []
        };
    },
    mounted() {
        this.fetchPCs();
    },
    methods: {
        async fetchPCs() {
            try {
                const response = await fetch('http://localhost:8080/computers');
                const data = await response.json();
                this.pcs = data;
            } catch (error) {
                console.error('Error al obtener computadoras:', error);
            }
        }
    }
};
</script>

<template>
    <PanelView>
        <div class="container">
            <div class="header">HOLA MUNDO</div>
            <div class="content">
                <div class="content-inputs">
                    <input class="input-find" type="text" placeholder="🔍 Búsqueda por usuario">

                    <button>Agregar Empleado</button>
                    <button><strong>?</strong></button>
                </div>

                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Service Tag</th>
                                <th>Ip</th>
                                <th>Activo</th>
                                <th>Fecha de Adquisición</th>
                                <th>Comentarios</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="pc in pcs" :key="pc.id">
                                <td> {{ pc.name }} </td>
                                <td> {{ pc.service_tag }} </td>
                                <td> {{ pc.password }} </td>
                                <td> {{ pc.active }} </td>
                                <td> {{ pc.id_computer }} </td>
                                <td> {{ pc.comment }} </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </PanelView>
</template>

<style scoped>
.container {
    width: 100%;
    overflow: hidden;
}

.header {
    height: 60px;
    width: 100%;
    display: flex;
    /* justify-content: center; */
    align-items: center;
    background-color: yellowgreen;
    border-bottom: var(--light-gray) solid 1px;
}

/* :root {
    --white: white;
    --white-dark: #fafbfb;
    --light-gray: #e4e4e7;
    --gray: #6b7280;
    --dark: #111827;
    --btn-selected: #f3f4f6;
} */

.content {
    /* width: 100%; */
    padding: 30px;
    height: calc(100% - 60px);
    display: flex;
    flex-direction: column;
}

.content-inputs {}

.input-find,
button {
    border-radius: 10px;
    border: var(--light-gray) 1px solid;
    padding: 10px;
    margin-bottom: 20px;
    font-size: 15px;
    color: var(--gray);
    /* width: 200px; */
}

button:hover {
    cursor: pointer;
    color: var(--dark);
    background-color: var(--btn-selected);
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
</style>