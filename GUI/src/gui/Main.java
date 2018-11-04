package gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;

public class Main {
    public void calcCourse(java.io.File file1,java.io.File file2) throws IOException {
        final int maxStudents = 30;
        TimetableOrganizer testTable = new TimetableOrganizer(file1.getPath(), file2.getPath());
        testTable.readCourseData(5, "Course Name", "Course Capacity");
        testTable.createTimetables();
        for (Timetable student : testTable.timetables) {
            System.out.println(student.name);
            for (Course course : student.givenCourses) {
                if (course != null) {
                    System.out.format("%15s %5d\n", course.name, course.period + 1);
                }
            }
            System.out.println();
        }
        for (int i = 0; i < testTable.timetables[0].numCourses; i++) {
            //System.out.println(Arrays.toString(testTable.timetables[0].givenCourses));
            System.out.println(testTable.timetables[0].givenCourses[i].name + " " + Arrays.toString(testTable.assignedCoursePeriods.get(testTable.timetables[0].givenCourses[i].name)));
        }
    }
    public void calcClub() throws IOException {
        ClubOrganizer testClub = new ClubOrganizer("Clubs");
        testClub.readClubData( "Full Name", "Student Number");
        testClub.matchClubs();
    }
}
