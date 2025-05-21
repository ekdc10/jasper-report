package com.eka.user.view;

import com.eka.user.model.User;
import com.eka.user.repository.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

/**
 * View for registering a new user or editing an existing user.
 * Accessible at route "register" or "register/{id}".
 */
@Route("register/:id?")
public class RegisterUserView extends VerticalLayout implements BeforeEnterObserver {

    private final UserRepository userRepository;

    // Form fields
    private final TextField name = new TextField("User Name");
    private final TextField fullName = new TextField("Full Name");
    private final Button save = new Button("Save");

    // Current user being edited or created
    private User currentUser;

    /**
     * Constructor initializes the form and layout.
     * @param userRepository the repository for saving and retrieving users
     */
    public RegisterUserView(UserRepository userRepository) {
        this.userRepository = userRepository;

        // Setup layout style
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create form layout with fields and button
        VerticalLayout form = new VerticalLayout(name, fullName, save);
        form.setWidth("400px");

        // Add click listener for Save button
        save.addClickListener(e -> {
            // Validate inputs
            if (name.isEmpty() || fullName.isEmpty()) {
                Notification.show("Please fill in all fields", 3000, Notification.Position.MIDDLE);
                return;
            }

            // If no current user loaded, create a new user entity
            if (currentUser == null) {
                currentUser = new User();
            }

            // Set user data from form fields
            currentUser.setUserName(name.getValue());
            currentUser.setFullName(fullName.getValue());

            // Save user entity to the database
            userRepository.save(currentUser);

            // Show success notification
            Notification.show("User saved!", 1500, Notification.Position.TOP_CENTER);

            // Navigate back to main view (root route)
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        // Add form to this layout
        add(form);
    }

    /**
     * Called before entering the view.
     * Loads user data if an ID is present in the URL.
     * @param event navigation event containing route parameters
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check if the URL contains an "id" parameter
        event.getRouteParameters().get("id").ifPresent(id -> {
            try {
                // Parse id to Long and fetch user from database
                Long userId = Long.parseLong(id);
                currentUser = userRepository.findById(userId).orElse(null);

                // If user found, populate form fields
                if (currentUser != null) {
                    name.setValue(currentUser.getUserName());
                    fullName.setValue(currentUser.getFullName());
                }
            } catch (NumberFormatException e) {
                // Show error notification for invalid ID
                Notification.show("Invalid user ID", 3000, Notification.Position.MIDDLE);
            }
        });
    }
}
