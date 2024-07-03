/**Doug Bogle
 * This LexiconTrie represents a Lexicon built with LexiconNodes. It is a dictionary without definitions, and similarly,
 * allows adding words, removing words, and asking if it contains words and/or prefixes. It has an alphabetical iterator so
 * that one can print all its words in alphabetical order. It additionally has two advanced methods: suggestCorrections
 * and matchRegex. The former takes a target word and a max distance and looks for words that are within max distance
 * letters of the target word. For example: In a large English Dictionary lexicon suggestCorrections(crw, 1) would
 * certainly return [cow] among other things. The latter method takes a regular expression and returns all of its
 * matches in the Lexicon. The wildcard characters it takes are explained in its docstring, but it essentially lets
 * a user find all words in the Lexicon given a certain rule. Refer to Lexicon interface for @Overriden methods.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LexiconTrie implements Lexicon{
    /**
     * Keeps track of the number of words in the Lexicon.
     */
    private int numWords;

    /**
     * The start of the Lexicon, initialized as ' '.
     */
    LexiconNode root;

    public LexiconTrie(){
        numWords = 0;
        root = new LexiconNode(' ', false);
    }

    /**
     * Adds a given word to the Lexicon if it's not already in it.
     *
     * @param word The lowercase word to add to the lexicon.
     * @return true if added to Lexicon, false if it was already in it
     */
    @Override
    public boolean addWord(String word) {
        if(!containsWord(word)){
            numWords++;
            return addHelper(root, word);
        }
        return false;
    }

    /**
     * Given a word to add, iterates through the trie's children until one of the letters in the new word doesn't
     * exist in the trie (in the correct spot), and then simply creates new LexiconNodes and children until the
     * word has been fully added.
     *
     * @param curr current LexiconNode we are investigating
     * @param word String word we want to add
     * @return This method always returns true because it is only called if the word being added isn't in the
     * Lexicon
     */
    public boolean addHelper(LexiconNode curr, String word){
        int i = 0;
        while(i < word.length()){
            if(curr.getChild(word.charAt(i)) == null){
                curr.addChild(new LexiconNode(word.charAt(i), false));
            }
            curr = curr.getChild(word.charAt(i));
            i++;
        }
        curr.setWord(true);
        return true;
    }

    @Override
    public int addWordsFromFile(String filename) {
        Scanner scan;
        try {
            scan = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            return -1;
        }
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (!containsWord(line)) {
                addWord(line);
            }
        }
        scan.close();
        return numWords;
    }

    @Override
    public boolean removeWord(String word) {
        if(!containsWord(word)) return false;
        Stack<LexiconNode> letterStack = new Stack<>();
        //go down to the bottom of the word, turn the isWord boolean off
        LexiconNode curr = root;
        for(int i = 0; i < word.length(); i++){
            letterStack.push(curr.getChild(word.charAt(i)));
            curr=curr.getChild(word.charAt(i));
        }

        //the following code's functionality:
        //turn off isWord for the last letter in the word we are removing
        //if the current letter has no children, go to its parent
        //and delete its child unnecessary child
        //repeat until a LexiconNode has children
        letterStack.peek().setWord(false);
        while(letterStack.peek().getChildren().isEmpty()
                && letterStack.size() > 1 && !letterStack.peek().isWord()){
            char temp = letterStack.pop().toChar();
            letterStack.peek().removeChild(temp);
        }
        numWords--;
        return true;
    }

    @Override
    public int numWords() {
        return numWords;
    }

    @Override
    public boolean containsWord(String word) {
        LexiconNode curr = containsHelper(word);
        if(curr == null){
            return false;
        }
        return curr.isWord();
    }

    @Override
    public boolean containsPrefix(String prefix) {
        LexiconNode curr = containsHelper(prefix);
        if((curr == null)){
            return false;
        }
        return !curr.isWord();
    }

    /**
     * Given a word, either locates and returns its last letter as its LexiconNode in the trie or null if the word
     * doesn't exist.
     *
     * @param word word are looking for.
     * @return last LexiconNode in the word/null if it doesn't exist.
     */
    public LexiconNode containsHelper(String word){
        LexiconNode curr = root;
        for(char c : word.toCharArray()){
            if(curr == null){
                return null;
            }
            curr = curr.getChild(c);
        }
        return curr;
    }

    @Override
    public Iterator<String> iterator() {
        ArrayList<String> allWords = new ArrayList<>();
        return iteratorHelper(allWords, root, "").iterator();
    }

    /**
     * Deals with iteration recursively. Goes through each child of the currentNode, (while keeping track of the
     * current word) and if the child is a word, the current word is added to the ArrayList of words. Otherwise, the
     * method keeps recursing until it finds a word.
     *
     * @param myWords List of words in the Lexicon (in alphabetical order)
     * @param curr current LexiconNode we are investigating
     * @param currWord the current word we are building up (will be added to myWords)
     * @return myWords, the alphabetized list of all the words in the Lexicon.
     */
    public ArrayList<String> iteratorHelper(ArrayList<String> myWords, LexiconNode curr, String currWord){
        if(myWords.size() == numWords){
            return myWords;
        }

        for(LexiconNode child: curr){
            if (child.isWord()) {
                myWords.add(currWord + child.toChar());
            }
            iteratorHelper(myWords, child, currWord + child.toChar());
        }
        return myWords;
    }

    @Override
    public Set<String> suggestCorrections(String target, int maxDistance) {
        HashSet<String> suggestions = new HashSet<>();
        correctionsHelper(root, target, maxDistance, "", suggestions);
        return suggestions;
    }

    /**
     * Handles the main functions of suggestCorrections recursively. The base cases check to ensure that the
     * recursion hasn't deviated too far from the original word, and that, if targetWord is empty (we have a possible
     * suggestion to add) is its last LexiconNode is a word. If it is it's added to the set, otherwise not. The rest of
     * the method is just a for loop that goes through each child node and checks if there's a child matching the next
     * letter in targetWord. If there is maxDist stays the same and if there isn't it goes down by one.
     *
     * @param curr LexiconNode we are investigating
     * @param targetWord the portion of targetWord that still exists, this parameter slowly shortens until it is empty
     * @param maxDist max distance from targetWord we are allowed
     * @param prefixStr potential suggestion we are building
     * @param suggestions set of suggestions for targetWord
     */
    public void correctionsHelper(LexiconNode curr, String targetWord, int maxDist, String prefixStr, HashSet<String> suggestions){
        //if we reach our targetWord length without breaking the
        //maxDist rule and curr is a word we can add it to the set!
        if(maxDist < 0) {
            return;
        } if(targetWord.isEmpty()) {
            if (curr.isWord()) {
                suggestions.add(prefixStr);
                return;
            }
            return;
        }

        for(LexiconNode child : curr) {
            if (child.toChar() == targetWord.charAt(0)) {
                correctionsHelper(child, targetWord.substring(1), maxDist, prefixStr + String.valueOf(child.toChar()), suggestions);
            } else {
                //explore possible suggestions and therefore subtract 1 from maxDist
                correctionsHelper(child, targetWord.substring(1), maxDist - 1, prefixStr + String.valueOf(child.toChar()), suggestions);
            }
        }
    }

    @Override
    public Set<String> matchRegex(String pattern) {
        HashSet<String> regexMatches = new HashSet<>();
        regexHelper(root, pattern, "", regexMatches);
        return regexMatches;
    }

    /**
     * Handles the main functions of matchRegex. Given a regular expression consisting of Strings,*'s, ?'s, and _'s
     * (*e* for example), finds all the matches in the Lexicon recursively.
     * '_' matches any one character
     * '*' matches any amount of zero or more characters
     * '?' matches zero or one character
     *
     * The recursion functions by first checking if the pattern parameter is empty (the pattern string given by the user
     * is made shorter and shorter as we build up a possible match) and if the current LexiconNode is a word. Based on
     * this, it either adds the prefix that was built (the possible match to the regular expression) to the set of
     * matches, or doesn't. Either way it returns to exit the current recursion rabbit hole. Otherwise, it is made up
     * of three if statements for each possible wildcard character and has appropriate recursive calls within each of
     * them. The '*' wildcard if statement has inline comments to help understanding.
     *
     * @param curr LexiconNode we are investigating.
     * @param pattern regular expression pattern that was given, sometimes edited by recursive calls in
     *                to explore all possible matches. Eventually becomes zero, signifying a possible match.
     * @param prefix the pattern we are building
     * @param matches set of matches
     */
    public void regexHelper(LexiconNode curr, String pattern, String prefix, Set<String> matches){
        //if we've found a possible match and curr
        //is a word we can add this match to our set!
        if(pattern.isEmpty()) {
            if(curr.isWord()){
                matches.add(prefix);
                return;
            }
            return;
        }

        if(pattern.charAt(0) == '_') {
            for(LexiconNode child: curr) {
                regexHelper(child, pattern.substring(1), prefix + String.valueOf(child.toChar()), matches);
            }
        } else if(pattern.charAt(0) == '*') {
            for(LexiconNode child: curr) {
                regexHelper(child, pattern, prefix + String.valueOf(child.toChar()), matches);
                //investigate child nodes without altering pattern
                regexHelper(child, pattern.substring(1), prefix + String.valueOf(child.toChar()), matches);
                //investigate child nodes and decrease pattern
                regexHelper(curr, pattern.substring(1), prefix, matches);
                //passes curr instead of child to avoid non-words being considered words
            }
        } else if(pattern.charAt(0) == '?') {
            for(LexiconNode child: curr) {
                regexHelper(child, pattern.substring(1), prefix + String.valueOf(child.toChar()), matches);
                regexHelper(curr, pattern.substring(1), prefix, matches);
            }
        } else {
            //if we just have a normal character
            LexiconNode toAdd = curr.getChild(pattern.charAt(0));
            if(toAdd == null) return; //if no child exists we've messed up so stop recursing
            regexHelper(toAdd, pattern.substring(1), prefix + String.valueOf(toAdd.toChar()), matches);
        }
    }
}
