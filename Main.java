import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Arrays;
public class Main {

    public static class WordFrequency {
        String word;
        int frequency;

        WordFrequency(String word) {
            this.word = word;
            this.frequency = 1;
        }
    }

    public static WordFrequency[] train(String text) {
        String[] tokens = text.split("\\s+");
        HashSet<String> uniqueWords = new HashSet<>();
        for (String token : tokens) {
            uniqueWords.add(token);
        }

        WordFrequency[] model = new WordFrequency[uniqueWords.size()];
        int index = 0;
        for (String word : uniqueWords) {
            model[index++] = new WordFrequency(word);
        }

        return model;
    }

    public static boolean isValidChar(char c) {
        // Adjust this method based on the specific characters in Tibetan script
        return Character.isLetterOrDigit(c) || c == '་';
    }

    public static boolean isAlpha(char c) {
        // Adjust this method based on the specific characters in Tibetan script
        return Character.isLetter(c) || c == '་';
    }

    public static void toLowerCase(char[] str) {
        for (int i = 0; i < str.length; i++) {
            str[i] = Character.toLowerCase(str[i]);
        }
    }

    public static int edits1(char[] word, HashSet<String> edits) {
        // Implement edits1 for Tibetan language based on its script
        // This might involve considering specific rules for character transformations
        // For simplicity, this example uses the same edits1 as the previous examples

        int wordLen = word.length;

        // Deletes
        for (int i = 0; i < wordLen; i++) {
            char[] deleted = new char[wordLen - 1];
            System.arraycopy(word, 0, deleted, 0, i);
            System.arraycopy(word, i + 1, deleted, i, wordLen - i - 1);
            edits.add(new String(deleted));
        }

        // Transposes
        for (int i = 0; i < wordLen - 1; i++) {
            char[] transposed = Arrays.copyOf(word, wordLen);
            transposed[i] = word[i + 1];
            transposed[i + 1] = word[i];
            edits.add(new String(transposed));
        }

        // Replaces
        for (int i = 0; i < wordLen; i++) {
            for (char c = 'ཀ'; c <= 'ཿ'; c++) {
                char[] replaced = Arrays.copyOf(word, wordLen);
                replaced[i] = c;
                edits.add(new String(replaced));
            }
        }

        // Inserts
        for (int i = 0; i <= wordLen; i++) {
            for (char c = 'ཀ'; c <= 'ཿ'; c++) {
                char[] inserted = new char[wordLen + 1];
                System.arraycopy(word, 0, inserted, 0, i);
                inserted[i] = c;
                System.arraycopy(word, i, inserted, i + 1, wordLen - i);
                edits.add(new String(inserted));
            }
        }

        return edits.size();
    }

    public static int knownEdits2(char[] word, WordFrequency[] model, HashSet<String> edits) {
        int knownEditsCount = 0;
        for (String edit : edits) {
            for (WordFrequency frequency : model) {
                if (edit.equals(frequency.word)) {
                    knownEditsCount++;
                    break;
                }
            }
        }
        return knownEditsCount;
    }

    public static boolean known(String word, WordFrequency[] model) {
        for (WordFrequency frequency : model) {
            if (word.equals(frequency.word)) {
                return true;
            }
        }
        return false;
    }

    public static void correct(String word, WordFrequency[] model) {
        char[] inputWord = word.toCharArray();
        toLowerCase(inputWord);

        HashSet<String> edits = new HashSet<>();
        int editCount = edits1(inputWord, edits);

        if (!known(word, model) && knownEdits2(inputWord, model, edits) > 0) {
            System.out.println("Input: " + word);
            System.out.println("Corrected: " + edits.iterator().next());
        } else {
            System.out.println("Input: " + word);
            System.out.println("Corrected: " + word);
        }
    }

    public static void main(String[] args) {
        // Example usage
        try {
            // Read the Tibetan corpus from a file
            BufferedReader reader = new BufferedReader(new FileReader("tibetan_corpus.txt"));
            StringBuilder corpusBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                corpusBuilder.append(line).append(" ");
            }
            reader.close();

            // Train the model
            String corpus = corpusBuilder.toString();
            WordFrequency[] model = train(corpus);

            // Example correction
            String inputWord = "འདུ";
            correct(inputWord, model);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

