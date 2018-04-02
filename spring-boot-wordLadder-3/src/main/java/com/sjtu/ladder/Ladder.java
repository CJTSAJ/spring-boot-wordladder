package com.sjtu.ladder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Scanner;
import java.util.Stack;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
@RestController
public class Ladder {
	public String printStack(Stack<String>ladder, String word1, String word2) {
		String result = "A ladder from " + word1 + " back to " + word2 +" : ";
		while(!ladder.isEmpty()) {
			result = result + " " + ladder.pop();
		}
		return result;
	}
	
	public String wordLadder(Set<String>dictionary, String word1, String word2) {
		String alpha = "abcdefghijklmnopqrstuvwxyz";//字母表
		String newWord = word1;
		Set<String>usedWord = new HashSet<String>();
		Deque<Stack<String>>allLadder = new LinkedList<Stack<String>>();
		Stack<String>tempLadder = new Stack<String>();
		
		usedWord.add(word1);
		tempLadder.push(word1);
		allLadder.push(tempLadder);
		
		while(true) {
			int count = allLadder.size();
			if(allLadder.isEmpty())break;
			
			for(int i = 0; i < count; i++) {
				tempLadder = allLadder.peekFirst();
				String tempWord = tempLadder.peek();
				
				//单词变换
				for(int j = 0; j < tempWord.length(); j++) {
					newWord = tempWord;
					for(int k = 0; k < 26; k++) {
						StringBuilder sb = new StringBuilder(newWord);
						sb.replace(j, j+1, String.valueOf(alpha.charAt(k)));
						newWord = sb.toString();
						
						if(!usedWord.contains(newWord) && dictionary.contains(newWord)) {
							Stack<String>copyTempLadder = (Stack<String>)tempLadder.clone();
							copyTempLadder.push(newWord);
							allLadder.addLast(copyTempLadder);
							usedWord.add(newWord);
						}
						if(newWord.equals(word2))break;
					}
					if(newWord.equals(word2))break;
				}
				if(newWord.equals(word2))break;
				allLadder.pop();
			}
			if(newWord.equals(word2))break;
		}
		if(newWord.equals(word2)) {
			Ladder la = new Ladder();
			return la.printStack(allLadder.peekLast(), word1, word2);
		}
		else return "there is no laader";
	}
	@RequestMapping(value = "/ladder", method = RequestMethod.GET)
	public String say(@RequestParam("filename") String pathName,@RequestParam("word1") String word1,@RequestParam("word2") String word2) {
		Set<String>dictionary = new HashSet<String>();
		String word;
		
		File file = new File(pathName);
		
		if(file.exists()) {
			try {
				FileReader fr = new FileReader(pathName);
				BufferedReader buffReader = new BufferedReader(fr);
				while((word = buffReader.readLine()) != null) {
					dictionary.add(word);
				}
				buffReader.close();
			}catch (FileNotFoundException ex){
				ex.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		else System.out.println("the file name does't exist!");
		
		word1 = word1.toLowerCase();
		word2 = word2.toLowerCase();
		
		if(word1.length() == word2.length() && !word1.equals(word2) && dictionary.contains(word1) && dictionary.contains(word2)) {
			Ladder ladder = new Ladder();
			return ladder.wordLadder(dictionary, word1, word2);
		}
		if(word1.equals(word2)) {
			return "The two words must be diffrent!";
		}
		
		if(word1.length() != word2.length()) {
			return "The two words must have the same length!";
		}
		else return "soomething wrong";
		
	}
}
