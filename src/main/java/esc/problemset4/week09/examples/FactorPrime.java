package esc.problemset4.week09.examples;

import java.math.BigInteger;


public class FactorPrime {	
	//Precondition: n is a semi-prime number.
	//Postcondition: the returned value is a prime factor of n;
	public static BigInteger factor(BigInteger n) {
		BigInteger i = new BigInteger("2");
		BigInteger zero = new BigInteger("0");
		
		while (i.compareTo(n) < 0) {
		    
		    System.out.println("i: " + i + ", n: " + n + ", n.remainder(i): " + n.remainder(i));
		    
			if (n.remainder(i).compareTo(zero) == 0) {
				return i;
			}
			
			i = i.add(new BigInteger("1"));
		}
		
		assert(false); //if this is reached, an error occurs.
		return null;
	}
}
