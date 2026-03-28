package com.myadvice.service;

import com.myadvice.model.*;
import com.myadvice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    // Add a new course to the database
    public Course addCourse(Course course){
        // Check if course already exists
        if(courseRepository.findAll().stream().anyMatch(c -> c.getCourseCode().equals(course.getCourseCode()))){
            throw new RuntimeException("Course already exists");
        }
        // Save course if it doesn't already exist
        return courseRepository.save(course);
    }

    // Edit an existing course
    public Course editCourse(Long id, Course updatedCourse){
        // Check if course exists
        Course existingCourse = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        // Update course details
        existingCourse.setCourseCode(updatedCourse.getCourseCode());
        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setCredits(updatedCourse.getCredits());
        existingCourse.setDescription(updatedCourse.getDescription());
        // Save updated course to the database
        return courseRepository.save(existingCourse);
    }

    // Delete an existing course
    public void deleteCourse(Long id){
        // Check if course exists, or throw an exception if not
        courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        // Delete course from the database
        courseRepository.deleteById(id);
    }

    public Course addPrerequisite(Long courseId, Long prerequisiteId){
        // Find the course and prerequisite by ID
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Course prerequisite = courseRepository.findById(prerequisiteId).orElseThrow(() -> new RuntimeException("Prerequisite requested is not found"));

        // Prevent adding a prerequisite to itself
        if(courseId.equals(prerequisiteId)){
            throw new RuntimeException("Prerequisite cannot be the same as the course");
        }
        // Prevent adding a prerequisite that already exists
        if(course.getPrerequisites().contains(prerequisite)){
            throw new RuntimeException("Prerequisite already exists");
        }
        // Prevent circular prerequisites (direct or indirect)
        if (wouldCreatePrerequisiteCycle(course, prerequisite)) {
            throw new RuntimeException("Cannot add prerequisite because it creates a circular dependency");
        }
        // Add the prerequisite to the course's prerequisites list
        course.getPrerequisites().add(prerequisite);
        // Save the updated course back to the database
        return courseRepository.save(course);

    }

    public Course removePrerequisite(Long courseId, Long prerequisiteId){
        // Find the course and prerequisite by ID
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Course prerequisite = courseRepository.findById(prerequisiteId).orElseThrow(() -> new RuntimeException("Prerequisite requested is not found"));
        // Message to send if prerequisite does not exist
        if(!course.getPrerequisites().contains(prerequisite)){
            throw new RuntimeException("Prerequisite does not exist");
        }
        // Remove the prerequisite from the course's prerequisite list
        course.getPrerequisites().remove(prerequisite);
        // Save the updated course back to the database
        return courseRepository.save(course);
    }

    public List<Course> viewPrerequisites(Long courseId){
        // Find the course by ID
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        // Return the list of prerequisites for the course
        return course.getPrerequisites();
    }

    private boolean wouldCreatePrerequisiteCycle(Course course, Course prerequisiteCandidate) {
        return containsCourseInPrerequisiteChain(prerequisiteCandidate, course.getId(), new java.util.HashSet<>());
    }

    private boolean containsCourseInPrerequisiteChain(Course current, Long targetCourseId, java.util.Set<Long> visited) {
        if (current == null || current.getId() == null || targetCourseId == null) {
            return false;
        }
        if (current.getId().equals(targetCourseId)) {
            return true;
        }
        if (!visited.add(current.getId())) {
            return false;
        }
        List<Course> next = current.getPrerequisites();
        if (next == null || next.isEmpty()) {
            return false;
        }
        for (Course child : next) {
            if (containsCourseInPrerequisiteChain(child, targetCourseId, visited)) {
                return true;
            }
        }
        return false;
    }

    public Section addSection(Course course, Faculty faculty, String sectionNumber, Integer capacity, Integer enrolledCount,
                              String instructorName, String dayOfWeek, String labDayOfWeek, String labTime, String room){
        if (course == null || course.getId() == null) {
            throw new RuntimeException("Course is required");
        }
        if (faculty == null || faculty.getId() == null) {
            throw new RuntimeException("Faculty is required");
        }

        Course managedCourse = courseRepository.findById(course.getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Faculty managedFaculty = facultyRepository.findById(faculty.getId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Section newSectionToAdd = new Section(managedCourse, managedFaculty, sectionNumber, capacity, enrolledCount, instructorName, dayOfWeek);
        newSectionToAdd.setLabDayOfWeek(labDayOfWeek);
        newSectionToAdd.setLabTime(labTime);
        newSectionToAdd.setRoom(room);
        sectionRepository.save(newSectionToAdd);
        return newSectionToAdd; // return newly added section
    }

    public Section removeSection(Long id){
        Section section = sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        sectionRepository.delete(section);
        return section; // return deleted section
    }

    public Section editSection(Long id, Section updatedSection){
        Section sectionToEdit = sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));

        if (updatedSection.getCourse() == null || updatedSection.getCourse().getId() == null) {
            throw new RuntimeException("Course is required");
        }
        if (updatedSection.getFaculty() == null || updatedSection.getFaculty().getId() == null) {
            throw new RuntimeException("Faculty is required");
        }

        Course managedCourse = courseRepository.findById(updatedSection.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Faculty managedFaculty = facultyRepository.findById(updatedSection.getFaculty().getId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        sectionToEdit.setCourse(managedCourse);
        sectionToEdit.setFaculty(managedFaculty);
        sectionToEdit.setSectionNumber(updatedSection.getSectionNumber());
        sectionToEdit.setCapacity(updatedSection.getCapacity());
        sectionToEdit.setEnrolledCount(updatedSection.getEnrolledCount());
        sectionToEdit.setInstructorName(updatedSection.getInstructorName());
        sectionToEdit.setDayOfWeek(updatedSection.getDayOfWeek());
        sectionToEdit.setLabDayOfWeek(updatedSection.getLabDayOfWeek());
        sectionToEdit.setLabTime(updatedSection.getLabTime());
        sectionToEdit.setRoom(updatedSection.getRoom());
        return sectionRepository.save(sectionToEdit);
    }

    public Schedule addSchedule(Course course, String dayOfWeek, LocalTime startTime, java.time.LocalTime endTime, String roomNumber, String term){
        Schedule schedule = new Schedule(dayOfWeek, startTime, endTime, roomNumber,  term);
        schedule.setCourse(course);
        scheduleRepository.save(schedule);
        return schedule; // return newly added schedule
    }

    public Schedule removeSchedule(Long id){
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        scheduleRepository.delete(schedule);
        return schedule;
    }
    public Schedule editSchedule(Course course, Long id, Schedule updatedSchedule){
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
       schedule.setCourse(course);
        schedule.setDayOfWeek(updatedSchedule.getDayOfWeek());
        schedule.setStartTime(updatedSchedule.getStartTime());
        schedule.setEndTime(updatedSchedule.getEndTime());
        schedule.setRoomNumber(updatedSchedule.getRoomNumber());
        schedule.setTerm(updatedSchedule.getTerm());
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> viewScheduleByCourseId(String courseCode){
        return scheduleRepository.findByCourseCourseCode(courseCode);
    }

    // Transcript management methods
    public Transcript addTranscript(Student student, Course course, Double grade, String term){
        // Input validation
        if (student == null || student.getId() == null) {
            throw new RuntimeException("Student is required");
        }
        if (course == null || course.getId() == null) {
            throw new RuntimeException("Course is required");
        }
        if (grade == null || term == null || term.isBlank()) {
            throw new RuntimeException("Grade and term are required");
        }

        // Find the student and course by ID
        Student managedStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course managedCourse = courseRepository.findById(course.getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Create and save the new transcript
        Transcript transcript = new Transcript(managedStudent, managedCourse, grade, term);
        return transcriptRepository.save(transcript);
    }

    public void removeTranscript(Long id){
        Transcript transcript = transcriptRepository.findById(id).orElseThrow(() -> new RuntimeException("Transcript not found"));
        transcriptRepository.delete(transcript);
    }

    public Transcript editTranscript(Long id, Transcript updatedTranscript){
       // Find the transcript by ID
        Transcript transcript = transcriptRepository.findById(id).orElseThrow(() -> new RuntimeException("Transcript not found"));

        if (updatedTranscript.getStudent() == null || updatedTranscript.getStudent().getId() == null) {
            throw new RuntimeException("Student is required");
        }
        if (updatedTranscript.getCourse() == null || updatedTranscript.getCourse().getId() == null) {
            throw new RuntimeException("Course is required");
        }

        // Find the student and course by ID
        Student managedStudent = studentRepository.findById(updatedTranscript.getStudent().getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course managedCourse = courseRepository.findById(updatedTranscript.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Update the transcript details
        transcript.setStudent(managedStudent);
        transcript.setCourse(managedCourse);
        transcript.setGrade(updatedTranscript.getGrade());
        transcript.setTerm(updatedTranscript.getTerm());
        return transcriptRepository.save(transcript);
    }

    public List<Transcript> viewTranscript(Long studentId){
        return transcriptRepository.findByStudentId(studentId);
    }

    public List<Transcript> viewAllTranscripts(){
        return transcriptRepository.findAll();
    }

    public Student addStudent(String firstName, String lastName, String email, String password){
        Student student = new Student(firstName, lastName, email, password);
        studentRepository.save(student);
        return student;
    }

    public Student addStudent(Student student){
        if (student == null) {
            throw new RuntimeException("Student is required");
        }
        if (student.getFirstName() == null || student.getFirstName().isBlank()
                || student.getLastName() == null || student.getLastName().isBlank()
                || student.getEmail() == null || student.getEmail().isBlank()
                || student.getStudentNumber() == null || student.getStudentNumber().isBlank()
                || student.getFacultyName() == null || student.getFacultyName().isBlank()
                || student.getProgramName() == null || student.getProgramName().isBlank()) {
            throw new RuntimeException("Student first name, last name, email, student number, faculty, and program are required");
        }
        return studentRepository.save(student);
    }

    public void removeStudent(Long id){
        Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.delete(student);
    }

    public Student editStudent(Long id, Student updatedStudent){
        Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        student.setFirstName(updatedStudent.getFirstName());
        student.setLastName(updatedStudent.getLastName());
        student.setEmail(updatedStudent.getEmail());
        student.setStudentNumber(updatedStudent.getStudentNumber());
        student.setFacultyName(updatedStudent.getFacultyName());
        student.setProgramName(updatedStudent.getProgramName());
        return studentRepository.save(student);
    }

    public Faculty addFaculty(Faculty faculty){
        if (faculty == null) {
            throw new RuntimeException("Faculty is required");
        }
        if (faculty.getFirstName() == null || faculty.getFirstName().isBlank()
                || faculty.getLastName() == null || faculty.getLastName().isBlank()
                || faculty.getEmail() == null || faculty.getEmail().isBlank()
                || faculty.getDepartment() == null || faculty.getDepartment().isBlank()) {
            throw new RuntimeException("Faculty first name, last name, email, and department are required");
        }
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(Long id){
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new RuntimeException("Faculty not found"));
        facultyRepository.delete(faculty);
    }

    public Faculty editFaculty(Long id, Faculty updatedFaculty){
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new RuntimeException("Faculty not found"));
        faculty.setFirstName(updatedFaculty.getFirstName());
        faculty.setLastName(updatedFaculty.getLastName());
        faculty.setEmail(updatedFaculty.getEmail());
        faculty.setDepartment(updatedFaculty.getDepartment());
        return facultyRepository.save(faculty);
    }
    public List<Faculty> viewFaculty(){
        return facultyRepository.findAll();
    }

     public List<Student> viewStudents(){
        return studentRepository.findAll();
    }

     public List<Course> viewCourses(){
        return courseRepository.findAll();
    }

     public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

     public List<Section> viewSections(){
        return sectionRepository.findAll();
    }

     public List<Schedule> viewSchedules(){
        return scheduleRepository.findAll();
    }
}
