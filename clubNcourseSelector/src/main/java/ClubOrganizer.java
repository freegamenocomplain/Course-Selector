import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClubOrganizer {
    int numDays;
    int numClubs;
    String CSVDirName;
    File CSVDir;
    File[] CSVDirFiles;
    Club[] Clubs;
    HashMap<String, Integer> clubIndex;
    ArrayList<Club>[] daySchedule;


    ClubOrganizer(int numDays, String CSVDirName) {
        this.CSVDirName = CSVDirName;
        this.CSVDir = new File(CSVDirName);
        this.CSVDirFiles = this.CSVDir.listFiles();
        this.numClubs = this.CSVDirFiles.length;
        this.Clubs = new Club[numClubs];
        this.clubIndex = new HashMap<>();
        this.numDays = numDays;
        daySchedule = new ArrayList[numDays];
        for (int i = 0; i < numDays; i++) {
            daySchedule[i] = new ArrayList<>();
        }
    }

    public void readClubData(String studentNameTitle, String studentNumberTitle) throws IOException {
        studentNameTitle = studentNameTitle.toLowerCase();
        studentNumberTitle = studentNumberTitle.toLowerCase();
        int curClub = 0;
        for (File CSVFile : this.CSVDirFiles) {
            CSVReader reader = new CSVReader(CSVFile.getPath());
            CSVParser parser = reader.readRecords();
            List<CSVRecord> recordList = parser.getRecords();
            Clubs[curClub] = new Club(recordList.get(0).get(0));
            clubIndex.put(Clubs[curClub].name, curClub);
            curClub++;
        }

        curClub = 0;
        HashMap<String, ArrayList<Club>> studentClubs = new HashMap<>();
        for (File CSVFile : this.CSVDirFiles) {
            CSVReader reader = new CSVReader(CSVFile.getPath());
            CSVParser parser = reader.readRecords();
            List<CSVRecord> recordList = parser.getRecords();
            int nameCol = -1;
            int idCol = -1;
            for (int j = 0; j < recordList.size(); j++) {
                CSVRecord record = recordList.get(j);
                for (int k = 0; k < record.size(); k++) {
                    String entry = record.get(k).toLowerCase();
                    if (entry.contains(studentNameTitle)) {
                        nameCol = k;
                    } else if (entry.contains(studentNumberTitle)) {
                        idCol = k;
                    }
                }
            }
            ArrayList<String> studentNums = new ArrayList<>();
            for (int i = 0; i < recordList.size(); i++) {
                CSVRecord record = recordList.get(i);
                String studentNum = record.get(idCol);
                if (!studentNum.isEmpty() && studentNum.chars().allMatch(Character::isDigit)) {
                    if (studentNum.length() > 9) {
                        studentNum = studentNum.substring(0, 9);
                    }
                    studentNums.add(studentNum);
                }
            }
            calculateMatchValues(studentNums, studentClubs, Clubs[curClub]);
            curClub++;
        }
        for (Club c : Clubs) {
            for (Map.Entry<String, Double> entry : c.relations.entrySet()) {
                String otherClubName = entry.getKey();
                double otherClubOverlap = entry.getValue();
                if (otherClubOverlap < c.worstOverlap) {
                    c.worstOverlap = otherClubOverlap;
                }
            }
        }
    }

    public void calculateMatchValues(ArrayList<String> studentNums, HashMap<String, ArrayList<Club>> studentClubs, Club curClub) {
        for (String studentNum : studentNums) {
            if (studentClubs.containsKey(studentNum)) {
                ArrayList<Club> clubsGoingTo = studentClubs.get(studentNum);
                for (Club club : clubsGoingTo) {
                    int otherClubIndex = clubIndex.get(club.name);
                    Club otherClub = Clubs[otherClubIndex];
                    if (!curClub.name.equals(otherClub.name)) {
                        if (curClub.relations.containsKey(otherClub.name)) {
                            double otherClubRelation = curClub.relations.get(otherClub.name);
                            curClub.relations.put(otherClub.name, otherClubRelation - 1.0);
                        } else {
                            curClub.relations.put(otherClub.name, -1.0);
                        }
                        if (otherClub.relations.containsKey(curClub.name)) {
                            double curClubRelation = otherClub.relations.get(curClub.name);
                            otherClub.relations.put(curClub.name, curClubRelation - 1.0);
                        } else {
                            otherClub.relations.put(curClub.name, -1.0);
                        }
                    }
                }
                clubsGoingTo.add(curClub);
                studentClubs.put(studentNum, clubsGoingTo);
            } else {
                ArrayList<Club> clubsGoingTo = new ArrayList<>();
                clubsGoingTo.add(curClub);
                studentClubs.put(studentNum, clubsGoingTo);
            }
        }
    }

    public void matchClubs() {
        int satisfiedCount = 0;
        boolean[] scheduled = new boolean[numClubs];
        Arrays.sort(Clubs, (a, b) -> Double.compare(a.worstOverlap, b.worstOverlap));
        clubIndex.clear();
        for (int i = 0; i < numClubs; i++) {
            clubIndex.put(Clubs[i].name, i);
        }
        for (int i = 0; i < numClubs; i++) {
            Club club = Clubs[i];
            if (!club.scheduled) {
                int bestDayIndex = 0;
                double bestDayOverlap = Integer.MIN_VALUE;
                for (int j = 0; j < daySchedule.length; j++) {
                    if (daySchedule[j].size() == 0) {
                        bestDayIndex = j;
                        break;
                    } else if (!club.relations.isEmpty()) {
                        double goodness = 0;
                        for (int k = 0; k < daySchedule[j].size(); k++) {
                            Club onDay = daySchedule[j].get(k);
                            int onDayIndex = clubIndex.get(onDay.name);
                            if (club.relations.containsKey(onDay.name)) {
                                goodness += club.relations.get(onDay.name);
                            }
                        }
                        if (goodness > bestDayOverlap) {
                            bestDayOverlap = goodness;
                            bestDayIndex = j;
                        }
                    }
                }
                daySchedule[bestDayIndex].add(club);
                club.scheduled = true;
            }
        }
    }
    public void writeClubSchedule(String outputFileDirName, String outputFileName) throws IOException {
        final String[] DAY_HEADERS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String outputFilePath = outputFileDirName + "/" + outputFileName;
        FileWriter writer = new FileWriter(outputFilePath);
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(DAY_HEADERS));
        for (int i = 0; i < daySchedule.length; i++) {
            String[] clubRow = new String[numDays];
            for (int j = 0; j < numDays; j++) {
                if (i < daySchedule[j].size()) {
                    clubRow[j] = daySchedule[j].get(i).name;
                }
            }
            printer.printRecord((Object[])clubRow);
        }
        printer.println();
        printer.flush();
        printer.close();
    }
}
