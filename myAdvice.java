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
        {"1", "Curriculum Advising","Plan your courses & program requirements"},
        {"2", "Scheduling","Build and manage your term timetable"},
        {"3", "Bookings","Schedule advising appointments with faculty"},
        {"4", "System Administration","Manage curriculum, profiles & transcripts"},
        {"5", "Reports & Dashboards","Analytics on students, faculty & courses"},
    };
    //short descriptions shown on the main menu cards
    static final String[] MENU_DESCS = {
        "Plan future courses & program requirements",
        "Build your timetable for the upcoming year",
        "Book advising appointments with faculty",
        "Manage curriculum, profiles & transcripts",
        "View analytics on students, faculty & courses",
    };

    //all the action cards with their description at inside each module from the main
    static final String[][][] MODULE_ACTIONS = {
        { //Curriculum Advising
            {"View Program Requirements", "See all required courses for your degree"},
            {"Course Prerequisite Tree", "Visualize prerequisite chains"},
            {"Plan Future Semesters", "Map out remaining courses"},
            {"Check Completion Status", "Track completed vs. outstanding credits"},
            {"Seek Faculty Advice", "Request a curriculum review"},
            {"Export Academic Plan", "Download your plan as a PDF"},
        },
        { //Scheduling
            {"Browse Course Sections", "View available sections & instructors"},
            {"Build Timetable", "Select courses and time slots"},
            {"Check Conflicts", "Detect overlapping schedules"},
            {"View Room Assignments", "Find classroom locations"},
            {"Lab & Tutorial Matching", "Pair lectures with labs/tutorials"},
            {"Export Schedule", "Print or save your timetable"},
        },
        { //Bookings
            {"Book Advising Appointment",    "Reserve a slot with your advisor"          },
            {"Browse Faculty Availability",  "See who is available and when"             },
            {"My Upcoming Appointments",     "View and manage your bookings"             },
            {"Find COMP 400/405 Supervisor", "Locate a project supervisor"               },
            {"Research Interest Matching",   "Find faculty by research area"             },
            {"Cancel / Reschedule",          "Modify an existing appointment"            },
        },
        { //System Administration
            {"Manage Courses",               "Add, edit, or remove course records"       },
            {"Edit Prerequisite Structures", "Define prerequisite chains"                },
            {"Create / Edit Timetable",      "Set up course sections & schedules"        },
            {"Manage User Profiles",         "Update student, faculty & staff info"      },
            {"Update Transcript Records",    "Modify grade and enrollment data"          },
        },
        { //Reports & Dashboards
            {"Student List",                 "Browse and filter all students"            },
            {"Faculty List",                 "Browse and filter all faculty"             },
            {"Most Active Students",         "Students with most appointments"           },
            {"Busiest Advisors",             "Faculty with most advising load"           },
            {"Course Enrolment Stats",       "Enrolment figures by course/section"       },
            {"Dashboard Overview",           "At-a-glance system summary"               },
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
