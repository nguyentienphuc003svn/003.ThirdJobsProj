package com.letter.ciphertext;

import java.io.*;
class CipherFreqLetter
{
	public static void main(String args[])throws IOException
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Plese enter the ciphertext to analyze the frequency of letter: ");
		String s = br.readLine();

		s=s.toLowerCase(); //change the ciphertext into lowercase
		int l=s.length(); //finding the length of the ciphertext

		char ch;
		System.out.println("Ciphtertext Frequency Letters RESULTS:");
		System.out.println("-------------------------------------------------"); 
		System.out.println("Letter\tFrequency\t\tPercent");
		System.out.println("-------------------------------------------------");

		///Start to count 
		int count=0;
		for(char i='a'; i<='z'; i++)
		{
			count = 0;
			for(int j=0; j<l; j++)
			{
				ch=s.charAt(j); 
				if(ch==i) 
					count++; 
			}
			
			if(count!=0)//Only print letters that appears in the ciphertext
			{
				double dcount = count;
				double dtotal = l;
				double percent = dcount/dtotal*100;
				System.out.printf("\n"+i+"\t\t"+count+"\t\t %.2f"+"%%",percent);
			}
		}
	}
}
