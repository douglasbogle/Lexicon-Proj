/**Doug Bogle
 *This class represents an Individual LexiconNode. LexiconTrie is a recursive data structure in that it is made up of
 * these LexiconNode objects. The important functionalities/information about LexiconNodes is that they have an
 * alphabetized ArrayList of children which can be added to or removed from, are either a word or not a word
 * (and can be changed), are iterable (they just call the ArrayList's iterator on their children), and have a char
 * representation of themselves which can be returned by toChar().
 */
import java.util.ArrayList;
import java.util.Iterator;

public class LexiconNode implements Iterable<LexiconNode>{
    /**
     * ArrayList of LexiconNodes representing the current LexiconNode's children. This List is alphabetized.
     */
    private ArrayList<LexiconNode> children;

    /**
     * The actual letter this LexiconNode represents.
     */
    private char letter;

    /**
     * A boolean indicating if the current LexiconNode is the end of a word.
     */
    private boolean isWord;

    public LexiconNode(char letter, boolean isWord){
        children = new ArrayList<>();
        this.isWord = isWord;
        this.letter = letter;
    }

    /**
     * Gets LexiconNode's char representation
     *
     * @return the char representation of a LexiconNode object.
     */
    public char toChar(){
        return letter;
    }

    /**
     * Puts a new child node in the correct place alphabetically. Returns true if successful, false if not.
     *
     * @param toAdd a LexiconNode to be added to another LexiconNode's ArrayList of children.
     */
    public boolean addChild(LexiconNode toAdd){
        if (children.isEmpty()){
            children.add(toAdd);
            return true;
        }
        int i = 0;
        while (i < children.size()){
            if (Character.toLowerCase(toAdd.toChar()) < Character.toLowerCase((children.get(i).toChar()))){
                children.add(i, toAdd);
                return true;
            } else if (Character.toLowerCase(toAdd.toChar()) == Character.toLowerCase((children.get(i).toChar()))){
                return false;//if chars are equal this child already exists.
            }
            i++;
        }
        children.add(toAdd);
        return true;
    }

    /**
     * Removes a child from a LexiconNode's ArrayList of children. If it isn't in children it does nothing.
     *
     * @param toRemove char representation of child that we want to remove.
     */
    public void removeChild(char toRemove){
        int i = 0;
        while (i < children.size()){
            if (Character.toLowerCase(toRemove) == Character.toLowerCase(children.get(i).toChar())){
                children.remove(i);
                return;
            }
            i++;
        }
    }

    public ArrayList<LexiconNode> getChildren(){
        return children;
    }

    /**
     * Attempts to get a child from a LexiconNode's children. If this child is found, it is returned (as a LexiconNode,
     * not just a char). If it is not found, the method returns null.
     *
     * @param toGet char representation of LexiconNode we want to get.
     * @return the child or null if child not found.
     */
    public LexiconNode getChild(char toGet){
        for (LexiconNode child: children){
            if (Character.toLowerCase(toGet) == Character.toLowerCase(child.toChar())){
                return child;
            }
        }
        return null;
    }

    public boolean isWord(){
        return isWord;
    }

    public void setWord(boolean isWord){
        this.isWord = isWord;
    }

    /**
     * Lets us iterate through a LexiconNode by just calling the ArrayList of children's iterator.
     * @return each child in alphabetical order.
     */
    @Override
    public Iterator<LexiconNode> iterator() {
        return children.iterator();
    }
}
