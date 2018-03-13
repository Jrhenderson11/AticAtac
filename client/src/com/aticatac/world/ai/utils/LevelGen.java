package com.aticatac.world.ai.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

public class LevelGen {

    public static int[][] get(int width, int height) {
        int[][] map = new int[width][height];

        Random random = new Random();
        int elements = 5 + random.nextInt(6);


        for (int i = 0; i < elements; i++) {

            if (random.nextBoolean()) {

                int x = random.nextInt((int) (width * 0.6));
                int y = random.nextInt((int) (height * 0.6));
                int radius = width / 16 + random.nextInt(width / 8);

                map = circle(map, x, y, radius, true);

            } else {

                int x = random.nextInt(width / 2);
                int y = random.nextInt(height / 2);
                int w = width / 15 + random.nextInt(width / 5);
                int h = height / 15 + random.nextInt(height / 5);

                map = rectangle(map, x, y, w, h, true);
            }

        }

        map = quadMirror(map);
        map = boarder(map);

        // If the map isn't connected, try again
        boolean connected = connected(map);

        System.err.println("Generated map is connected : " + connected);

        if (connected) {
            return map;
        } else {
            return get(width, height);
        }

    }

    // Takes the top left slice of map and mirrors it 4 times
    private static int[][] quadMirror(int[][] map) {

        int[][] mirror = map;

        for (int r = 0; r < mirror.length; r++) {

            if (r < mirror.length / 2.0) {
                mirror[mirror.length - 1 - r] = ArrayUtils.clone(mirror[r]);
            }

            for (int c = 0; c < mirror[r].length; c++) {
                if (c < mirror[r].length / 2.0) {
                    mirror[r][mirror[r].length - 1 - c] = mirror[r][c];
                }
            }
        }

        return map;

    }

    private static int[][] circle(int[][] map, int x, int y, int radius, boolean filled) {

        for (int w = 0; w < map.length; w++) {
            for (int h = 0; h < map[0].length; h++) {

                if (filled) {
                    if ((h - x) * (h - x) + (w - y) * (w - y) <= radius * radius) {
                        map[w][h] = 1;
                    }
                } else {
                    if (h * h + w * w <= radius * radius) {
                        map[w][h] = 1;
                    }

                    int smallrad = radius - 2;

                    if (h * h + w * w <= smallrad * smallrad) {
                        map[w][h] = 0;
                    }

                }

            }

        }
        return map;
    }

    private static int[][] rectangle(int[][] map, int x, int y, int rw, int rh, boolean filled) {
        for (int w = 0; w < map.length; w++) {
            for (int h = 0; h < map[0].length; h++) {
                if (filled) {

                    if (w > x && w < x + rw && h > y && h < y + rh) {
                        map[w][h] = 1;
                    }

                } else {

                    if (w > x && w < x + rw && h > y && h < y + rh) {
                        map[w][h] = 1;
                    }
                }
            }
        }

        return map;

    }

    private static int[][] boarder(int[][] map) {
        for (int w = 0; w < map.length; w++) {
            map[w][0] = 1;
            map[w][map[0].length - 1] = 1;
        }
        for (int h = 0; h < map[0].length; h++) {
            map[0][h] = 1;
            map[map[0].length - 1][h] = 1;
        }
        return map;
    }

    private static boolean connected(int[][] map) {
        int startx = 0;
        int starty = 0;

        // Might have to copy individual bits here
        int[][] test = new int[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            test[i] = ArrayUtils.clone(map[i]);
        }

        for (int x = 0; x < test.length; x++) {
            for (int y = 0; y < test[0].length; y++) {
                if (test[x][y] == 0) {
                    startx = x;
                    starty = y;
                    break;
                }
            }
            if (test[startx][starty] == 0) {
                break;
            }
        }

        // Found starting test x, y
        test = recConnect(test, startx, starty);

        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[0].length; j++) {

                if (test[i][j] == 0) {
                    // Not connected
                    return false;
                }
            }
        }

        return true;
    }

    // 0 untested, 1 wall, -1 tested
    private static int[][] recConnect(int[][] test, int x, int y) {

        if (x == -1 || y == -1 || x >= test.length || y >= test[0].length) return test;

        if (test[x][y] == 0) {
            test[x][y] = -1;
            test = recConnect(test, x + 1, y);
            test = recConnect(test, x - 1, y);
            test = recConnect(test, x, y + 1);
            test = recConnect(test, x, y - 1);
        }

        return test;

    }

}
