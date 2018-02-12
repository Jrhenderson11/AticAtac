package com.aticatac.world.ai.utils;

import java.util.ArrayList;

import javafx.util.Pair;

public class Translations {
	public static final int STEP = 2;
	public static final ArrayList<Pair<Integer, Integer>> TRANSLATIONS = getTranslations();

	private static ArrayList<Pair<Integer, Integer>> getTranslations() {
		ArrayList<Pair<Integer, Integer>> translations = new ArrayList<>();
		translations.add(new Pair<>(-STEP, -STEP));
		translations.add(new Pair<>(0, -STEP));
		translations.add(new Pair<>(STEP, -STEP));
		translations.add(new Pair<>(-STEP, 0));
		translations.add(new Pair<>(STEP, 0));
		translations.add(new Pair<>(-STEP, STEP));
		translations.add(new Pair<>(0, STEP));
		translations.add(new Pair<>(STEP, STEP));
		return translations;
	}
}
