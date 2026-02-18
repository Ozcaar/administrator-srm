package com.sellosmonterrey.administrador.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sellosmonterrey.administrador.BackupUtils;
import com.sellosmonterrey.administrador.models.UserModel;
import com.sellosmonterrey.administrador.models.AdminModel;
import com.sellosmonterrey.administrador.models.ComputerModel;
import com.sellosmonterrey.administrador.models.MailModel;
import com.sellosmonterrey.administrador.models.ServiceModel;
import com.sellosmonterrey.administrador.repositories.UserRepository;
import com.sellosmonterrey.administrador.services.AuthService;
import com.sellosmonterrey.administrador.repositories.AdminRepository;
import com.sellosmonterrey.administrador.repositories.ComputerRepository;
import com.sellosmonterrey.administrador.repositories.MailRepository;
import com.sellosmonterrey.administrador.repositories.ServiceRepository;

@RestController
@RequestMapping("/admin/backup")
// @CrossOrigin(origins = { "http://localhost:5173", "http://10.21.11.156:5173",
// "http://192.168.1.15:5173" })
public class BackupController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComputerRepository computerRepository;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AuthService authService;

    // String backupDirPath = BackupUtils.getBackupDirPath();
    String backupDirPath = "./backups";
    String fileName = "backup_" + BackupUtils.getCurrentDateString() + ".csv";
    String filePath = backupDirPath + "/" + fileName;
    String currentDateString = BackupUtils.getCurrentDateString();

    @GetMapping
    public ResponseEntity<String> createBackup(@RequestParam("username") String username,
            @RequestParam("token") String token) throws IOException {

        if (!authService.authCurrentSession(username, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<AdminModel> admins = (List<AdminModel>) adminRepository.findAll();
        List<UserModel> users = (List<UserModel>) userRepository.findAll();
        List<ComputerModel> computers = (List<ComputerModel>) computerRepository.findAll();
        List<MailModel> mails = (List<MailModel>) mailRepository.findAll();
        List<ServiceModel> services = (List<ServiceModel>) serviceRepository.findAll();

        File backupDir = new File(backupDirPath);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // Write header admins
            writer.write("ADMINISTRADORES");
            writer.newLine();
            writer.write("id,username,password,salt,token,last_login");
            writer.newLine();

            // Write admins
            for (AdminModel admin : admins) {
                writer.write(admin.getId() + "," + admin.getUsername() + "," + admin.getPassword() + ","
                        + admin.getSalt() + "," + admin.getToken() + "," + admin.getLast_login());
                writer.newLine();
            }
            writer.newLine();

            // Write header users
            writer.write("USUARIOS");
            writer.newLine();
            writer.write("id,app_user,password,active,name,id_computer,comment");
            writer.newLine();

            // Write users
            for (UserModel app_user : users) {
                writer.write(app_user.getId() + "," + app_user.getUser() + "," + app_user.getPassword() + ","
                        + app_user.getActive()
                        + "," + app_user.getName() + "," + app_user.getId_computer() + "," + app_user.getComment());
                writer.newLine();
            }
            writer.newLine();

            // Write header computers
            writer.write("COMPUTADORAS");
            writer.newLine();
            writer.write("id,name,active,service_tag,ip,adquisition_date,comment");
            writer.newLine();

            // Write computers
            for (ComputerModel computer : computers) {
                writer.write(computer.getId() + "," + computer.getName() + "," + computer.getActive() + "," +
                        computer.getService_tag() + "," + computer.getIp() + "," + computer.getAdquisition_date() + ","
                        + computer.getComment());
                writer.newLine();
            }
            writer.newLine();

            // Write header mails
            writer.write("MAILS");
            writer.newLine();
            writer.write("id,service,app_user,mail,password,recovery_mail");
            writer.newLine();

            // Write mails
            for (MailModel mail : mails) {
                writer.write(mail.getId() + "," + mail.getService() + "," + mail.getUser() + "," + mail.getMail() + ","
                        + mail.getPassword() + "," + mail.getRecovery_mail());
                writer.newLine();
            }
            writer.newLine();

            // Write header services
            writer.write("SERVICES");
            writer.newLine();
            writer.write("id,servie,app_user,password,access_link");
            writer.newLine();

            // Write services
            for (ServiceModel service : services) {
                writer.write(service.getId() + "," + service.getService() + "," + service.getUser() + "," +
                        service.getPassword() + "," + service.getAccess_link());
                writer.newLine();
            }
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el backup: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Respaldo de hoy actualizado");
    }

    @GetMapping("/downloadBackup")
    public ResponseEntity<FileSystemResource> downloadBackup(@RequestParam("username") String username,
            @RequestParam("token") String token) throws IOException {

        if (!authService.authCurrentSession(username, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        FileSystemResource resource = new FileSystemResource(filePath);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/octet-stream"))
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}