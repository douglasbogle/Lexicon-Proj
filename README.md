# Lexicon Trie Project

## Overview

This Java project implements a Lexicon using a Trie data structure. A Trie is a tree with nodes representing letters of words, allowing for efficient storage and quick access to words. Each node in the Trie can have multiple children, making it an ideal structure for dictionary-like applications. This project focuses on two main features:
1. **matchRegex**: Finds words that match a given pattern.
2. **suggestCorrections**: Suggests corrections for misspelled words similar to the functionality found in word processors like Microsoft Word.

## Key Classes and Methods

### LexiconTrie

The `LexiconTrie` class represents the Trie-based Lexicon. It includes the following key methods:

- **addWord(String word)**: Adds a word to the Lexicon.
- **removeWord(String word)**: Removes a word from the Lexicon.
- **containsWord(String word)**: Checks if a word is in the Lexicon.
- **containsPrefix(String prefix)**: Checks if a prefix exists in the Lexicon.
- **addWordsFromFile(String filename)**: Adds words to the Lexicon from a specified file.
- **suggestCorrections(String target, int maxDistance)**: Suggests corrections for a target word within a specified maximum distance.
- **matchRegex(String pattern)**: Finds words that match a given regular expression pattern.
- **iterator()**: Returns an iterator to iterate over all words in the Lexicon in alphabetical order.

### LexiconNode

The `LexiconNode` class represents individual nodes in the Trie. Each node stores:
- A character.
- A list of child nodes.
- A boolean indicating if the node represents the end of a word.

Key methods include:
- **addChild(LexiconNode toAdd)**: Adds a child node.
- **removeChild(char toRemove)**: Removes a child node.
- **getChild(char toGet)**: Gets a specific child node.
- **isWord()**: Checks if the node is the end of a word.
- **setWord(boolean isWord)**: Sets the node as the end of a word.

### Lexicon
- An interface provided by a CS Instructor at my college

## Testing
- Some files filled with words are included in the repository for you to ue in testing
