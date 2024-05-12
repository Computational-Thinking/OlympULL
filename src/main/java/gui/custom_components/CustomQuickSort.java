package gui.custom_components;

import java.util.ArrayList;

public interface CustomQuickSort {
    static void sort(ArrayList<Integer> punctuations, ArrayList<String> teams) {
        if (punctuations == null || teams == null || punctuations.size() != teams.size()) {
            throw new IllegalArgumentException("Los ArrayLists deben ser no nulos y de la misma longitud");
        }
        quickSort(punctuations, teams, 0, punctuations.size() - 1);
    }

    private static void quickSort(ArrayList<Integer> punctuations, ArrayList<String> teams, int low, int high) {
        if (low < high) {
            int pi = partition(punctuations, teams, low, high);
            quickSort(punctuations, teams, low, pi - 1);
            quickSort(punctuations, teams, pi + 1, high);
        }
    }

    private static int partition(ArrayList<Integer> punctuations, ArrayList<String> teams, int low, int high) {
        int pivot = punctuations.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (punctuations.get(j) >= pivot) {
                i++;
                swapIntegers(punctuations, i, j);
                swapStrings(teams, i, j);
            }
        }
        swapIntegers(punctuations, i + 1, high);
        swapStrings(teams, i + 1, high);
        return i + 1;
    }

    private static void swapIntegers(ArrayList<Integer> arr, int i, int j) {
        int temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    private static void swapStrings(ArrayList<String> arr, int i, int j) {
        String temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }
}
