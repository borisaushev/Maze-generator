package backend.academy.mazegame.labyrinth.navigation.impl;

import backend.academy.mazegame.labyrinth.navigation.PathFinder;
import backend.academy.mazegame.maze.Maze;
import backend.academy.mazegame.maze.Point;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static backend.academy.mazegame.parameters.MazeSymbols.WALL;

/**
 * Finds a way from one <b>Point</b> to another
 */
@SuppressFBWarnings({"PL_PARALLEL_LISTS"})
public class DFSMazePathFinder implements PathFinder {
    //change of values of x and y when we go: left, right, up, down
    final int[] dx = {-1, 1, 0, 0};
    final int[] dy = {0, 0, 1, -1};
    HashSet<Point> visitedPoints;
    HashMap<Point, Point> previousPoint;
    LinkedList<Point> que;

    /**
     * Finds and returns a path between 2 given points in a given maze using DFS algorithm
     *
     * @param startingPoint starting point
     * @param endingPoint   ending point, can equal to starting point
     * @param maze          maze
     * @return <b>Empty List</b> if no path was found, otherwise returns list of all points in any order
     */
    @Override
    public List<Point> findPath(Point startingPoint, Point endingPoint, Maze maze) {
        visitedPoints = new HashSet<>();
        previousPoint = new HashMap<>();
        que = new LinkedList<>();
        que.add(startingPoint);

        return dfsSearch(startingPoint, endingPoint, maze);
    }

    public List<Point> dfsSearch(Point currentPoint, Point endingPoint, Maze maze) {
        if (currentPoint.equals(endingPoint)) {
            return reconstructPath(endingPoint, previousPoint);
        }

        visitedPoints.add(currentPoint);
        int x = currentPoint.x();
        int y = currentPoint.y();
        for (int i = 0; i < dx.length; i++) {
            Point newPoint = new Point(x + dx[i], y + dy[i]);
            if (maze.pointIsInBounds(newPoint)
                && !visitedPoints.contains(newPoint)
                && maze.valueAt(newPoint) != WALL.value) {
                previousPoint.put(newPoint, currentPoint);
                List<Point> path = dfsSearch(newPoint, endingPoint, maze);
                if (!path.isEmpty()) {
                    return path;
                }
            }
        }
        return List.of();
    }

    /**
     * Reconstructs and returns a path of Point object,
     * based on an Ending point and a map of previous points to given ones
     *
     * @param endingPoint ending point, can equal to starting point
     * @return <b>Empty List</b> if no path was found, otherwise returns list of all points in any order
     */
    public List<Point> reconstructPath(Point endingPoint, Map<Point, Point> previousPoint) {
        List<Point> path = new LinkedList<>();

        Point currentPoint = endingPoint;
        while (currentPoint != null) {
            path.add(currentPoint);
            currentPoint = previousPoint.get(currentPoint);
        }
        return path;
    }
}
