import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.*;

/**
 * Юнит тесты функциорования методов из класса {@link RouteCalculator}
 * Тестовая схема метро
 * <img src='doc-files/metro.png' alt=''/>
 */
public class RouteCalculatorTest extends TestCase {

    RouteCalculator routeCalculator;
    StationIndex stationIndex;

    /**
     * инищиализация входных данных для тестирования
     *
     *   остров --- полюс/север -             начало
     *                          |             |
     * ст1  -  ст2/запад  -  ст3/восток/централ  -  ст4
     *                 |        |            |
     *                 - юг ----         конец
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        stationIndex = new StationIndex();

        Line line1 = new Line(1, "Главная линия");
        Line line2 = new Line(2, "Круговое");
        Line line3 = new Line(3, "Вертикальная");
        Line line4 = new Line(4, "Полюс-Север");

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);
        stationIndex.addLine(line4);

        addStations(
                stationIndex,
                line1,
                new Station("Ст1", line1),
                new Station("Ст2", line1),
                new Station("Ст3", line1),
                new Station("Ст4", line1)
        );

        addStations(
                stationIndex,
                line2,
                new Station("Север", line2),
                new Station("Восток", line2),
                new Station("Юг", line2),
                new Station("Запад", line2)
        );

        addStations(
                stationIndex,
                line3,
                new Station("Начало", line3),
                new Station("Централ", line3),
                new Station("Конец", line3)
        );

        addStations(
                stationIndex,
                line4,
                new Station("Остров", line4),
                new Station("Полюс", line4)
        );

        List<Station> connectionOnCentral = Arrays.asList(
                stationIndex.getStation("Ст3"),
                stationIndex.getStation("Восток"),
                stationIndex.getStation("Централ")
        );

        List<Station> connectionOnMain = Arrays.asList(
                stationIndex.getStation("Ст2"),
                stationIndex.getStation("Запад")
        );

        List<Station> connectionPoleNord = Arrays.asList(
                stationIndex.getStation("Полюс"),
                stationIndex.getStation("Север")
        );

        stationIndex.addConnection(connectionOnCentral);
        stationIndex.addConnection(connectionOnMain);
        stationIndex.addConnection(connectionPoleNord);

    }

    /**
     * Проверка работы метода {@link RouteCalculator#calculateDuration(List)
     * без пересадок, на одной ветке
     */
    public void testCalculateDuration_onSameLine() {
        double actual = RouteCalculator.calculateDuration(buildRoute("Ст1 -> Ст2 -> Ст3 -> Ст4"));
        //ст1 - 2.5 - ст2 - 2.5 - ст3 - 2.5 - ст4
        double expected = 7.5;

        assertEquals(expected, actual);
    }

    /**
     * Проверка работы метода {@link RouteCalculator#calculateDuration(List)
     * с одним переходом
     */
    public void testCalculateDuration_withOneJump() {
        double actual = RouteCalculator.calculateDuration(buildRoute("Начало -> Централ -> Восток -> Юг"));
        //начало - 2.5 - централ - 3.5 - восток - 2.5 - юг
        double expected = 8.5;

        assertEquals(expected, actual);
    }

    /**
     * Проверка работы метода {@link RouteCalculator#getShortestRoute(Station, Station)}
     * без пересадок, на одной ветке
     */
    public void testGetShortestRoute_onSameLine() {

        routeCalculator = new RouteCalculator(stationIndex);
        List<Station> actual = routeCalculator.getShortestRoute(
                stationIndex.getStation("Север"),
                stationIndex.getStation("Восток")
        );
        List<Station> expected = buildRoute("Север -> Восток");

        assertEquals(expected, actual);
    }

    /**
     * Проверка работы метода {@link RouteCalculator#getShortestRoute(Station, Station)}
     * с одной пересадкой
     */
    public void testGetShortestRoute_withOneJump() {

        routeCalculator = new RouteCalculator(stationIndex);
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Начало"), stationIndex.getStation("Юг"));
        List<Station> expected = buildRoute("Начало -> Централ -> Восток -> Юг");

        assertEquals(expected, actual);
    }

    /**
     * Проверка работы метода {@link RouteCalculator#getShortestRoute(Station, Station)}
     * с двумя пересадками
     */
    public void testGetShortestRoute_withTwoJump() {

        routeCalculator = new RouteCalculator(stationIndex);
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Начало"), stationIndex.getStation("Остров"));
        List<Station> expected = buildRoute("Начало -> Централ -> Восток -> Север -> Полюс -> Остров");

        assertEquals(expected, actual);
    }

    /**
     * Метод заносит в stationIndex и line указанные станции
     *
     * @param stationIndex
     * @param line
     * @param stations
     */
    private void addStations(StationIndex stationIndex, Line line, Station... stations) {
        if (stations.length > 0) {
            Arrays.stream(stations).forEach(station -> {
                stationIndex.addStation(station);
                line.addStation(station);
            });
        } else {
            System.out.println("Станции отсутствуют");
        }
    }

    /**
     * Метод строит маршрут по входящей строке, которая разбивается на маршруты
     *
     * @param input
     * @return
     */
    private List<Station> buildRoute(String input) {
        List<Station> stations = new ArrayList<>();

        Arrays.stream(input.split("->")).forEach(station -> {
            Station searchStation = stationIndex.getStation(station.trim());
            if (searchStation != null) {
                stations.add(searchStation);
            } else {
                System.out.printf("При создании маршрута '%s' возникла ошибка", input);
                stations.clear();
                return;
            }
        });

        return stations;
    }

}
