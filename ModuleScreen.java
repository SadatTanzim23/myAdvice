import java.awt.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import org.json.*;

public class ModuleScreen extends JPanel {

    private static final String BASE = "http://localhost:8080";

    public ModuleScreen(int idx) {
        setLayout(new BorderLayout());
        setOpaque(false);
        add(buildSubHeader(idx), BorderLayout.NORTH);
        add(buildActionsPanel(idx), BorderLayout.CENTER);
    }

    private JPanel buildSubHeader(int idx) {
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

    private JPanel buildActionsPanel(int idx) {
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

        String[][] items = myAdvice.MODULE_ACTIONS[idx];
        for (int i = 0; i < items.length; i++) {
            final String title = items[i][0];
            final String desc  = items[i][1];
            final int moduleIdx = idx;
            final int actionIdx = i;

            gbc.gridy = i / 2 + 1;
            gbc.gridx = i % 2;
            gbc.gridwidth = 1;
            p.add(new ActionCard(title, desc, () -> handleAction(moduleIdx, actionIdx)), gbc);
        }
        return p;
    }

    private void handleAction(int module, int action) {
        switch (module) {
            case 0 -> handleCurriculum(action);
            case 1 -> handleScheduling(action);
            case 2 -> handleBookings(action);
            case 3 -> handleAdmin(action);
            case 4 -> handleReports(action);
        }
    }

    // ── CURRICULUM ADVISING ──────────────────────────────────────────────────
    private void handleCurriculum(int action) {
        switch (action) {
            case 0 -> fetchAndShow("View Program Requirements",
                BASE + "/admin/courses/1/prerequisites",
                new String[]{"ID", "Course Code", "Course Name", "Credits"});
            case 1 -> fetchAndShow("Course Prerequisite Tree",
                BASE + "/admin/courses/1/prerequisites",
                new String[]{"ID", "Course Code", "Course Name", "Credits"});
            default -> showComingSoon();
        }
    }

    // ── SCHEDULING ───────────────────────────────────────────────────────────
    private void handleScheduling(int action) {
        switch (action) {
            case 0 -> fetchAndShow("Browse Course Sections",
                BASE + "/admin/courses/1/prerequisites",
                new String[]{"ID", "Course Code", "Course Name", "Credits"});
            default -> showComingSoon();
        }
    }

    // ── BOOKINGS ─────────────────────────────────────────────────────────────
    private void handleBookings(int action) {
        switch (action) {
            case 0 -> showBookingForm();
            case 1 -> fetchAndShow("Browse Faculty Availability",
                BASE + "/bookings/faculty/1",
                new String[]{"ID", "Student ID", "Faculty ID", "Date/Time", "Status"});
            case 2 -> fetchAndShow("My Upcoming Appointments",
                BASE + "/bookings/student/1",
                new String[]{"ID", "Student ID", "Faculty ID", "Date/Time", "Status"});
            default -> showComingSoon();
        }
    }

    // ── ADMIN ────────────────────────────────────────────────────────────────
    private void handleAdmin(int action) {
        switch (action) {
            case 0 -> fetchAndShow("Manage Courses",
                BASE + "/admin/courses/1/prerequisites",
                new String[]{"ID", "Course Code", "Course Name", "Credits"});
            case 1 -> fetchAndShow("Edit Prerequisite Structures",
                BASE + "/admin/courses/1/prerequisites",
                new String[]{"ID", "Course Code", "Course Name", "Credits"});
            default -> showComingSoon();
        }
    }

    // ── REPORTS ──────────────────────────────────────────────────────────────
    private void handleReports(int action) {
        switch (action) {
            case 0 -> fetchAndShow("Student List",
                BASE + "/bookings/student/1",
                new String[]{"ID", "Student ID", "Faculty ID", "Date/Time", "Status"});
            case 1 -> fetchAndShow("Faculty List",
                BASE + "/bookings/faculty/1",
                new String[]{"ID", "Student ID", "Faculty ID", "Date/Time", "Status"});
            default -> showComingSoon();
        }
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    private void fetchAndShow(String title, String url, String[] columns) {
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override protected String doInBackground() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET().build();
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                return resp.body();
            }
            @Override protected void done() {
                try {
                    String json = get();
                    showJsonTable(title, json, columns);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ModuleScreen.this,
                        "Could not connect to backend.\nMake sure Spring Boot is running.",
                        "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void showJsonTable(String title, String json, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            org.json.JSONArray arr = new org.json.JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                org.json.JSONObject obj = arr.getJSONObject(i);
                if (obj.has("student") && obj.has("faculty")) {
                    org.json.JSONObject student = obj.getJSONObject("student");
                    org.json.JSONObject faculty = obj.getJSONObject("faculty");
                    model.addRow(new Object[]{
                        obj.optInt("id"),
                        student.optString("firstName") + " " + student.optString("lastName"),
                        faculty.optString("firstName") + " " + faculty.optString("lastName"),
                        obj.optString("dateTime"),
                        obj.optString("status")
                    });
                } else if (obj.has("courseCode")) {
                    model.addRow(new Object[]{
                        obj.optInt("id"),
                        obj.optString("courseCode"),
                        obj.optString("courseName"),
                        obj.optInt("credits")
                    });
                } else {
                    model.addRow(new Object[]{obj.toString()});
                }
            }
        } catch (Exception e) {
            model.addRow(new Object[]{"Error parsing data: " + e.getMessage()});
        }
        JTable table = new JTable(model);
        table.setBackground(myAdvice.CARD_BG);
        table.setForeground(myAdvice.TEXT_LIGHT);
        table.setGridColor(myAdvice.DIVIDER);
        table.setRowHeight(28);
        table.getTableHeader().setBackground(myAdvice.PANEL_BG);
        table.getTableHeader().setForeground(myAdvice.ACCENT);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(700, 350));
        scroll.getViewport().setBackground(myAdvice.CARD_BG);
        JOptionPane.showMessageDialog(null, scroll, title, JOptionPane.PLAIN_MESSAGE);
    }

    private void showBookingForm() {
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBackground(myAdvice.BG);

        JLabel sl = new JLabel("Student ID:");
        sl.setForeground(myAdvice.TEXT_LIGHT);
        JTextField studentField = new JTextField("1");

        JLabel fl = new JLabel("Faculty ID:");
        fl.setForeground(myAdvice.TEXT_LIGHT);
        JTextField facultyField = new JTextField("1");

        JLabel dl = new JLabel("Date/Time:");
        dl.setForeground(myAdvice.TEXT_LIGHT);
        JTextField dateField = new JTextField("2026-04-10 10:00:00");

        form.add(sl); form.add(studentField);
        form.add(fl); form.add(facultyField);
        form.add(dl); form.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Book Advising Appointment", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String body = String.format(
                "{\"studentId\":%s,\"facultyId\":%s,\"dateTime\":\"%s\",\"status\":\"PENDING\"}",
                studentField.getText(), facultyField.getText(), dateField.getText());

            SwingWorker<Integer, Void> worker = new SwingWorker<>() {
                @Override protected Integer doInBackground() throws Exception {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(BASE + "/bookings/book"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
                    return client.send(req, HttpResponse.BodyHandlers.ofString()).statusCode();
                }
                @Override protected void done() {
                    try {
                        int code = get();
                        if (code == 200 || code == 201) {
                            JOptionPane.showMessageDialog(ModuleScreen.this,
                                "Appointment booked successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(ModuleScreen.this,
                                "Booking failed. Status: " + code, "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(ModuleScreen.this,
                            "Connection error.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }

    private void showComingSoon() {
        JOptionPane.showMessageDialog(this,
            "This feature is coming soon!", "Coming Soon",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
