import java.util.HashMap;

public class Timetable {
    int numCourses;
    String name;
    HashMap<String, Integer> courseSelection;
    Course[] givenCourses;
    Timetable(int numCourses, String name) {
        this.numCourses = numCourses;
        this.name = name;
        this.courseSelection = new HashMap<>();
        this.givenCourses = new Course[numCourses];
    }
}
