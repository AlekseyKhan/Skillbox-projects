import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.*;

public class RouteCalculatorTest extends TestCase {

    RouteCalculator routeCalculator;
    static StationIndex stationIndex;
    List<Station> route1;
    List<Station> route2;
    List<Station> route3;

    @Override
    protected void setUp() throws Exception {

        stationIndex = new StationIndex();

        Line line1 = new Line(1, "Главная линия");
        Line line2 = new Line(2, "Круговое");
        Line line3 = new Line(3, "Вертикальная");

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);

        stationIndex.addStation(new Station("Ст1", line1));
        line1.addStation(stationIndex.getStation("Ст1"));
        stationIndex.addStation(new Station("Ст2", line1));
        line1.addStation(stationIndex.getStation("Ст2"));
        stationIndex.addStation(new Station("Ст3", line1));
        line1.addStation(stationIndex.getStation("Ст3"));
        stationIndex.addStation(new Station("Ст4", line1));
        line1.addStation(stationIndex.getStation("Ст4"));
        stationIndex.addStation(new Station("Север", line2));
        line1.addStation(stationIndex.getStation("Север"));
        stationIndex.addStation(new Station("Запад", line2));
        line1.addStation(stationIndex.getStation("Запад"));
        stationIndex.addStation(new Station("Юг", line2));
        line1.addStation(stationIndex.getStation("Юг"));
        stationIndex.addStation(new Station("Восток", line2));
        line1.addStation(stationIndex.getStation("Восток"));
        stationIndex.addStation(new Station("Начало", line3));
        line1.addStation(stationIndex.getStation("Начало"));
        stationIndex.addStation(new Station("Централ", line3));
        line1.addStation(stationIndex.getStation("Централ"));
        stationIndex.addStation(new Station("Конец", line3));
        line1.addStation(stationIndex.getStation("Конец"));

        List<Station> connectionOnCentral = new ArrayList<>(Arrays.asList(
                stationIndex.getStation("Ст3"),
                stationIndex.getStation("Восток"),
                stationIndex.getStation("Централ")
        ));

        List<Station> connectionOnMain = new ArrayList<>(Arrays.asList(
                stationIndex.getStation("Ст2"),
                stationIndex.getStation("Запад")
        ));

        stationIndex.addConnection(connectionOnCentral);
        stationIndex.addConnection(connectionOnMain);

        //прямой от ст1 до ст4 без пересадок
        route1 = new ArrayList<>();
        route1.add(stationIndex.getStation("Ст1"));
        route1.add(stationIndex.getStation("Ст2"));
        route1.add(stationIndex.getStation("Ст3"));
        route1.add(stationIndex.getStation("Ст4"));

        //с пересадкой
        route2 = new ArrayList<>();
        route2.add(stationIndex.getStation("Начало"));
        route2.add(stationIndex.getStation("Централ"));
        route2.add(stationIndex.getStation("Восток"));
        route2.add(stationIndex.getStation("Юг"));


    }

    public void testCalculateDuration_onSameLine() {
        double actual = RouteCalculator.calculateDuration(route1);
        //ст1 - 2.5 - ст2 - 2.5 - ст3 - 2.5 - ст4
        double expected = 7.5;

        assertEquals(expected, actual);
    }

    public void testCalculateDuration_withOneJump() {
        double actual = RouteCalculator.calculateDuration(route2);
        //начало - 2.5 - централ - 3.5 - восток - 2.5 - юг
        double expected = 8.5;

        assertEquals(expected, actual);
    }

    public void testGetShortestRoute_onSameLine() {

        routeCalculator = new RouteCalculator(stationIndex);
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Ст1"), stationIndex.getStation("Ст4"));
        List<Station> expected = route1;
//        List<Station> expected = new ArrayList<>(Arrays.asList(
//                stationIndex.getStation("Ст1"),
//                stationIndex.getStation("Ст2"),
//                stationIndex.getStation("Ст3"),
//                stationIndex.getStation("Ст4")
//        ));


        assertEquals(expected, actual);
    }

    public void testGetShortestRoute_withOneJump() {

        routeCalculator = new RouteCalculator(stationIndex);
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Ст1"), stationIndex.getStation("Ст4"));
        List<Station> expected = new ArrayList<>(Arrays.asList(
                stationIndex.getStation("Ст1"),
                stationIndex.getStation("Ст2"),
                stationIndex.getStation("Ст3"),
                stationIndex.getStation("Ст4")
        ));

        assertEquals(expected, actual);
    }

    @Override
    protected void tearDown() throws Exception {

    }
}
