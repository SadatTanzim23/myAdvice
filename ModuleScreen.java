import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.List;

public class ModuleScreen extends JPanel {//the module screens you go in through the main screen 
    private final int moduleIdx;
    private List<Course> allCourses;

    public ModuleScreen(int idx) {
        this.moduleIdx = idx;
        setLayout(new BorderLayout());
        setOpaque(false);

        add(buildSubHeader(idx), BorderLayout.NORTH);

        // Load course data from API for Curriculum Advising module
        if (idx == 0) {
            loadCoursesAndDisplay();
        } else {
            add(buildActionsPanel(myAdvice.MODULE_ACTIONS[idx]), BorderLayout.CENTER);
        }
    }

    // Load courses from API and display them in a scrollable panel
    private void loadCoursesAndDisplay() {
        // Fetch courses in a new thread
        new Thread(() -> {
            try {
                List<Course> courses = ApiClient.getAllCourses(); // Fetch all courses
                if (courses == null) {
                    courses = new java.util.ArrayList<>();
                }
                allCourses = courses;

                //
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Remove all CENTER panels (keep only NORTH header)
                        removeAll();
                        add(buildSubHeader(moduleIdx), BorderLayout.NORTH);

                        // Display courses if available, otherwise show 'No courses available' message
                        if (allCourses.isEmpty()) {
                            JPanel emptyPanel = new JPanel();
                            emptyPanel.setBackground(myAdvice.BG);
                            JLabel emptyLabel = new JLabel("No courses available");
                            emptyLabel.setForeground(myAdvice.TEXT_MUTED);
                            emptyPanel.add(emptyLabel);
                            add(emptyPanel, BorderLayout.CENTER);
                        } else {
                            // Build action items from existing courses
                            String[][] courseActions = new String[allCourses.size()][];
                            for (int i = 0; i < allCourses.size(); i++) {
                                Course course = allCourses.get(i);
                                courseActions[i] = new String[]{
                                        course.getCourseCode() + " - " + course.getCourseName(),
                                        "Credits: " + course.getCredits()
                                };
                            }
                            JPanel coursePanel = buildCoursePanel(courseActions);
                            JScrollPane scrollPane = new JScrollPane(coursePanel);
                            scrollPane.setOpaque(false);
                            scrollPane.getViewport().setOpaque(false);
                            add(scrollPane, BorderLayout.CENTER);
                        }
                        revalidate();
                        repaint();
                    } catch (Exception layoutError) {
                        String errorMsg = layoutError.getMessage() != null ? layoutError.getMessage() : layoutError.toString();
                        JOptionPane.showMessageDialog(this,
                                "Error rendering course layout: " + errorMsg,
                                "UI Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        removeAll();
                        add(buildSubHeader(moduleIdx), BorderLayout.NORTH);
                        String errorMsg = "Error loading courses: " + (e.getMessage() != null ? e.getMessage() : e.toString());
                        add(buildErrorPanel(errorMsg), BorderLayout.CENTER);
                        revalidate();
                        repaint();
                    } catch (Exception ex) {
                        String nestedErrorMsg = ex.getMessage() != null ? ex.getMessage() : ex.toString();
                        JOptionPane.showMessageDialog(this,
                                "Error updating course screen: " + nestedErrorMsg,
                                "UI Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }).start();
    }

    // Build a GridBagLayout panel with ActionCard components for each course
    private JPanel buildCoursePanel(String[][] items) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(myAdvice.BG);
        p.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Available Courses");
        heading.setFont(new Font("Dialog", Font.BOLD, 13));
        heading.setForeground(myAdvice.TEXT_MUTED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        p.add(heading, gbc); // Add heading

        // Create and add ActionCard components for each course
        for (int i = 0; i < items.length; i++) {
            gbc.gridy = i / 2 + 1;
            gbc.gridx = i % 2;
            gbc.gridwidth = 1;
            p.add(new ActionCard(items[i][0], items[i][1]), gbc);
        }
        return p;
    }

    // Error panel to display API error messages
    private JPanel buildErrorPanel(String message) {
        JPanel p = new JPanel();
        p.setBackground(myAdvice.BG);
        JLabel lbl = new JLabel(message);
        lbl.setForeground(myAdvice.TEXT_MUTED);
        p.add(lbl);
        return p;
    }

    private JPanel buildSubHeader(int idx) {//the subheading under the main heading panel with the module name and subhead
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(myAdvice.PANEL_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(myAdvice.DIVIDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(780, 54));
        p.setLayout(new BorderLayout(0, 2));
        p.setBorder(new EmptyBorder(8, 32, 8, 32));

        JLabel titleLbl = new JLabel(myAdvice.MODULE_META[idx][1]);
        titleLbl.setFont(new Font("Dialog", Font.BOLD, 17));
        titleLbl.setForeground(myAdvice.TEXT_LIGHT);

        JLabel subtitleLbl = new JLabel(myAdvice.MODULE_META[idx][2]);
        subtitleLbl.setFont(new Font("Dialog", Font.PLAIN, 12));
        subtitleLbl.setForeground(myAdvice.TEXT_MUTED);

        JPanel stack = new JPanel(new GridLayout(2, 1, 0, 2));
        stack.setOpaque(false);
        stack.add(titleLbl);
        stack.add(subtitleLbl);
        p.add(stack, BorderLayout.CENTER);
        return p;
    }

    //2-column grid of ActionCard panels
    private JPanel buildActionsPanel(String[][] items) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(myAdvice.BG);
        p.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Available Actions");
        heading.setFont(new Font("Dialog", Font.BOLD, 13));
        heading.setForeground(myAdvice.TEXT_MUTED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        p.add(heading, gbc);

        for (int i = 0; i < items.length; i++) {
            gbc.gridy = i / 2 + 1;
            gbc.gridx = i % 2;
            gbc.gridwidth = 1;
            ActionCard card = new ActionCard(items[i][0], items[i][1]);

            // Add click handlers for course management
            if (items[i][0].equals("Manage Courses")) {
                card.setOnClickAction(this::showManageCoursesDialog);
            } else if (items[i][0].equals("Edit Prerequisite Structures")) {
                card.setOnClickAction(this::showManagePrerequisitesDialog);
            } else if (items[i][0].equals("Create / Edit Timetable")){
                card.setOnClickAction(this::showManageSectionsDialog);
            }

            p.add(card, gbc);
        }
        return p;
    }

    // Helper method to show the Manage Courses dialog
    private void showManageCoursesDialog() {
        fetchCoursesThen(this::showManageCoursesDialogWithData);
    }

    // Dialog for course management actions (add/edit/delete course)
    private void showManageCoursesDialogWithData() {

        // Show course management options
        String[] options = {"Add Course", "Edit Course", "Delete Course", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select an action:",
                "Manage Courses",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        // Show dialog based on button selection (add/edit/delete)
        switch (choice) {
            case 0:
                showAddCourseDialog();
                break;
            case 1:
                showEditCourseDialog();
                break;
            case 2:
                showDeleteCourseDialog();
                break;
            default:
                break;
        }
    }

    // Dialog for adding a new course
    private void showAddCourseDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel codeLabel = new JLabel("Course Code:");
        JTextField codeField = new JTextField();

        JLabel nameLabel = new JLabel("Course Name:");
        JTextField nameField = new JTextField();

        JLabel creditsLabel = new JLabel("Credits:");
        JTextField creditsField = new JTextField();

        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField();

        // Add label and field to panel for getting user input
        panel.add(codeLabel);
        panel.add(codeField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(creditsLabel);
        panel.add(creditsField);
        panel.add(descLabel);
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If user clicks OK, add the course using the API and refresh the course list, otherwise do nothing
        if (result == JOptionPane.OK_OPTION) {
            String courseCode = codeField.getText().trim();
            String courseName = nameField.getText().trim();
            String creditsStr = creditsField.getText().trim();
            String description = descField.getText().trim();

            // Input validation for course addition
            if (courseCode.isEmpty() || courseName.isEmpty() || creditsStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Integer credits = Integer.parseInt(creditsStr);

                new Thread(() -> {
                    try {
                        // Add the new course to the database using the API
                        ApiClient.addCourse(courseCode, courseName, credits, description);

                        // Show success message and refresh the course list
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this,
                                    "Course added successfully!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);

                            refreshCoursesAsync(moduleIdx == 0);
                        });
                    } catch (Exception e) {
                        SwingUtilities.invokeLater(() -> {
                            String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                            JOptionPane.showMessageDialog(this,
                                    "Error: " + errorMsg,
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }).start();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Credits must be a number",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Show Dialog for editing an existing course
    private void showEditCourseDialog() {
        if (allCourses == null || allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses available to edit",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create list of course options
        String[] courseOptions = new String[allCourses.size()];
        for (int i = 0; i < allCourses.size(); i++) {
            Course c = allCourses.get(i);
            courseOptions[i] = c.getCourseCode() + " - " + c.getCourseName();
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select a course to edit:",
                "Edit Course",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                courseOptions,
                courseOptions[0]);

        if (selectedIndex < 0) return; // Cancel

        Course selectedCourse = allCourses.get(selectedIndex);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel codeLabel = new JLabel("Course Code:");
        JTextField codeField = new JTextField(selectedCourse.getCourseCode());

        JLabel nameLabel = new JLabel("Course Name:");
        JTextField nameField = new JTextField(selectedCourse.getCourseName());

        JLabel creditsLabel = new JLabel("Credits:");
        JTextField creditsField = new JTextField(selectedCourse.getCredits().toString());

        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField(selectedCourse.getDescription() != null ? selectedCourse.getDescription() : "");

        panel.add(codeLabel);
        panel.add(codeField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(creditsLabel);
        panel.add(creditsField);
        panel.add(descLabel);
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If user clicks OK, update the course using the API and refresh the course list, otherwise do nothing
        if (result == JOptionPane.OK_OPTION) {
            String courseCode = codeField.getText().trim();
            String courseName = nameField.getText().trim();
            String creditsStr = creditsField.getText().trim();
            String description = descField.getText().trim();

            if (courseCode.isEmpty() || courseName.isEmpty() || creditsStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Integer credits = Integer.parseInt(creditsStr);

                new Thread(() -> {
                    try {
                        ApiClient.editCourse(selectedCourse.getId(), courseCode, courseName, credits, description);
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this,
                                    "Course updated successfully!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            refreshCoursesAsync(moduleIdx == 0);
                        });
                    } catch (Exception e) {
                        SwingUtilities.invokeLater(() -> {
                            String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                            JOptionPane.showMessageDialog(this,
                                    "Error: " + errorMsg,
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }).start();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Credits must be a number",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Dialog for deleting a course
    private void showDeleteCourseDialog() {

        // Check if there are courses available to delete, or show an info message if there are none
        if (allCourses == null || allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses available to delete",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Show all courses for selection before deletion
        String[] courseOptions = new String[allCourses.size()];
        for (int i = 0; i < allCourses.size(); i++) {
            Course c = allCourses.get(i);
            courseOptions[i] = c.getCourseCode() + " - " + c.getCourseName();
        }

        // Fetch selected course index from user selection
        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select a course to delete:",
                "Delete Course",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                courseOptions,
                courseOptions[0]);

        if (selectedIndex < 0) return; // Cancel

        Course selectedCourse = allCourses.get(selectedIndex);

        // Confirm deletion with user before proceeding
        int confirmDelete = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete: " + selectedCourse.getCourseCode() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        // If user confirms deletion, delete the course using the API and refresh the course list, otherwise do nothing
        if (confirmDelete == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                try {
                    ApiClient.deleteCourse(selectedCourse.getId());
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Course deleted successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshCoursesAsync(moduleIdx == 0);
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                        JOptionPane.showMessageDialog(this,
                                "Error: " + errorMsg,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }

    // Helper method to fetch courses
    private void fetchCoursesThen(Runnable onSuccess) {
        new Thread(() -> {
            try {
                List<Course> courses = ApiClient.getAllCourses();
                if (courses == null) {
                    courses = new java.util.ArrayList<>();
                }
                allCourses = courses;
                if (onSuccess != null) {
                    SwingUtilities.invokeLater(onSuccess);
                }
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    // Refresh courses and update UI
    private void refreshCoursesAsync(boolean refreshCurriculumUI) {
        fetchCoursesThen(() -> {
            if (refreshCurriculumUI && moduleIdx == 0) {
                loadCoursesAndDisplay();
            }
        });
    }

    // Update the module index
    public int getModuleIdx() {
        return moduleIdx;
    }


    public void refreshCurriculumIfNeeded() {
        if (moduleIdx == 0) {
            loadCoursesAndDisplay();
        }
    }

    // Prerequisite Dialog methods

    // Method to call the prerequisite dialog method
    private void showManagePrerequisitesDialog() {
        fetchCoursesThen(this::showManagePrerequisitesDialogWithData);
    }

    // Method to show the prerequisite dialog
    private void showManagePrerequisitesDialogWithData() {
        // Check if there are courses available to manage prerequisites, or show an info message if there are none
        if (allCourses == null || allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No courses available to manage prerequisites.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        // Build course labels for selection
        String[] courseOptions = new String[allCourses.size()];
        for (int i = 0; i < allCourses.size(); i++) {
            Course c = allCourses.get(i);
            courseOptions[i] = c.getCourseCode() + " - " + c.getCourseName();
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select a course to view its prerequisites:",
                "Manage Prerequisites",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                courseOptions,
                courseOptions[0]);

        if (selectedIndex < 0) return; // Cancel

        // Fetch selected course from the list based on user selection
        Course selectedCourse = allCourses.get(selectedIndex);

        // Fetch & load prerequisites in new thread to avoid blocking the UI
        new Thread(() -> {

            // Fetch course prerequisites using the ApiClient, with error handling
            try {
                List<Course> prerequisites = ApiClient.getCoursePrerequisites(selectedCourse.getId());
                if (prerequisites == null) {
                    prerequisites = new java.util.ArrayList<>();
                }
                List<Course> finalPrerequisites = prerequisites;

                SwingUtilities.invokeLater(() ->
                        showCoursePrerequisitesDialog(selectedCourse, finalPrerequisites));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading prerequisites: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    // Method to show course with prerequisites
    private void showCoursePrerequisitesDialog(Course selectedCourse, List<Course> prerequisites) {
        // Dialog message with course name and prerequisites
        StringBuilder sb = new StringBuilder();
        sb.append("Course: ")
                .append(selectedCourse.getCourseCode())
                .append(" - ")
                .append(selectedCourse.getCourseName())
                .append("\n\nCurrent prerequisites:\n");

        if (prerequisites.isEmpty()) {
            sb.append("- None");
        } else {
            for (Course prereq : prerequisites) {
                sb.append("- ")
                        .append(prereq.getCourseCode())
                        .append(" - ")
                        .append(prereq.getCourseName())
                        .append("\n");
            }
        }

        // Display message of course with prerequisites
        JTextArea textArea = new JTextArea(sb.toString(), 14, 45);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);

        // Show options for adding/removing prerequisites or exiting
        String[] options = {"Add Prerequisite", "Remove Prerequisite", "Close"};
        int choice = JOptionPane.showOptionDialog(
                this,
                scrollPane,
                "Prerequisite Details",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Handle user choice to add/remove prerequisite
        if (choice == 0) {
            showAddPrerequisiteDialog(selectedCourse, prerequisites);
        } else if (choice == 1) {
            showRemovePrerequisiteDialog(selectedCourse, prerequisites);
        }
    }

    private void showAddPrerequisiteDialog(Course selectedCourse, List<Course> currentPrereqs) {
        java.util.Set<Long> currentIds = new java.util.HashSet<>();

        // Get all course IDs in the current prerequisites list
        for (Course c : currentPrereqs) {
            if (c.getId() != null) currentIds.add(c.getId());
        }


        // Only add eligible courses to be a prerequisite
        java.util.List<Course> candidates = new java.util.ArrayList<>();
        for (Course c : allCourses) {
            if (c.getId() == null) continue;
            if (c.getId().equals(selectedCourse.getId())) continue; // cannot add itself
            if (currentIds.contains(c.getId())) continue;            // already a prereq
            candidates.add(c);
        }
        // No eligible courses to add as a prerequisite
        if (candidates.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No available courses to add as prerequisites.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create prerequisite course options for selection
        String[] prereqOptions = new String[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) {
            Course c = candidates.get(i);
            prereqOptions[i] = c.getCourseCode() + " - " + c.getCourseName();
        }

        // Store selected course for later use
        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select a course to add as prerequisite:",
                "Add Prerequisite",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                prereqOptions,
                prereqOptions[0]);

        if (selectedIndex < 0) return; // cancel

        Course toAdd = candidates.get(selectedIndex); // Selected prerequisite course

        new Thread(() -> {
            try {
                // Add prerequisite, then fetch updated prerequisite list
                ApiClient.addCoursePrerequisite(selectedCourse.getId(), toAdd.getId());
                List<Course> updatedPrereqs = ApiClient.getCoursePrerequisites(selectedCourse.getId());
                if (updatedPrereqs == null) updatedPrereqs = new java.util.ArrayList<>();
                List<Course> finalUpdated = updatedPrereqs;

                // Show success message and refresh the prerequisite dialog with updated data
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Prerequisite added successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    // keep local data updated
                    refreshCoursesAsync(moduleIdx == 0);
                    showCoursePrerequisitesDialog(selectedCourse, finalUpdated);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error adding prerequisite: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }


    private void showRemovePrerequisiteDialog(Course selectedCourse, List<Course> currentPrereqs) {
        // Check if there are prerequisites to remove
        if (currentPrereqs == null || currentPrereqs.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "This course has no prerequisites to remove.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create options for prerequisite courses to select for removal
        String[] options = new String[currentPrereqs.size()];
        for (int i = 0; i < currentPrereqs.size(); i++) {
            Course c = currentPrereqs.get(i);
            options[i] = c.getCourseCode() + " - " + c.getCourseName();
        }

        // Show prerequisite selection dialog with options
        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select a prerequisite to remove:",
                "Remove Prerequisite",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) return; // cancel
        Course toRemove = currentPrereqs.get(selectedIndex);

        // Confirm removal with user before proceeding
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove prerequisite " + toRemove.getCourseCode() + " from " + selectedCourse.getCourseCode() + "?",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        // Remove the prerequisite using the API in a new thread to avoid blocking the UI
        new Thread(() -> {
            try {
                // Remove prerequisite, then fetch updated prerequisite list
                ApiClient.removeCoursePrerequisite(selectedCourse.getId(), toRemove.getId());
                List<Course> updatedPrereqs = ApiClient.getCoursePrerequisites(selectedCourse.getId());
                if (updatedPrereqs == null) updatedPrereqs = new java.util.ArrayList<>();
                List<Course> finalUpdated = updatedPrereqs;

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Prerequisite removed successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Keep related data updated
                    refreshCoursesAsync(moduleIdx == 0);
                    showCoursePrerequisitesDialog(selectedCourse, finalUpdated);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error removing prerequisite: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    // Section methods
    private void showManageSectionsDialog() {
        // Display options for Section management
        String[] options = {"List Sections", "Add Section", "Edit Section", "Delete Section", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select a section management action:",
                "Create / Edit Timetable",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        // Get user selection and handle accordingly
        switch (choice) {
            case 0 -> showListSectionsDialog();
            case 1 -> JOptionPane.showMessageDialog(this, "Add Section");
            case 2 -> JOptionPane.showMessageDialog(this, "Edit Section");
            case 3 -> JOptionPane.showMessageDialog(this, "Delete Section");
            default -> { }
        }
    }

    // Method to show the list of sections in a dialog
    private void showListSectionsDialog() {
        // Fetch all sections from the API in a new thread to avoid blocking the UI
        new Thread(() -> {
            try {
                List<Section> sections = ApiClient.getAllSections();
                if (sections == null) {
                    sections = new java.util.ArrayList<>();
                }
                List<Section> finalSections = sections;

                // Build a list of section options for selection
                SwingUtilities.invokeLater(() -> {
                    StringBuilder sb = new StringBuilder("Available Sections:\n\n");
                    if (finalSections.isEmpty()) {
                        sb.append("No sections found.");
                    } else {
                        for (Section s : finalSections) {
                            String courseCode = (s.getCourse() != null && s.getCourse().getCourseCode() != null)
                                    ? s.getCourse().getCourseCode()
                                    : "N/A";
                            String secNo = s.getSectionNumber() != null ? s.getSectionNumber() : "N/A";
                            String instructor = s.getInstructorName() != null ? s.getInstructorName() : "N/A";
                            String day = s.getDayOfWeek() != null ? s.getDayOfWeek() : "N/A";

                            sb.append("- ")
                                    .append(courseCode).append(" | Sec ").append(secNo)
                                    .append(" | ").append(instructor)
                                    .append(" | ").append(day)
                                    .append("\n");
                        }
                    }

                    JTextArea textArea = new JTextArea(sb.toString(), 18, 55);
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    // Show the dialog with the list of sections when available
                    JOptionPane.showMessageDialog(this,
                            new JScrollPane(textArea),
                            "Sections",
                            JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading sections: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

}
