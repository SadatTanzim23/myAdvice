import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiClient {
    private static volatile String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    // Admin API methods

    // Course API methods
    public static List<Course> getAllCourses() throws Exception {
        String lastError = null;
        for (String base : getBaseUrlCandidates()) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(base + "/admin/courses"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    BASE_URL = base;
                    return gson.fromJson(response.body(), new TypeToken<List<Course>>(){}.getType());
                }
                lastError = "status " + response.statusCode() + " from " + base;
            } catch (Exception e) {
                lastError = "connection failed to " + base;
            }
        }
        throw new Exception("Failed to load courses: " + (lastError != null ? lastError : "unable to reach backend"));
    }

    private static List<String> getBaseUrlCandidates() {
        LinkedHashSet<String> candidates = new LinkedHashSet<>();
        candidates.add(BASE_URL);
        candidates.add("http://localhost:8080");
        candidates.add("http://localhost:8081");
        candidates.add("http://localhost:8082");
        return new ArrayList<>(candidates);
    }

    public static Course addCourse(String courseCode, String courseName, Integer credits, String description) throws Exception {
        Course course = new Course(courseCode, courseName, credits, description);
        String json = gson.toJson(course);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Error handling for failed API calls
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to add course: " + response.body());
        }
        return gson.fromJson(response.body(), Course.class);
    }

    public static Course editCourse(Long courseId, String courseCode, String courseName, Integer credits, String description) throws Exception {
        Course course = new Course(courseCode, courseName, credits, description);
        String json = gson.toJson(course);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses/edit/" + courseId))
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Error handling for failed API calls
        if (response.statusCode() != 200) {
            throw new Exception("Failed to edit course: " + response.body());
        }
        return gson.fromJson(response.body(), Course.class);
    }

    public static void deleteCourse(Long courseId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses/delete/" + courseId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to delete course: " + response.body());
        }
    }

    public static List<Course> getCoursePrerequisites(Long courseId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses/" + courseId + "/prerequisites"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load prerequisites: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Course>>(){}.getType());
    }

    public static void addCoursePrerequisite(Long courseId, Long prerequisiteId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses/" + courseId + "/prerequisites/add/" + prerequisiteId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to add prerequisite: " + response.body());
        }
    }

    public static void removeCoursePrerequisite(Long courseId, Long prerequisiteId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses/" + courseId + "/prerequisites/remove/" + prerequisiteId))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to remove prerequisite: " + response.body());
        }
    }

    public static List<Faculty> getAllFaculty() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/faculty"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load faculty: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Faculty>>(){}.getType());
    }

    public static Faculty addFaculty(String firstName, String lastName, String email, String department) throws Exception {
        Faculty faculty = new Faculty();
        faculty.setFirstName(firstName);
        faculty.setLastName(lastName);
        faculty.setEmail(email);
        faculty.setDepartment(department);

        String json = gson.toJson(faculty);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/faculty/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to add faculty: " + response.body());
        }
        return gson.fromJson(response.body(), Faculty.class);
    }

    public static Faculty editFaculty(Long facultyId, Faculty faculty) throws Exception {
        String json = gson.toJson(faculty);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/faculty/edit/" + facultyId))
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to edit faculty: " + response.body());
        }
        return gson.fromJson(response.body(), Faculty.class);
    }

    public static void deleteFaculty(Long facultyId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/faculty/delete/" + facultyId))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to delete faculty: " + response.body());
        }
    }

    public static List<Student> getAllStudents() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/students"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load students: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Student>>(){}.getType());
    }

    public static Student addStudent(Student student) throws Exception {
        String json = gson.toJson(student);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/students/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to add student: " + response.body());
        }
        return gson.fromJson(response.body(), Student.class);
    }

    public static Student editStudent(Long studentId, Student student) throws Exception {
        String json = gson.toJson(student);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/students/edit/" + studentId))
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to edit student: " + response.body());
        }
        return gson.fromJson(response.body(), Student.class);
    }

    public static void deleteStudent(Long studentId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/students/delete/" + studentId))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to delete student: " + response.body());
        }
    }
// Transcript API methods
    public static List<Transcript> getAllTranscripts() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/transcripts"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load transcripts: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Transcript>>(){}.getType());
    }

    public static List<Transcript> getTranscriptsByStudent(Long studentId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/transcripts/student/" + studentId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load transcripts by student: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Transcript>>(){}.getType());
    }

    public static Transcript addTranscript(Transcript transcript) throws Exception {
        String json = gson.toJson(transcript);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/transcripts/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to add transcript: " + response.body());
        }
        return gson.fromJson(response.body(), Transcript.class);
    }

    public static Transcript editTranscript(Long transcriptId, Transcript transcript) throws Exception {
        String json = gson.toJson(transcript);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/transcripts/edit/" + transcriptId))
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to edit transcript: " + response.body());
        }
        return gson.fromJson(response.body(), Transcript.class);
    }

    public static void deleteTranscript(Long transcriptId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/transcripts/delete/" + transcriptId))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to delete transcript: " + response.body());
        }
    }

    // Booking API methods
    public static Appointment bookAppointment(Long studentId, Long facultyId, String dateTime, String status) throws Exception {
        String safeStatus = (status == null || status.isBlank()) ? "scheduled" : status.trim();
        String json = "{"
                + "\"studentId\":" + studentId + ","
                + "\"facultyId\":" + facultyId + ","
                + "\"dateTime\":\"" + escapeJson(dateTime) + "\","
                + "\"status\":\"" + escapeJson(safeStatus) + "\""
                + "}";

        HttpResponse<String> response = sendRequestWithFallback("/bookings/book", "POST", json);
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to book appointment: " + response.body());
        }
        return gson.fromJson(response.body(), Appointment.class);
    }

    public static List<Appointment> getStudentAppointments(Long studentId) throws Exception {
        HttpResponse<String> response = sendRequestWithFallback("/bookings/student/" + studentId, "GET", null);
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load student appointments: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Appointment>>(){}.getType());
    }

    public static List<Appointment> getFacultyAppointments(Long facultyId) throws Exception {
        HttpResponse<String> response = sendRequestWithFallback("/bookings/faculty/" + facultyId, "GET", null);
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load faculty appointments: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Appointment>>(){}.getType());
    }

    public static void cancelAppointment(Long appointmentId) throws Exception {
        HttpResponse<String> response = sendRequestWithFallback("/bookings/cancel/" + appointmentId, "DELETE", null);
        if (response.statusCode() != 200) {
            throw new Exception("Failed to cancel appointment: " + response.body());
        }
    }

    public static Appointment rescheduleAppointment(Long appointmentId, String newDateTime) throws Exception {
        // BookingController expects LocalDateTime in request body, represented as a JSON string.
        String json = "\"" + escapeJson(newDateTime) + "\"";
        HttpResponse<String> response = sendRequestWithFallback("/bookings/reschedule/" + appointmentId, "PUT", json);
        if (response.statusCode() != 200) {
            throw new Exception("Failed to reschedule appointment: " + response.body());
        }
        return gson.fromJson(response.body(), Appointment.class);
    }

    // Section API methods
    public static List<Section> getAllSections() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/sections"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load sections: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Section>>(){}.getType());
    }

    public static Section addSection(Section section) throws Exception {
        String json = gson.toJson(section);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/sections/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to add section: " + response.body());
        }
        return gson.fromJson(response.body(), Section.class);
    }

    public static void deleteSection(Long sectionId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/sections/delete/" + sectionId))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to delete section: " + response.body());
        }
        return;
    }

    public static Section editSection(Long sectionId, Section section) throws Exception {
        String json = gson.toJson(section);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/sections/edit/" + sectionId))
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to edit section: " + response.body());
        }
        return gson.fromJson(response.body(), Section.class);
    }

    // Report API methods
    public static List<Student> getReportStudents() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/reports/students"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load report students: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Student>>(){}.getType());
    }

    public static List<Faculty> getReportFaculty() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/reports/faculty"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load report faculty: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Faculty>>(){}.getType());
    }

    public static List<Map<String, Object>> getMostActiveStudents() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/reports/students/most-appointments"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load most active students: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Map<String, Object>>>(){}.getType());
    }

    public static List<Map<String, Object>> getBusiestAdvisors() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/reports/faculty/most-appointments"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load busiest advisors: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Map<String, Object>>>(){}.getType());
    }

    public static Map<String, Double> getAppointmentCounts() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/reports/appointments/counts"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load appointment counts: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<Map<String, Double>>(){}.getType());
    }

    // Advising API methods
    public static String getStudentProgram(Long studentId) throws Exception {
        HttpResponse<String> response = sendAdvisingRequestWithFallback("/advising/program/" + studentId);
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load student program: " + response.body());
        }
        String body = response.body();
        if (body == null) {
            return "";
        }

        String trimmed = body.trim();
        // Backend may return either plain text or a JSON string; support both.
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return gson.fromJson(trimmed, String.class);
        }
        return trimmed;
    }

    public static List<Transcript> getAdvisingCompletedCourses(Long studentId) throws Exception {
        HttpResponse<String> response = sendAdvisingRequestWithFallback("/advising/completed/" + studentId);
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load completed courses: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Transcript>>(){}.getType());
    }

    public static List<Course> getAdvisingRemainingCourses(Long studentId) throws Exception {
        HttpResponse<String> response = sendAdvisingRequestWithFallback("/advising/remaining/" + studentId);
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load remaining courses: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Course>>(){}.getType());
    }

    public static boolean checkAdvisingPrerequisites(Long studentId, Long courseId) throws Exception {
        HttpResponse<String> response = sendAdvisingRequestWithFallback("/advising/prerequisites/" + studentId + "/" + courseId);
        if (response.statusCode() != 200) {
            throw new Exception("Failed to check prerequisites: " + response.body());
        }
        return gson.fromJson(response.body(), Boolean.class);
    }

    private static HttpResponse<String> sendAdvisingRequestWithFallback(String path) throws Exception {
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(BASE_URL);
        candidates.add("http://localhost:8082");
        candidates.add("http://localhost:8081");
        candidates.add("http://localhost:8080");

        List<String> attempted = new ArrayList<>();
        HttpResponse<String> lastResponse = null;

        for (String base : candidates) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(base + path))
                    .GET()
                    .build();

            HttpResponse<String> response;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception ex) {
                attempted.add(base + " -> connection failed");
                continue;
            }

            attempted.add(base + " -> " + response.statusCode());

            if (response.statusCode() == 200) {
                return response;
            }

            // Keep first non-404 response as useful backend error context.
            if (lastResponse == null || lastResponse.statusCode() == 404) {
                lastResponse = response;
            }
        }

        if (lastResponse != null) {
            return lastResponse;
        }
        throw new Exception("Unable to reach advising service. Attempts: " + String.join(", ", attempted));
    }

    private static HttpResponse<String> sendRequestWithFallback(String path, String method, String body) throws Exception {
        List<String> attempted = new ArrayList<>();
        HttpResponse<String> lastResponse = null;

        for (String base : getBaseUrlCandidates()) {
            HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(base + path));

            if ("GET".equals(method)) {
                builder.GET();
            } else if ("DELETE".equals(method)) {
                builder.DELETE();
            } else if ("POST".equals(method)) {
                builder.header("Content-Type", "application/json");
                builder.POST(body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
            } else if ("PUT".equals(method)) {
                builder.header("Content-Type", "application/json");
                builder.method("PUT", body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
            } else {
                throw new Exception("Unsupported HTTP method: " + method);
            }

            try {
                HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                attempted.add(base + " -> " + response.statusCode());

                if (response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 204) {
                    BASE_URL = base;
                    return response;
                }

                if (lastResponse == null || lastResponse.statusCode() == 404) {
                    lastResponse = response;
                }
            } catch (Exception ex) {
                attempted.add(base + " -> connection failed");
            }
        }

        if (lastResponse != null) {
            return lastResponse;
        }

        throw new Exception("Unable to reach backend for " + path + ". Attempts: " + String.join(", ", attempted));
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

