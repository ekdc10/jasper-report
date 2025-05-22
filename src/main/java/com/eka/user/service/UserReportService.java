package com.eka.user.service;

import com.eka.user.model.User;
import com.eka.user.repository.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;


import java.io.InputStream;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class UserReportService {

    private final UserRepository userRepository;

    @Autowired
    public UserReportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public byte[] generatePdf() {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/user.jrxml");
            // InputStream reportStream = getReportStream("/report/user.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            if (reportStream == null) {
                throw new RuntimeException("Report template not found in classpath");
            }

            // Fill the report
            List<User> users = userRepository.findAll();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, new HashMap<>(), dataSource);

            // Export to PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Could not generate PDF", e);
        }
    }

}
