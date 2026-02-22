package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        ru.yandex.practicum.gym.Timetable timetable = new ru.yandex.practicum.gym.Timetable();

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
        ru.yandex.practicum.gym.Timetable timetable = new ru.yandex.practicum.gym.Timetable();

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
        ru.yandex.practicum.gym.Timetable timetable = new ru.yandex.practicum.gym.Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Получаем список тренировок на 13:00
        List<TrainingSession> monday13Sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        Assertions.assertNotNull(monday13Sessions);
        Assertions.assertEquals(1, monday13Sessions.size());
        Assertions.assertEquals(singleTrainingSession, monday13Sessions.get(0));
        Assertions.assertEquals(new TimeOfDay(13, 0), monday13Sessions.get(0).getTimeOfDay());

        // Получаем список тренировок на 14:00 (должен быть пустым)
        List<TrainingSession> monday14Sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        Assertions.assertNotNull(monday14Sessions);
        Assertions.assertTrue(monday14Sessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime_MultipleSessionsAtSameTime() {
        ru.yandex.practicum.gym.Timetable timetable = new ru.yandex.practicum.gym.Timetable();

        // Создаем двух разных тренеров
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");

        // Создаем две разные группы
        Group groupYoga = new Group("Йога", Age.ADULT, 60);
        Group groupPilates = new Group("Пилатес", Age.ADULT, 60);

        // Обе тренировки в понедельник в 10:00
        TrainingSession yogaSession = new TrainingSession(groupYoga, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession pilatesSession = new TrainingSession(groupPilates, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(yogaSession);
        timetable.addNewTrainingSession(pilatesSession);

        // Получаем список тренировок на понедельник 10:00
        List<TrainingSession> monday10Sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        // Проверяем, что вернулось две тренировки
        Assertions.assertEquals(2, monday10Sessions.size());

        // Проверяем, что обе тренировки присутствуют (порядок может быть любым)
        Assertions.assertTrue(monday10Sessions.contains(yogaSession));
        Assertions.assertTrue(monday10Sessions.contains(pilatesSession));

        // Проверяем, что это именно те тренировки, которые мы добавили
        for (TrainingSession session : monday10Sessions) {
            Assertions.assertEquals(DayOfWeek.MONDAY, session.getDayOfWeek());
            Assertions.assertEquals(new TimeOfDay(10, 0), session.getTimeOfDay());
        }
    }

    @Test
    void testGetTrainingSessionsForDayAndTime_NoSessionsForDay() {
        ru.yandex.practicum.gym.Timetable timetable = new ru.yandex.practicum.gym.Timetable();

        // Добавляем тренировку только на понедельник
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession mondaySession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(mondaySession);

        // Запрашиваем тренировку на вторник - день, в котором вообще нет тренировок
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0));

        // Должен вернуться пустой список, а не null
        Assertions.assertNotNull(tuesdaySessions);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime_NoSessionsAtThatTime() {
        ru.yandex.practicum.gym.Timetable timetable = new ru.yandex.practicum.gym.Timetable();

        // Добавляем тренировку на понедельник 13:00
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession mondaySession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(mondaySession);

        // Запрашиваем тренировку на понедельник, но на другое время (14:00)
        List<TrainingSession> monday14Sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        // Должен вернуться пустой список, а не null
        Assertions.assertNotNull(monday14Sessions);
        Assertions.assertTrue(monday14Sessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime_EmptyTimetable() {
        ru.yandex.practicum.gym.Timetable timetable = new ru.yandex.practicum.gym.Timetable();

        // Запрашиваем тренировку в полностью пустом расписании
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        // Должен вернуться пустой список, а не null
        Assertions.assertNotNull(mondaySessions);
        Assertions.assertTrue(mondaySessions.isEmpty());
    }
}