import java.awt.*;
import javax.swing.*;

// ── Entry point + shared constants & data ─────────────────────────────────────
public class myAdvice {
    //colors
    static final Color BG = new Color(0x0D1B2A);
    static final Color PANEL_BG = new Color(0x132233);
    static final Color ACCENT = new Color(0x00B4D8);
    static final Color ACCENT2 = new Color(0x0077A8);
    static final Color TEXT_LIGHT = new Color(0xE0F4FF);
    static final Color TEXT_MUTED = new Color(0x7BAEC8);
    static final Color CARD_BG = new Color(0x1A2F44);
    static final Color CARD_HOVER = new Color(0x1E3A52);
    static final Color DIVIDER = new Color(0x1F3A52);
    //different fonts
    static final Font FONT_TITLE = new Font("Dialog", Font.BOLD,  32);
    static final Font FONT_SUBTITLE = new Font("Dialog", Font.PLAIN, 13);
    static final Font FONT_BTN = new Font("Dialog", Font.BOLD,  15);
    static final Font FONT_BTN_SUB = new Font("Dialog", Font.PLAIN, 11);
    //id, title, subtitle
    static final String[][] MODULE_META = {
        {"1", "Curriculum Advising", "Plan degree requirements and course pathways"},
        {"2", "Scheduling", "Find sections and manage student enrollments"},
        {"3", "Bookings", "Book and manage advising appointments"},
        {"4", "System Administration", "Maintain courses, sections, users, and transcripts"},
        {"5", "Reports & Dashboards", "View operational summaries and enrollment metrics"},
    };
    //short descriptions shown on the main menu cards
    static final String[] MENU_DESCS = {
        "Review requirements and plan future courses",
        "Browse sections and enroll students into timetable slots",
        "Book, view, and update advising appointments",
        "Manage academic data, profiles, and transcript records",
        "Track key stats for students, faculty, and enrollments",
    };

    //all the action cards with their description at inside each module from the main
    static final String[][][] MODULE_ACTIONS = {
        { //Curriculum Advising
            {"View Degree Requirements", "See required courses for a selected student"},
            {"View Prerequisite Map", "Check prerequisite relationships between courses"},
            {"Plan Upcoming Semesters", "Review remaining courses before registration"},
            {"Check Program Progress", "Compare completed and remaining requirements"},
            {"Request Faculty Advising", "Find faculty guidance for course planning"},
            {"Export Academic Plan", "Export the student plan for sharing"},
        },
        { //Scheduling
            {"Find Course Sections", "Search available sections by course"},
            {"Enroll Student (Timetable)", "Enroll a student into a section and lab"},
            {"Manage Student Enrollments", "View or delete enrollments using student ID"},
            {"View Room Assignments", "Check room and lab location details by section"},
        },
        { //Bookings
            {"Book Advising Appointment",    "Create a new advising appointment"          },
            {"View Faculty Availability",    "See available faculty appointment slots"    },
            {"View My Appointments",         "Review upcoming appointments by student ID" },
            {"Cancel or Reschedule Appointment", "Update or cancel an existing booking"   },
        },
        { //System Administration
            {"Manage Courses",               "Create, update, or delete course records"  },
            {"Manage Prerequisites",         "Add or remove prerequisite relationships"   },
            {"Manage Sections & Timetable",  "Create, edit, or remove course sections"   },
            {"Manage User Profiles",         "Maintain student and faculty profile data"  },
            {"Manage Transcript Records",    "Add, edit, or delete transcript entries"   },
        },
        { //Reports & Dashboards
            {"View Student List",            "Browse all students and profile details"   },
            {"View Faculty List",            "Browse all faculty and department details" },
            {"Most Active Students",         "Identify students with highest activity"   },
            {"Busiest Advisors",             "Identify advisors with highest workload"   },
            {"Course Enrollment Stats",      "Review enrollment totals and capacity usage"},
            {"System Dashboard Overview",    "View combined academic and booking metrics"},
        },
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new MainApp().setVisible(true);
        });
    }
}
