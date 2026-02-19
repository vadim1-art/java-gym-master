package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions =
                timetable.get(trainingSession.getDayOfWeek());

        if (daySessions == null) {
            daySessions = new TreeMap<>();
            timetable.put(trainingSession.getDayOfWeek(), daySessions);
        }

        TimeOfDay time = trainingSession.getTimeOfDay();
        List<TrainingSession> sessionsAtTime = daySessions.get(time);

        if (sessionsAtTime == null) {
            sessionsAtTime = new ArrayList<>();
            daySessions.put(time, sessionsAtTime);
        }

        sessionsAtTime.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);

        if (daySessions == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> allSessionsForDay = new ArrayList<>();
        for (List<TrainingSession> sessionsAtTime : daySessions.values()) {
            allSessionsForDay.addAll(sessionsAtTime);
        }

        return allSessionsForDay;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);

        if (daySessions == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessionsAtTime = daySessions.get(timeOfDay);
        if (sessionsAtTime == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(sessionsAtTime);
    }

    public List<Map.Entry<Coach, Integer>> countTrainerSessionsForWeekSorted() {
        Map<Coach, Integer> trainerCount = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(day);
            if (daySessions != null) {
                for (List<TrainingSession> sessionsAtTime : daySessions.values()) {
                    for (TrainingSession session : sessionsAtTime) {
                        Coach coach = session.getCoach();
                        trainerCount.put(coach, trainerCount.getOrDefault(coach, 0) + 1);
                    }
                }
            }
        }

        List<Map.Entry<Coach, Integer>> sortedList =
                new ArrayList<>(trainerCount.entrySet());

        Collections.sort(sortedList,
                (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        return sortedList;
    }
}