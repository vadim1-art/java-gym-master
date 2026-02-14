package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(0));
        Assertions.assertEquals(new TimeOfDay(13, 0), mondaySessions.get(0).getTimeOfDay());

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(new TimeOfDay(13, 0), mondaySessions.get(0).getTimeOfDay());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertEquals(new TimeOfDay(13, 0), thursdaySessions.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(20, 0), thursdaySessions.get(1).getTimeOfDay());

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        TrainingSession monday13Session = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertNotNull(monday13Session);
        Assertions.assertEquals(singleTrainingSession, monday13Session);
        Assertions.assertEquals(new TimeOfDay(13, 0), monday13Session.getTimeOfDay());

        TrainingSession monday14Session = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertNull(monday14Session);
    }

    @Test
    void testCountTrainerSessionsForWeekSorted() {
        Timetable timetable = new Timetable();

        Coach coachIvan = new Coach("Иванов", "Иван", "Иванович");
        Coach coachPetr = new Coach("Петров", "Петр", "Петрович");
        Coach coachMaria = new Coach("Сидорова", "Мария", "Ивановна");

        Group group = new Group("Акробатика", Age.ADULT, 60);

        // Добавляем тренировки: Иван - 4, Петр - 2, Мария - 1
        timetable.addNewTrainingSession(new TrainingSession(group, coachIvan,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachIvan,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachIvan,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachIvan,
                DayOfWeek.FRIDAY, new TimeOfDay(10, 0))); // Иван: 4

        timetable.addNewTrainingSession(new TrainingSession(group, coachPetr,
                DayOfWeek.TUESDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachPetr,
                DayOfWeek.THURSDAY, new TimeOfDay(15, 0))); // Петр: 2

        timetable.addNewTrainingSession(new TrainingSession(group, coachMaria,
                DayOfWeek.SATURDAY, new TimeOfDay(11, 0))); // Мария: 1

        // Получаем отсортированный список
        List<Map.Entry<String, Integer>> sorted = timetable.countTrainerSessionsForWeekSorted();

        // Проверяем размер
        Assertions.assertEquals(3, sorted.size());

        // Проверяем порядок сортировки (по убыванию)
        Assertions.assertEquals("Иванов Иван Иванович", sorted.get(0).getKey());
        Assertions.assertEquals(4, sorted.get(0).getValue());

        Assertions.assertEquals("Петров Петр Петрович", sorted.get(1).getKey());
        Assertions.assertEquals(2, sorted.get(1).getValue());

        Assertions.assertEquals("Сидорова Мария Ивановна", sorted.get(2).getKey());
        Assertions.assertEquals(1, sorted.get(2).getValue());
    }
}