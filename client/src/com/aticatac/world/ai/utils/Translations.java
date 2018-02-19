package com.aticatac.world.ai.utils;

import java.util.ArrayList;

import javafx.util.Pair;

public class Translations {
	private enum Step {
		GRID(1), DISPLAY(2);
		private final int STEP;

		Step(int step) {
			this.STEP = step;
		}
	}

	public static final ArrayList<Pair<Integer, Integer>> TRANSLATIONS_GRID = getTranslations(Step.GRID);
	public static final ArrayList<Pair<Integer, Integer>> TRANSLATIONS_DISPLAY = getTranslations(Step.DISPLAY);

	private static ArrayList<Pair<Integer, Integer>> getTranslations(Step type) {
		ArrayList<Pair<Integer, Integer>> translations = new ArrayList<>();
		translations.add(new Pair<>(-type.STEP, -type.STEP));
		translations.add(new Pair<>(0, -type.STEP));
		translations.add(new Pair<>(type.STEP, -type.STEP));
		translations.add(new Pair<>(-type.STEP, 0));
		translations.add(new Pair<>(type.STEP, 0));
		translations.add(new Pair<>(-type.STEP, type.STEP));
		translations.add(new Pair<>(0, type.STEP));
		translations.add(new Pair<>(type.STEP, type.STEP));
		return translations;
	}
}
