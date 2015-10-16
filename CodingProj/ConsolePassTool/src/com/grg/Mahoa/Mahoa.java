package com.grg.Mahoa;

import java.util.Scanner;

public class Mahoa {

	public static String key = "31323334353637383132333435363738";
	
	public static void main(String[] args) {
		ccas.Des des = new ccas.Des(key);
		//String cipherText = des.enc("feel");
		
		System.out.println("Xin Nhap Chuoi Binh Thuong");
		Scanner scan = new Scanner(System.in);
		String s = scan.nextLine();
		
		System.out.println("Chuoi Ma Hoa: " + des.enc(s.trim()));
	}

}
