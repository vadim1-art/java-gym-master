package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private Map<DayOfWeek, TreeMap<TimeOfDay, TrainingSession>> timetable = new HashMap<>();


    public void addNewTrainingSession(TrainingSession trainingSession) {
        TreeMap<TimeOfDay, TrainingSession> daySessions =
                timetable.get(trainingSession.getDayOfWeek());

        if (daySessions == null) {
            daySessions = new TreeMap<>();
            timetable.put(trainingSession.getDayOfWeek(), daySessions);
        }

        daySessions.put(trainingSession.getTimeOfDay(), trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, TrainingSession> daySessions = timetable.get(dayOfWeek);

        if (daySessions == null) {
            return Collections.emptyList();
        }

        NavigableSet<TimeOfDay> sortedKeys = daySessions.navigableKeySet();
        List<TrainingSession> result = new ArrayList<>();

        for (TimeOfDay key : sortedKeys) {
            result.add(daySessions.get(key));
        }

        return result;
    }

    public TrainingSession getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, TrainingSession> daySessions = timetable.get(dayOfWeek);

        if (daySessions == null) {
            return null;
        }

        return daySessions.get(timeOfDay);
    }

    public List<Map.Entry<String, Integer>> countTrainerSessionsForWeekSorted() {
        Map<String, Integer> trainerCount = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            TreeMap<TimeOfDay, TrainingSession> daySessions = timetable.get(day);
            if (daySessions != null) {
                for (TrainingSession session : daySessions.values()) {
                    String coachName = session.getCoach().getName();
                    trainerCount.put(coachName,
                            trainerCount.getOrDefault(coachName, 0) + 1);
                }
            }
        }

        List<Map.Entry<String, Integer>> sortedList =
                new ArrayList<>(trainerCount.entrySet());

        Collections.sort(sortedList,
                (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        return sortedList;
    }
}
