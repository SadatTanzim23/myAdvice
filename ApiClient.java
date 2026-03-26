import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myadvice.model.Section;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    // Admin API methods

    // Course API methods
    public static List<Course> getAllCourses() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Error handling for failed API calls
        if (response.statusCode() != 200) {
            throw new Exception("Failed to load courses: " + response.body());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Course>>(){}.getType());
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

    public static Course addCoursePrerequisite(Long courseId, Long prerequisiteId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses/" + courseId + "/prerequisites/add/" + prerequisiteId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to add prerequisite: " + response.body());
        }
        return gson.fromJson(response.body(), Course.class);

    }

    public static Course removeCoursePrerequisite(Long courseId, Long prerequisiteId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/admin/courses/" + courseId + "/prerequisites/remove/" + prerequisiteId))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to remove prerequisite: " + response.body());
        }
        return gson.fromJson(response.body(), Course.class);
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
}

