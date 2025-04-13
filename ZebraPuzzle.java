/**
 * ======================================================
 * 🧠 Zebra Puzzle – Résolution optimisée en Java
 * Résout l’énigme classique : "Qui boit de l'eau ? Qui possède le zèbre ?"
 * Basé sur 15 contraintes logiques.
 * ======================================================
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ZebraPuzzle {

    // Représente une maison avec toutes ses caractéristiques
    static class House {
        String color;
        String nationality;
        String pet;
        String drink;
        String hobby;
    }

    // Tableau représentant les 5 maisons
    House[] houses = new House[5];

    // Valeurs possibles pour chaque attribut
    List<String> colors = Arrays.asList("Red", "Green", "Ivory", "Yellow", "Blue");
    List<String> nationalities = Arrays.asList("Norwegian", "Ukrainian", "Englishman", "Spaniard", "Japanese");
    List<String> pets = Arrays.asList("Dog", "Snail", "Fox", "Horse", "Zebra");
    List<String> drinks = Arrays.asList("Water", "Tea", "Milk", "Orange juice", "Coffee");
    List<String> hobbies = Arrays.asList("Dancing", "Painting", "Reading", "Football", "Chess");

    // Vérifie si deux attributs sont liés dans la même maison
    private static boolean match(House[] houses, String attr1, String val1, String attr2, String val2) {
        for (House h : houses) {
            if (get(h, attr1).equals(val1)) {
                return get(h, attr2).equals(val2);
            }
        }
        return false;
    }

    // Vérifie toutes les contraintes du puzzle (les 14 règles données)
    public static boolean satisfiesAllConstraints(House[] h) {
        return
            match(h, "nationality", "Englishman", "color", "Red") &&
            match(h, "nationality", "Spaniard", "pet", "Dog") &&
            match(h, "color", "Green", "drink", "Coffee") &&
            match(h, "nationality", "Ukrainian", "drink", "Tea") &&
            rightOf(h, "Ivory", "Green", "color") &&
            match(h, "hobby", "Dancing", "pet", "Snail") &&
            match(h, "hobby", "Painting", "color", "Yellow") &&
            "Milk".equals(h[2].drink) &&
            "Norwegian".equals(h[0].nationality) &&
            nextTo(h, "hobby", "Reading", "pet", "Fox") &&
            nextTo(h, "hobby", "Painting", "pet", "Horse") &&
            match(h, "hobby", "Football", "drink", "Orange juice") &&
            match(h, "nationality", "Japanese", "hobby", "Chess") &&
            nextTo(h, "nationality", "Norwegian", "color", "Blue");
    }

    // Accès générique à une propriété de House
    private static String get(House h, String field) {
        switch (field) {
            case "color": return h.color;
            case "nationality": return h.nationality;
            case "pet": return h.pet;
            case "drink": return h.drink;
            case "hobby": return h.hobby;
        }
        return null;
    }

    // Vérifie si une valeur est immédiatement à droite d'une autre
    private static boolean rightOf(House[] houses, String leftVal, String rightVal, String attr) {
        for (int i = 0; i < 4; i++) {
            if (get(houses[i], attr).equals(leftVal) && get(houses[i + 1], attr).equals(rightVal)) {
                return true;
            }
        }
        return false;
    }

    // Vérifie si deux valeurs sont dans des maisons voisines
    private static boolean nextTo(House[] houses, String attr1, String val1, String attr2, String val2) {
        for (int i = 0; i < 5; i++) {
            if (get(houses[i], attr1).equals(val1)) {
                if ((i > 0 && get(houses[i - 1], attr2).equals(val2)) ||
                    (i < 4 && get(houses[i + 1], attr2).equals(val2))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Génère toutes les permutations possibles d'une liste de chaînes (List<String>),
     * sans utiliser de récursion.
     * 
     * Exemple :
     * Input : ["A", "B", "C"]
     * Output : 
     * [
     *   ["A", "B", "C"],
     *   ["A", "C", "B"],
     *   ["B", "A", "C"],
     *   ["B", "C", "A"],
     *   ["C", "A", "B"],
     *   ["C", "B", "A"]
     * ]
     *
     * L'idée est d'ajouter les éléments un par un dans toutes les positions
     * possibles de chaque permutation partielle existante.
     */
    public static List<List<String>> permutations(List<String> items) {
        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (String item : items) {
            List<List<String>> newResult = new ArrayList<>();
            for (List<String> perm : result) {
                for (int i = 0; i <= perm.size(); i++) {
                    List<String> newPerm = new ArrayList<>(perm);
                    newPerm.add(i, item);
                    newResult.add(newPerm);
                }
            }
            result = newResult;
        }

        return result;
    }

    // Trouve la nationalité de la personne qui boit de l'eau
    public String getWaterDrinker() {
        List<String> natPerm = Arrays.asList("Norwegian", "Ukrainian", "Englishman", "Spaniard", "Japanese");
        List<String> drinkPerm = Arrays.asList("Water", "Tea", "Milk", "Orange juice", "Coffee");

        for (List<String> colorPerm : permutations(colors)) {
            boolean validColor = false;
            for (int i = 0; i < 4; i++) {
                if (colorPerm.get(i).equals("Ivory") && colorPerm.get(i + 1).equals("Green")) {
                    validColor = true;
                    break;
                }
            }
            if (!validColor) continue;

            for (List<String> petPerm : permutations(pets))
            for (List<String> hobbyPerm : permutations(hobbies)) {
                for (int i = 0; i < 5; i++) {
                    houses[i] = new House();
                    houses[i].color = colorPerm.get(i);
                    houses[i].nationality = natPerm.get(i);
                    houses[i].pet = petPerm.get(i);
                    houses[i].drink = drinkPerm.get(i);
                    houses[i].hobby = hobbyPerm.get(i);
                }

                if (satisfiesAllConstraints(houses)) {
                    for (House h : houses) {
                        if ("Water".equals(h.drink)) return h.nationality;
                    }
                }
            }
        }
        return null;
    }

    // Trouve la nationalité de la personne qui possède le zèbre
    public String getZebraOwner() {
        List<String> natPerm = Arrays.asList("Norwegian", "Ukrainian", "Englishman", "Spaniard", "Japanese");
        List<String> drinkPerm = Arrays.asList("Water", "Tea", "Milk", "Orange juice", "Coffee");

        for (List<String> colorPerm : permutations(colors)) {
            boolean validColor = false;
            for (int i = 0; i < 4; i++) {
                if (colorPerm.get(i).equals("Ivory") && colorPerm.get(i + 1).equals("Green")) {
                    validColor = true;
                    break;
                }
            }
            if (!validColor) continue;

            for (List<String> petPerm : permutations(pets))
            for (List<String> hobbyPerm : permutations(hobbies)) {
                for (int i = 0; i < 5; i++) {
                    houses[i] = new House();
                    houses[i].color = colorPerm.get(i);
                    houses[i].nationality = natPerm.get(i);
                    houses[i].pet = petPerm.get(i);
                    houses[i].drink = drinkPerm.get(i);
                    houses[i].hobby = hobbyPerm.get(i);
                }

                if (satisfiesAllConstraints(houses)) {
                    for (House h : houses) {
                        if ("Zebra".equals(h.pet)) return h.nationality;
                    }
                }
            }
        }
        return null;
    }

    // Point d’entrée du programme
    public static void main(String[] args) {
        ZebraPuzzle puzzle = new ZebraPuzzle();

        System.out.println("💧 Water is drunk by: " + puzzle.getWaterDrinker());
        System.out.println("🦓 Zebra is owned by: " + puzzle.getZebraOwner());
    }
}
