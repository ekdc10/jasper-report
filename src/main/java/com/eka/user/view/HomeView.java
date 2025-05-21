package com.eka.user.view;

import com.eka.user.model.User;
import com.eka.user.repository.UserRepository;
import com.eka.user.service.UserReportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;

/**
 * Main view showing list of users in a grid with options to register new users,
 * update, delete existing users, and export the list as PDF.
 */
@Route("")
public class HomeView extends VerticalLayout {

    private final UserRepository userRepository;
    private final UserReportService userReportService;
    
    private final Grid<User> grid = new Grid<>(User.class, false);
    private final Button register = new Button("Register");
    private final Button export = new Button("Export");

    /**
     * Constructor injects repository and report service, sets up UI components.
     */
    @Autowired
    public HomeView(UserRepository userRepository, UserReportService userReportService) {
        this.userRepository = userRepository;
        this.userReportService = userReportService;

        // Configure grid columns manually (disable default)
        grid.addColumn(User::getUserId).setHeader("Id").setAutoWidth(true);
        grid.addColumn(User::getUserName).setHeader("User Name").setAutoWidth(true);
        grid.addColumn(User::getFullName).setHeader("Full Name").setAutoWidth(true);

        // Add update button column
        grid.addComponentColumn(user -> {
            Button updateButton = new Button("Update");
            updateButton.addClickListener(e -> 
                getUI().ifPresent(ui -> ui.navigate("register/" + user.getUserId()))
            );
            return updateButton;
        }).setHeader("").setAutoWidth(true);

        // Add delete button column
        grid.addComponentColumn(user -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                userRepository.delete(user);
                updateGrid(); // Refresh grid after deletion
            });
            return deleteButton;
        }).setHeader("").setAutoWidth(true);

        // Create a placeholder Anchor for the download link
        Anchor downloadLink = new Anchor();
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(export); // Add the export button inside the anchor

        export.addClickListener(e -> {
            try {
                byte[] pdfBytes = userReportService.generatePdf();
                StreamResource resource = new StreamResource("users.pdf", () -> new ByteArrayInputStream(pdfBytes));
                downloadLink.setHref(resource);

            } catch (Exception ex) {
                Notification.show("Failed to generate PDF: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
            }
        });

        // Header layout with Register and Export buttons aligned to the right
        HorizontalLayout head = new HorizontalLayout();
        head.setWidthFull();
        head.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        head.add(register, downloadLink);

        // Small spacing between buttons
        register.getStyle().set("margin-right", "10px");

        // Register button navigates to empty register page for new user creation
        register.addClickListener(e -> 
            getUI().ifPresent(ui -> ui.navigate("register"))
        );

        // Add components to the main layout
        add(head, grid);

        // Load data into grid initially
        updateGrid();
    }

    /**
     * Refresh the grid data with all users from repository.
     */
    private void updateGrid() {
        grid.setItems(userRepository.findAll());
    }
}
