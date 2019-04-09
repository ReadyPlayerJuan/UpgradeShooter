package main.util;

import java.util.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class EfficiencyMetrics {
    private static final boolean PRINT_RESULTS_TO_CONSOLE = true;
    private static final int FRAMES_PER_MESSAGE_PRINT = 30;
    private static final int[] RECORD_FRAME_COUNTS = new int[] {30, 90, 300};
    private static int maxFrameCounts = 0;

    private static ArrayList<EfficiencyMetricType> processTypes = new ArrayList<>();
    private static ArrayList<Double> processTimers = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> recentProcessTimes = new ArrayList<>();
    public static ArrayList<String> processData = new ArrayList<>();

    private static ArrayList<Double> recentFrameTimes = new ArrayList<>();

    private static int frameCount = 0;
    private static double lastTime = 0;

    public static void init() {
        for(EfficiencyMetricType type: EfficiencyMetricType.values()) {
            startTimer(type);
            stopTimer(type);
        }

        for(Integer i: RECORD_FRAME_COUNTS) {
            if(i > maxFrameCounts)
                maxFrameCounts = i;
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
                processData.add("");
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
            if(queue.size() > maxFrameCounts) {
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
            if(recentFrameTimes.size() > maxFrameCounts) {
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
            int num;
            for(num = 0; num < RECORD_FRAME_COUNTS[j]; num++) {
                if(num == recentFrameTimes.size())
                    break;
                totalFrameTime[j] += recentFrameTimes.get(recentFrameTimes.size()-num-1);
            }
            averageFrameTime[j] = totalFrameTime[j] / num;
        }

        for(EfficiencyMetricType processName: processTypes) {
            int processIndex = getProcessIndex(processName);
            ArrayList<Double> processTimes = recentProcessTimes.get(processIndex);

            String data = processName + ":\n";
            for(int j = 0; j < RECORD_FRAME_COUNTS.length; j++) {
                double totalProcessTime = 0;
                int num;
                for(num = 0; num < RECORD_FRAME_COUNTS[j]; num++) {
                    if(num == processTimes.size())
                        break;
                    totalProcessTime += processTimes.get(processTimes.size()-num-1);
                }
                double averageProcessTime = totalProcessTime / num;
                double averageProcessFramePct = averageProcessTime / averageFrameTime[j];
                data += "    Last " + String.format("%d", num) + " frames: " +
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
