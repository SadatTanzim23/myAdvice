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

    public Section addSection(Course course, Faculty faculty, String sectionNumber, Integer capacity, Integer enrolledCount, String instructorName, String dayOfWeek){
        Section newSectionToAdd = new Section(course, faculty, sectionNumber, capacity, enrolledCount, instructorName, dayOfWeek);
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
        sectionToEdit.setCourse(updatedSection.getCourse());
        sectionToEdit.setFaculty(updatedSection.getFaculty());
        sectionToEdit.setSectionNumber(updatedSection.getSectionNumber());
        sectionToEdit.setCapacity(updatedSection.getCapacity());
        sectionToEdit.setEnrolledCount(updatedSection.getEnrolledCount());
        sectionToEdit.setInstructorName(updatedSection.getInstructorName());
        sectionToEdit.setDayOfWeek(updatedSection.getDayOfWeek());
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

    public List<Schedule> viewScheduleByCourseId(Long courseCode){
        return scheduleRepository.findByCourseCode(courseCode);
    }

    public Transcript addTranscript(Student student, Course course, Double grade, String term){
        Transcript transcript = new Transcript(student, course, grade, term);
        transcriptRepository.save(transcript);
        return transcript;
    }

    public void removeTranscript(Long id){
        Transcript transcript = transcriptRepository.findById(id).orElseThrow(() -> new RuntimeException("Transcript not found"));
        transcriptRepository.delete(transcript);
    }

    public Transcript editTranscript(Long id, Transcript updatedTranscript){
        Transcript transcript = transcriptRepository.findById(id).orElseThrow(() -> new RuntimeException("Transcript not found"));
        transcript.setStudent(updatedTranscript.getStudent());
        transcript.setCourse(updatedTranscript.getCourse());
        transcript.setGrade(updatedTranscript.getGrade());
        transcript.setTerm(updatedTranscript.getTerm());
        return transcriptRepository.save(transcript);
    }

    public List<Transcript> viewTranscript(Long studentId){
        return transcriptRepository.findByStudentId(studentId);
    }
}
