package main.util;

import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class EfficiencyMetrics {
    private static final boolean PRINT_RESULTS_TO_CONSOLE = true;
    private static final int FRAMES_PER_MESSAGE_PRINT = 60;
    private static final int[] RECORD_FRAME_COUNTS = new int[] {60, 600, 1500};
    private static final int MAX_FRAME_COUNTS = 300;

    private static ArrayList<EfficiencyMetricType> processTypes = new ArrayList<>();
    private static ArrayList<Double> processTimers = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> recentProcessTimes = new ArrayList<>();
    private static ArrayList<String> processData = new ArrayList<>();

    private static ArrayList<Double> recentFrameTimes = new ArrayList<>();

    private static int frameCount = 0;
    private static double lastTime = 0;

    public static void init() {
        for(EfficiencyMetricType type: EfficiencyMetricType.values()) {
            startTimer(type);
            stopTimer(type);
        }
    }

    private static int getProcessIndex(EfficiencyMetricType processType) {
        for(int i = 0; i < processTypes.size(); i++) {
            if(processTypes.get(i).equals(processType))
                return i;
        }
        return -1;
    }

    public static void startTimer(EfficiencyMetricType processType) {
        if(Settings.get(SettingType.USE_EFFICIENCY_METRICS) == 1) {
            int processIndex = getProcessIndex(processType);

            if(processIndex == -1) {
                //new process, add to maps
                processIndex = processTypes.size();
                processTypes.add(processType);
                processTimers.add(0.0);
                recentProcessTimes.add(new ArrayList<>());
                processData.add(null);
            }

            processTimers.set(processIndex, glfwGetTime());
        }
    }

    public static void stopTimer(EfficiencyMetricType processType) {
        if(Settings.get(SettingType.USE_EFFICIENCY_METRICS) == 1) {
            int processIndex = getProcessIndex(processType);

            double startTime = processTimers.get(processIndex);
            double processTime = glfwGetTime() - startTime;

            ArrayList<Double> queue = recentProcessTimes.get(processIndex);
            queue.add(processTime);
            if(queue.size() > MAX_FRAME_COUNTS) {
                queue.remove(0);
            }
        }
    }

    public static void frameStart() {
        if(Settings.get(SettingType.USE_EFFICIENCY_METRICS) == 1) {
            lastTime = glfwGetTime();
        }
    }

    public static void frameEnd() {
        if(Settings.get(SettingType.USE_EFFICIENCY_METRICS) == 1) {
            recentFrameTimes.add(glfwGetTime() - lastTime);
            if(recentFrameTimes.size() > MAX_FRAME_COUNTS) {
                recentFrameTimes.remove(0);
            }

            frameCount++;

            if(frameCount % FRAMES_PER_MESSAGE_PRINT == 0) {
                calculateData();

                if(PRINT_RESULTS_TO_CONSOLE)
                    printData();
            }
        }
    }

    private static void calculateData() {
        double[] totalFrameTime = new double[RECORD_FRAME_COUNTS.length];
        double[] averageFrameTime = new double[RECORD_FRAME_COUNTS.length];
        for(int j = 0; j < RECORD_FRAME_COUNTS.length; j++) {
            for(int i = 0; i < RECORD_FRAME_COUNTS[j]; i++) {
                totalFrameTime[j] += recentFrameTimes.get(Math.max(0, recentFrameTimes.size()-i-1));
            }
            averageFrameTime[j] = totalFrameTime[j] / RECORD_FRAME_COUNTS[j];
        }

        for(EfficiencyMetricType processName: processTypes) {
            int processIndex = getProcessIndex(processName);
            ArrayList<Double> processTimes = recentProcessTimes.get(processIndex);

            String data = processName + ":\n";
            for(int j = 0; j < RECORD_FRAME_COUNTS.length; j++) {
                double totalProcessTime = 0;
                for(int i = 0; i < RECORD_FRAME_COUNTS[j]; i++) {
                    totalProcessTime += processTimes.get(Math.max(0, processTimes.size()-i-1));
                }
                double averageProcessTime = totalProcessTime / RECORD_FRAME_COUNTS[j];
                double averageProcessFramePct = averageProcessTime / averageFrameTime[j];
                data += "    Last " + String.format("%d", RECORD_FRAME_COUNTS[j]) + " frames: " +
                        "avg " + String.format("%.4f", averageProcessFramePct * 100) + "% (" + String.format("%.4f", averageProcessTime) + "s), " +
                        "total " + String.format("%.4f", totalProcessTime) + "s\n";
            }

            processData.set(processIndex, data);
        }
    }

    private static void printData() {
        System.out.println("\nFRAME " + frameCount);

        for(EfficiencyMetricType processName: processTypes) {
            int processIndex = getProcessIndex(processName);
            System.out.print(processData.get(processIndex));
        }
    }
}
