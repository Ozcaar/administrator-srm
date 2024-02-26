<script setup>
import PanelView from '../views/PanelView.vue'

import Swal from 'sweetalert2'
</script>

<template>
    <PanelView>
        <div class="container">
            <h2 class="header">Computadoras</h2>
            <div class="content">
                <div class="content-inputs">
                    <input class="input-find" type="text" placeholder="🔍 Búsqueda por usuario" />

                    <button class="btn">🧑‍💼 Agregar computadora</button>
                    <!-- <button class="btn">👁️ Mostrar contraseña</button> -->
                    <button class="btn"><strong>❔</strong></button>
                </div>

                <div class="table-container" @click="handleClick">
                    <table>
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Acrivo</th>
                                <th>Service Tag</th>
                                <th>IP</th>
                                <th>Fecha de adquisición</th>
                                <th>Comentarios</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <tr v-for="computer in computers" :key="computer.id" :id="`${computer.id}`" class="computer-row"
                                @click="openModal(computer)">
                                <td>{{ computer.name }}</td>
                                <td>{{ computer.active }}</td>
                                <td>{{ computer.service_tag }}</td>
                                <td>{{ computer.ip }}</td>
                                <td>{{ computer.adquisition_date }}</td>
                                <td>{{ computer.comment }}</td>
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
            // tempIdComputer: 0,
            // users: [],
            computers: [],
            modalData: {
                id: '',
                name: '',
                active: false,
                service_tag: '',
                ip: '',
                adquisition_date: '',
                comment: ''
            },
            // computerData: {
            //     id: '',
            //     name: '',
            //     active: false,
            //     service_tag: '',
            //     ip: '',
            //     adquisition_date: '',
            //     comment: ''
            // }
        };
    },
    mounted() {
        this.fetchComputers()
    },
    methods: {
        async fetchComputers() {
            try {
                const response = await fetch('http://localhost:8080/computers');
                const data = await response.json();
                this.computers = data;

            } catch (error) {
                console.error('Error al obtener las computadoras:', error);
            }

            // try {
            //     const response = await fetch('http://localhost:8080/computers');
            //     const data = await response.json();
            //     this.computers = data;

            // } catch (error) {
            //     console.error('Error al obtener computadoras:', error);
            // }
        },
        async updateComputer() {
            try {
                const computersResponse = await fetch('http://localhost:8080/computers', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                        // KEYS DEL CORS
                    },
                    body: JSON.stringify(this.modalData)
                });

                if (!computersResponse.ok) {
                    throw new Error('Network users response was not ok');
                }

                await this.fetchComputers();
                Swal.fire({
                    title: 'Hecho!',
                    icon: 'success'
                });
            } catch (error) {
                console.error('There was a problem with the fetch operation:', error);
                Swal.fire({
                    title: 'Error!',
                    text: 'Ocurrió un error en la captura de los datos',
                    icon: 'warning'
                });
            }
            console.log(JSON.stringify(this.modalData));
            console.log(this.modalData);
        },
        // findComputer(id) {
        //     const computer = this.computers.find(c => c.id === id);
        //     return computer != null ? computer : "";
        // },
        // findComputerByName(name) {
        //     const computer = this.computers.find(c => c.name === name);
        //     return computer != null ? computer : null;
        // },
        // findUserComputerId(id_computer) {
        //     const user = this.users.find(u => u.id_computer === id_computer);
        //     return user != undefined ? user : null;
        // },
        // async updateComputer() {
        //     const computersResponse = await fetch('http://localhost:8080/computers', {
        //         method: 'POST',
        //         headers: {
        //             'Content-Type': 'application/json'
        //             // KEYS DEL CORS
        //         },
        //         body: JSON.stringify(this.computerData)
        //     });

        //     if (!computersResponse.ok) {
        //         throw new Error('Network computers response was not ok');
        //     }
        //     const jsonResponse = await computersResponse.json();
        //     return jsonResponse;
        // },
        openModal(computer) {
            this.modalData = {
                id: computer.id,
                name: computer.name,
                active: computer.active,
                service_tag: computer.service_tag,
                ip: computer.ip,
                adquisition_date: computer.adquisition_date,
                comment: computer.comment
            };

            // ABRIR MODAL DE MODIFICACIÓN DE COMPUTADORA
            Swal.fire({
                showCancelButton: true,
                confirmButtonText: 'Modificar',
                cancelButtonText: 'Cancelar',
                html: `
                            <form class="form-container">
                                <label>Id</label>
                                <input disabled value ="${this.modalData.id}" id="swal-input0" class="input input-text input-id" placeholder="Id" autocomplete="off">

                                <label>Nombre</label>
                                <input value ="${this.modalData.name ? this.modalData.name.trim() : ''}" id="swal-input1" class="input input-text" placeholder="Nombre" autocomplete="off">
                                
                                <label>Fecha de adquisición</label>
                                <input value ="${this.modalData.adquisition_date ? this.modalData.adquisition_date.trim() : ''}" id="swal-input2" type="date" class="input input-text" placeholder="Fecha de adquisición" autocomplete="off">
                                
                                <label>Service Tag</label>
                                <input value ="${this.modalData.service_tag ? this.modalData.service_tag.trim() : ''}" id="swal-input3" class="input input-text" placeholder="Service Tag" autocomplete="off">

                                <label>IP</label>
                                <input value ="${this.modalData.ip ? this.modalData.ip.trim() : ''}" id="swal-input5" class="input input-text" placeholder="IP" autocomplete="off">
                                
                                <label>Comentarios</label>
                                <textarea maxlength="300" id="swal-input6" class="input input-textarea" placeholder="Comentarios adicionales">${this.modalData.comment}</textarea>
                            
                                <div>
                                    <input ${this.modalData.active ? 'checked' : ''} id="swal-input4" type="checkbox" class="active-checkbox">
                                    <span>Computadora activa</span>
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
                preConfirm: async () => {
                    // let computer = this.findComputerByName(document.getElementById("swal-input5").value)

                    // if (computer == null) {
                    //     this.computerData = {
                    //         id: null,
                    //         name: document.getElementById("swal-input5").value,
                    //         active: true,
                    //         service_tag: null,
                    //         ip: null,
                    //         adquisition_date: "",
                    //         comment: ""
                    //     };

                    //     computer = await this.updateComputer();
                    // }

                    // this.computerData = computer;
                    // this.tempIdComputer = this.modalData.id_computer;

                    this.modalData = {
                        id: document.getElementById("swal-input0").value, // id
                        name: document.getElementById("swal-input1").value.trim() == "" ? null : document.getElementById("swal-input1").value, // name
                        active: document.getElementById("swal-input4").checked, // active
                        service_tag: document.getElementById("swal-input3").value.trim() == "" ? null : document.getElementById("swal-input3").value, // service_tag
                        ip: document.getElementById("swal-input5").value == "" ? null : document.getElementById("swal-input5").value, // ip
                        adquisition_date: document.getElementById("swal-input2").value, // adquisition_date
                        comment: document.getElementById("swal-input6").value // comment
                    };

                }
            }).then(result => {
                if (result.isConfirmed) {
                    this.updateComputer();
                } else if (!result.isDismissed) {
                    Swal.fire({
                        icon: "error",
                        title: "Error!",
                        text: "Error desconocido #10001"
                    });
                }
            });
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
    align-items: center;
    background-color: #f5f5f5;
    font-family: system-ui, sans-serif;
    font-weight: bold;
    padding: 0 0 0 20px;
    /* border-bottom: var(--light-gray) solid 1px; */

    border-bottom: 1px solid #e4e4e7;

}

.content {
    padding: 30px;
    height: calc(100% - 60px);
    display: flex;
    flex-direction: column;
}

.content-inputs {
    display: flex;
}

.input-find,
.btn {
    border-radius: 10px;
    /* border: var(--light-gray) 1px solid; */
    padding: 10px;
    margin-bottom: 20px;
    font-size: 15px;
    /* color: var(--gray); */
    color: #6b7280;
    border: #e4e4e7 1px solid;
    height: 50px;
    margin-right: 5px;

    min-width: 50px;
}

.btn:hover {
    cursor: pointer;
    color: var(--dark);
    background-color: var(--btn-selected);
}

.btn {}

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
