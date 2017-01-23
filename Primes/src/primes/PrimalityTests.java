package primes;

public final class PrimalityTests {
	
	/**
	 * Tests a number for being a prime.
	 * 
	 * @param n - the number to test
	 * @return true if the number is a prime, false otherwise
	 */
	public static boolean isPrime(int n){

		if(n<10){
			return test_tiny_primes(n);
		} 
		
		return test_divisors(n);
	}
	
	/**
	 * Tests for a prime for n<10 by comparing to a list of known primes
	 * @param n - an integer less than 10.
	 * @return true if the number is a prime, false otherwise
	 */
	
	public static boolean test_tiny_primes(int n){
		if(n == 2 || n == 3 || n == 5 || n == 7){
			return true;
		} else if (n>10 || n<0){
			throw new NumberFormatException();
		}
		return false;
	}
	
	/**
	 *  Tests for a prime by an exhaustive search for factors
	 *  
	 *  @return true if the number is a prime, false otherwise
	 *  
	 *  Optimisations:
	 *  - Only check factors up to sqrt(n)
	 *  - Only check for factors 2, 3, 6k-1 and 6k+1 (integer k)
	 */
	public static boolean test_divisors(int n){
		if(n%2 == 0){
			return false;
		}
		if(n%3 == 0){
			return false;
		}
		
		double square_root_n = Math.sqrt(n);
		
		for(Long i = new Long(1); (6*i)-1<=square_root_n; i++){
			
			if(n%(6*i - 1) == 0){
				return false;
			}
			
			if(n%(6*i + 1) == 0){
				return false;
			}
		}
		return true;
	}
	
}
