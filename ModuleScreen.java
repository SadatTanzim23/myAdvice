import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ModuleScreen extends JPanel {//the module screens you go in through the main screen 
    private final int moduleIdx;
    private List<Course> allCourses;
    private List<Faculty> allFaculty;
    private List<Student> allStudents;
    private List<Transcript> allTranscripts;
    private List<Appointment> allAppointments;

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

                            JPanel curriculumPanel = new JPanel(new BorderLayout(0, 12));
                            curriculumPanel.setOpaque(false);
                            curriculumPanel.add(buildAdvisingToolsPanel(), BorderLayout.NORTH);

                            JPanel coursePanel = buildCoursePanel(courseActions);
                            JScrollPane scrollPane = new JScrollPane(coursePanel);
                            scrollPane.setOpaque(false);
                            scrollPane.getViewport().setOpaque(false);
                            curriculumPanel.add(scrollPane, BorderLayout.CENTER);

                            add(curriculumPanel, BorderLayout.CENTER);
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

    private JPanel buildAdvisingToolsPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(16, 40, 0, 40));

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Advising Tools");
        heading.setFont(new Font("Dialog", Font.BOLD, 13));
        heading.setForeground(myAdvice.TEXT_MUTED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        grid.add(heading, gbc);

        String[][] tools = {
                {"View Student Program", "Lookup program by student"},
                {"View Completed Courses", "Show completed transcript entries"},
                {"View Remaining Courses", "Show courses left to complete"},
                {"Check Course Eligibility", "Check prerequisites for a course"}
        };

        for (int i = 0; i < tools.length; i++) {
            gbc.gridy = i / 2 + 1;
            gbc.gridx = i % 2;
            gbc.gridwidth = 1;
            ActionCard card = new ActionCard(tools[i][0], tools[i][1]);
            if (i == 0) {
                card.setOnClickAction(this::showAdvisingProgramDialog);
            } else if (i == 1) {
                card.setOnClickAction(this::showAdvisingCompletedDialog);
            } else if (i == 2) {
                card.setOnClickAction(this::showAdvisingRemainingDialog);
            } else {
                card.setOnClickAction(this::showAdvisingPrerequisiteCheckDialog);
            }
            grid.add(card, gbc);
        }

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
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
            if (items[i][0].equals("Browse Course Sections")) {
                card.setOnClickAction(this::showSchedulingBrowseSectionsDialog);
            } else if (items[i][0].equals("Build Timetable")) {
                card.setOnClickAction(this::showSchedulingBuildTimetableDialog);
            } else if (items[i][0].equals("Check Conflicts")) {
                card.setOnClickAction(this::showSchedulingCheckConflictsDialog);
            } else if (items[i][0].equals("View Room Assignments")) {
                card.setOnClickAction(this::showSchedulingRoomAssignmentsDialog);
            } else if (items[i][0].equals("Lab & Tutorial Matching")) {
                card.setOnClickAction(this::showSchedulingLabTutorialMatchingDialog);
            } else if (items[i][0].equals("Export Schedule")) {
                card.setOnClickAction(this::showSchedulingExportDialog);
            } else if (items[i][0].equals("Manage Courses")) {
                card.setOnClickAction(this::showManageCoursesDialog);
            } else if (items[i][0].equals("Edit Prerequisite Structures")) {
                card.setOnClickAction(this::showManagePrerequisitesDialog);
            } else if (items[i][0].equals("Create / Edit Timetable")){
                card.setOnClickAction(this::showManageSectionsDialog);
            } else if (items[i][0].equals("Manage User Profiles")) {
                card.setOnClickAction(this::showManageUserProfilesDialog);
            } else if (items[i][0].equals("Update Transcript Records")) {
                card.setOnClickAction(this::showManageTranscriptsDialog);
            } else if (items[i][0].equals("Student List")) {
                card.setOnClickAction(() -> fetchReportStudentsThen(this::showStudentListDialog));
            } else if (items[i][0].equals("Faculty List")) {
                card.setOnClickAction(() -> fetchReportFacultyThen(this::showFacultyListDialog));
            } else if (items[i][0].equals("Most Active Students")) {
                card.setOnClickAction(this::showMostActiveStudentsDialog);
            } else if (items[i][0].equals("Busiest Advisors")) {
                card.setOnClickAction(this::showBusiestAdvisorsDialog);
            } else if (items[i][0].equals("Course Enrolment Stats")) {
                card.setOnClickAction(this::showCourseEnrolmentStatsDialog);
            } else if (items[i][0].equals("Dashboard Overview")) {
                card.setOnClickAction(this::showDashboardOverviewDialog);
            } else if (items[i][0].equals("Book Advising Appointment")) {
                card.setOnClickAction(this::showBookAdvisingAppointmentDialog);
            } else if (items[i][0].equals("Browse Faculty Availability")) {
                card.setOnClickAction(this::showBrowseFacultyAvailabilityDialog);
            } else if (items[i][0].equals("My Upcoming Appointments")) {
                card.setOnClickAction(this::showMyUpcomingAppointmentsDialog);
            } else if (items[i][0].equals("Cancel / Reschedule")) {
                card.setOnClickAction(this::showCancelOrRescheduleDialog);
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

    // Helper method to fetch faculty list
    private void fetchFacultyThen(Runnable onSuccess) {
        new Thread(() -> {
            try {
                List<Faculty> faculty = ApiClient.getAllFaculty();
                if (faculty == null) {
                    faculty = new java.util.ArrayList<>();
                }
                allFaculty = faculty;
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

    private void fetchStudentsThen(Runnable onSuccess) {
        new Thread(() -> {
            try {
                List<Student> students = ApiClient.getAllStudents();
                if (students == null) {
                    students = new java.util.ArrayList<>();
                }
                allStudents = students;
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

    private void showAdvisingProgramDialog() {
        fetchStudentsThen(() -> {
            Student student = selectStudent("Select student:", "View Student Program");
            if (student == null) {
                return;
            }

            new Thread(() -> {
                try {
                    String programName = ApiClient.getStudentProgram(student.getId());
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Student: " + student + "\nProgram: " + (programName != null ? programName : "N/A"),
                            "Student Program", JOptionPane.INFORMATION_MESSAGE));
                } catch (Exception e) {
                    showAsyncError("Error loading student program", e);
                }
            }).start();
        });
    }

    private void showAdvisingCompletedDialog() {
        fetchStudentsThen(() -> {
            Student student = selectStudent("Select student:", "Completed Courses");
            if (student == null) {
                return;
            }

            new Thread(() -> {
                try {
                    List<Transcript> completed = ApiClient.getAdvisingCompletedCourses(student.getId());
                    if (completed == null) {
                        completed = new ArrayList<>();
                    }
                    List<Transcript> finalCompleted = completed;

                    SwingUtilities.invokeLater(() -> {
                        StringBuilder sb = new StringBuilder("Completed Courses for ")
                                .append(student)
                                .append(":\n\n");

                        if (finalCompleted.isEmpty()) {
                            sb.append("No completed courses found.");
                        } else {
                            for (Transcript t : finalCompleted) {
                                String courseLabel = t.getCourse() != null ? t.getCourse().toString() : "Unknown Course";
                                sb.append("- ").append(courseLabel)
                                        .append(" | Grade: ").append(t.getGrade() != null ? t.getGrade() : "N/A")
                                        .append(" | Term: ").append(t.getTerm() != null ? t.getTerm() : "N/A")
                                        .append("\n");
                            }
                        }

                        JTextArea textArea = new JTextArea(sb.toString(), 15, 58);
                        textArea.setEditable(false);
                        JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                                "Completed Courses", JOptionPane.INFORMATION_MESSAGE);
                    });
                } catch (Exception e) {
                    showAsyncError("Error loading completed courses", e);
                }
            }).start();
        });
    }

    private void showAdvisingRemainingDialog() {
        fetchStudentsThen(() -> {
            String studentIdInput = JOptionPane.showInputDialog(
                    this,
                    "Enter Student ID:",
                    "Remaining Courses",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (studentIdInput == null || studentIdInput.trim().isEmpty()) {
                return;
            }

            final String input = studentIdInput.trim();
            Student matchedStudent = null;
            if (allStudents != null) {
                for (Student s : allStudents) {
                    if (s == null || s.getId() == null) continue;
                    if (input.equals(String.valueOf(s.getId()))) {
                        matchedStudent = s;
                        break;
                    }
                    if (s.getStudentNumber() != null && input.equalsIgnoreCase(s.getStudentNumber().trim())) {
                        matchedStudent = s;
                        break;
                    }
                }
            }

            if (matchedStudent == null) {
                JOptionPane.showMessageDialog(this,
                        "Student not found for ID: " + input,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            final Long studentId = matchedStudent.getId();
            final Student displayStudent = matchedStudent;

            new Thread(() -> {
                try {
                    List<Course> remaining = ApiClient.getAdvisingRemainingCourses(studentId);
                    if (remaining == null) {
                        remaining = new ArrayList<>();
                    }
                    List<Course> finalRemaining = remaining;

                    SwingUtilities.invokeLater(() -> {
                        String studentLabel = displayStudent != null
                                ? displayStudent.toString()
                                : ("Student ID " + studentId);

                        StringBuilder sb = new StringBuilder("Remaining Courses for ")
                                .append(studentLabel)
                                .append(":\n\n");

                        if (finalRemaining.isEmpty()) {
                            sb.append("No remaining courses found.");
                        } else {
                            for (Course c : finalRemaining) {
                                sb.append("- ").append(c)
                                        .append(" | Credits: ").append(c.getCredits() != null ? c.getCredits() : "N/A")
                                        .append("\n");
                            }
                        }

                        JTextArea textArea = new JTextArea(sb.toString(), 15, 58);
                        textArea.setEditable(false);
                        JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                                "Remaining Courses", JOptionPane.INFORMATION_MESSAGE);
                    });
                } catch (Exception e) {
                    showAsyncError("Error loading remaining courses", e);
                }
            }).start();
        });
    }

    private void showAdvisingPrerequisiteCheckDialog() {
        fetchStudentsThen(() -> fetchCoursesThen(() -> {
            Student student = selectStudent("Select student:", "Check Course Eligibility");
            if (student == null) {
                return;
            }

            Course course = selectCourse("Select course:", "Check Course Eligibility");
            if (course == null) {
                return;
            }

            new Thread(() -> {
                try {
                    boolean eligible = ApiClient.checkAdvisingPrerequisites(student.getId(), course.getId());
                    String message = eligible
                            ? "Eligible: " + student + " can take " + course + "."
                            : "Not eligible: prerequisites are not fully completed for " + course + ".";

                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            message,
                            "Course Eligibility", eligible ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE));
                } catch (Exception e) {
                    showAsyncError("Error checking prerequisites", e);
                }
            }).start();
        }));
    }

    private Student selectStudent(String message, String title) {
        if (allStudents == null || allStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No students available.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        String[] options = new String[allStudents.size()];
        for (int i = 0; i < allStudents.size(); i++) {
            options[i] = allStudents.get(i).toString();
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) {
            return null;
        }
        return allStudents.get(selectedIndex);
    }

    private Course selectCourse(String message, String title) {
        if (allCourses == null || allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No courses available.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        String[] options = new String[allCourses.size()];
        for (int i = 0; i < allCourses.size(); i++) {
            options[i] = allCourses.get(i).toString();
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) {
            return null;
        }
        return allCourses.get(selectedIndex);
    }

    private void showAsyncError(String prefix, Exception e) {
        String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                prefix + ": " + errorMsg,
                "Error", JOptionPane.ERROR_MESSAGE));
    }


    //Helper to fetch appointments for a student
    private void fetchAppointmentsThen(Long studentId, Runnable onSuccess) {
        new Thread(() -> {
            try {
                allAppointments = ApiClient.getStudentAppointments(studentId);
                if (allAppointments == null) allAppointments = new ArrayList<>();
                if (onSuccess != null) SwingUtilities.invokeLater(onSuccess);
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    // Accept either DB student id or student number and resolve to the DB id used by booking APIs.
    private Long resolveStudentIdInput(String studentIdentifier) throws Exception {
        String value = studentIdentifier == null ? "" : studentIdentifier.trim();
        if (value.isEmpty()) {
            throw new Exception("Student ID is required.");
        }

        List<Student> students = ApiClient.getAllStudents();
        if (students == null || students.isEmpty()) {
            throw new Exception("No students found. Please create a student profile first.");
        }

        for (Student s : students) {
            if (s != null && s.getId() != null && value.equals(String.valueOf(s.getId()))) {
                return s.getId();
            }
        }

        for (Student s : students) {
            if (s == null || s.getId() == null || s.getStudentNumber() == null) continue;
            if (value.equalsIgnoreCase(s.getStudentNumber().trim())) {
                return s.getId();
            }
        }

        throw new Exception("Student not found for input: " + value + ". Use a valid DB ID or student number.");
    }

    //Book Advising Appointment dialog
    private void showBookAppointmentDialog() {
        if (allFaculty == null || allFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No faculty available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        JTextField studentIdField = new JTextField();
        JComboBox<Faculty> facultyCombo = new JComboBox<>(allFaculty.toArray(new Faculty[0]));
        JTextField dateTimeField = new JTextField("2026-04-01T10:00:00");
        JTextField statusField = new JTextField("scheduled");
        panel.add(new JLabel("Student ID:")); panel.add(studentIdField);
        panel.add(new JLabel("Faculty:")); panel.add(facultyCombo);
        panel.add(new JLabel("Date & Time (yyyy-MM-ddTHH:mm:ss):")); panel.add(dateTimeField);
        panel.add(new JLabel("Status:")); panel.add(statusField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Book Advising Appointment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        String studentIdentifier = studentIdField.getText().trim();
        Faculty selectedFaculty = (Faculty) facultyCombo.getSelectedItem();
        String dateTime = dateTimeField.getText().trim();
        String status = statusField.getText().trim();
        if (studentIdentifier.isEmpty() || selectedFaculty == null || dateTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new Thread(() -> {
            try {
                Long resolvedStudentId = resolveStudentIdInput(studentIdentifier);
                ApiClient.bookAppointment(resolvedStudentId, selectedFaculty.getId(), dateTime, status);
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Appointment booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    //Browse Faculty Availability dialog
    private void showFacultyAvailabilityDialog() {
        new Thread(() -> {
            try {
                List<Faculty> faculty = ApiClient.getAllFaculty();
                if (faculty == null || faculty.isEmpty()) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "No faculty available.", "Info", JOptionPane.INFORMATION_MESSAGE));
                    return;
                }
                String[] options = new String[faculty.size()];
                for (int i = 0; i < faculty.size(); i++) {
                    options[i] = faculty.get(i).getFirstName() + " " + faculty.get(i).getLastName();
                }
                SwingUtilities.invokeLater(() -> {
                    int selectedIndex = JOptionPane.showOptionDialog(this,
                            "Select a faculty member to view appointments:",
                            "Browse Faculty Availability",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);
                    if (selectedIndex < 0) return;
                    Faculty selected = faculty.get(selectedIndex);
                    new Thread(() -> {
                        try {
                            List<Appointment> appointments = ApiClient.getFacultyAppointments(selected.getId());
                            SwingUtilities.invokeLater(() -> {
                                if (appointments == null || appointments.isEmpty()) {
                                    JOptionPane.showMessageDialog(this,
                                            selected.getFirstName() + " " + selected.getLastName() + " has no appointments.",
                                            "Faculty Availability", JOptionPane.INFORMATION_MESSAGE);
                                    return;
                                }
                                StringBuilder sb = new StringBuilder("Appointments for " + selected.getFirstName() + " " + selected.getLastName() + ":\n\n");
                                for (Appointment a : appointments) {
                                    Student student = a.getStudent();
                                    String studentName = "Unknown Student";
                                    if (student != null) {
                                        String first = student.getFirstName() != null ? student.getFirstName().trim() : "";
                                        String last = student.getLastName() != null ? student.getLastName().trim() : "";
                                        String fullName = (first + " " + last).trim();
                                        if (!fullName.isEmpty()) {
                                            studentName = fullName;
                                        } else if (student.getStudentNumber() != null && !student.getStudentNumber().trim().isEmpty()) {
                                            studentName = "Student " + student.getStudentNumber().trim();
                                        }
                                    }

                                    sb.append("- ").append(a.getDateTime())
                                            .append(" | Student: ").append(studentName)
                                            .append(" | Status: ").append(a.getStatus()).append("\n");
                                }
                                JTextArea textArea = new JTextArea(sb.toString(), 14, 50);
                                textArea.setEditable(false);
                                JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                                        "Faculty Availability", JOptionPane.INFORMATION_MESSAGE);
                            });
                        } catch (Exception e) {
                            String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                                    "Error: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE));
                        }
                    }).start();
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    //My Upcoming Appointments dialog
    private void showMyAppointmentsDialog() {
        String studentInput = JOptionPane.showInputDialog(this,
                "Enter your Student ID:",
                "My Appointments", JOptionPane.PLAIN_MESSAGE);
        if (studentInput == null || studentInput.trim().isEmpty()) return;

        new Thread(() -> {
            try {
                Long studentId = resolveStudentIdInput(studentInput);
                List<Appointment> appointments = ApiClient.getStudentAppointments(studentId);
                SwingUtilities.invokeLater(() -> {
                    if (appointments == null || appointments.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No appointments found.", "My Appointments", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    StringBuilder sb = new StringBuilder("Your Appointments:\n\n");
                    for (Appointment a : appointments) {
                        sb.append("- ID: ").append(a.getId())
                                .append(" | ").append(a.getDateTime())
                                .append(" | Faculty: ").append(a.getFaculty() != null ? a.getFaculty().getFirstName() + " " + a.getFaculty().getLastName() : "N/A")
                                .append(" | Status: ").append(a.getStatus()).append("\n");
                    }
                    JTextArea textArea = new JTextArea(sb.toString(), 14, 55);
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "My Appointments", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    //Find COMP 400/405 Supervisor dialog
    private void showFindSupervisorDialog() {
        if (allFaculty == null || allFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No faculty available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("Available Supervisors for COMP 400/405:\n\n");
        for (Faculty f : allFaculty) {
            sb.append("- ").append(f.getFirstName()).append(" ").append(f.getLastName())
                    .append(" | ").append(f.getDepartment())
                    .append(" | ").append(f.getEmail()).append("\n");
        }
        JTextArea textArea = new JTextArea(sb.toString(), 14, 55);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Find COMP 400/405 Supervisor", JOptionPane.INFORMATION_MESSAGE);
    }

    //Research Interest Matching dialog
    private void showResearchInterestDialog() {
        if (allFaculty == null || allFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No faculty available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String interest = JOptionPane.showInputDialog(this, "Enter a research interest or keyword:", "Research Interest Matching", JOptionPane.PLAIN_MESSAGE);
        if (interest == null || interest.trim().isEmpty()) return;
        String keyword = interest.trim().toLowerCase();
        List<Faculty> matched = new ArrayList<>();
        for (Faculty f : allFaculty) {
            if (f.getDepartment() != null && f.getDepartment().toLowerCase().contains(keyword)) {
                matched.add(f);
            }
        }
        if (matched.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No faculty matched your interest.", "Research Interest Matching", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("Faculty matching \"" + interest + "\":\n\n");
        for (Faculty f : matched) {
            sb.append("- ").append(f.getFirstName()).append(" ").append(f.getLastName())
                    .append(" | ").append(f.getDepartment())
                    .append(" | ").append(f.getEmail()).append("\n");
        }
        JTextArea textArea = new JTextArea(sb.toString(), 14, 55);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Research Interest Matching", JOptionPane.INFORMATION_MESSAGE);
    }

    //Cancel / Reschedule dialog
    private void showCancelRescheduleDialog() {
        String studentInput = JOptionPane.showInputDialog(this,
                "Enter your Student ID:",
                "Cancel / Reschedule", JOptionPane.PLAIN_MESSAGE);
        if (studentInput == null || studentInput.trim().isEmpty()) return;

        new Thread(() -> {
            try {
                Long studentId = resolveStudentIdInput(studentInput);
                List<Appointment> appointments = ApiClient.getStudentAppointments(studentId);
                    SwingUtilities.invokeLater(() -> {
                        if (appointments == null || appointments.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No appointments found.", "Cancel / Reschedule", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        String[] options = new String[appointments.size()];
                        for (int i = 0; i < appointments.size(); i++) {
                            Appointment a = appointments.get(i);
                            options[i] = "ID: " + a.getId() + " | " + a.getDateTime() + " | " + a.getStatus();
                        }
                        int selectedIndex = JOptionPane.showOptionDialog(this,
                                "Select an appointment:",
                                "Cancel / Reschedule",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        if (selectedIndex < 0) return;
                        Appointment selected = appointments.get(selectedIndex);
                        String[] actions = {"Cancel", "Reschedule", "Back"};
                        int action = JOptionPane.showOptionDialog(this,
                                "What do you want to do?",
                                "Cancel / Reschedule",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, actions, actions[0]);
                        if (action == 0) {
                            new Thread(() -> {
                                try {
                                    ApiClient.cancelAppointment(selected.getId());
                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                                            "Appointment cancelled!", "Success", JOptionPane.INFORMATION_MESSAGE));
                                } catch (Exception e) {
                                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                                            "Error: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE));
                                }
                            }).start();
                        } else if (action == 1) {
                            String newDateTime = JOptionPane.showInputDialog(this,
                                    "Enter new date and time (yyyy-MM-ddTHH:mm:ss):",
                                    "Reschedule", JOptionPane.PLAIN_MESSAGE);
                            if (newDateTime == null || newDateTime.trim().isEmpty()) return;
                            new Thread(() -> {
                                try {
                                    ApiClient.rescheduleAppointment(selected.getId(), newDateTime.trim());
                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                                            "Appointment rescheduled!", "Success", JOptionPane.INFORMATION_MESSAGE));
                                } catch (Exception e) {
                                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                                            "Error: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE));
                                }
                            }).start();
                        }
                    });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }
    private void fetchReportStudentsThen(Runnable onSuccess) {
        new Thread(() -> {
            try {
                List<Student> students = ApiClient.getReportStudents();
                if (students == null) {
                    students = new ArrayList<>();
                }
                allStudents = students;
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

    private void fetchReportFacultyThen(Runnable onSuccess) {
        new Thread(() -> {
            try {
                List<Faculty> faculty = ApiClient.getReportFaculty();
                if (faculty == null) {
                    faculty = new ArrayList<>();
                }
                allFaculty = faculty;
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

    private void showManageUserProfilesDialog() {
        String[] options = {"Manage Faculty", "Manage Students", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose which profiles to manage:",
                "Manage User Profiles",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0 -> showManageFacultyProfilesDialog();
            case 1 -> showManageStudentProfilesDialog();
            default -> { }
        }
    }

    private void showManageFacultyProfilesDialog() {
        String[] options = {"Add Faculty", "Edit Faculty", "Delete Faculty", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose a faculty action:",
                "Manage Faculty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0 -> showAddFacultyDialog(null);
            case 1 -> fetchFacultyThen(this::showEditFacultyDialog);
            case 2 -> fetchFacultyThen(this::showDeleteFacultyDialog);
            default -> { }
        }
    }

    private void showFacultyListDialog() {
        if (allFaculty == null || allFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No faculty found.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = {"Search by Name", "View All", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose how to view faculty:",
                "Faculty List",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 1) {
            showFacultyResultsDialog(allFaculty, "Faculty");
            return;
        }

        if (choice != 0) {
            return;
        }

        String query = JOptionPane.showInputDialog(
                this,
                "Enter faculty name to search:",
                "Faculty Search",
                JOptionPane.PLAIN_MESSAGE
        );

        if (query == null) {
            return;
        }

        String normalizedQuery = query.trim().toLowerCase();
        if (normalizedQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a full first name, last name, or full name.",
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Faculty> filteredFaculty = new ArrayList<>();
        for (Faculty f : allFaculty) {
            String firstName = f.getFirstName() != null ? f.getFirstName().trim().toLowerCase() : "";
            String lastName = f.getLastName() != null ? f.getLastName().trim().toLowerCase() : "";
            String fullName = (firstName + " " + lastName).trim();

            if (fullName.equals(normalizedQuery)
                    || firstName.equals(normalizedQuery)
                    || lastName.equals(normalizedQuery)) {
                filteredFaculty.add(f);
            }
        }

        if (filteredFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No faculty matched your search.",
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        showFacultyResultsDialog(filteredFaculty, "Search Results");
    }

    private void showFacultyResultsDialog(List<Faculty> faculty, String title) {
        StringBuilder sb = new StringBuilder("Faculty List:\n\n");
        for (Faculty f : faculty) {
            sb.append("- ")
                    .append(f.getFirstName()).append(" ").append(f.getLastName())
                    .append(" | ").append(f.getEmail())
                    .append(" | ").append(f.getDepartment())
                    .append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString(), 16, 50);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEditFacultyDialog() {
        if (allFaculty == null || allFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No faculty available to edit.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = new String[allFaculty.size()];
        for (int i = 0; i < allFaculty.size(); i++) {
            Faculty f = allFaculty.get(i);
            options[i] = f.getFirstName() + " " + f.getLastName() + " (" + f.getEmail() + ")";
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select faculty to edit:",
                "Edit Faculty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) return;
        Faculty selected = allFaculty.get(selectedIndex);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        JTextField firstNameField = new JTextField(selected.getFirstName());
        JTextField lastNameField = new JTextField(selected.getLastName());
        JTextField emailField = new JTextField(selected.getEmail());
        JTextField departmentField = new JTextField(selected.getDepartment());

        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Department:")); panel.add(departmentField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Faculty",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        Faculty updated = new Faculty();
        updated.setFirstName(firstNameField.getText().trim());
        updated.setLastName(lastNameField.getText().trim());
        updated.setEmail(emailField.getText().trim());
        updated.setDepartment(departmentField.getText().trim());

        if (updated.getFirstName().isEmpty() || updated.getLastName().isEmpty()
                || updated.getEmail().isEmpty() || updated.getDepartment().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                ApiClient.editFaculty(selected.getId(), updated);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Faculty updated successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    fetchFacultyThen(null);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error editing faculty: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showDeleteFacultyDialog() {
        if (allFaculty == null || allFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No faculty available to delete.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = new String[allFaculty.size()];
        for (int i = 0; i < allFaculty.size(); i++) {
            Faculty f = allFaculty.get(i);
            options[i] = f.getFirstName() + " " + f.getLastName() + " (" + f.getEmail() + ")";
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select faculty to delete:",
                "Delete Faculty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) return;
        Faculty selected = allFaculty.get(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete faculty " + selected + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        new Thread(() -> {
            try {
                ApiClient.deleteFaculty(selected.getId());
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Faculty deleted successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    fetchFacultyThen(null);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error deleting faculty: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showManageStudentProfilesDialog() {
        String[] options = {"Add Student", "Edit Student", "Delete Student", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose a student action:",
                "Manage Students",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0 -> showAddStudentDialog();
            case 1 -> fetchStudentsThen(this::showEditStudentDialog);
            case 2 -> fetchStudentsThen(this::showDeleteStudentDialog);
            default -> { }
        }
    }

    private void showStudentListDialog() {
        if (allStudents == null || allStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No students found.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = {"Search by Name", "View All", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose how to view students:",
                "Student List",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 1) {
            showStudentResultsDialog(allStudents, "Students");
            return;
        }

        if (choice != 0) {
            return;
        }

        String query = JOptionPane.showInputDialog(
                this,
                "Enter student name to search:",
                "Student Search",
                JOptionPane.PLAIN_MESSAGE
        );

        if (query == null) {
            return;
        }

        String normalizedQuery = query.trim().toLowerCase();
        if (normalizedQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a full first name, last name, or full name.",
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        List<Student> filteredStudents = new ArrayList<>();
        for (Student s : allStudents) {
            String firstName = s.getFirstName() != null ? s.getFirstName().trim().toLowerCase() : "";
            String lastName = s.getLastName() != null ? s.getLastName().trim().toLowerCase() : "";
            String fullName = (firstName + " " + lastName).trim();

            // Exact match only: full name, first name, or last name.
            if (fullName.equals(normalizedQuery)
                    || firstName.equals(normalizedQuery)
                    || lastName.equals(normalizedQuery)) {
                filteredStudents.add(s);
            }
        }

        if (filteredStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No students matched your search.",
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        showStudentResultsDialog(filteredStudents, "Search Results");
    }

    private void showStudentResultsDialog(List<Student> students, String title) {
        StringBuilder sb = new StringBuilder("Student List:\n\n");
        for (Student s : students) {
            sb.append("- ")
                    .append(s.getFirstName()).append(" ").append(s.getLastName())
                    .append(" | ").append(s.getStudentNumber())
                    .append(" | ").append(s.getEmail())
                    .append(" | ").append(s.getFacultyName())
                    .append(" / ").append(s.getProgramName())
                    .append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString(), 16, 55);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddStudentDialog() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField studentNumberField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField programField = new JTextField();

        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Student Number:")); panel.add(studentNumberField);
        panel.add(new JLabel("Department:")); panel.add(departmentField);
        panel.add(new JLabel("Program:")); panel.add(programField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        Student student = new Student();
        student.setFirstName(firstNameField.getText().trim());
        student.setLastName(lastNameField.getText().trim());
        student.setEmail(emailField.getText().trim());
        student.setStudentNumber(studentNumberField.getText().trim());
        student.setFacultyName(departmentField.getText().trim());
        student.setProgramName(programField.getText().trim());

        if (student.getFirstName().isEmpty() || student.getLastName().isEmpty() || student.getEmail().isEmpty()
                || student.getStudentNumber().isEmpty() || student.getFacultyName().isEmpty() || student.getProgramName().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                ApiClient.addStudent(student);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Student added successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    fetchStudentsThen(null);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error adding student: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showEditStudentDialog() {
        if (allStudents == null || allStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No students available to edit.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = new String[allStudents.size()];
        for (int i = 0; i < allStudents.size(); i++) {
            Student s = allStudents.get(i);
            options[i] = s.getFirstName() + " " + s.getLastName() + " (" + s.getStudentNumber() + ")";
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select student to edit:",
                "Edit Student",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) return;
        Student selected = allStudents.get(selectedIndex);

        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField(selected.getFirstName());

        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField(selected.getLastName());

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(selected.getEmail());

        JLabel studentNumberLabel = new JLabel("Student Number:");
        JTextField studentNumberField = new JTextField(selected.getStudentNumber());

        JLabel departmentLabel = new JLabel("Department:");
        JTextField departmentField = new JTextField(selected.getFacultyName());

        JLabel programLabel = new JLabel("Program:");
        JTextField programField = new JTextField(selected.getProgramName());

        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(studentNumberLabel);
        panel.add(studentNumberField);
        panel.add(departmentLabel);
        panel.add(departmentField);
        panel.add(programLabel);
        panel.add(programField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If user clicks OK, update the student using the API and refresh the student list, otherwise do nothing
        if (result == JOptionPane.OK_OPTION) {
            Student updated = new Student();
            updated.setId(selected.getId());
            updated.setFirstName(firstNameField.getText().trim());
            updated.setLastName(lastNameField.getText().trim());
            updated.setEmail(emailField.getText().trim());
            updated.setStudentNumber(studentNumberField.getText().trim());
            updated.setFacultyName(departmentField.getText().trim());
            updated.setProgramName(programField.getText().trim());

            if (updated.getFirstName().isEmpty() || updated.getLastName().isEmpty() || updated.getEmail().isEmpty()
                    || updated.getStudentNumber().isEmpty() || updated.getFacultyName().isEmpty() || updated.getProgramName().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new Thread(() -> {
                try {
                    ApiClient.editStudent(selected.getId(), updated);
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Student updated successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        fetchStudentsThen(null);
                    });
                } catch (Exception e) {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Error editing student: " + errorMsg,
                            "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        }
    }

    private void showDeleteStudentDialog() {
        if (allStudents == null || allStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No students available to delete.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = new String[allStudents.size()];
        for (int i = 0; i < allStudents.size(); i++) {
            Student s = allStudents.get(i);
            options[i] = s.getFirstName() + " " + s.getLastName() + " (" + s.getStudentNumber() + ")";
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select student to delete:",
                "Delete Student",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) return;
        Student selected = allStudents.get(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student " + selected + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        new Thread(() -> {
            try {
                ApiClient.deleteStudent(selected.getId());
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    fetchStudentsThen(null);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error deleting student: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void fetchTranscriptsThen(Runnable onSuccess) {
        new Thread(() -> {
            try {
                List<Transcript> transcripts = ApiClient.getAllTranscripts();
                if (transcripts == null) {
                    transcripts = new java.util.ArrayList<>();
                }
                allTranscripts = transcripts;
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

    private void showManageTranscriptsDialog() {
        String[] options = {"View Transcripts", "Add Transcript", "Edit Transcript", "Delete Transcript", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose a transcript action:",
                "Manage Transcript Records",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0 -> fetchTranscriptsThen(this::showTranscriptListDialog);
            case 1 -> fetchCoursesThen(() -> fetchStudentsThen(this::showAddTranscriptDialog));
            case 2 -> fetchTranscriptsThen(() -> fetchCoursesThen(() -> fetchStudentsThen(this::showEditTranscriptDialog)));
            case 3 -> fetchTranscriptsThen(this::showDeleteTranscriptDialog);
            default -> { }
        }
    }

    private void showTranscriptListDialog() {
        if (allTranscripts == null || allTranscripts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No transcript records found.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Transcript Records:\n\n");
        for (Transcript t : allTranscripts) {
            String studentLabel = t.getStudent() != null ? t.getStudent().toString() : "Unknown Student";
            String courseLabel = t.getCourse() != null ? t.getCourse().toString() : "Unknown Course";
            sb.append("- ")
                    .append(studentLabel)
                    .append(" | ")
                    .append(courseLabel)
                    .append(" | Grade: ")
                    .append(t.getGrade() != null ? t.getGrade() : "N/A")
                    .append(" | Term: ")
                    .append(t.getTerm() != null ? t.getTerm() : "N/A")
                    .append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString(), 16, 60);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                "Transcripts", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddTranscriptDialog() {
        if (allStudents == null || allStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No students available. Add students first.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (allCourses == null || allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No courses available. Add courses first.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        JComboBox<Student> studentCombo = new JComboBox<>(allStudents.toArray(new Student[0]));
        JComboBox<Course> courseCombo = new JComboBox<>(allCourses.toArray(new Course[0]));
        JTextField gradeField = new JTextField();
        JTextField termField = new JTextField();

        panel.add(new JLabel("Student:")); panel.add(studentCombo);
        panel.add(new JLabel("Course:")); panel.add(courseCombo);
        panel.add(new JLabel("Grade:")); panel.add(gradeField);
        panel.add(new JLabel("Term:")); panel.add(termField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Transcript",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        Student selectedStudent = (Student) studentCombo.getSelectedItem();
        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        String gradeStr = gradeField.getText().trim();
        String term = termField.getText().trim();

        if (selectedStudent == null || selectedCourse == null || gradeStr.isEmpty() || term.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Double grade = Double.parseDouble(gradeStr);
            Transcript transcript = new Transcript();
            transcript.setStudent(selectedStudent);
            transcript.setCourse(selectedCourse);
            transcript.setGrade(grade);
            transcript.setTerm(term);

            new Thread(() -> {
                try {
                    ApiClient.addTranscript(transcript);
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Transcript added successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        fetchTranscriptsThen(null);
                    });
                } catch (Exception e) {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Error adding transcript: " + errorMsg,
                            "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Grade must be a number.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEditTranscriptDialog() {
        if (allTranscripts == null || allTranscripts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No transcripts available to edit.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = new String[allTranscripts.size()];
        for (int i = 0; i < allTranscripts.size(); i++) {
            options[i] = allTranscripts.get(i).toString();
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select transcript to edit:",
                "Edit Transcript",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) return;
        Transcript selected = allTranscripts.get(selectedIndex);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        JComboBox<Student> studentCombo = new JComboBox<>(allStudents.toArray(new Student[0]));
        JComboBox<Course> courseCombo = new JComboBox<>(allCourses.toArray(new Course[0]));
        JTextField gradeField = new JTextField(selected.getGrade() != null ? String.valueOf(selected.getGrade()) : "");
        JTextField termField = new JTextField(selected.getTerm() != null ? selected.getTerm() : "");

        if (selected.getStudent() != null && selected.getStudent().getId() != null) {
            for (int i = 0; i < studentCombo.getItemCount(); i++) {
                Student s = studentCombo.getItemAt(i);
                if (s != null && s.getId() != null && s.getId().equals(selected.getStudent().getId())) {
                    studentCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (selected.getCourse() != null && selected.getCourse().getId() != null) {
            for (int i = 0; i < courseCombo.getItemCount(); i++) {
                Course c = courseCombo.getItemAt(i);
                if (c != null && c.getId() != null && c.getId().equals(selected.getCourse().getId())) {
                    courseCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        panel.add(new JLabel("Student:")); panel.add(studentCombo);
        panel.add(new JLabel("Course:")); panel.add(courseCombo);
        panel.add(new JLabel("Grade:")); panel.add(gradeField);
        panel.add(new JLabel("Term:")); panel.add(termField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Transcript",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        Student updatedStudent = (Student) studentCombo.getSelectedItem();
        Course updatedCourse = (Course) courseCombo.getSelectedItem();
        String gradeStr = gradeField.getText().trim();
        String term = termField.getText().trim();

        if (updatedStudent == null || updatedCourse == null || gradeStr.isEmpty() || term.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Double grade = Double.parseDouble(gradeStr);
            Transcript updated = new Transcript();
            updated.setStudent(updatedStudent);
            updated.setCourse(updatedCourse);
            updated.setGrade(grade);
            updated.setTerm(term);

            new Thread(() -> {
                try {
                    ApiClient.editTranscript(selected.getId(), updated);
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Transcript updated successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        fetchTranscriptsThen(null);
                    });
                } catch (Exception e) {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Error editing transcript: " + errorMsg,
                            "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Grade must be a number.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDeleteTranscriptDialog() {
        if (allTranscripts == null || allTranscripts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No transcripts available to delete.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = new String[allTranscripts.size()];
        for (int i = 0; i < allTranscripts.size(); i++) {
            options[i] = allTranscripts.get(i).toString();
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select transcript to delete:",
                "Delete Transcript",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedIndex < 0) return;
        Transcript selected = allTranscripts.get(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete transcript record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        new Thread(() -> {
            try {
                ApiClient.deleteTranscript(selected.getId());
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Transcript deleted successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    fetchTranscriptsThen(null);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error deleting transcript: " + errorMsg,
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

    public void refreshReportsIfNeeded() {
        if (moduleIdx != 4) {
            return;
        }
        fetchReportStudentsThen(null);
        fetchReportFacultyThen(null);
    }

    private void showMostActiveStudentsDialog() {
        new Thread(() -> {
            try {
                List<Map<String, Object>> rows = ApiClient.getMostActiveStudents();
                SwingUtilities.invokeLater(() -> {
                    if (rows == null || rows.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "No appointment data available.",
                                "Most Active Students", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    StringBuilder sb = new StringBuilder("Most Active Students:\n\n");
                    for (Map<String, Object> row : rows) {
                        String firstName = String.valueOf(row.getOrDefault("firstName", ""));
                        String lastName = String.valueOf(row.getOrDefault("lastName", ""));
                        long count = toLong(row.get("appointmentCount"));
                        sb.append("- ").append(firstName).append(" ").append(lastName)
                                .append(" | Appointments: ").append(count)
                                .append("\n");
                    }

                    JTextArea textArea = new JTextArea(sb.toString(), 14, 50);
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                            "Most Active Students", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading most active students: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showBusiestAdvisorsDialog() {
        new Thread(() -> {
            try {
                List<Map<String, Object>> rows = ApiClient.getBusiestAdvisors();
                SwingUtilities.invokeLater(() -> {
                    if (rows == null || rows.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "No appointment data available.",
                                "Busiest Advisors", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    StringBuilder sb = new StringBuilder("Busiest Advisors:\n\n");
                    for (Map<String, Object> row : rows) {
                        String firstName = String.valueOf(row.getOrDefault("firstName", ""));
                        String lastName = String.valueOf(row.getOrDefault("lastName", ""));
                        long count = toLong(row.get("appointmentCount"));
                        sb.append("- ").append(firstName).append(" ").append(lastName)
                                .append(" | Appointments: ").append(count)
                                .append("\n");
                    }

                    JTextArea textArea = new JTextArea(sb.toString(), 14, 50);
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                            "Busiest Advisors", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading busiest advisors: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showCourseEnrolmentStatsDialog() {
        new Thread(() -> {
            try {
                List<Section> sections = ApiClient.getAllSections();
                if (sections == null) {
                    sections = new ArrayList<>();
                }

                java.util.Map<String, int[]> totalsByCourse = new java.util.LinkedHashMap<>();
                for (Section section : sections) {
                    String courseCode = (section.getCourse() != null && section.getCourse().getCourseCode() != null)
                            ? section.getCourse().getCourseCode()
                            : "N/A";
                    int enrolled = section.getEnrolledCount() != null ? section.getEnrolledCount() : 0;
                    int capacity = section.getCapacity() != null ? section.getCapacity() : 0;
                    int[] totals = totalsByCourse.computeIfAbsent(courseCode, key -> new int[]{0, 0, 0});
                    totals[0] += enrolled;
                    totals[1] += capacity;
                    totals[2] += 1;
                }

                List<java.util.Map.Entry<String, int[]>> rows = new ArrayList<>(totalsByCourse.entrySet());
                rows.sort(Comparator.comparingInt((java.util.Map.Entry<String, int[]> e) -> e.getValue()[0]).reversed());

                SwingUtilities.invokeLater(() -> {
                    if (rows.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "No section data available.",
                                "Course Enrolment Stats", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    StringBuilder sb = new StringBuilder("Course Enrolment Stats:\n\n");
                    for (java.util.Map.Entry<String, int[]> row : rows) {
                        int enrolled = row.getValue()[0];
                        int capacity = row.getValue()[1];
                        int sectionCount = row.getValue()[2];
                        double utilization = capacity > 0 ? (enrolled * 100.0 / capacity) : 0.0;
                        sb.append("- ").append(row.getKey())
                                .append(" | Enrolled: ").append(enrolled)
                                .append(" / ").append(capacity)
                                .append(" | Sections: ").append(sectionCount)
                                .append(" | Utilization: ").append(String.format(java.util.Locale.US, "%.1f", utilization)).append("%")
                                .append("\n");
                    }

                    JTextArea textArea = new JTextArea(sb.toString(), 16, 60);
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                            "Course Enrolment Stats", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading enrolment stats: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showDashboardOverviewDialog() {
        new Thread(() -> {
            try {
                Map<String, Double> counts = ApiClient.getAppointmentCounts();
                List<Student> students = ApiClient.getReportStudents();
                List<Faculty> faculty = ApiClient.getReportFaculty();
                List<Course> courses = ApiClient.getAllCourses();

                long totalAppointments = toLong(counts != null ? counts.get("totalAppointments") : null);
                long activeAppointments = toLong(counts != null ? counts.get("activeAppointments") : null);
                long cancelledAppointments = toLong(counts != null ? counts.get("cancelledAppointments") : null);

                int studentCount = students != null ? students.size() : 0;
                int facultyCount = faculty != null ? faculty.size() : 0;
                int courseCount = courses != null ? courses.size() : 0;

                StringBuilder sb = new StringBuilder("Dashboard Overview:\n\n");
                sb.append("Students: ").append(studentCount).append("\n");
                sb.append("Faculty: ").append(facultyCount).append("\n");
                sb.append("Courses: ").append(courseCount).append("\n\n");
                sb.append("Appointments (Total): ").append(totalAppointments).append("\n");
                sb.append("Appointments (Active): ").append(activeAppointments).append("\n");
                sb.append("Appointments (Cancelled): ").append(cancelledAppointments).append("\n");

                SwingUtilities.invokeLater(() -> {
                    JTextArea textArea = new JTextArea(sb.toString(), 14, 48);
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                            "Dashboard Overview", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading dashboard overview: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private long toLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value == null) {
            return 0L;
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
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

    // Scheduling module methods

    private void showSchedulingBrowseSectionsDialog() {
        fetchCoursesThen(this::showSchedulingBrowseSectionsByCourseDialog);
    }

    private void showSchedulingBrowseSectionsByCourseDialog() {
        if (allCourses == null || allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No courses found.",
                    "Browse Course Sections", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<Course> courseCombo = new JComboBox<>(allCourses.toArray(new Course[0]));
        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel("Course:"));
        panel.add(courseCombo);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Browse Course Sections",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        showFilteredSectionsByCourse(selectedCourse);
    }

    private void showFilteredSectionsByCourse(Course selectedCourse) {
        new Thread(() -> {
            try {
                List<Section> sections = ApiClient.getAllSections();
                if (sections == null) {
                    sections = new ArrayList<>();
                }

                List<Section> filtered = new ArrayList<>();
                Long selectedId = selectedCourse.getId();
                String selectedCode = selectedCourse.getCourseCode() != null
                        ? selectedCourse.getCourseCode().trim()
                        : "";

                for (Section s : sections) {
                    Course c = s != null ? s.getCourse() : null;
                    if (c == null) continue;

                    boolean idMatch = selectedId != null && c.getId() != null && selectedId.equals(c.getId());
                    boolean codeMatch = !selectedCode.isEmpty()
                            && c.getCourseCode() != null
                            && selectedCode.equalsIgnoreCase(c.getCourseCode().trim());

                    if (idMatch || codeMatch) {
                        filtered.add(s);
                    }
                }

                String courseCode = selectedCourse.getCourseCode() != null
                        ? selectedCourse.getCourseCode()
                        : "Selected Course";

                List<Section> finalFiltered = filtered;
                SwingUtilities.invokeLater(() -> {
                    StringBuilder sb = new StringBuilder("Sections for ")
                            .append(courseCode)
                            .append(":\n\n");

                    if (finalFiltered.isEmpty()) {
                        sb.append("No sections found for this course.");
                    } else {
                        for (Section s : finalFiltered) {
                            String secNo = s.getSectionNumber() != null ? s.getSectionNumber() : "N/A";
                            String instructor = s.getInstructorName() != null ? s.getInstructorName() : "N/A";
                            String day = s.getDayOfWeek() != null ? s.getDayOfWeek() : "N/A";
                            String room = (s.getRoom() != null && !s.getRoom().isBlank()) ? s.getRoom() : "N/A";
                            String labDay = (s.getLabDayOfWeek() != null && !s.getLabDayOfWeek().isBlank()) ? s.getLabDayOfWeek() : "N/A";
                            String labTime = (s.getLabTime() != null && !s.getLabTime().isBlank()) ? s.getLabTime() : "N/A";

                            sb.append("- Sec ").append(secNo)
                                    .append(" | ").append(instructor)
                                    .append(" | ").append(day)
                                    .append(" | Room: ").append(room)
                                    .append(" | Lab: ").append(labDay).append(" ").append(labTime)
                                    .append("\n");
                        }
                    }

                    JTextArea textArea = new JTextArea(sb.toString(), 18, 55);
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    JOptionPane.showMessageDialog(this,
                            new JScrollPane(textArea),
                            "Browse Course Sections",
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

    private void showSchedulingBuildTimetableDialog() {
        String[] options = {"All Schedules", "By Course", "By Day", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose how to view timetable data:",
                "Build Timetable",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice < 0 || choice == 3) {
            return;
        }

        if (choice == 1) {
            fetchCoursesThen(this::showSchedulingCourseTimetableDialog);
            return;
        }

        if (choice == 2) {
            showSchedulingDayTimetableDialog();
            return;
        }

        new Thread(() -> {
            try {
                List<Schedule> schedules = ApiClient.getAllSchedules();
                SwingUtilities.invokeLater(() -> showScheduleResultsDialog(
                        schedules,
                        "All Schedules",
                        "Timetable"
                ));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading timetable: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showSchedulingCourseTimetableDialog() {
        if (allCourses == null || allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No courses found.",
                    "Build Timetable", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<Course> courseCombo = new JComboBox<>(allCourses.toArray(new Course[0]));
        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel("Course:"));
        panel.add(courseCombo);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Build Timetable (By Course)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        Course selected = (Course) courseCombo.getSelectedItem();
        if (selected == null || selected.getId() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a valid course.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                List<Schedule> schedules = ApiClient.getCourseSchedules(selected.getId());
                String code = selected.getCourseCode() != null ? selected.getCourseCode() : "Course";
                SwingUtilities.invokeLater(() -> showScheduleResultsDialog(
                        schedules,
                        "Timetable for " + code,
                        "Course Timetable"
                ));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading course timetable: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showSchedulingDayTimetableDialog() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select day:",
                "Build Timetable (By Day)",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                days,
                days[0]);
        if (selectedIndex < 0) {
            return;
        }

        String selectedDay = days[selectedIndex];
        new Thread(() -> {
            try {
                List<Schedule> schedules = ApiClient.getSchedulesByDay(selectedDay);
                SwingUtilities.invokeLater(() -> showScheduleResultsDialog(
                        schedules,
                        "Timetable for " + selectedDay,
                        "Day Timetable"
                ));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading schedules by day: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showSchedulingCheckConflictsDialog() {
        new Thread(() -> {
            try {
                List<Schedule> schedules = ApiClient.getAllSchedules();
                if (schedules == null || schedules.size() < 2) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "At least two schedules are required to check conflicts.",
                            "Check Conflicts", JOptionPane.INFORMATION_MESSAGE));
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    String[] options = toScheduleOptions(schedules);

                    int firstIndex = JOptionPane.showOptionDialog(this,
                            "Select first schedule:",
                            "Check Conflicts",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (firstIndex < 0) return;

                    int secondIndex = JOptionPane.showOptionDialog(this,
                            "Select second schedule:",
                            "Check Conflicts",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (secondIndex < 0) return;

                    if (firstIndex == secondIndex) {
                        JOptionPane.showMessageDialog(this,
                                "Please choose two different schedules.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Schedule s1 = schedules.get(firstIndex);
                    Schedule s2 = schedules.get(secondIndex);
                    if (s1.getId() == null || s2.getId() == null) {
                        JOptionPane.showMessageDialog(this,
                                "Selected schedule is missing an ID.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    new Thread(() -> {
                        try {
                            boolean conflict = ApiClient.checkScheduleConflict(s1.getId(), s2.getId());
                            String message = conflict
                                    ? "Conflict detected between selected schedules."
                                    : "No conflict found between selected schedules.";
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                                    message,
                                    "Check Conflicts",
                                    conflict ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE));
                        } catch (Exception e) {
                            String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                                    "Error checking conflict: " + errorMsg,
                                    "Error", JOptionPane.ERROR_MESSAGE));
                        }
                    }).start();
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading schedules: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showSchedulingRoomAssignmentsDialog() {
        new Thread(() -> {
            try {
                List<Section> sections = ApiClient.getAllSections();
                if (sections == null || sections.isEmpty()) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "No room assignments found.",
                            "Room Assignments", JOptionPane.INFORMATION_MESSAGE));
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    JComboBox<Section> sectionCombo = new JComboBox<>(sections.toArray(new Section[0]));
                    JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));
                    panel.setBorder(new EmptyBorder(12, 12, 12, 12));
                    panel.add(new JLabel("Section:"));
                    panel.add(sectionCombo);

                    int selection = JOptionPane.showConfirmDialog(this,
                            panel,
                            "View Room Assignments",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);

                    if (selection != JOptionPane.OK_OPTION) {
                        return;
                    }

                    Section selected = (Section) sectionCombo.getSelectedItem();
                    if (selected == null) {
                        JOptionPane.showMessageDialog(this,
                                "Please select a section.",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String courseCode = (selected.getCourse() != null && selected.getCourse().getCourseCode() != null)
                            ? selected.getCourse().getCourseCode() : "N/A";
                    String sectionNo = selected.getSectionNumber() != null ? selected.getSectionNumber() : "N/A";
                    String room = (selected.getRoom() != null && !selected.getRoom().isBlank()) ? selected.getRoom() : "N/A";
                    String day = selected.getDayOfWeek() != null ? selected.getDayOfWeek() : "N/A";
                    String labDay = (selected.getLabDayOfWeek() != null && !selected.getLabDayOfWeek().isBlank()) ? selected.getLabDayOfWeek() : "N/A";
                    String labTime = (selected.getLabTime() != null && !selected.getLabTime().isBlank()) ? selected.getLabTime() : "N/A";

                    StringBuilder sb = new StringBuilder("Room Assignment:\n\n")
                            .append("Course: ").append(courseCode).append("\n")
                            .append("Section: ").append(sectionNo).append("\n")
                            .append("Room: ").append(room).append("\n")
                            .append("Day(s): ").append(day).append("\n")
                            .append("Lab: ").append(labDay).append(" ").append(labTime);

                    JOptionPane.showMessageDialog(this,
                            sb.toString(),
                            "Room Assignments",
                            JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading room assignments: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showSchedulingLabTutorialMatchingDialog() {
        fetchCoursesThen(() -> {
            if (allCourses == null || allCourses.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No courses found.",
                        "Lab & Tutorial Matching", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JComboBox<Course> courseCombo = new JComboBox<>(allCourses.toArray(new Course[0]));
            JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));
            panel.setBorder(new EmptyBorder(12, 12, 12, 12));
            panel.add(new JLabel("Course:"));
            panel.add(courseCombo);

            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Lab & Tutorial Matching",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            Course selected = (Course) courseCombo.getSelectedItem();
            if (selected == null || selected.getId() == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select a valid course.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new Thread(() -> {
                try {
                    List<Schedule> schedules = ApiClient.getCourseSchedules(selected.getId());
                    String courseCode = selected.getCourseCode() != null ? selected.getCourseCode() : "Course";
                    SwingUtilities.invokeLater(() -> showScheduleResultsDialog(
                            schedules,
                            "Schedules for " + courseCode + " (use these to pair lectures/labs/tutorials)",
                            "Lab & Tutorial Matching"
                    ));
                } catch (Exception e) {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Error loading course schedules: " + errorMsg,
                            "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        });
    }

    private void showSchedulingExportDialog() {
        new Thread(() -> {
            try {
                List<Schedule> schedules = ApiClient.getAllSchedules();
                if (schedules == null || schedules.isEmpty()) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "No schedules to export.",
                            "Export Schedule", JOptionPane.INFORMATION_MESSAGE));
                    return;
                }

                String exportContent = buildScheduleReportText("Exported Schedule", schedules);
                SwingUtilities.invokeLater(() -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Export Schedule");
                    chooser.setSelectedFile(new java.io.File("myadvice-schedule.txt"));
                    int result = chooser.showSaveDialog(this);
                    if (result != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    java.io.File file = chooser.getSelectedFile();
                    try {
                        java.nio.file.Files.writeString(
                                file.toPath(),
                                exportContent,
                                java.nio.charset.StandardCharsets.UTF_8
                        );
                        JOptionPane.showMessageDialog(this,
                                "Schedule exported to: " + file.getAbsolutePath(),
                                "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception writeEx) {
                        String errorMsg = writeEx.getMessage() != null ? writeEx.getMessage() : writeEx.toString();
                        JOptionPane.showMessageDialog(this,
                                "Failed to export schedule: " + errorMsg,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading schedules for export: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private String[] toScheduleOptions(List<Schedule> schedules) {
        String[] options = new String[schedules.size()];
        for (int i = 0; i < schedules.size(); i++) {
            Schedule s = schedules.get(i);
            String courseCode = (s.getCourse() != null && s.getCourse().getCourseCode() != null)
                    ? s.getCourse().getCourseCode() : "N/A";
            options[i] = "ID " + (s.getId() != null ? s.getId() : "-")
                    + " | " + courseCode
                    + " | " + (s.getDayOfWeek() != null ? s.getDayOfWeek() : "N/A")
                    + " " + (s.getStartTime() != null ? s.getStartTime() : "N/A")
                    + "-" + (s.getEndTime() != null ? s.getEndTime() : "N/A");
        }
        return options;
    }

    private String buildScheduleReportText(String heading, List<Schedule> schedules) {
        StringBuilder sb = new StringBuilder(heading).append("\n\n");
        if (schedules == null || schedules.isEmpty()) {
            sb.append("No schedules found.");
            return sb.toString();
        }

        for (Schedule s : schedules) {
            String courseCode = (s.getCourse() != null && s.getCourse().getCourseCode() != null)
                    ? s.getCourse().getCourseCode() : "N/A";
            sb.append("- ID: ").append(s.getId() != null ? s.getId() : "-")
                    .append(" | Course: ").append(courseCode)
                    .append(" | Day: ").append(s.getDayOfWeek() != null ? s.getDayOfWeek() : "N/A")
                    .append(" | Time: ").append(s.getStartTime() != null ? s.getStartTime() : "N/A")
                    .append("-").append(s.getEndTime() != null ? s.getEndTime() : "N/A")
                    .append(" | Room: ").append(s.getRoomNumber() != null ? s.getRoomNumber() : "N/A")
                    .append(" | Term: ").append(s.getTerm() != null ? s.getTerm() : "N/A")
                    .append("\n");
        }
        return sb.toString();
    }

    private void showScheduleResultsDialog(List<Schedule> schedules, String heading, String title) {
        String content = buildScheduleReportText(heading, schedules);
        JTextArea textArea = new JTextArea(content, 16, 60);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JOptionPane.showMessageDialog(this,
                new JScrollPane(textArea),
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Section methods

    // Show main Section management dialog
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
            case 1 -> fetchCoursesThen(() -> fetchFacultyThen(this::showAddSectionDialog));
            case 2 -> fetchCoursesThen(() -> fetchFacultyThen(this::showEditSectionDialog));
            case 3 -> showDeleteSectionDialog();
            default -> { }
        }
    }

    // Show all sections in a dialog
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
                                    ? s.getCourse().getCourseCode() : "N/A";
                            String secNo = s.getSectionNumber() != null ? s.getSectionNumber() : "N/A";
                            String instructor = s.getInstructorName() != null ? s.getInstructorName() : "N/A";
                            String day = s.getDayOfWeek() != null ? s.getDayOfWeek() : "N/A";
                            String room = (s.getRoom() != null && !s.getRoom().isBlank()) ? s.getRoom() : "N/A";
                            String labDay = (s.getLabDayOfWeek() != null && !s.getLabDayOfWeek().isBlank()) ? s.getLabDayOfWeek() : "N/A";
                            String labTime = (s.getLabTime() != null && !s.getLabTime().isBlank()) ? s.getLabTime() : "N/A";

                            sb.append("- ")
                                    .append(courseCode).append(" | Sec ").append(secNo)
                                    .append(" | ").append(instructor)
                                    .append(" | ").append(day)
                                    .append(" | Room: ").append(room)
                                    .append(" | Lab: ").append(labDay).append(" ").append(labTime)
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

    // Add Sections dialog
    private void showAddSectionDialog() {
        // Ensure there are courses available to add sections to
        if (allCourses == null || allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No courses available. Add courses first.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (allFaculty == null || allFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No faculty available. Please add faculty from Manage User Profiles first.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Build a panel for Section addition with information
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Course selection dropdown and input fields for section details
        JComboBox<Course> courseCombo = new JComboBox<>(allCourses.toArray(new Course[0]));
        JComboBox<Faculty> facultyCombo = new JComboBox<>(allFaculty.toArray(new Faculty[0]));
        JTextField sectionNumberField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField enrolledField = new JTextField("0");
        JTextField instructorField = new JTextField();
        JTextField roomField = new JTextField();
        JList<String> dayList = new JList<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        dayList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane dayScrollPane = new JScrollPane(dayList);
        JList<String> labDayList = new JList<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        labDayList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane labDayScrollPane = new JScrollPane(labDayList);
        JTextField labTimeField = new JTextField();

        Dimension inputSize = new Dimension(220, 26);
        courseCombo.setPreferredSize(inputSize);
        facultyCombo.setPreferredSize(inputSize);
        sectionNumberField.setPreferredSize(inputSize);
        capacityField.setPreferredSize(inputSize);
        enrolledField.setPreferredSize(inputSize);
        instructorField.setPreferredSize(inputSize);
        roomField.setPreferredSize(inputSize);
        dayScrollPane.setPreferredSize(new Dimension(220, 90));
        labDayScrollPane.setPreferredSize(new Dimension(220, 90));
        labTimeField.setPreferredSize(inputSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1; panel.add(courseCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Faculty:"), gbc);
        gbc.gridx = 1; panel.add(facultyCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Section Number:"), gbc);
        gbc.gridx = 1; panel.add(sectionNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1; panel.add(capacityField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Enrolled Count:"), gbc);
        gbc.gridx = 1; panel.add(enrolledField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Instructor Name:"), gbc);
        gbc.gridx = 1; panel.add(instructorField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 1; panel.add(roomField, gbc);

        gbc.gridx = 0; gbc.gridy = 7; panel.add(new JLabel("Day(s):"), gbc);
        gbc.gridx = 1; panel.add(dayScrollPane, gbc);

        gbc.gridx = 0; gbc.gridy = 8; panel.add(new JLabel("Lab Day(s):"), gbc);
        gbc.gridx = 1; panel.add(labDayScrollPane, gbc);

        gbc.gridx = 0; gbc.gridy = 9; panel.add(new JLabel("Lab Time(s) (HH:mm-HH:mm; ...):"), gbc);
        gbc.gridx = 1; panel.add(labTimeField, gbc);

        // Confirmation button for adding the section
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Section",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        // Get selected course and section details from the form fields
        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        Faculty selectedFaculty = (Faculty) facultyCombo.getSelectedItem();
        String sectionNumber = sectionNumberField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        String enrolledStr = enrolledField.getText().trim();
        String instructorName = instructorField.getText().trim();
        String room = roomField.getText().trim();
        List<String> selectedDays = dayList.getSelectedValuesList();
        String dayOfWeek = String.join(", ", selectedDays);
        List<String> selectedLabDays = labDayList.getSelectedValuesList();
        String labDayOfWeek = String.join(", ", selectedLabDays);
        String labTime = labTimeField.getText().trim();

        if (selectedCourse == null || selectedFaculty == null || sectionNumber.isEmpty()
                || capacityStr.isEmpty() || enrolledStr.isEmpty()
                || instructorName.isEmpty() || selectedDays.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields and select at least one day.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lab fields are optional, but if one is provided both are required.
        if ((selectedLabDays.isEmpty() && !labTime.isEmpty()) || (!selectedLabDays.isEmpty() && labTime.isEmpty())) {
            JOptionPane.showMessageDialog(this,
                    "Please provide both Lab Day(s) and Lab Time(s), or leave both empty.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!selectedLabDays.isEmpty() && !labTime.isEmpty()) {
            String[] labSlots = java.util.Arrays.stream(labTime.split(";"))
                    .map(String::trim)
                    .filter(slot -> !slot.isEmpty())
                    .toArray(String[]::new);
            if (labSlots.length != selectedLabDays.size()) {
                JOptionPane.showMessageDialog(this,
                        "Enter one lab time per selected lab day, separated by ';'.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            // Fetch capacity and enrolled count variables and validate
            Integer capacity = Integer.parseInt(capacityStr);
            Integer enrolled = Integer.parseInt(enrolledStr);

            if (capacity <= 0 || enrolled < 0 || enrolled > capacity) {
                JOptionPane.showMessageDialog(this,
                        "Ensure capacity > 0 and 0 <= enrolled count <= capacity.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and save the new section with all fields using the API
            Section newSection = new Section();
            newSection.setCourse(selectedCourse);
            newSection.setFaculty(selectedFaculty);
            newSection.setSectionNumber(sectionNumber);
            newSection.setCapacity(capacity);
            newSection.setEnrolledCount(enrolled);
            newSection.setInstructorName(instructorName);
            newSection.setRoom(room.isEmpty() ? null : room);
            newSection.setDayOfWeek(dayOfWeek);
            newSection.setLabDayOfWeek(labDayOfWeek.isEmpty() ? null : labDayOfWeek);
            newSection.setLabTime(labTime.isEmpty() ? null : labTime);

            new Thread(() -> {
                try {
                    Section savedSection = ApiClient.addSection(newSection);
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Section added successfully.\n"
                                    + "Room: " + (savedSection.getRoom() != null ? savedSection.getRoom() : "N/A") + "\n"
                                    + "Lab Day: " + (savedSection.getLabDayOfWeek() != null ? savedSection.getLabDayOfWeek() : "N/A") + "\n"
                                    + "Lab Time: " + (savedSection.getLabTime() != null ? savedSection.getLabTime() : "N/A"),
                            "Success", JOptionPane.INFORMATION_MESSAGE));
                    SwingUtilities.invokeLater(this::showListSectionsDialog);
                } catch (Exception e) {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Error adding section: " + errorMsg,
                            "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Capacity and Enrolled Count must be numbers.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit section dialog
    private void showEditSectionDialog() {
        new Thread(() -> {
            // Fetch all sections from the API
            try {
                List<Section> sections = ApiClient.getAllSections();
                if (sections == null || sections.isEmpty()) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "No sections available to edit.",
                            "Info", JOptionPane.INFORMATION_MESSAGE));
                    return;
                }
                // Show section chooser containing all sections
                List<Section> finalSections = sections;
                SwingUtilities.invokeLater(() -> showEditSectionChooser(finalSections));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading sections: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showEditSectionChooser(List<Section> sections) {
        // Get current section options
        String[] sectionOptions = new String[sections.size()];
        for (int i = 0; i < sections.size(); i++) {
            Section s = sections.get(i);
            String courseCode = (s.getCourse() != null && s.getCourse().getCourseCode() != null)
                    ? s.getCourse().getCourseCode() : "N/A";
            String secNo = s.getSectionNumber() != null ? s.getSectionNumber() : "N/A";
            sectionOptions[i] = courseCode + " - Sec " + secNo;
        }

        // Store selected section for editing
        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select a section to edit:",
                "Edit Section",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                sectionOptions,
                sectionOptions[0]);

        if (selectedIndex < 0) return;
        Section selectedSection = sections.get(selectedIndex);
        showEditSectionForm(selectedSection);
    }

    private void showEditSectionForm(Section selectedSection) {
        if (allFaculty == null || allFaculty.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No faculty available. Please add faculty from Manage User Profiles first.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a panel for editing section details
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Course selection dropdown and input fields for section details
        JComboBox<Course> courseCombo = new JComboBox<>(allCourses.toArray(new Course[0]));
        JComboBox<Faculty> facultyCombo = new JComboBox<>(allFaculty.toArray(new Faculty[0]));
        JTextField sectionNumberField = new JTextField(selectedSection.getSectionNumber() != null ? selectedSection.getSectionNumber() : "");
        JTextField capacityField = new JTextField(selectedSection.getCapacity() != null ? String.valueOf(selectedSection.getCapacity()) : "");
        JTextField enrolledField = new JTextField(selectedSection.getEnrolledCount() != null ? String.valueOf(selectedSection.getEnrolledCount()) : "0");
        JTextField instructorField = new JTextField(selectedSection.getInstructorName() != null ? selectedSection.getInstructorName() : "");
        JTextField roomField = new JTextField(selectedSection.getRoom() != null ? selectedSection.getRoom() : "");
        JList<String> dayList = new JList<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        dayList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane dayScrollPane = new JScrollPane(dayList);
        JList<String> labDayList = new JList<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        labDayList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane labDayScrollPane = new JScrollPane(labDayList);
        JTextField labTimeField = new JTextField(selectedSection.getLabTime() != null ? selectedSection.getLabTime() : "");

        // Get current course selected in the combobox
        if (selectedSection.getCourse() != null && selectedSection.getCourse().getId() != null) {
            for (int i = 0; i < courseCombo.getItemCount(); i++) {
                Course c = courseCombo.getItemAt(i);
                if (c != null && c.getId() != null && c.getId().equals(selectedSection.getCourse().getId())) {
                    courseCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (selectedSection.getDayOfWeek() != null && !selectedSection.getDayOfWeek().isBlank()) {
            String[] existingDays = selectedSection.getDayOfWeek().split(",");
            java.util.Set<String> existingDaySet = new java.util.HashSet<>();
            for (String d : existingDays) {
                existingDaySet.add(d.trim());
            }
            java.util.List<Integer> indices = new java.util.ArrayList<>();
            ListModel<String> model = dayList.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                if (existingDaySet.contains(model.getElementAt(i))) {
                    indices.add(i);
                }
            }
            int[] selectedIndices = new int[indices.size()];
            for (int i = 0; i < indices.size(); i++) {
                selectedIndices[i] = indices.get(i);
            }
            dayList.setSelectedIndices(selectedIndices);
        }

        // Set input field dimensions
        Dimension inputSize = new Dimension(220, 26);
        courseCombo.setPreferredSize(inputSize);
        facultyCombo.setPreferredSize(inputSize);
        sectionNumberField.setPreferredSize(inputSize);
        capacityField.setPreferredSize(inputSize);
        enrolledField.setPreferredSize(inputSize);
        instructorField.setPreferredSize(inputSize);
        roomField.setPreferredSize(inputSize);
        dayScrollPane.setPreferredSize(new Dimension(220, 90));
        labDayScrollPane.setPreferredSize(new Dimension(220, 90));
        labTimeField.setPreferredSize(inputSize);

        if (selectedSection.getLabDayOfWeek() != null && !selectedSection.getLabDayOfWeek().isBlank()) {
            String[] existingLabDays = selectedSection.getLabDayOfWeek().split(",");
            java.util.Set<String> existingLabDaySet = new java.util.HashSet<>();
            for (String d : existingLabDays) {
                existingLabDaySet.add(d.trim());
            }
            java.util.List<Integer> labIndices = new java.util.ArrayList<>();
            ListModel<String> labModel = labDayList.getModel();
            for (int i = 0; i < labModel.getSize(); i++) {
                if (existingLabDaySet.contains(labModel.getElementAt(i))) {
                    labIndices.add(i);
                }
            }
            int[] selectedLabIndices = new int[labIndices.size()];
            for (int i = 0; i < labIndices.size(); i++) {
                selectedLabIndices[i] = labIndices.get(i);
            }
            labDayList.setSelectedIndices(selectedLabIndices);
        }

        if (selectedSection.getFaculty() != null && selectedSection.getFaculty().getId() != null) {
            for (int i = 0; i < facultyCombo.getItemCount(); i++) {
                Faculty f = facultyCombo.getItemAt(i);
                if (f != null && f.getId() != null && f.getId().equals(selectedSection.getFaculty().getId())) {
                    facultyCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Layout all fields in the panel using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1; panel.add(courseCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Faculty:"), gbc);
        gbc.gridx = 1; panel.add(facultyCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Section Number:"), gbc);
        gbc.gridx = 1; panel.add(sectionNumberField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1; panel.add(capacityField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Enrolled Count:"), gbc);
        gbc.gridx = 1; panel.add(enrolledField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Instructor Name:"), gbc);
        gbc.gridx = 1; panel.add(instructorField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 1; panel.add(roomField, gbc);
        gbc.gridx = 0; gbc.gridy = 7; panel.add(new JLabel("Day(s):"), gbc);
        gbc.gridx = 1; panel.add(dayScrollPane, gbc);

        gbc.gridx = 0; gbc.gridy = 8; panel.add(new JLabel("Lab Day(s):"), gbc);
        gbc.gridx = 1; panel.add(labDayScrollPane, gbc);

        gbc.gridx = 0; gbc.gridy = 9; panel.add(new JLabel("Lab Time(s) (HH:mm-HH:mm; ...):"), gbc);
        gbc.gridx = 1; panel.add(labTimeField, gbc);

        // Confirmation button for editing the section
        int result = JOptionPane.showConfirmDialog(this, panel, "Confirm Edit",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        // Get selected course and section details from the form fields
        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        Faculty selectedFaculty = (Faculty) facultyCombo.getSelectedItem();
        String sectionNumber = sectionNumberField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        String enrolledStr = enrolledField.getText().trim();
        String instructorName = instructorField.getText().trim();
        String room = roomField.getText().trim();
        List<String> selectedDays = dayList.getSelectedValuesList();
        String dayOfWeek = String.join(", ", selectedDays);
        List<String> selectedLabDays = labDayList.getSelectedValuesList();
        String labDayOfWeek = String.join(", ", selectedLabDays);
        String labTime = labTimeField.getText().trim();

        // Handle validation/empty errors
        if (selectedCourse == null || selectedFaculty == null || sectionNumber.isEmpty()
                || capacityStr.isEmpty() || enrolledStr.isEmpty() || instructorName.isEmpty() || selectedDays.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields and select at least one day.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((selectedLabDays.isEmpty() && !labTime.isEmpty()) || (!selectedLabDays.isEmpty() && labTime.isEmpty())) {
            JOptionPane.showMessageDialog(this,
                    "Please provide both Lab Day(s) and Lab Time(s), or leave both empty.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!selectedLabDays.isEmpty() && !labTime.isEmpty()) {
            String[] labSlots = java.util.Arrays.stream(labTime.split(";"))
                    .map(String::trim)
                    .filter(slot -> !slot.isEmpty())
                    .toArray(String[]::new);
            if (labSlots.length != selectedLabDays.size()) {
                JOptionPane.showMessageDialog(this,
                        "Enter one lab time per selected lab day, separated by ';'.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            Integer capacity = Integer.parseInt(capacityStr);
            Integer enrolled = Integer.parseInt(enrolledStr);
            if (capacity <= 0 || enrolled < 0 || enrolled > capacity) {
                JOptionPane.showMessageDialog(this,
                        "Ensure capacity > 0 and 0 <= enrolled count <= capacity.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Store updated section and use the API to update it
            Section updated = new Section();
            updated.setCourse(selectedCourse);
            updated.setFaculty(selectedFaculty);
            updated.setSectionNumber(sectionNumber);
            updated.setCapacity(capacity);
            updated.setEnrolledCount(enrolled);
            updated.setInstructorName(instructorName);
            updated.setRoom(room.isEmpty() ? null : room);
            updated.setDayOfWeek(dayOfWeek);
            updated.setLabDayOfWeek(labDayOfWeek.isEmpty() ? null : labDayOfWeek);
            updated.setLabTime(labTime.isEmpty() ? null : labTime);

            new Thread(() -> {
                try {
                    Section savedSection = ApiClient.editSection(selectedSection.getId(), updated);
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Section updated successfully.\n"
                                    + "Room: " + (savedSection.getRoom() != null ? savedSection.getRoom() : "N/A") + "\n"
                                    + "Lab Day: " + (savedSection.getLabDayOfWeek() != null ? savedSection.getLabDayOfWeek() : "N/A") + "\n"
                                    + "Lab Time: " + (savedSection.getLabTime() != null ? savedSection.getLabTime() : "N/A"),
                            "Success", JOptionPane.INFORMATION_MESSAGE));
                    SwingUtilities.invokeLater(this::showListSectionsDialog);
                } catch (Exception e) {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Error editing section: " + errorMsg,
                            "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Capacity and Enrolled Count must be numbers.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void showAddFacultyDialog(Runnable onSuccess) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField departmentField = new JTextField();

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Department:"));
        panel.add(departmentField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Faculty",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String department = departmentField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || department.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all faculty fields.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                ApiClient.addFaculty(firstName, lastName, email, department);
                SwingUtilities.invokeLater(() -> fetchFacultyThen(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Faculty added successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                }));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error adding faculty: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    // Delete section dialog
    private void showDeleteSectionDialog() {
        new Thread(() -> {
            try {
                List<Section> sections = ApiClient.getAllSections();
                if (sections == null || sections.isEmpty()) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "No sections available to delete.",
                            "Info", JOptionPane.INFORMATION_MESSAGE));
                    return;
                }

                List<Section> finalSections = sections;
                SwingUtilities.invokeLater(() -> showDeleteSectionChooser(finalSections));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error loading sections: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showDeleteSectionChooser(List<Section> sections) {
        String[] sectionOptions = new String[sections.size()];
        for (int i = 0; i < sections.size(); i++) {
            Section s = sections.get(i);
            String courseCode = (s.getCourse() != null && s.getCourse().getCourseCode() != null)
                    ? s.getCourse().getCourseCode() : "N/A";
            String secNo = s.getSectionNumber() != null ? s.getSectionNumber() : "N/A";
            sectionOptions[i] = courseCode + " - Sec " + secNo;
        }

        int selectedIndex = JOptionPane.showOptionDialog(this,
                "Select a section to delete:",
                "Delete Section",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                sectionOptions,
                sectionOptions[0]);

        if (selectedIndex < 0) return;
        Section selectedSection = sections.get(selectedIndex);

        String courseCode = (selectedSection.getCourse() != null && selectedSection.getCourse().getCourseCode() != null)
                ? selectedSection.getCourse().getCourseCode() : "N/A";
        String secNo = selectedSection.getSectionNumber() != null ? selectedSection.getSectionNumber() : "N/A";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete section " + courseCode + " - Sec " + secNo + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        new Thread(() -> {
            try {
                ApiClient.deleteSection(selectedSection.getId());
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Section deleted successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE));
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                        "Error deleting section: " + errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void showBookAdvisingAppointmentDialog() {
        fetchFacultyThen(this::showBookAppointmentDialog);
    }

    private void showBrowseFacultyAvailabilityDialog() {
        showFacultyAvailabilityDialog();
    }

    private void showMyUpcomingAppointmentsDialog() {
        showMyAppointmentsDialog();
    }

    private void showCancelOrRescheduleDialog() {
        showCancelRescheduleDialog();
    }
}
