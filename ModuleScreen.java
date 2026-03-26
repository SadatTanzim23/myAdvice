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
                        layoutError.printStackTrace();
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
                        ex.printStackTrace();
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
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Available Courses");
        heading.setFont(new Font("Dialog", Font.BOLD, 13));
        heading.setForeground(myAdvice.TEXT_MUTED);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
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
            @Override protected void paintComponent(Graphics g) {
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
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Available Actions");
        heading.setFont(new Font("Dialog", Font.BOLD, 13));
        heading.setForeground(myAdvice.TEXT_MUTED);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        p.add(heading, gbc);

        for (int i = 0; i < items.length; i++) {
            gbc.gridy = i / 2 + 1;
            gbc.gridx = i % 2;
            gbc.gridwidth = 1;
            ActionCard card = new ActionCard(items[i][0], items[i][1]);
            
            // Add click handlers for course management
            if (items[i][0].equals("Manage Courses")) {
                card.setOnClickAction(() -> showManageCoursesDialog());
            } else if (items[i][0].equals("Edit Prerequisite Structures")) {
                card.setOnClickAction(this::showManagePrerequisitesDialog);
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
            case 0: showAddCourseDialog(); break;
            case 1: showEditCourseDialog(); break;
            case 2: showDeleteCourseDialog(); break;
            default: break;
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
                        Course addedCourse = ApiClient.addCourse(courseCode, courseName, credits, description);

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
                        Course editedCourse = ApiClient.editCourse(selectedCourse.getId(), courseCode, courseName, credits, description);
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
    }
}
