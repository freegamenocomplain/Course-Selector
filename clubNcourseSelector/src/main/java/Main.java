import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ClubOrganizer testClub = new ClubOrganizer(5, "Clubs");
        testClub.readClubData("Full Name", "Student Number");
        testClub.matchClubs();
        testClub.writeClubSchedule("ClubSchedules", "Schedule.csv");
        TimetableOrganizer testTable = new TimetableOrganizer("CoursePlan.csv", "StudentCourseSelections.csv");
        testTable.readCourseData(5, "Course Name", "Course Capacity");
        testTable.createTimetables();
        testTable.writeStudentTimetables("CourseTimetables", "Timetables.csv");
    }
}
