package gui;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetableOrganizer {
    int numTimetables;
    String coursesInfoDirName;
    String courseSelectionsDirName;
    File coursesInfo;
    File courseSelections;
    Timetable[] timetables;
    HashMap<String, Integer> courseAvailability;
    HashMap<String, int[]> assignedCoursePeriods;

    TimetableOrganizer(String coursesInfoFileName, String courseSelectionsFileName) {
        this.coursesInfoDirName = coursesInfoFileName;
        this.courseSelectionsDirName = courseSelectionsFileName;
        this.coursesInfo = new File(this.coursesInfoDirName);
        this.courseSelections = new File(this.courseSelectionsDirName);
        this.courseAvailability = new HashMap<>();
        this.assignedCoursePeriods = new HashMap<>();
    }

    public void readCourseData(int timetablePeriodCount, String courseNameTitle, String courseCapacityTitle) throws IOException {
        courseNameTitle = courseNameTitle.toLowerCase();
        courseCapacityTitle = courseCapacityTitle.toLowerCase();
        CSVReader reader = new CSVReader(coursesInfo.getPath());
        CSVParser parser = reader.readRecords();
        List<CSVRecord> recordList = parser.getRecords();
        int courseCol = -1;
        int capacityCol = -1;
        for (int i = 0; i < recordList.size(); i++) {
            CSVRecord record = recordList.get(i);
            for (int j = 0; j < record.size(); j++) {
                String entry = record.get(j).toLowerCase();
                if (entry.contains(courseNameTitle)) {
                    courseCol = j;
                } else if (entry.contains(courseCapacityTitle)) {
                    capacityCol = j;
                }
            }
        }
        for (int i = 0; i < recordList.size(); i++) {
            CSVRecord record = recordList.get(i);
            String courseName = record.get(courseCol);
            String spotsCount = record.get(capacityCol);
            if (spotsCount.chars().allMatch(Character::isDigit)) {
                courseAvailability.put(courseName, Integer.parseInt(spotsCount));
            }
        }
        for (Map.Entry<String, Integer> entry : courseAvailability.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        int curCourseSelection = 0;
        reader = new CSVReader(courseSelections.getPath());
        parser = reader.readRecords();
        recordList = parser.getRecords();
        numTimetables = recordList.size() - 1;
        timetables = new Timetable[numTimetables];
        for (int i = 1; i < recordList.size(); i++) {
            CSVRecord record = recordList.get(i);
            Timetable person = new Timetable(timetablePeriodCount, record.get(0));
            for (int j = 0; j < person.numCourses; j++) {
                String courseName = record.get(j + 1);
                String priority = record.get(j + 6);
                if (priority.chars().allMatch(Character::isDigit)) {
                    person.courseSelection.put(courseName, Integer.parseInt(priority));
                }
            }
            for (Map.Entry<String, Integer> entry : person.courseSelection.entrySet()) {
//                    System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            timetables[curCourseSelection] = person;
            curCourseSelection++;
        }
    }

    public void createTimetables() {
        for (int i = 0; i < timetables.length; i++) {
            Timetable student = timetables[i];
            for (Map.Entry<String, Integer> entry : student.courseSelection.entrySet()) {
                String courseName = entry.getKey();
//                System.out.println("Course name: " + courseName + " " + entry.getValue());
                if (courseAvailability.containsKey(courseName)) {
                    int openSpots = courseAvailability.get(courseName);
                    if (openSpots > 0) {
                        if (assignedCoursePeriods.containsKey(courseName)) {
                            int minPeriod = Integer.MAX_VALUE;
                            int assignPeriod = -1;
                            int[] coursePeriods = assignedCoursePeriods.get(courseName);
                            for (int j = 0; j < coursePeriods.length; j++) {
                                int periodCount = coursePeriods[j];
                                if (student.givenCourses[j] == null && periodCount < minPeriod) {
//                                    System.out.println(minPeriod + " " + periodCount + " " + j);
                                    minPeriod = periodCount;
                                    assignPeriod = j;
                                }
                            }
                            coursePeriods[assignPeriod]++;
                            Course given = new Course(courseName, assignPeriod);
                            student.givenCourses[assignPeriod] = given;
//                            System.out.println("CN: " + student.name + " " + courseName + " " + Arrays.toString(coursePeriods));
//                            timetables[i] = student;
//                            courseAvailability.put(courseName, openSpots);
                        } else {
                            int assignPeriod = 0;
                            while (student.givenCourses[assignPeriod] != null) {
                                assignPeriod++;
                                if (assignPeriod >= student.numCourses) {
                                    break;
                                }
                            }
//                            System.out.println(": " + courseName + " " + assignPeriod);
                            if (assignPeriod < student.numCourses) {
                                int[] coursePeriods = new int[student.numCourses];
                                coursePeriods[assignPeriod]++;
                                assignedCoursePeriods.put(courseName, coursePeriods);
                                Course given = new Course(courseName, assignPeriod);
                                student.givenCourses[assignPeriod] = given;
                            }
                        }
                    } else {
                        System.out.println("No more space left in classes for " + courseName);
                    }
                }
            }
        }
    }
}
