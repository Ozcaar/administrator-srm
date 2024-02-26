<script setup>
import PanelView from '../views/PanelView.vue'

import Swal from 'sweetalert2' // es6 module
</script>

<template>
    <PanelView>
        <div class="container">
            <div class="header">HOLA MUNDO</div>
            <div class="content">
                <div class="content-inputs">
                    <input class="input-find" type="text" placeholder="🔍 Búsqueda por usuario" />

                    <button>Agregar Empleado</button>
                    <button>👁️ mostrar contraseña</button>
                    <button><strong>?</strong></button>
                </div>

                <div class="table-container" @click="handleClick">
                    <table>
                        <thead>
                            <tr>
                                <th>Usuario</th>
                                <th>Nombre</th>
                                <th>Contraseña</th>
                                <th>Activo</th>
                                <th>PC</th>
                                <th>Comentarios</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <!--tr v-for="user in users" :key="user.id" :id="`${user.id}`" class="user-row">
                                <td>{{ user.user }}</td>
                                <td>{{ user.name }}</td>
                                <td>{{ user.password }}</td>
                                <td>{{ user.active }}</td>
                                <td>{{ user.id_computer }}</td>
                                <td>{{ user.comment }}</td>
                            </tr -->
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
            users: [],
            modalData: {
                id: '',
                user: '',
                name: '',
                password: '',
                active: false,
                pc_id: '',
                comment: ''
            }
        };
    },
    mounted() {
        this.fetchUsers().then(() => {
            // Una vez que se obtengan los usuarios, agregamos el event listener a las filas de usuario
            // this.addClickListenerToUserRows()
        })
    },
    methods: {
        async fetchUsers() {
            try {
                const response = await fetch('http://localhost:8080/users')
                const data = await response.json()
                this.users = data
            } catch (error) {
                console.error('Error al obtener usuarios:', error)
            }
        },
        addClickListenerToUserRows() {
            // Obtener todas las filas de usuario por su clase
            var filasUsuarios = document.querySelectorAll('.user-row')

            // Iterar sobre las filas de usuario y agregar un event listener a cada una
            filasUsuarios.forEach(fila => {

                // MANEJAR EL EVENTO AL CLICKEAR EN UNA FILA
                fila.addEventListener('click', async function () {

                    // ABRIR MODAL DE MODIFICACIÓN DE USUARIO
                    await Swal.fire({
                        showCancelButton: true,
                        confirmButtonText: 'Modificar',
                        cancelButtonText: 'Cancelar',
                        html: `
                            <form class="form-container">
                                <label>Id</label>
                                <input disabled value ="${fila.id}" id="swal-input0" class="input input-text input-id" placeholder="Id">

                                <label>Usuario</label>
                                <input value ="${fila.querySelector('td:nth-child(1)').textContent}" id="swal-input1" class="input input-text" placeholder="Usuario / usrm">
                                
                                <label>Nombre</label>
                                <input value ="${fila.querySelector('td:nth-child(2)').textContent}" id="swal-input2" class="input input-text" placeholder="Nombre de usuario">
                                
                                <div class="">
                                    <label>Contraseña</label>
                                    <div class="password-field">
                                        <input value ="${fila.querySelector('td:nth-child(3)').textContent}" type="${fila.querySelector('td:nth-child(3)').textContent.trim() === 'N/A' || fila.querySelector('td:nth-child(3)').textContent.trim() === 'S/D' ? 'text' : 'password'}" id="swal-input3" class="input input-text" placeholder="Contraseña">
                                        <input ${fila.querySelector('td:nth-child(3)').textContent.trim() === 'N/A' || fila.querySelector('td:nth-child(3)').textContent.trim() === 'S/D' ? 'checked' : ''} type="checkbox" class="show-hide-checkbox">
                                        <span>Mostrar contraseña</span>
                                    </div>
                                </div>

                                <label>PC</label>
                                <input value ="${fila.querySelector('td:nth-child(5)').textContent}" id="swal-input5" class="input input-text" placeholder="Etiqueta de activo fijo">
                                
                                <label>Comentarios</label>
                                <textarea maxlength="300" id="swal-input6" class="input input-textarea" placeholder="Comentarios adicionales">${fila.querySelector('td:nth-child(6)').textContent}</textarea>
                            
                                <div>
                                    <input ${fila.querySelector('td:nth-child(4)').textContent.trim() === 'true' ? 'checked' : ''} id="swal-input4" type="checkbox" class="active-checkbox">
                                    <span>Usuario activo</span>
                                </div>
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
                                    width: 100%;
                                }

                                .input-id {
                                    color: gray;
                                }

                                .password-container{
                                }
                                
                                .password-field {
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
                                    width: 100%;
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
                            </style>
                        `,
                        preConfirm: () => {
                            return [

                                // BUSCAR LOS CAMPOS DEL FORMULARIO EN LA VENTANA MODAL
                                document.getElementById("swal-input0").value, // id
                                document.getElementById("swal-input1").value, // user
                                document.getElementById("swal-input2").value, // name
                                document.getElementById("swal-input3").value, // password
                                document.getElementById("swal-input4").checked, // active
                                document.getElementById("swal-input5").value, // pc_id
                                document.getElementById("swal-input6").value // comment
                            ]
                        }
                    }).then((result) => {
                        const formValues = result.value;

                        // MODIFICAR USUARIO
                        if (result.isConfirmed) {
                            try {

                                // PREPARAR BODY PARA MODIFICAR EL USUARIO
                                const user = {
                                    "id": formValues[0],
                                    "user": formValues[1],
                                    "name": formValues[2],
                                    "password": formValues[3],
                                    "active": formValues[4],
                                    "id_computer": formValues[5],
                                    "comment": formValues[6]
                                }

                                fetch('http://localhost:8080/users', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json'
                                        // KEYS DEL CORS
                                    },
                                    body: JSON.stringify(user)
                                })
                                    .then(response => {

                                        // MANEJAR UN ERROR DE PARTE DEL SERVIDOR O LA RED
                                        if (!response.ok) {
                                            throw new Error('Network response was not ok');
                                        }
                                        return response.json();
                                    })
                                    .then(() => {
                                        // .then(data => {
                                        // console.log('User created:', data);

                                        this.fetchUsers().then(() => {
                                            this.addClickListenerToUserRows();
                                        });
                                        Swal.fire({
                                            title: 'Hecho!',
                                            //text: "Your file has been deleted.",
                                            icon: 'success'
                                        });
                                    })
                                    .catch(error => {
                                        console.error('There was a problem with your fetch operation:', error);
                                        Swal.fire({
                                            title: "Error!",
                                            //text: "Your file has been deleted.",
                                            icon: "warning"
                                        });
                                    });


                            } catch (error) {
                                console.error(error);
                                // METER ALERTA DE ERROR
                            }

                        }

                        // if (result.isDismissed) {
                        //     Swal.fire({
                        //     title: "Cancelado!",
                        //     //text: "Your file has been deleted.",
                        //     icon: "error"
                        //     });
                        // }
                    })

                    // await console.log(formValues)
                    // Puedes acceder a los datos de la fila aquí
                    // console.log("Usuario:", fila.querySelector("td:nth-child(1)").textContent);
                    // console.log("Nombre:", fila.querySelector("td:nth-child(2)").textContent);
                    // Y así sucesivamente para los otros campos de la fila
                })
            })
        },
        async updateUser() {
            try {
                const response = await fetch('http://localhost:8080/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                        // KEYS DEL CORS
                    },
                    body: JSON.stringify(this.modalData)
                });
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                await response.json();
                await this.fetchUsers();
                this.closeModal();
                Swal.fire({
                    title: 'Hecho!',
                    icon: 'success'
                });
            } catch (error) {
                console.error('There was a problem with your fetch operation:', error);
                Swal.fire({
                    title: 'Error!',
                    icon: 'warning'
                });
            }
        },
        openModal(fila) {
            this.modalData = {
                id: fila.id,
                user: fila.querySelector('td:nth-child(1)').textContent,
                name: fila.querySelector('td:nth-child(2)').textContent,
                password: fila.querySelector('td:nth-child(3)').textContent,
                active: fila.querySelector('td:nth-child(4)').textContent.trim() === 'true',
                pc_id: fila.querySelector('td:nth-child(5)').textContent,
                comment: fila.querySelector('td:nth-child(6)').textContent
            };
            // Lógica para abrir el modal aquí
        },
        closeModal() {
            // Lógica para cerrar el modal aquí
        }
    }
}
</script>

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
